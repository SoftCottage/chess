package dataaccess;

import model.UserData;
import model.GameData;
import model.AuthData;

import java.util.List;

public interface DataAccess {

    // User methods
    void createUser(UserData u) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    // Auth methods
    void createAuth(AuthData a) throws DataAccessException;

    AuthData getAuth(String token) throws DataAccessException;

    void deleteAuth(String token) throws DataAccessException;

    boolean isValidAuthToken(String token) throws DataAccessException;

    // Game methods
    int createGame(String gameName) throws DataAccessException;

    List<GameData> listGames() throws DataAccessException;

    GameData getGameByID(int id) throws DataAccessException;

    void updateGame(GameData game) throws DataAccessException;

    void clearDatabase() throws DataAccessException;

}
