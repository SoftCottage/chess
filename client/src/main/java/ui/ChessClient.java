package ui;

import client.ServerFacade;
import model.GameData;
import chess.ChessGame;

import java.util.*;

public class ChessClient {

    private final ServerFacade facade;
    private final Scanner scanner = new Scanner(System.in);

    private String authToken = null;
    private String username = null;

    private List<GameData> lastListedGames = new ArrayList<>();

    public ChessClient(ServerFacade facade) {
        this.facade = facade;
    }

    public void run() {
        preloginLoop();
    }

    private void preloginLoop() {

        printPreloginHelp();

        while (true) {
            System.out.print("\n[Prelogin] Enter command (help for options): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("help")) {
                printPreloginHelp();
            }
            else if (input.equalsIgnoreCase("quit")) {
                System.out.println("Goodbye!");
                return;
            }
            else if (input.equalsIgnoreCase("register")) {
                handleRegister();
            }
            else if (input.equalsIgnoreCase("login")) {
                handleLogin();
            }
            else {
                System.out.println("Unknown command. Type 'help'.");
            }
        }
    }

    private void printPreloginHelp() {
        System.out.println("""
                Prelogin Commands:
                  help       - Show this help message
                  register   - Create a new user account
                  login      - Log into an existing account
                  quit       - Exit the program
                """);
    }

    private void handleRegister() {
        try {
            System.out.print("Choose a username: ");
            String u = scanner.nextLine().trim();

            System.out.print("Choose a password: ");
            String p = scanner.nextLine().trim();

            System.out.print("Enter an email: ");
            String e = scanner.nextLine().trim();

            var res = facade.register(u, p, e);

            authToken = res.authToken();
            username = res.username();

            System.out.println("Registration successful! You are now logged in as " + username);
            postloginLoop();

        } catch (Exception ex) {
            System.out.println("Registration failed: " + ex.getMessage());
        }
    }

    private void handleLogin() {
        try {
            System.out.print("Username: ");
            String u = scanner.nextLine().trim();

            System.out.print("Password: ");
            String p = scanner.nextLine().trim();

            var res = facade.login(u, p);

            authToken = res.authToken();
            username = res.username();

            System.out.println("Login successful! Welcome back, " + username);
            postloginLoop();

        } catch (Exception ex) {
            System.out.println("Login failed: " + ex.getMessage());
        }
    }

    private void postloginLoop() {

        printPostloginHelp();

        while (authToken != null) {
            System.out.print("\n[Postlogin] Enter command (help for options): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("help")) {
                printPostloginHelp();
            }
            else if (input.equalsIgnoreCase("logout")) {
                handleLogout();
            }
            else if (input.equalsIgnoreCase("creategame")) {
                handleCreateGame();
            }
            else if (input.equalsIgnoreCase("listgames")) {
                handleListGames();
            }
            else if (input.equalsIgnoreCase("playgame")) {
                handlePlayGame();
            }
            else if (input.equalsIgnoreCase("observeline")) {  // spelled correctly below
                System.out.println("Did you mean 'observegame'?");
            }
            else if (input.equalsIgnoreCase("observegame")) {
                handleObserveGame();
            }
            else {
                System.out.println("Unknown command. Type 'help'.");
            }
        }
    }

    private void printPostloginHelp() {
        System.out.println("""
                Postlogin Commands:
                  help         - Show this help message
                  logout       - Log out of your account
                  creategame   - Create a new chess game
                  listgames    - List existing games
                  playgame     - Join a game as a player
                  observegame  - Observe a game
                """);
    }

    private void handleLogout() {
        try {
            facade.logout(authToken);
            System.out.println("Logged out successfully.");
        } catch (Exception ex) {
            System.out.println("Logout failed: " + ex.getMessage());
        }
        authToken = null;
        username = null;
        lastListedGames.clear();
    }

    private void handleCreateGame() {
        try {
            System.out.print("Enter a name for your new game: ");
            String name = scanner.nextLine().trim();

            var res = facade.createGame(name, authToken);

            System.out.println("Game created with ID: " + res.gameID());
        } catch (Exception ex) {
            System.out.println("Failed to create game: " + ex.getMessage());
        }
    }

    private void handleListGames() {
        try {
            GameData[] games = facade.listGames(authToken);

            lastListedGames = Arrays.asList(games);

            if (games.length == 0) {
                System.out.println("No games available.");
                return;
            }

            System.out.println("\nExisting Games:");
            for (int i = 0; i < games.length; i++) {
                GameData g = games[i];
                String white = g.whiteUsername() != null ? g.whiteUsername() : "—";
                String black = g.blackUsername() != null ? g.blackUsername() : "—";
                System.out.printf("  %d. %-15s White: %-10s Black: %-10s%n",
                        i + 1, g.gameName(), white, black);
            }

        } catch (Exception ex) {
            System.out.println("Failed to list games: " + ex.getMessage());
        }
    }

    private void handlePlayGame() {
        if (lastListedGames.isEmpty()) {
            System.out.println("List games first (use 'listgames').");
            return;
        }

        try {
            System.out.print("Enter game number to join: ");
            int num = Integer.parseInt(scanner.nextLine().trim());

            if (num < 1 || num > lastListedGames.size()) {
                System.out.println("Invalid game number.");
                return;
            }

            GameData game = lastListedGames.get(num - 1);

            ChessGame.TeamColor joinColor = null;

            if (username.equals(game.whiteUsername())) {
                joinColor = ChessGame.TeamColor.WHITE;
                System.out.println("Rejoining game as WHITE...");
            }
            else if (username.equals(game.blackUsername())) {
                joinColor = ChessGame.TeamColor.BLACK;
                System.out.println("Rejoining game as BLACK...");
            }
            else {
                System.out.print("Choose color (WHITE/BLACK): ");
                String colorStr = scanner.nextLine().trim().toUpperCase();

                if (colorStr.equals("WHITE")) {
                    joinColor = ChessGame.TeamColor.WHITE;
                }
                else if (colorStr.equals("BLACK")) {
                    joinColor = ChessGame.TeamColor.BLACK;
                }
                else {
                    System.out.println("Invalid color.");
                    return;
                }
            }

            facade.joinGame(game.gameID(), joinColor, authToken);

            System.out.println("Joined game '" + game.gameName() + "' as " + joinColor);

            drawInitialBoard(joinColor);

        } catch (Exception ex) {
            System.out.println("Failed to join game: " + ex.getMessage());
        }
    }

    private void handleObserveGame() {
        if (lastListedGames.isEmpty()) {
            System.out.println("List games first (use 'listgames').");
            return;
        }

        try {
            System.out.print("Enter game number to observe: ");
            int num = Integer.parseInt(scanner.nextLine().trim());

            if (num < 1 || num > lastListedGames.size()) {
                System.out.println("Invalid game number.");
                return;
            }

            GameData game = lastListedGames.get(num - 1);

            // Observer → color = null
            facade.joinGame(game.gameID(), null, authToken);

            System.out.println("Now observing game: " + game.gameName());

            drawInitialBoard(ChessGame.TeamColor.WHITE); // observers see white perspective

        } catch (Exception ex) {
            System.out.println("Failed to observe game: " + ex.getMessage());
        }
    }

    private void drawInitialBoard(ChessGame.TeamColor perspective) {
        System.out.println("\n=== INITIAL BOARD (" + perspective + " perspective) ===");

        BoardDrawer.draw(perspective);
    }
}
