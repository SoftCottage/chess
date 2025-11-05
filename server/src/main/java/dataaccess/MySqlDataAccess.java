package dataaccess;

import model.UserData;
import model.AuthData;
import model.GameData;
import chess.ChessGame;
import java.sql.*;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MySqlDataAccess implements DataAccess {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void clearDatabase() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DELETE FROM Games");
                stmt.executeUpdate("DELETE FROM Auths");
                stmt.executeUpdate("DELETE FROM Users");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to clear database", e);
        }
    }

    @Override
    public void createUser(UserData u) throws DataAccessException {
        if (u == null || u.username() == null) throw new DataAccessException("Invalid user");
        String sql = "INSERT INTO Users (username, password_hash, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.username());
            stmt.setString(2, u.password());
            stmt.setString(3, u.email());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create user", e);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String sql = "SELECT username, password_hash, email FROM Users WHERE username = ?";
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
                    throw new DataAccessException("User not found");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get user", e);
        }
    }

    @Override
    public void createAuth(AuthData a) throws DataAccessException {
        if (a == null || a.authToken() == null) throw new DataAccessException("Invalid auth");
        String sql = "INSERT INTO Auths (auth_token, username) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, a.authToken());
            stmt.setString(2, a.username());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create auth", e);
        }
    }

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        String sql = "SELECT auth_token, username FROM Auths WHERE auth_token = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new AuthData(rs.getString("auth_token"), rs.getString("username"));
                } else {
                    throw new DataAccessException("Auth not found");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get auth", e);
        }
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {
        String sql = "DELETE FROM Auths WHERE auth_token = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            int rows = stmt.executeUpdate();
            if (rows == 0) throw new DataAccessException("Auth not found");
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete auth", e);
        }
    }

    @Override
    public boolean isValidAuthToken(String token) throws DataAccessException {
        String sql = "SELECT 1 FROM Auths WHERE auth_token = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to validate auth token", e);
        }
    }


    @Override
    public int createGame(String gameName) throws DataAccessException {
        if (gameName == null) throw new DataAccessException("Invalid game name");
        String sql = "INSERT INTO Games (game_name, white_username, black_username, game_state) VALUES (?, ?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, gameName);
            stmt.setNull(2, Types.VARCHAR);
            stmt.setNull(3, Types.VARCHAR);
            stmt.setString(4, gson.toJson(new ChessGame()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create game", e);
        }
        return generatedId;
    }


    @Override
    public List<GameData> listGames() throws DataAccessException {
        List<GameData> games = new ArrayList<>();
        String sql = "SELECT game_id, white_username, black_username, game_name, game_state FROM Games";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ChessGame game = gson.fromJson(rs.getString("game_state"), ChessGame.class);
                games.add(new GameData(
                        rs.getInt("game_id"),
                        rs.getString("white_username"),
                        rs.getString("black_username"),
                        rs.getString("game_name"),
                        game
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to list games", e);
        }
        return games;
    }

    @Override
    public GameData getGameByID(int id) throws DataAccessException {
        String sql = "SELECT game_id, white_username, black_username, game_name, game_state FROM Games WHERE game_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ChessGame game = gson.fromJson(rs.getString("game_state"), ChessGame.class);
                    return new GameData(
                            rs.getInt("game_id"),
                            rs.getString("white_username"),
                            rs.getString("black_username"),
                            rs.getString("game_name"),
                            game
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get game by ID", e);
        }
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        String sql = "UPDATE Games SET white_username = ?, black_username = ?, game_state = ? WHERE game_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, gameData.whiteUsername());
            stmt.setString(2, gameData.blackUsername());
            stmt.setString(3, gson.toJson(gameData.game()));
            stmt.setInt(4, gameData.gameID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update game", e);
        }
    }
}
