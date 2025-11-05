package dataaccess;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {

    private static Properties dbProperties;
    private static boolean initialized = false;

    // Constructor (still optional, but ensures properties are loaded)
    public DatabaseManager() {
        loadPropertiesFromResources();
    }

    /**
     * Load properties from a Properties object (e.g., for testing)
     */
//    public static Properties loadPropertiesFromResources() throws IOException {
//        ensurePropertiesLoaded();
//        return dbProperties;
//    }

    /**
     * Load properties from a given Properties object (used by tests)
     */
    public static void loadProperties(Properties properties) {
        dbProperties = properties;
        initialized = true;
    }


    /**
     * Ensure properties are loaded from resources if not already
     */
    private static void loadPropertiesFromResources() {
        if (dbProperties == null) {
            try (InputStream input = DatabaseManager.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (input == null) {
                    throw new RuntimeException("db.properties not found in resources");
                }
                Properties props = new Properties();
                props.load(input);
                dbProperties = props;
                initialized = true;
            } catch (IOException e) {
                throw new RuntimeException("Failed to load db.properties: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Get a connection to the database.
     */
    public static Connection getConnection() throws DataAccessException {
        loadPropertiesFromResources();

        String host = dbProperties.getProperty("db.host");
        String port = dbProperties.getProperty("db.port");
        String name = dbProperties.getProperty("db.name");
        String user = dbProperties.getProperty("db.user");
        String password = dbProperties.getProperty("db.password", ""); // default to empty

        String jdbcUrl = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", host, port, name);

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
            // Initialize schema if not already done
            SchemaInitializer.initialize(connection);
            return connection;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to connect to database: " + e.getMessage(), e);
        }
    }

    /**
     * Utility method to create the database itself if it doesn't exist.
     */
    public static void createDatabase() throws DataAccessException {
        loadPropertiesFromResources();

        String host = dbProperties.getProperty("db.host");
        String port = dbProperties.getProperty("db.port");
        String name = dbProperties.getProperty("db.name");
        String user = dbProperties.getProperty("db.user");
        String password = dbProperties.getProperty("db.password", "");

        String jdbcUrl = String.format("jdbc:mysql://%s:%s/?serverTimezone=UTC", host, port);

        try (Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
             var stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + name);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create database: " + e.getMessage(), e);
        }
    }

    /**
     * Initialize the database (creates tables)
     */
    public static void initialize() throws DataAccessException, SQLException {
        try (Connection conn = getConnection()) {
            SchemaInitializer.initialize(conn);
        }
    }
}
