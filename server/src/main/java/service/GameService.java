// GameService.java
package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.*;
import java.util.*;

public class GameService {
    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    /**
     * Creates a new game if the auth token is valid.
     */
    public CreateGameResult createGame(CreateGameRequest request) {
        try {
            if (request == null || request.getGameName() == null || request.getGameName().isBlank()) {
                return new CreateGameResult("Error: bad request");
            }

            // Check auth
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
}
