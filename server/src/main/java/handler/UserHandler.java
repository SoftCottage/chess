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
            RegisterRequest request = gson.fromJson(ctx.body(), RegisterRequest.class);

            RegisterResult result = userService.register(request);

            String jsonResult = gson.toJson(result);

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
            String errorJson = gson.toJson(new RegisterResult("Error: " + e.getMessage()));
            ctx.status(500).result(errorJson);
        }
    }

    public void handleLogin(Context ctx) {
        try {
            LoginRequest request = gson.fromJson(ctx.body(), LoginRequest.class);

            LoginResult result = userService.login(request);

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
            String errorJson = gson.toJson(new LoginResult("Error: " + e.getMessage()));
            ctx.status(500).result(errorJson);
        }
    }

    public void handleLogout(Context ctx) {
        try {
            String authHeader = ctx.header("Authorization");

            if (authHeader == null || authHeader.isBlank()) {
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
