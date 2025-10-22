package server;

import io.javalin.Javalin;
import handler.ClearHandler;
import handler.RegisterHandler;
import service.ClearService;
import service.RegisterService;
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

        // Register endpoint
        var registerService = new RegisterService(dataAccess);
        var registerHandler = new RegisterHandler(registerService);
        javalin.post("/user", registerHandler::handle);

        // future: login, session delete, /game, etc.
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
