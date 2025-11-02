package dataaccess;

import model.UserData;
import model.GameData;
import model.AuthData;

import java.util.List;

public class MySqlDataAccess {

    // User Methods
    public void createUser(UserData u) throws DataAccessException {
        // TODO: implement MySQL insert
    }

    public UserData getUser(String username) throws DataAccessException {
        // TODO: implement MySQL select
        return null;
    }

    // Auth Methods
    public void createAuth(AuthData a) throws DataAccessException {
        // TODO: implement MySQL insert
    }

    public AuthData getAuth(String token) throws DataAccessException {
        // TODO: implement MySQL select
        return null;
    }

    public void deleteAuth(String token) throws DataAccessException {
        // TODO: implement MySQL delete
    }

    public boolean isValidAuthToken(String token) throws DataAccessException {
        // TODO: implement MySQL check
        return false;
    }

    // Game Methods
    public int createGame(String gameName) throws DataAccessException {
        // TODO: implement MySQL insert, return auto-increment ID
        return -1;
    }

    public List<GameData> listGames() throws DataAccessException {
        // TODO: implement MySQL select all
        return List.of();
    }

    public GameData getGameByID(int id) throws DataAccessException {
        // TODO: implement MySQL select by ID
        return null;
    }

    public void updateGame(GameData game) throws DataAccessException {
        // TODO: implement MySQL update
    }
}
