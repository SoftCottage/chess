package handler;

import io.javalin.http.Context;
import service.ClearService;
import dataaccess.DataAccessException;

public class ClearHandler {
    private final ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public void handle(Context ctx) {
        try {
            clearService.clear();
            ctx.status(200).json(new Object()); // empty JSON response
        } catch (DataAccessException e) {
            ctx.status(500).result("Error clearing database: " + e.getMessage());
        }
    }
}
