package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.RegisterRequest;
import model.RegisterResult;
import model.UserData;

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

    // Placeholder methods for future endpoints
    public void login(/*LoginRequest request*/) {
        // TODO: implement login
    }

    public void logout(/*LogoutRequest request*/) {
        // TODO: implement logout
    }
}
