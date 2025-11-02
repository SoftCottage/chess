package dataaccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static String databaseName;
    private static String dbUsername;
    private static String dbPassword;
    private static String connectionUrl;

    static {
        loadPropertiesFromResources();
    }

    public static void initialize() throws DataAccessException {
        createDatabase();
        createTables();
        System.out.println("Database initialized.");
    }

    public static void createDatabase() throws DataAccessException {
        var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database", ex);
        }
    }

    private static void createTables() throws DataAccessException {
        try (var conn = getConnection();
             var stmt = conn.createStatement()) {

            var createUsers = """
                CREATE TABLE IF NOT EXISTS users (
                    username VARCHAR(50) NOT NULL PRIMARY KEY,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(100)
                );
            """;

            var createAuth = """
                CREATE TABLE IF NOT EXISTS auth (
                    authToken VARCHAR(255) NOT NULL PRIMARY KEY,
                    username VARCHAR(50) NOT NULL,
                    FOREIGN KEY (username) REFERENCES users(username)
                        ON DELETE CASCADE
                );
            """;

            var createGames = """
                CREATE TABLE IF NOT EXISTS games (
                    gameID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                    whiteUsername VARCHAR(50),
                    blackUsername VARCHAR(50),
                    gameName VARCHAR(100) NOT NULL,
                    game TEXT,
                    FOREIGN KEY (whiteUsername) REFERENCES users(username)
                        ON DELETE SET NULL,
                    FOREIGN KEY (blackUsername) REFERENCES users(username)
                        ON DELETE SET NULL
                );
            """;

            stmt.executeUpdate(createUsers);
            stmt.executeUpdate(createAuth);
            stmt.executeUpdate(createGames);

        } catch (SQLException ex) {
            throw new DataAccessException("failed to create tables", ex);
        }
    }

    static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get connection", ex);
        }
    }

    private static void loadPropertiesFromResources() {
        try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
            if (propStream == null) {
                throw new Exception("Unable to load db.properties");
            }
            Properties props = new Properties();
            props.load(propStream);
            loadProperties(props);
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties", ex);
        }
    }

    private static void loadProperties(Properties props) {
        databaseName = props.getProperty("db.name");
        dbUsername = props.getProperty("db.user");
        dbPassword = props.getProperty("db.password");

        var host = props.getProperty("db.host");
        var port = Integer.parseInt(props.getProperty("db.port"));
        connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
    }
}
