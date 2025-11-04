package server;

import io.javalin.Javalin;
import handler.ClearHandler;
import handler.UserHandler;
import handler.GameHandler;
import service.ClearService;
import service.UserService;
import service.GameService;
import dataaccess.*;

public class Server {

    private final Javalin javalin;
    private final InMemoryDataAccess inMemoryDataAccess;

    public Server() {
        // Initialize database and create tables if needed
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.initialize(); // This method will create tables if they don't exist
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to initialize database: " + e.getMessage(), e);
        }

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Initialize DataAccess layer (you can replace this with MySqlDataAccess)
        inMemoryDataAccess = new InMemoryDataAccess();

        // Clear endpoint
        var clearService = new ClearService(inMemoryDataAccess);
        var clearHandler = new ClearHandler(clearService);
        javalin.delete("/db", clearHandler::handle);

        // User endpoints
        var userService = new UserService(inMemoryDataAccess);
        var userHandler = new UserHandler(userService);
        javalin.post("/user", userHandler::handleRegister);
        javalin.post("/session", userHandler::handleLogin);
        javalin.delete("/session", userHandler::handleLogout);

        // Game endpoints
        var gameService = new GameService(inMemoryDataAccess);
        var gameHandler = new GameHandler(gameService);
        javalin.post("/game", gameHandler::createGame);
        javalin.get("/game", gameHandler::listGames);
        javalin.put("/game", gameHandler::joinGame);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
