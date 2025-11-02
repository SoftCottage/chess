package dataaccess;

import model.UserData;
import model.GameData;
import model.AuthData;

import java.sql.ResultSet;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class MySqlDataAccess {

    // User Methods
    public void createUser(UserData u) throws DataAccessException {
        if (u == null || u.username() == null) {
            throw new DataAccessException("Invalid user");
        }

        // Hash the password using bcrypt
        String hashedPassword = BCrypt.hashpw(u.password(), BCrypt.gensalt());

        String sql = "INSERT INTO users (username, password_hash, email) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.username());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, u.email());

            stmt.executeUpdate();
        } catch (Exception ex) {
            throw new DataAccessException("Failed to create user", ex);
        }
    }

    public static void main(String[] args) throws Exception {
        MySqlDataAccess dao = new MySqlDataAccess();
        dao.createUser(new UserData("alice", "mypassword", "alice@mail.com"));
        System.out.println("User created successfully");
    }

    public UserData getUser(String username) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("Invalid username");
        }

        String sql = "SELECT username, password_hash, email FROM users WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new UserData(
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getString("email")
                    );
                } else {
                    return null; // user not found
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException("Failed to get user", ex);
        }
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
