package service;

import dataaccess.InMemoryDataAccess;
import dataaccess.DataAccessException;
import model.*;
import requestresult.*;

import java.util.List;

public class GameService {
    private final InMemoryDataAccess inMemoryDataAccess;

    public GameService(InMemoryDataAccess inMemoryDataAccess) {
        this.inMemoryDataAccess = inMemoryDataAccess;
    }

    // Create Game
    public CreateGameResult createGame(CreateGameRequest request) {
        try {
            if (request == null || request.getGameName() == null || request.getGameName().isBlank()) {
                return new CreateGameResult("Error: bad request");
            }

            AuthData auth = inMemoryDataAccess.getAuth(request.getAuthToken());
            if (auth == null) return new CreateGameResult("Error: unauthorized");

            int gameID = inMemoryDataAccess.createGame(request.getGameName());
            return new CreateGameResult(gameID);

        } catch (DataAccessException e) {
            String msg = e.getMessage().toLowerCase().contains("auth") ? "Error: unauthorized" : "Error: " + e.getMessage();
            return new CreateGameResult(msg);
        } catch (Exception e) {
            return new CreateGameResult("Error: " + e.getMessage());
        }
    }

    // List Games
    public ListGamesResult listGames(ListGamesRequest request) {
        try {
            if (request == null || request.getAuthToken() == null || request.getAuthToken().isBlank()) {
                return new ListGamesResult("Error: unauthorized");
            }

            if (!inMemoryDataAccess.isValidAuthToken(request.getAuthToken())) {
                return new ListGamesResult("Error: unauthorized");
            }

            List<GameData> games = inMemoryDataAccess.listGames();
            return new ListGamesResult(games);

        } catch (Exception e) {
            return new ListGamesResult("Error: unexpected failure - " + e.getMessage());
        }
    }

    // Join Games
    public JoinGameResult joinGame(JoinGameRequest request) {
        try {
            if (request == null
                    || request.getAuthToken() == null || request.getAuthToken().isBlank()
                    || request.getGameID() == null
                    || request.getPlayerColor() == null) {
                return new JoinGameResult("Error: bad request");
            }

            AuthData auth = inMemoryDataAccess.getAuth(request.getAuthToken());
            if (auth == null) return new JoinGameResult("Error: unauthorized");

            GameData game = inMemoryDataAccess.getGameByID(request.getGameID());
            if (game == null) return new JoinGameResult("Error: bad request");

            String color = request.getPlayerColor().toUpperCase();

            if (color.equals("WHITE")) {
                if (game.whiteUsername() != null) return new JoinGameResult("Error: already taken");
                game = game.withWhiteUsername(auth.username());
            } else if (color.equals("BLACK")) {
                if (game.blackUsername() != null) return new JoinGameResult("Error: already taken");
                game = game.withBlackUsername(auth.username());
            } else {
                return new JoinGameResult("Error: bad request");
            }

            inMemoryDataAccess.updateGame(game);

            return new JoinGameResult();

        } catch (DataAccessException e) {
            String msg = e.getMessage().toLowerCase().contains("auth") ? "Error: unauthorized" : "Error: " + e.getMessage();
            return new JoinGameResult(msg);
        } catch (Exception e) {
            return new JoinGameResult("Error: unexpected failure - " + e.getMessage());
        }
    }
}
