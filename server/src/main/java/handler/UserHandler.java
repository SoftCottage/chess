package handler;

import io.javalin.http.Context;
import service.UserService;
import model.RegisterRequest;
import model.RegisterResult;
import com.google.gson.Gson;
import model.LoginRequest;
import model.LoginResult;
import model.LogoutRequest;
import model.LogoutResult;

public class UserHandler {

    private final UserService userService;
    private final Gson gson;

    public UserHandler(UserService userService) {
        this.userService = userService;
        this.gson = new Gson();
    }

    public void handleRegister(Context ctx) {
        try {
            // Parse the request body as RegisterRequest
            RegisterRequest request = gson.fromJson(ctx.body(), RegisterRequest.class);

            // Call the service
            RegisterResult result = userService.register(request);

            // Serialize response
            String jsonResult = gson.toJson(result);

            // Set HTTP status based on error or success
            if (result.getMessage() != null) {
                if (result.getMessage().contains("bad request")) {
                    ctx.status(400).result(jsonResult);
                } else if (result.getMessage().contains("already taken")) {
                    ctx.status(403).result(jsonResult);
                } else {
                    ctx.status(500).result(jsonResult);
                }
            } else {
                ctx.status(200).result(jsonResult);
            }

        } catch (Exception e) {
            // Unexpected error
            String errorJson = gson.toJson(new RegisterResult("Error: " + e.getMessage()));
            ctx.status(500).result(errorJson);
        }
    }

    public void handleLogin(Context ctx) {
        try {
            // Parse request
            LoginRequest request = gson.fromJson(ctx.body(), LoginRequest.class);

            // Call service
            LoginResult result = userService.login(request);

            // Convert to JSON
            String json = gson.toJson(result);
            ctx.contentType("application/json");

            // Determine status code
            if (result.getMessage() != null) {
                if (result.getMessage().contains("bad request")) {
                    ctx.status(400).result(json);
                } else if (result.getMessage().contains("unauthorized")) {
                    ctx.status(401).result(json);
                } else {
                    ctx.status(500).result(json);
                }
            } else {
                ctx.status(200).result(json);
            }

        } catch (Exception e) {
            String errorJson = gson.toJson(new LoginResult("Error: " + e.getMessage()));
            ctx.status(500).result(errorJson);
        }
    }

    public void handleLogout(Context ctx) {
        try {
            // Prefer header for auth token
            String authHeader = ctx.header("Authorization");

            if (authHeader == null || authHeader.isBlank()) {
                // fallback to body (if tests send it that way)
                LogoutRequest request = gson.fromJson(ctx.body(), LogoutRequest.class);
                if (request == null || request.getAuthToken() == null) {
                    ctx.status(400).result(gson.toJson(new LogoutResult("Error: bad request")));
                    return;
                }
                authHeader = request.getAuthToken();
            }

            LogoutRequest request = new LogoutRequest(authHeader);
            LogoutResult result = userService.logout(request);

            String json = gson.toJson(result);
            ctx.contentType("application/json");

            if (result.getMessage() != null) {
                if (result.getMessage().contains("bad request")) {
                    ctx.status(400).result(json);
                } else if (result.getMessage().contains("unauthorized")) {
                    ctx.status(401).result(json);
                } else {
                    ctx.status(500).result(json);
                }
            } else {
                ctx.status(200).result(json);
            }

        } catch (Exception e) {
            String errorJson = gson.toJson(new LogoutResult("Error: " + e.getMessage()));
            ctx.status(500).result(errorJson);
        }
    }

}
