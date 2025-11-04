package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthInMemoryDataAccessTests {

    private static MySqlDataAccess dao;

    @BeforeAll
    public static void setup() throws DataAccessException {
        dao = new MySqlDataAccess();
        DatabaseManager.createDatabase();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            // Drop the table if it exists to ensure correct schema
            stmt.executeUpdate("DROP TABLE IF EXISTS auths");

            // Recreate the table with correct columns
            stmt.executeUpdate("""
                CREATE TABLE auths (
                    auth_token VARCHAR(100) PRIMARY KEY,
                    username VARCHAR(50) NOT NULL
                );
            """);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create auths table", e);
        }
    }

    @BeforeEach
    public void clearAuths() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM auths");
        } catch (SQLException e) {
            throw new DataAccessException("Failed to clear auths table", e);
        }
    }


    // createAuth tests
    @Test
    @DisplayName("createAuth stores auth token and username")
    @Order(1)
    public void createAuthStoresToken() throws DataAccessException {
        AuthData auth = new AuthData(UUID.randomUUID().toString(), "alice");
        dao.createAuth(auth);

        AuthData retrieved = dao.getAuth(auth.authToken());
        Assertions.assertNotNull(retrieved, "Retrieved auth should not be null");
        Assertions.assertEquals("alice", retrieved.username());
        Assertions.assertEquals(auth.authToken(), retrieved.authToken());
    }

    @Test
    @DisplayName("createAuth throws exception for null token")
    @Order(2)
    public void createAuthThrowsExceptionForNullToken() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            dao.createAuth(new AuthData(null, "alice"));
        }, "Creating auth with null token should throw exception");
    }

    // getAuth tests
    @Test
    @DisplayName("getAuth returns existing auth token")
    @Order(3)
    public void getAuthReturnsExistingToken() throws DataAccessException {
        AuthData auth = new AuthData(UUID.randomUUID().toString(), "bob");
        dao.createAuth(auth);

        AuthData retrieved = dao.getAuth(auth.authToken());
        Assertions.assertNotNull(retrieved, "Retrieved auth should not be null");
        Assertions.assertEquals(auth.authToken(), retrieved.authToken());
        Assertions.assertEquals("bob", retrieved.username());
    }

    @Test
    @DisplayName("getAuth returns null for nonexistent token")
    @Order(4)
    public void getAuthReturnsNullForNonexistentToken() throws DataAccessException {
        AuthData retrieved = dao.getAuth("nonexistentToken");
        Assertions.assertNull(retrieved, "Nonexistent token should return null");
    }

    // deleteAuth tests
    @Test
    @DisplayName("deleteAuth removes existing token")
    @Order(5)
    public void deleteAuthRemovesExistingToken() throws DataAccessException {
        AuthData auth = new AuthData(UUID.randomUUID().toString(), "charlie");
        dao.createAuth(auth);

        dao.deleteAuth(auth.authToken());

        AuthData retrieved = dao.getAuth(auth.authToken());
        Assertions.assertNull(retrieved, "Auth token should be deleted");
    }

    @Test
    @DisplayName("deleteAuth does nothing for nonexistent token")
    @Order(6)
    public void deleteAuthDoesNothingForNonexistentToken() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> dao.deleteAuth("nonexistentToken"),
                "Deleting a nonexistent token should not throw exception");
    }

    // isValidAuthToken tests
    @Test
    @DisplayName("isValidAuthToken returns true for existing token")
    @Order(7)
    public void isValidAuthTokenReturnsTrueForExistingToken() throws DataAccessException {
        AuthData auth = new AuthData(UUID.randomUUID().toString(), "dave");
        dao.createAuth(auth);

        boolean valid = dao.isValidAuthToken(auth.authToken());
        Assertions.assertTrue(valid, "Token should be valid");
    }

    @Test
    @DisplayName("isValidAuthToken returns false for nonexistent token")
    @Order(8)
    public void isValidAuthTokenReturnsFalseForNonexistentToken() throws DataAccessException {
        boolean valid = dao.isValidAuthToken("fakeToken");
        Assertions.assertFalse(valid, "Nonexistent token should be invalid");
    }
}
