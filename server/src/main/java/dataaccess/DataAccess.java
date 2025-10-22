package dataaccess;

import java.util.HashMap;
import java.util.Map;

public class DataAccess {

    private final Map<String, Object> users = new HashMap<>();
    private final Map<String, Object> games = new HashMap<>();
    private final Map<String, Object> auths = new HashMap<>();

    // Clears all data
    public void clearDatabase() throws DataAccessException {
        try {
            users.clear();
            games.clear();
            auths.clear();
        } catch (Exception ex) {
            throw new DataAccessException("Error clearing database", ex);
        }
    }
}
