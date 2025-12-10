package server;

import io.javalin.Javalin;
import handler.ClearHandler;
import handler.UserHandler;
import handler.GameHandler;
import service.ClearService;
import service.UserService;
import service.GameService;
import dataaccess.*;

import java.sql.SQLException;

public class Server {

    private final Javalin javalin;
    private final DataAccess dataAccess;

    public Server() {
        try {
            DatabaseManager.createDatabase();
     //       DatabaseManager.initialize();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to initialize database: " + e.getMessage(), e);
        }

        dataAccess = new MySqlDataAccess();

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Clear endpoint
        var clearService = new ClearService(dataAccess);
        var clearHandler = new ClearHandler(clearService);
        javalin.delete("/db", clearHandler::handle);

        // User endpoints
        var userService = new UserService(dataAccess);
        var userHandler = new UserHandler(userService);
        javalin.post("/user", userHandler::handleRegister);
        javalin.post("/session", userHandler::handleLogin);
        javalin.delete("/session", userHandler::handleLogout);

        // Game endpoints
        var gameService = new GameService(dataAccess);
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
