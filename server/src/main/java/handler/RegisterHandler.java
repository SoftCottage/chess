package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import model.RegisterRequest;
import model.RegisterResult;
import service.RegisterService;

public class RegisterHandler {
    private final RegisterService registerService;
    private final Gson gson = new Gson();

    public RegisterHandler(RegisterService registerService) {
        this.registerService = registerService;
    }

    public void handle(Context ctx) {
        try {
            RegisterRequest request = gson.fromJson(ctx.body(), RegisterRequest.class);
            RegisterResult result = registerService.register(request);

            // Success
            if (result.getMessage() == null) {
                // success result contains username and authToken
                ctx.status(200).result(gson.toJson(result));
                return;
            }

            // Failure: check messages to determine status code
            String msg = result.getMessage().toLowerCase();
            if (msg.contains("bad request")) {
                ctx.status(400).result(gson.toJson(result));
            } else if (msg.contains("already taken")) {
                ctx.status(403).result(gson.toJson(result));
            } else {
                ctx.status(500).result(gson.toJson(result));
            }
        } catch (Exception e) {
            // Unexpected parsing / runtime error
            RegisterResult err = new RegisterResult("Error: " + e.getMessage());
            ctx.status(500).result(gson.toJson(err));
        }
    }
}
