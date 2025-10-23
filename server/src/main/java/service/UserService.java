package service;
//comment to commit and push
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.UserData;
import model.AuthData;
import model.RegisterRequest;
import model.RegisterResult;
import model.LoginRequest;
import model.LoginResult;
import model.LogoutRequest;
import model.LogoutResult;

import java.util.UUID;

public class UserService {

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    /**
     * Registers a new user.
     * @param request the registration request containing username, password, email
     * @return the registration result containing username and auth token, or error message
     */
    public RegisterResult register(RegisterRequest request) {
        try {
            // Validate request
            if (request.getUsername() == null || request.getPassword() == null || request.getEmail() == null) {
                return new RegisterResult("Error: bad request");
            }

            // Check if username already exists
            try {
                dataAccess.getUser(request.getUsername());
                return new RegisterResult("Error: already taken");
            } catch (DataAccessException ignored) {
                // User doesn't exist — continue
            }

            // Create user
            UserData newUser = new UserData(request.getUsername(), request.getPassword(), request.getEmail());
            dataAccess.createUser(newUser);

            // Generate auth token
            String token = UUID.randomUUID().toString();
            dataAccess.createAuth(new AuthData(token, request.getUsername()));

            return new RegisterResult(request.getUsername(), token);

        } catch (DataAccessException e) {
            return new RegisterResult("Error: database failure - " + e.getMessage());
        } catch (Exception e) {
            return new RegisterResult("Error: unexpected failure - " + e.getMessage());
        }
    }

    public LoginResult login(LoginRequest request) {
        try {
            // Validate request
            if (request.getUsername() == null || request.getPassword() == null) {
                return new LoginResult("Error: bad request");
            }

            // Retrieve user
            UserData user = dataAccess.getUser(request.getUsername());

            // Check password (plain text for now — later hash)
            if (!user.password().equals(request.getPassword())) {
                return new LoginResult("Error: unauthorized");
            }

            // Generate new auth token
            String token = UUID.randomUUID().toString();
            dataAccess.createAuth(new AuthData(token, request.getUsername()));

            return new LoginResult(request.getUsername(), token);

        } catch (DataAccessException e) {
            if (e.getMessage().contains("not found")) {
                return new LoginResult("Error: unauthorized");
            }
            return new LoginResult("Error: database failure - " + e.getMessage());
        } catch (Exception e) {
            return new LoginResult("Error: unexpected failure - " + e.getMessage());
        }
    }

    public LogoutResult logout(LogoutRequest request) {
        try {
            // Validate request
            if (request.getAuthToken() == null) {
                return new LogoutResult("Error: bad request");
            }

            // Try to delete the auth token
            dataAccess.deleteAuth(request.getAuthToken());

            // Successful logout
            return new LogoutResult(null);

        } catch (DataAccessException e) {
            if (e.getMessage().contains("Auth not found")) {
                return new LogoutResult("Error: unauthorized");
            }
            return new LogoutResult("Error: database failure - " + e.getMessage());
        } catch (Exception e) {
            return new LogoutResult("Error: unexpected failure - " + e.getMessage());
        }
    }

}
