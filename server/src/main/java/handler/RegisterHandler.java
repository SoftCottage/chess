package handler;

import io.javalin.http.Context;
import service.UserService;
import model.RegisterRequest;
import model.RegisterResult;
import com.google.gson.Gson;

public class RegisterHandler {

    private final UserService userService;
    private final Gson gson;

    public RegisterHandler(UserService userService) {
        this.userService = userService;
        this.gson = new Gson();
    }

    public void handle(Context ctx) {
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
}
