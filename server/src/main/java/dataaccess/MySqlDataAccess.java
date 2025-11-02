package dataaccess;

import model.UserData;
import model.GameData;
import model.AuthData;

import java.sql.ResultSet;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;


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
        if (a == null || a.authToken() == null || a.username() == null) {
            throw new DataAccessException("Invalid AuthData");
        }

        String sql = "INSERT INTO auths (auth_token, username) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, a.authToken());
            stmt.setString(2, a.username());

            stmt.executeUpdate();
        } catch (Exception ex) {
            throw new DataAccessException("Failed to create auth", ex);
        }
    }

    public AuthData getAuth(String token) throws DataAccessException {
        if (token == null) {
            throw new DataAccessException("Invalid token");
        }

        String sql = "SELECT auth_token, username FROM auths WHERE auth_token = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new AuthData(
                            rs.getString("auth_token"),
                            rs.getString("username")
                    );
                } else {
                    return null; // token not found
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException("Failed to get auth", ex);
        }
    }

    public void deleteAuth(String token) throws DataAccessException {
        if (token == null) {
            throw new DataAccessException("Invalid token");
        }

        String sql = "DELETE FROM auths WHERE auth_token = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);
            stmt.executeUpdate();
        } catch (Exception ex) {
            throw new DataAccessException("Failed to delete auth", ex);
        }
    }

    public boolean isValidAuthToken(String token) throws DataAccessException {
        if (token == null) {
            return false;
        }

        String sql = "SELECT 1 FROM auths WHERE auth_token = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (Exception ex) {
            throw new DataAccessException("Failed to validate auth token", ex);
        }
    }

    // Game Methods
    public int createGame(String gameName) throws DataAccessException {
        if (gameName == null) {
            throw new DataAccessException("Invalid game name");
        }

        String sql = "INSERT INTO games (game_name) VALUES (?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, gameName);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // auto-increment ID
                } else {
                    throw new DataAccessException("Failed to retrieve game ID");
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create game", ex);
        }
    }

    public List<GameData> listGames() throws DataAccessException {
        String sql = "SELECT game_id, white_username, black_username, game_name FROM games";

        List<GameData> games = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                games.add(new GameData(
                        rs.getInt("game_id"),
                        rs.getString("white_username"),
                        rs.getString("black_username"),
                        rs.getString("game_name"),
                        null // ChessGame object is null initially
                ));
            }

            return games;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to list games", ex);
        }
    }

    public GameData getGameByID(int id) throws DataAccessException {
        String sql = "SELECT game_id, white_username, black_username, game_name FROM games WHERE game_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new GameData(
                            rs.getInt("game_id"),
                            rs.getString("white_username"),
                            rs.getString("black_username"),
                            rs.getString("game_name"),
                            null
                    );
                } else {
                    return null; // game not found
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to get game by ID", ex);
        }
    }

    public void updateGame(GameData game) throws DataAccessException {
        if (game == null) {
            throw new DataAccessException("Invalid game");
        }

        String sql = "UPDATE games SET white_username = ?, black_username = ?, game_name = ? WHERE game_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, game.whiteUsername());
            stmt.setString(2, game.blackUsername());
            stmt.setString(3, game.gameName());
            stmt.setInt(4, game.gameID());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update game", ex);
        }
    }



}
