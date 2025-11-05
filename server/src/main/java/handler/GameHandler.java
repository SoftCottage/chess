package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import requestresult.*;
import service.GameService;

public class GameHandler {

    private final GameService gameService;
    private final Gson gson;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
        this.gson = new Gson();
    }

    // Create Game
    public void createGame(Context ctx) {
        try {
            String authToken = ctx.header("Authorization");
            CreateGameRequest request = gson.fromJson(ctx.body(), CreateGameRequest.class);

            if (request == null) {
                ctx.status(400).result(gson.toJson(new CreateGameResult("Error: bad request")));
                return;
            }

            request = new CreateGameRequest(request.getGameName(), authToken);

            CreateGameResult result;
            try {
                result = gameService.createGame(request);
            } catch (Exception e) {
                // Treat any DB-related exception as 500
                ctx.status(500).result(gson.toJson(new CreateGameResult("Error: " + e.getMessage())));
                return;
            }

            respondWithProperStatus(ctx, result);

        } catch (Exception e) {
            ctx.status(500).result(gson.toJson(new CreateGameResult("Error: " + e.getMessage())));
        }
    }

    // List Games
    public void listGames(Context ctx) {
        try {
            String authToken = ctx.header("Authorization");
            ListGamesRequest request = new ListGamesRequest(authToken);

            ListGamesResult result;
            try {
                result = gameService.listGames(request);
            } catch (Exception e) {
                ctx.status(500).result(gson.toJson(new ListGamesResult("Error: " + e.getMessage())));
                return;
            }

            respondWithProperStatus(ctx, result);

        } catch (Exception e) {
            ctx.status(500).result(gson.toJson(new ListGamesResult("Error: " + e.getMessage())));
        }
    }

    // Join Game
    public void joinGame(Context ctx) {
        try {
            String authToken = ctx.header("Authorization");
            JoinGameRequest request = gson.fromJson(ctx.body(), JoinGameRequest.class);

            if (request == null) {
                ctx.status(400).result(gson.toJson(new JoinGameResult("Error: bad request")));
                return;
            }

            request.setAuthToken(authToken);

            JoinGameResult result;
            try {
                result = gameService.joinGame(request);
            } catch (Exception e) {
                ctx.status(500).result(gson.toJson(new JoinGameResult("Error: " + e.getMessage())));
                return;
            }

            respondWithProperStatus(ctx, result);

        } catch (Exception e) {
            ctx.status(500).result(gson.toJson(new JoinGameResult("Error: " + e.getMessage())));
        }
    }

    // Helper to centralize status handling
    private void respondWithProperStatus(Context ctx, Object resultObj) {
        String json = gson.toJson(resultObj);
        ctx.contentType("application/json");

        String message = null;
        try {
            if (resultObj instanceof CreateGameResult) message = ((CreateGameResult) resultObj).getMessage();
            else if (resultObj instanceof ListGamesResult) message = ((ListGamesResult) resultObj).getMessage();
            else if (resultObj instanceof JoinGameResult) message = ((JoinGameResult) resultObj).getMessage();
        } catch (Exception ignored) {}

        if (message != null) {
            String msgLower = message.toLowerCase();
            if (msgLower.contains("unauthorized")) ctx.status(401).result(json);
            else if (msgLower.contains("bad request")) ctx.status(400).result(json);
            else if (msgLower.contains("taken")) ctx.status(403).result(json);
            else ctx.status(500).result(json);
        } else {
            ctx.status(200).result(json);
        }
    }
}
