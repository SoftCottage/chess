package dataaccess;

import java.sql.Connection;
import java.sql.Statement;

public class SchemaInitializer {

    public static void ensureTablesExist() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            // USERS table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    username VARCHAR(50) PRIMARY KEY,
                    password_hash VARCHAR(60) NOT NULL,
                    email VARCHAR(100) NOT NULL
                )
            """);

            // AUTHS table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS auths (
                    authToken VARCHAR(64) PRIMARY KEY,
                    username VARCHAR(50) NOT NULL,
                    FOREIGN KEY (username) REFERENCES users(username)
                )
            """);

            // GAMES table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS games (
                    gameID INT AUTO_INCREMENT PRIMARY KEY,
                    whiteUsername VARCHAR(50),
                    blackUsername VARCHAR(50),
                    gameName VARCHAR(100) NOT NULL,
                    gameState JSON,
                    FOREIGN KEY (whiteUsername) REFERENCES users(username),
                    FOREIGN KEY (blackUsername) REFERENCES users(username)
                )
            """);

        } catch (Exception ex) {
            throw new DataAccessException("Failed to create tables", ex);
        }
    }
}
