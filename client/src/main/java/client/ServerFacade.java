package client;

import model.*;
import com.google.gson.Gson;
import model.GameData;
import chess.ChessGame;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class ServerFacade {

    private final String serverUrl;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    // ========================
    // Helpers
    // ========================

    private HttpRequest.Builder auth(HttpRequest.Builder builder, String authToken) {
        if (authToken != null) {
            builder.header("Authorization", authToken);
        }
        return builder;
    }

    private <T> T parseResponse(HttpResponse<String> response, Class<T> type) throws Exception {
        System.out.println("DEBUG RESPONSE: " + response.body());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return gson.fromJson(response.body(), type);
        }
        // error
        var error = gson.fromJson(response.body(), java.util.Map.class);
        throw new Exception((String) error.get("message"));
    }

    // ========================
    // Clear DB (for tests only)
    // DELETE /db
    // ========================
    public ClearResponse clear() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/db"))
                .DELETE()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return parseResponse(response, ClearResponse.class);
    }

    // ========================
    // Register: POST /user
    // ========================
    public RegisterResponse register(String username, String password, String email) throws Exception {

        RegisterRequest req = new RegisterRequest(username, password, email);
        String json = gson.toJson(req);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/user"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return parseResponse(response, RegisterResponse.class);
    }

    // ========================
    // Login: POST /session
    // ========================
    public LoginResponse login(String username, String password) throws Exception {

        LoginRequest req = new LoginRequest(username, password);
        String json = gson.toJson(req);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/session"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return parseResponse(response, LoginResponse.class);
    }

    // ========================
    // Logout: DELETE /session
    // ========================
    public LogoutResponse logout(String authToken) throws Exception {

        HttpRequest.Builder b = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/session"))
                .DELETE()
                .header("Content-Type", "application/json");

        auth(b, authToken);

        HttpResponse<String> response =
                client.send(b.build(), HttpResponse.BodyHandlers.ofString());

        return parseResponse(response, LogoutResponse.class);
    }

    // ========================
    // Create Game: POST /game
    // ========================
    public CreateGameResponse createGame(String gameName, String authToken) throws Exception {

        CreateGameRequest req = new CreateGameRequest(gameName);
        String json = gson.toJson(req);

        HttpRequest.Builder b = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/game"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json");

        auth(b, authToken);

        HttpResponse<String> response =
                client.send(b.build(), HttpResponse.BodyHandlers.ofString());

        return parseResponse(response, CreateGameResponse.class);
    }

    // ========================
    // List Games: GET /game
    // ========================
    public GameData[] listGames(String authToken) throws Exception {

        HttpRequest.Builder b = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/game"))
                .GET();

        auth(b, authToken);

        HttpResponse<String> response =
                client.send(b.build(), HttpResponse.BodyHandlers.ofString());

        ListGamesResponse list = parseResponse(response, ListGamesResponse.class);
        return list.games();
    }

    // ========================
    // Join Game: PUT /game
    // ========================
    public JoinGameResponse joinGame(int gameID, ChessGame.TeamColor color, String authToken) throws Exception {

        String colorString = (color == null ? null : color.toString());
        JoinGameRequest req = new JoinGameRequest(gameID, colorString);
        String json = gson.toJson(req);

        HttpRequest.Builder b = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/game"))
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json");

        auth(b, authToken);

        HttpResponse<String> response =
                client.send(b.build(), HttpResponse.BodyHandlers.ofString());

        return parseResponse(response, JoinGameResponse.class);
    }
}
