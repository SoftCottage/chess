package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.RegisterRequest;
import model.RegisterResult;
import model.UserData;

import java.util.UUID;

public class RegisterService {
    private final DataAccess dataAccess;

    public RegisterService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public RegisterResult register(RegisterRequest request) {
        try {
            // Validate request
            if (request.getUsername() == null || request.getPassword() == null || request.getEmail() == null) {
                return new RegisterResult("Error: bad request");
            }

            // Check if username exists
            try {
                dataAccess.getUser(request.getUsername());
                return new RegisterResult("Error: already taken");
            } catch (DataAccessException ignored) {
                // This means the user doesn't exist — continue
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
}
