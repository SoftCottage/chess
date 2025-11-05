package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;
import java.sql.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserInMemoryDataAccessTests {

    private static MySqlDataAccess dao;

    @BeforeAll
    public static void setup() throws DataAccessException {
        dao = new MySqlDataAccess();
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    username VARCHAR(50) PRIMARY KEY,
                    password_hash VARCHAR(60) NOT NULL,
                    email VARCHAR(100) NOT NULL
                );
            """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void clearUsers() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM users");
        } catch (SQLException e) {
            throw new DataAccessException("Failed to clear users table", e);
        }
    }

    // createUser tests
    @Test
    @DisplayName("createUser stores user correctly")
    @Order(1)
    public void createUserStoresUserCorrectly() throws DataAccessException {
        UserData user = new UserData("alice", "password123", "alice@mail.com");
        dao.createUser(user);

        UserData retrieved = dao.getUser("alice");
        Assertions.assertEquals("alice", retrieved.username());
        Assertions.assertEquals("alice@mail.com", retrieved.email());
        Assertions.assertEquals("password123", retrieved.password(), "Password should match what was stored");
    }

    @Test
    @DisplayName("createUser throws exception for duplicate username")
    @Order(2)
    public void createUserDuplicateUsernameThrowsException() throws DataAccessException {
        UserData user1 = new UserData("bob", "pass1", "bob@mail.com");
        UserData user2 = new UserData("bob", "pass2", "bob2@mail.com");

        dao.createUser(user1);

        Assertions.assertThrows(DataAccessException.class, () -> dao.createUser(user2),
                "Creating a user with duplicate username should throw exception");
    }

    // getUser tests
    @Test
    @DisplayName("getUser retrieves existing user by username")
    @Order(3)
    public void getUserReturnsExistingUser() throws DataAccessException {
        UserData user = new UserData("charlie", "mypassword", "charlie@mail.com");
        dao.createUser(user);

        UserData retrieved = dao.getUser("charlie");

        Assertions.assertNotNull(retrieved, "Retrieved user should not be null");
        Assertions.assertEquals("charlie", retrieved.username());
        Assertions.assertEquals("charlie@mail.com", retrieved.email());
        Assertions.assertEquals("mypassword", retrieved.password(), "Password should match what was stored");
    }

    @Test
    @DisplayName("getUser throws exception if username does not exist")
    @Order(4)
    public void getUserThrowsForNonexistentUser() {
        Assertions.assertThrows(DataAccessException.class, () -> dao.getUser("nonexistentUser"),
                "Getting a nonexistent user should throw DataAccessException");
    }

}
