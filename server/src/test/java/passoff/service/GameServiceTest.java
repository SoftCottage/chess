package passoff.service;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import service.GameService;
import service.UserService;
import model.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTest {

    private static DataAccess dataAccess;
    private static GameService gameService;
    private static UserService userService;

    @BeforeAll
    public static void setup() {
        dataAccess = new DataAccess();
        gameService = new GameService(dataAccess);
        userService = new UserService(dataAccess);
    }

    @BeforeEach
    public void beforeEach() throws DataAccessException {
        // ensure clean database state
        dataAccess.clearDatabase();
    }

    // ----------------- CREATE GAME TESTS -----------------

    @Test
    @Order(1)
    @DisplayName("Create game success")
    public void createGameSuccess() {
        // Register and login to get authToken
        RegisterRequest regReq = new RegisterRequest("alice", "pw", "a@mail.com");
        userService.register(regReq);
        LoginResult loginRes = userService.login(new LoginRequest("alice", "pw"));
        assertNotNull(loginRes.getAuthToken(), "Auth token should be present");

        // Create game request
        CreateGameRequest gameReq = new CreateGameRequest("My First Game", loginRes.getAuthToken());
        CreateGameResult gameRes = gameService.createGame(gameReq);

        assertNull(gameRes.getMessage(), "Success should have null message");
        assertNotNull(gameRes.getGameID(), "GameID should be assigned");
    }

    @Test
    @Order(2)
    @DisplayName("Create game bad request (missing game name)")
    public void createGameBadRequest() {
        // Register and login first
        RegisterRequest regReq = new RegisterRequest("bob", "pw", "b@mail.com");
        userService.register(regReq);
        LoginResult loginRes = userService.login(new LoginRequest("bob", "pw"));

        // Missing game name
        CreateGameRequest gameReq = new CreateGameRequest(null, loginRes.getAuthToken());
        CreateGameResult gameRes = gameService.createGame(gameReq);

        assertNotNull(gameRes.getMessage());
        assertTrue(gameRes.getMessage().toLowerCase().contains("bad request"));
        assertNull(gameRes.getGameID());
    }

    @Test
    @Order(3)
    @DisplayName("Create game unauthorized (invalid token)")
    public void createGameUnauthorized() {
        // Attempt with fake token
        CreateGameRequest gameReq = new CreateGameRequest("Unauthorized Game", "fakeToken");
        CreateGameResult gameRes = gameService.createGame(gameReq);

        assertNotNull(gameRes.getMessage());
        assertTrue(gameRes.getMessage().toLowerCase().contains("unauthorized"));
        assertNull(gameRes.getGameID());
    }

    @Test
    @Order(4)
    @DisplayName("Create multiple games success")
    public void createMultipleGames() {
        // Register and login
        userService.register(new RegisterRequest("charlie", "pw", "c@mail.com"));
        LoginResult loginRes = userService.login(new LoginRequest("charlie", "pw"));

        // Create first game
        CreateGameRequest gameReq1 = new CreateGameRequest("Game One", loginRes.getAuthToken());
        CreateGameResult res1 = gameService.createGame(gameReq1);

        // Create second game
        CreateGameRequest gameReq2 = new CreateGameRequest("Game Two", loginRes.getAuthToken());
        CreateGameResult res2 = gameService.createGame(gameReq2);

        assertNull(res1.getMessage());
        assertNull(res2.getMessage());
        assertNotEquals(res1.getGameID(), res2.getGameID(), "Each game should have unique ID");
    }

    @Test
    @Order(5)
    @DisplayName("Create game server error simulation (null request)")
    public void createGameServerError() {
        // Force null request
        CreateGameResult res = null;
        try {
            res = gameService.createGame(null);
        } catch (Exception e) {
            fail("createGame() should handle null gracefully without throwing");
        }

        assertNotNull(res);
        assertNotNull(res.getMessage());
        assertTrue(res.getMessage().toLowerCase().contains("error"));
    }
}
