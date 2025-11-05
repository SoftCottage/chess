package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthDataAccessTests {

    private static MySqlDataAccess dao;

    @BeforeAll
    public static void setup() throws DataAccessException {
        dao = new MySqlDataAccess();
        DatabaseManager.createDatabase();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("DROP TABLE IF EXISTS auths");

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
    @DisplayName("getAuth throws DataAccessException for nonexistent token")
    @Order(4)
    public void getAuthThrowsForNonexistentToken() {
        DataAccessException ex = Assertions.assertThrows(DataAccessException.class, () -> {
            dao.getAuth("nonexistentToken");
        });
        Assertions.assertEquals("Auth not found", ex.getMessage(), "Nonexistent token should throw exception");
    }

    @Test
    @DisplayName("deleteAuth removes existing token")
    @Order(5)
    public void deleteAuthRemovesExistingToken() throws DataAccessException {
        AuthData auth = new AuthData(UUID.randomUUID().toString(), "charlie");
        dao.createAuth(auth);

        dao.deleteAuth(auth.authToken());

        DataAccessException ex = Assertions.assertThrows(DataAccessException.class, () -> {
            dao.getAuth(auth.authToken());
        });
        Assertions.assertEquals("Auth not found", ex.getMessage(), "Deleted auth token should not be retrievable");
    }

    @Test
    @DisplayName("deleteAuth throws exception for nonexistent token")
    @Order(6)
    public void deleteAuthThrowsForNonexistentToken() {
        DataAccessException ex = Assertions.assertThrows(DataAccessException.class, () -> {
            dao.deleteAuth("nonexistentToken");
        });
        Assertions.assertEquals("Auth not found", ex.getMessage(), "Deleting nonexistent token should throw exception");
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
