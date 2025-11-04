package dataaccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaInitializer {

    /**
     * Initialize the database schema. Creates tables if they do not exist.
     */
    public static void initialize(Connection connection) throws DataAccessException {
        try (Statement stmt = connection.createStatement()) {

            // Users table
            String createUsers = """
                    CREATE TABLE IF NOT EXISTS Users (
                        username VARCHAR(255) PRIMARY KEY,
                        password VARCHAR(255) NOT NULL,
                        email VARCHAR(255) UNIQUE
                    );
                    """;
            stmt.execute(createUsers);

            // Auth table
            String createAuth = """
                    CREATE TABLE IF NOT EXISTS Auths (
                        auth_token CHAR(36) PRIMARY KEY,
                        username VARCHAR(255) NOT NULL,
                        FOREIGN KEY (username) REFERENCES Users(username) ON DELETE CASCADE
                    );
                    """;
            stmt.execute(createAuth);

            // Games table
            String createGames = """
                    CREATE TABLE IF NOT EXISTS Games (
                        game_id INT AUTO_INCREMENT PRIMARY KEY,
                        white_username VARCHAR(255),
                        black_username VARCHAR(255),
                        game_name VARCHAR(255),
                        game_state TEXT,
                        FOREIGN KEY (white_username) REFERENCES Users(username),
                        FOREIGN KEY (black_username) REFERENCES Users(username)
                    );
                    """;
            stmt.execute(createGames);

        } catch (SQLException e) {
            throw new DataAccessException("Failed to initialize database schema", e);
        }
    }
}
