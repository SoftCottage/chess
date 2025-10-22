package handler;

import io.javalin.http.Context;
import service.ClearService;
import dataaccess.DataAccessException;
import com.google.gson.Gson;
import model.ClearResult;

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
            ClearResult result = clearService.clear();
            ctx.status(200).result(gson.toJson(result));
        } catch (DataAccessException e) {
            ctx.status(500).json(Map.of("message", "Error: " + e.getMessage()));
        }
    }
}
