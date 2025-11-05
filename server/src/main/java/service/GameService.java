package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.*;
import requestresult.*;

import java.util.List;

public class GameService {
    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    // Create Game
    public CreateGameResult createGame(CreateGameRequest request) {
        try {
            if (request == null || request.getGameName() == null || request.getGameName().isBlank()) {
                return new CreateGameResult("Error: bad request");
            }

            AuthData auth = dataAccess.getAuth(request.getAuthToken());
            if (auth == null) {
                return new CreateGameResult("Error: unauthorized");
            }

            int gameID = dataAccess.createGame(request.getGameName());
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

            if (!dataAccess.isValidAuthToken(request.getAuthToken())) {
                return new ListGamesResult("Error: unauthorized");
            }

            List<GameData> games = dataAccess.listGames();
            return new ListGamesResult(games);

        } catch (Exception e) {
            return new ListGamesResult("Error: unexpected failure - " + e.getMessage());
        }
    }

    public JoinGameResult joinGame(JoinGameRequest request) {
        try {
            if (request == null
                    || request.getAuthToken() == null || request.getAuthToken().isBlank()
                    || request.getGameID() == null
                    || request.getPlayerColor() == null) {
                return new JoinGameResult("Error: bad request");
            }

            AuthData auth = dataAccess.getAuth(request.getAuthToken());
            if (auth == null) {
                return new JoinGameResult("Error: unauthorized");
            }

            GameData game = dataAccess.getGameByID(request.getGameID());
            if (game == null) {
                return new JoinGameResult("Error: bad request");
            }

            String color = request.getPlayerColor();

            // WHITE/BLACK
            if ("WHITE/BLACK".equalsIgnoreCase(color)) {
                if (game.whiteUsername() == null) {
                    color = "WHITE";
                } else if (game.blackUsername() == null) {
                    color = "BLACK";
                } else {
                    return new JoinGameResult("Error: game full");
                }
            }

            // Assign color
            if (color.equalsIgnoreCase("WHITE")) {
                if (game.whiteUsername() != null) {
                    return new JoinGameResult("Error: already taken");
                }
                game = game.withWhiteUsername(auth.username());
            } else if (color.equalsIgnoreCase("BLACK")) {
                if (game.blackUsername() != null) {
                    return new JoinGameResult("Error: already taken");
                }
                game = game.withBlackUsername(auth.username());
            } else {
                return new JoinGameResult("Error: bad request");
            }

            dataAccess.updateGame(game);

            return new JoinGameResult();

        } catch (DataAccessException e) {
            String msg = e.getMessage().toLowerCase().contains("auth") ? "Error: unauthorized" : "Error: " + e.getMessage();
            return new JoinGameResult(msg);
        } catch (Exception e) {
            return new JoinGameResult("Error: unexpected failure - " + e.getMessage());
        }
    }

}
