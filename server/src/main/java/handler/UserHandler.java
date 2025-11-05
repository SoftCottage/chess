package handler;

import io.javalin.http.Context;
import service.UserService;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;
import com.google.gson.Gson;
import requestresult.LoginRequest;
import requestresult.LoginResult;
import requestresult.LogoutRequest;
import requestresult.LogoutResult;

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

            extracted(result.getMessage(), ctx, jsonResult, "already taken", 403);

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

            extracted(result.getMessage(), ctx, json, "unauthorized", 401);

        } catch (Exception e) {
            String errorJson = gson.toJson(new LoginResult("Error: " + e.getMessage()));
            ctx.status(500).result(errorJson);
        }
    }

    private static void extracted(String result, Context ctx, String json, String unauthorized, int status) {
        if (result != null) {
            if (result.contains("bad request")) {
                ctx.status(400).result(json);
            } else if (result.contains(unauthorized)) {
                ctx.status(status).result(json);
            } else {
                ctx.status(500).result(json);
            }
        } else {
            ctx.status(200).result(json);
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

            extracted(result.getMessage(), ctx, json, "unauthorized", 401);

        } catch (Exception e) {
            String errorJson = gson.toJson(new LogoutResult("Error: " + e.getMessage()));
            ctx.status(500).result(errorJson);
        }
    }
}
