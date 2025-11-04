package dataaccess;

import model.GameData;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameInMemoryDataAccessTests {

    private static MySqlDataAccess dao;

    @BeforeAll
    public static void setup() throws DataAccessException {
        dao = new MySqlDataAccess();
        DatabaseManager.createDatabase();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            // Drop the table if it exists to avoid old schema conflicts
            stmt.executeUpdate("DROP TABLE IF EXISTS games");

            // Create the table with the correct columns
            stmt.executeUpdate("""
            CREATE TABLE games (
                game_id INT AUTO_INCREMENT PRIMARY KEY,
                white_username VARCHAR(50),
                black_username VARCHAR(50),
                game_name VARCHAR(100) NOT NULL
            );
        """);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to set up database for tests", e);
        }
    }

    @BeforeEach
    public void clearGames() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM games");
        } catch (SQLException e) {
            throw new DataAccessException("Failed to clear games table", e);
        }
    }

    // createGame tests
    @Test
    @DisplayName("createGame stores game and returns auto-increment ID")
    @Order(1)
    public void createGameStoresGameAndReturnsID() throws DataAccessException {
        int gameID = dao.createGame("Epic Match");

        Assertions.assertTrue(gameID > 0, "createGame should return positive auto-increment ID");

        GameData retrieved = dao.getGameByID(gameID);
        Assertions.assertNotNull(retrieved, "Game should exist after creation");
        Assertions.assertEquals("Epic Match", retrieved.gameName());
        Assertions.assertNull(retrieved.whiteUsername(), "White username should be null initially");
        Assertions.assertNull(retrieved.blackUsername(), "Black username should be null initially");
    }

    @Test
    @DisplayName("createGame throws exception for null game name")
    @Order(2)
    public void createGameThrowsExceptionForNullName() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            dao.createGame(null);
        }, "Creating a game with null name should throw exception");
    }

    // listGames tests
    @Test
    @DisplayName("listGames returns all games in database")
    @Order(3)
    public void listGamesReturnsAllGames() throws DataAccessException {
        int id1 = dao.createGame("Game One");
        int id2 = dao.createGame("Game Two");

        List<GameData> games = dao.listGames();
        Assertions.assertEquals(2, games.size(), "Should return two games");
        Assertions.assertTrue(games.stream().anyMatch(g -> g.gameName().equals("Game One")));
        Assertions.assertTrue(games.stream().anyMatch(g -> g.gameName().equals("Game Two")));
    }

    @Test
    @DisplayName("listGames returns empty list when no games exist")
    @Order(4)
    public void listGamesReturnsEmptyListWhenNoGames() throws DataAccessException {
        List<GameData> games = dao.listGames();
        Assertions.assertTrue(games.isEmpty(), "No games in DB should return empty list");
    }

    // getGameByID tests
    @Test
    @DisplayName("getGameByID retrieves existing game by ID")
    @Order(5)
    public void getGameByIDReturnsExistingGame() throws DataAccessException {
        int gameID = dao.createGame("Match A");
        GameData game = dao.getGameByID(gameID);

        Assertions.assertNotNull(game, "Game should exist");
        Assertions.assertEquals("Match A", game.gameName());
    }

    @Test
    @DisplayName("getGameByID returns null for nonexistent game")
    @Order(6)
    public void getGameByIDReturnsNullForNonexistentGame() throws DataAccessException {
        GameData game = dao.getGameByID(9999);
        Assertions.assertNull(game, "Nonexistent game ID should return null");
    }

    // updateGame tests
    @Test
    @DisplayName("updateGame updates white and black usernames")
    @Order(7)
    public void updateGameUpdatesUsernames() throws DataAccessException {
        int gameID = dao.createGame("Friendly Match");
        GameData game = dao.getGameByID(gameID);

        GameData updated = game.withWhiteUsername("alice").withBlackUsername("bob");
        dao.updateGame(updated);

        GameData retrieved = dao.getGameByID(gameID);
        Assertions.assertEquals("alice", retrieved.whiteUsername());
        Assertions.assertEquals("bob", retrieved.blackUsername());
        Assertions.assertEquals("Friendly Match", retrieved.gameName());
    }

    @Test
    @DisplayName("updateGame throws exception for null game")
    @Order(8)
    public void updateGameThrowsExceptionForNullGame() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            dao.updateGame(null);
        }, "Updating null game should throw exception");
    }
}
