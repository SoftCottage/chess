package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.ClearResult;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        clearService.clear();
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
        dataAccess.createUser(new UserData("user1", "pass1", "user1@mail.com"));
        dataAccess.createUser(new UserData("user2", "pass2", "user2@mail.com"));

        dataAccess.createAuth(new AuthData("token1", "user1"));
        dataAccess.createAuth(new AuthData("token2", "user2"));

        dataAccess.createGame("Test Game 1");
        dataAccess.createGame("Test Game 2");

        List<GameData> gamesBefore = dataAccess.listGames();
        assertEquals(2, gamesBefore.size(), "Database should have 2 games before clearing");

        ClearResult result = clearService.clear();
        assertEquals("Clear succeeded", result.getMessage());

        List<GameData> gamesAfter = dataAccess.listGames();
        assertEquals(0, gamesAfter.size(), "Games list should be empty after clearing");

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
