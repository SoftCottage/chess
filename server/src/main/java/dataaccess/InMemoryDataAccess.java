package dataaccess;

import model.UserData;
import model.GameData;
import model.AuthData;

import java.util.*;

public class InMemoryDataAccess implements DataAccess {
    private final Map<String, UserData> users = new HashMap<>();
    private final Map<Integer, GameData> games = new HashMap<>();
    private final Map<String, AuthData> auths = new HashMap<>();
    private int nextGameID = 1;

    @Override
    public void clearDatabase() throws DataAccessException {
        try {
            users.clear();
            games.clear();
            auths.clear();
            nextGameID = 1;
        } catch (Exception ex) {
            throw new DataAccessException("Error clearing database", ex);
        }
    }

    @Override
    public void createUser(UserData u) throws DataAccessException {
        if (u == null || u.username() == null) throw new DataAccessException("Invalid user");
        users.put(u.username(), u);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        UserData u = users.get(username);
        if (u == null) throw new DataAccessException("User not found");
        return u;
    }

    @Override
    public void createAuth(AuthData a) throws DataAccessException {
        if (a == null || a.authToken() == null) throw new DataAccessException("Invalid auth");
        auths.put(a.authToken(), a);
    }

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        AuthData a = auths.get(token);
        if (a == null) throw new DataAccessException("Auth not found");
        return a;
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {
        if (token == null || !auths.containsKey(token)) {
            throw new DataAccessException("Auth not found");
        }
        auths.remove(token);
    }

    @Override
    public boolean isValidAuthToken(String token) {
        return auths.containsKey(token);
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        if (gameName == null) throw new DataAccessException("Invalid game name");
        int id = nextGameID++;
        games.put(id, new GameData(id, null, null, gameName, null));
        return id;
    }

    @Override
    public List<GameData> listGames() {
        return new ArrayList<>(games.values());
    }

    @Override
    public GameData getGameByID(int id) {
        return games.get(id);
    }

    @Override
    public void updateGame(GameData game) {
        if (game != null) {
            games.put(game.gameID(), game);
        }
    }
}
