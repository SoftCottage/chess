package server;

import io.javalin.*;
import handler.ClearHandler;
import service.ClearService;
import dataaccess.DataAccess;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        var dataAccess = new DataAccess();
        var clearService = new ClearService(dataAccess);
        var clearHandler = new ClearHandler(clearService);

        javalin.delete("/db", clearHandler::handle);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
