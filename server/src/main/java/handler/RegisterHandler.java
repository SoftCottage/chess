package handler;
//comment to commit and push
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

            // Check for error
            if (result.getMessage() != null) {
                if (result.getMessage().contains("bad request")) {
                    ctx.status(400).json(result);
                } else if (result.getMessage().contains("already taken")) {
                    ctx.status(403).json(result);
                } else {
                    ctx.status(500).json(result);
                }
            } else {
                // Success
                ctx.status(200).json(result);
            }

        } catch (Exception e) {
            ctx.status(500).json(new RegisterResult("Error: " + e.getMessage()));
        }
    }
}
