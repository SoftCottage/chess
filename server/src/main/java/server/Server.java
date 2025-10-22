package server;
//comment to commit and push
import io.javalin.Javalin;
import handler.ClearHandler;
import handler.RegisterHandler;
import service.ClearService;
import service.UserService;
import dataaccess.DataAccess;

public class Server {

    private final Javalin javalin;
    private final DataAccess dataAccess;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Single shared DataAccess for all services
        dataAccess = new DataAccess();

        // Clear endpoint
        var clearService = new ClearService(dataAccess);
        var clearHandler = new ClearHandler(clearService);
        javalin.delete("/db", clearHandler::handle);

        // User endpoints
        var userService = new UserService(dataAccess);
        var registerHandler = new RegisterHandler(userService);
        javalin.post("/user", registerHandler::handle);

        // future: login, logout, /game, etc.
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
