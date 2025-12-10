package client;


import model.*;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        int port = server.run(0);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    public void clearDB() throws Exception {
        facade.clear();
    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }

    @Test
    public void registerSuccess() throws Exception {
        var res = facade.register("bob", "cool", "email");
        assertNotNull(res.authToken());
        assertEquals("bob", res.username());
    }

    @Test
    public void registerFailDuplicateUser() throws Exception {
        facade.register("sam", "pass", "e");
        assertThrows(Exception.class,
                () -> facade.register("sam", "pass", "e2"));
    }

    @Test
    public void loginSuccess() throws Exception {
        facade.register("fred", "pass", "x");
        var out = facade.login("fred", "pass");
        assertNotNull(out.authToken());
    }

    @Test
    public void loginFailWrongPassword() throws Exception {
        facade.register("amy", "mypw", "xx");
        assertThrows(Exception.class,
                () -> facade.login("amy", "wrongpw"));
    }

    @Test
    public void logoutSuccess() throws Exception {
        var reg = facade.register("tim", "pw", "x");
        var res = facade.logout(reg.authToken());
        assertNotNull(res);
    }

    @Test
    public void logoutBadToken() {
        assertThrows(Exception.class,
                () -> facade.logout("badtoken"));
    }

    @Test
    public void createGameSuccess() throws Exception {
        var reg = facade.register("kyle", "p", "e");
        var game = facade.createGame("MyGame", reg.authToken());
        assertTrue(game.gameID() > 0);
    }

    @Test
    public void createGameFailNoAuth() {
        assertThrows(Exception.class,
                () -> facade.createGame("BadGame", null));
    }

    @Test
    public void listGamesSuccess() throws Exception {
        var reg = facade.register("joel", "pw", "e");
        facade.createGame("Game1", reg.authToken());
        facade.createGame("Game2", reg.authToken());

        GameData[] games = facade.listGames(reg.authToken());
        assertEquals(2, games.length);
    }

    @Test
    public void listGamesFailUnauthorized() {
        assertThrows(Exception.class,
                () -> facade.listGames("badtoken"));
    }

    @Test
    public void joinGameSuccess() throws Exception {
        var reg = facade.register("al", "pw", "e");
        var game = facade.createGame("Game", reg.authToken());

        var res = facade.joinGame(game.gameID(), chess.ChessGame.TeamColor.WHITE, reg.authToken());
        assertNotNull(res);
    }

    @Test
    public void joinGameFailInvalidToken() throws Exception {
        var reg = facade.register("jim", "pw", "e");
        var game = facade.createGame("G", reg.authToken());

        assertThrows(Exception.class,
                () -> facade.joinGame(game.gameID(), chess.ChessGame.TeamColor.WHITE, "badtoken"));
    }
}
