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
            CreateGameResult result = gameService.createGame(request);
            String json = gson.toJson(result);
            ctx.contentType("application/json");

            if (result.getMessage() != null) {
                if (result.getMessage().toLowerCase().contains("unauthorized")) ctx.status(401).result(json);
                else if (result.getMessage().toLowerCase().contains("bad request")) ctx.status(400).result(json);
                else ctx.status(500).result(json);
            } else {
                ctx.status(200).result(json);
            }

        } catch (Exception e) {
            ctx.status(500).result(gson.toJson(new CreateGameResult("Error: " + e.getMessage())));
        }
    }

    // List Game
    public void listGames(Context ctx) {
        try {
            String authToken = ctx.header("Authorization");
            ListGamesRequest request = new ListGamesRequest(authToken);
            ListGamesResult result = gameService.listGames(request);
            String json = gson.toJson(result);
            ctx.contentType("application/json");

            if (result.getMessage() != null) {
                if (result.getMessage().toLowerCase().contains("unauthorized")) ctx.status(401).result(json);
                else ctx.status(500).result(json);
            } else {
                ctx.status(200).result(json);
            }

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
            JoinGameResult result = gameService.joinGame(request);
            String json = gson.toJson(result);
            ctx.contentType("application/json");

            if (result.getMessage() != null) {
                String msg = result.getMessage().toLowerCase();
                if (msg.contains("unauthorized")) ctx.status(401).result(json);
                else if (msg.contains("bad request")) ctx.status(400).result(json);
                else if (msg.contains("taken")) ctx.status(403).result(json);
                else ctx.status(500).result(json);
            } else {
                ctx.status(200).result(json);
            }

        } catch (Exception e) {
            ctx.status(500).result(gson.toJson(new JoinGameResult("Error: " + e.getMessage())));
        }
    }
}
