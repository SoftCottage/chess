package passoff.service;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import service.ClearService;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.UserData;
import model.GameData;
import model.AuthData;
import model.ClearResult;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClearServiceTest {

    private static DataAccess dataAccess;
    private static ClearService clearService;

    @BeforeAll
    public static void setup() {
        dataAccess = new DataAccess();
        clearService = new ClearService(dataAccess);
    }

    @BeforeEach
    public void beforeEach() throws DataAccessException {
        clearService.clear(); // ensure clean state before each test
    }

    @Test
    @DisplayName("Clear empty database")
    public void clearEmptyDatabase() {
        assertDoesNotThrow(() -> {
            ClearResult result = clearService.clear();
            assertEquals("Clear succeeded", result.getMessage());
        });
    }

    @Test
    @DisplayName("Clear database with users, games, and auths")
    public void clearPopulatedDatabase() throws DataAccessException {
        // Add users
        dataAccess.createUser(new UserData("user1", "pass1", "user1@mail.com"));
        dataAccess.createUser(new UserData("user2", "pass2", "user2@mail.com"));

        // Add auth tokens
        dataAccess.createAuth(new AuthData("token1", "user1"));
        dataAccess.createAuth(new AuthData("token2", "user2"));

        // Add games
        dataAccess.createGame("Test Game 1");
        dataAccess.createGame("Test Game 2");

        // Verify database populated
        List<GameData> gamesBefore = dataAccess.listGames();
        assertEquals(2, gamesBefore.size(), "Database should have 2 games before clearing");

        // Perform clear
        ClearResult result = clearService.clear();
        assertEquals("Clear succeeded", result.getMessage());

        // Verify database empty
        List<GameData> gamesAfter = dataAccess.listGames();
        assertEquals(0, gamesAfter.size(), "Games list should be empty after clearing");

        // Ensure users and auths are gone
        assertThrows(DataAccessException.class, () -> dataAccess.getUser("user1"));
        assertThrows(DataAccessException.class, () -> dataAccess.getAuth("token1"));
    }

    @Test
    @DisplayName("Multiple clears in a row")
    public void multipleClears() {
        for (int i = 0; i < 3; i++) {
            ClearResult result = assertDoesNotThrow(() -> clearService.clear());
            assertEquals("Clear succeeded", result.getMessage());
        }
    }
}
