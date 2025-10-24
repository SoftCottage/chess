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

    // ----------------- LIST GAMES TESTS -----------------

    @Test
    @Order(6)
    @DisplayName("List games success")
    public void listGamesSuccess() {
        // Register and login
        userService.register(new RegisterRequest("alice", "pw", "a@mail.com"));
        LoginResult loginRes = userService.login(new LoginRequest("alice", "pw"));
        String authToken = loginRes.getAuthToken();

        // Create a couple of games
        gameService.createGame(new CreateGameRequest("Game One", authToken));
        gameService.createGame(new CreateGameRequest("Game Two", authToken));

        // List games
        ListGamesRequest listReq = new ListGamesRequest(authToken);
        ListGamesResult listRes = gameService.listGames(listReq);

        assertNull(listRes.getMessage(), "Success response should have null message");
        assertNotNull(listRes.getGames(), "Games list should not be null");
        assertEquals(2, listRes.getGames().size(), "Should return 2 games");
    }

    @Test
    @Order(7)
    @DisplayName("List games unauthorized (invalid token)")
    public void listGamesUnauthorized() {
        ListGamesRequest req = new ListGamesRequest("fakeToken");
        ListGamesResult res = gameService.listGames(req);

        assertNotNull(res.getMessage(), "Unauthorized response should have a message");
        assertTrue(res.getMessage().toLowerCase().contains("unauthorized"));
        assertNull(res.getGames(), "Games list should be null on unauthorized");
    }

    @Test
    @Order(8)
    @DisplayName("List games empty list")
    public void listGamesEmpty() {
        // Register and login
        userService.register(new RegisterRequest("bob", "pw", "b@mail.com"));
        LoginResult loginRes = userService.login(new LoginRequest("bob", "pw"));

        // List games without creating any
        ListGamesRequest req = new ListGamesRequest(loginRes.getAuthToken());
        ListGamesResult res = gameService.listGames(req);

        assertNull(res.getMessage(), "Success response should have null message");
        assertNotNull(res.getGames(), "Games list should not be null");
        assertEquals(0, res.getGames().size(), "Games list should be empty");
    }

    @Test
    @Order(9)
    @DisplayName("List games bad request (null request)")
    public void listGamesBadRequest() {
        ListGamesResult res = gameService.listGames(null);

        assertNotNull(res.getMessage(), "Response should have an error message");
        assertTrue(res.getMessage().toLowerCase().contains("unauthorized")
                || res.getMessage().toLowerCase().contains("error"));
        assertNull(res.getGames(), "Games list should be null on bad request");
    }
}
