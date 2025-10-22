package dataaccess;

import model.UserData;
import model.GameData;
import model.AuthData;
import chess.ChessGame;

import java.util.*;

public class DataAccess {

    private final Map<String, UserData> users = new HashMap<>();
    private final Map<Integer, GameData> games = new HashMap<>();
    private final Map<String, AuthData> auths = new HashMap<>();
    private int nextGameID = 1; // auto-increment game ID

    /** Clears all data */
    public void clearDatabase() throws DataAccessException {
        try {
            users.clear();
            games.clear();
            auths.clear();
        } catch (Exception ex) {
            throw new DataAccessException("Error clearing database", ex);
        }
    }

    // ------------------------
    // User methods
    // ------------------------
    public void createUser(UserData u) throws DataAccessException {
        if (users.containsKey(u.username())) throw new DataAccessException("User already exists");
        users.put(u.username(), u);
    }

    public UserData getUser(String username) throws DataAccessException {
        UserData u = users.get(username);
        if (u == null) throw new DataAccessException("User not found");
        return u;
    }

    // ------------------------
    // Game methods
    // ------------------------
    public GameData createGame(String gameName) {
        int id = nextGameID++;
        GameData g = new GameData(id, null, null, gameName, new ChessGame());
        games.put(id, g);
        return g;
    }

    public GameData getGame(int gameID) throws DataAccessException {
        GameData g = games.get(gameID);
        if (g == null) throw new DataAccessException("Game not found");
        return g;
    }

    public List<GameData> listGames() {
        return new ArrayList<>(games.values());
    }

    public void updateGame(GameData g) throws DataAccessException {
        if (!games.containsKey(g.gameID())) throw new DataAccessException("Game not found");
        games.put(g.gameID(), g);
    }

    // ------------------------
    // Auth methods
    // ------------------------
    public void createAuth(AuthData a) {
        auths.put(a.authToken(), a);
    }

    public AuthData getAuth(String token) throws DataAccessException {
        AuthData a = auths.get(token);
        if (a == null) throw new DataAccessException("Auth not found");
        return a;
    }

    public void deleteAuth(String token) throws DataAccessException {
        if (!auths.containsKey(token)) throw new DataAccessException("Auth not found");
        auths.remove(token);
    }
}
