package handler;

import io.javalin.http.Context;
import service.ClearService;
import dataaccess.DataAccessException;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public class ClearHandler {

    private final ClearService clearService;
    private final Gson gson;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
        this.gson = new Gson();
    }

    public void handle(Context ctx) {
        try {
            clearService.clear();
            // Return empty JSON object
            ctx.status(200).result(gson.toJson(new HashMap<>()));
        } catch (DataAccessException e) {
            ctx.status(500).json(Map.of("message", "Error: " + e.getMessage()));
        }
    }
}
