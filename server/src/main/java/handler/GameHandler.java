package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import model.*;
import service.GameService;

public class GameHandler {

    private final GameService gameService;
    private final Gson gson;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
        this.gson = new Gson();
    }

    public void createGame(Context ctx) {
        try {
            // Get auth token from header
            String authToken = ctx.header("Authorization");
            if (authToken == null || authToken.isBlank()) {
                String json = gson.toJson(new CreateGameResult("Error: unauthorized"));
                ctx.status(401).result(json);
                return;
            }

            // Parse the request body
            CreateGameRequest request = gson.fromJson(ctx.body(), CreateGameRequest.class);
            if (request == null || request.getGameName() == null || request.getGameName().isBlank()) {
                String json = gson.toJson(new CreateGameResult("Error: bad request"));
                ctx.status(400).result(json);
                return;
            }

            // Add the auth token to the request object
            request = new CreateGameRequest(request.getGameName(), authToken);

            // Call the service
            CreateGameResult result = gameService.createGame(request);

            // Serialize response
            String jsonResult = gson.toJson(result);
            ctx.contentType("application/json");

            // Determine HTTP status
            if (result.getMessage() != null) {
                if (result.getMessage().contains("unauthorized")) {
                    ctx.status(401).result(jsonResult);
                } else if (result.getMessage().contains("bad request")) {
                    ctx.status(400).result(jsonResult);
                } else {
                    ctx.status(500).result(jsonResult);
                }
            } else {
                ctx.status(200).result(jsonResult);
            }

        } catch (Exception e) {
            // Handle unexpected server errors
            String errorJson = gson.toJson(new CreateGameResult("Error: " + e.getMessage()));
            ctx.status(500).result(errorJson);
        }
    }

    // ----------------- LIST GAMES -----------------
    public void listGames(Context ctx) {
        try {
            String authToken = ctx.header("authorization");
            if (authToken == null || authToken.isBlank()) {
                ctx.status(401).result(gson.toJson(new ListGamesResult("Error: unauthorized")));
                return;
            }

            ListGamesRequest request = new ListGamesRequest(authToken);
            ListGamesResult result = gameService.listGames(request);
            String json = gson.toJson(result);

            if (result.getMessage() != null) {
                if (result.getMessage().contains("unauthorized")) ctx.status(401);
                else ctx.status(500);
            } else {
                ctx.status(200);
            }

            ctx.result(json);
        } catch (Exception e) {
            ctx.status(500).result(gson.toJson(new ListGamesResult("Error: " + e.getMessage())));
        }
    }
}
