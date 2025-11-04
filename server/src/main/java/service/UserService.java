package service;
//comment to commit and push
import dataaccess.InMemoryDataAccess;
import dataaccess.DataAccessException;
import model.UserData;
import model.AuthData;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;
import requestresult.LoginRequest;
import requestresult.LoginResult;
import requestresult.LogoutRequest;
import requestresult.LogoutResult;

import java.util.UUID;

public class UserService {

    private final InMemoryDataAccess inMemoryDataAccess;

    public UserService(InMemoryDataAccess inMemoryDataAccess) {
        this.inMemoryDataAccess = inMemoryDataAccess;
    }

    /**
     * Registers a new user.
     * @param request the registration request containing username, password, email
     * @return the registration result containing username and auth token, or error message
     */
    public RegisterResult register(RegisterRequest request) {
        try {
            if (request.getUsername() == null || request.getPassword() == null || request.getEmail() == null) {
                return new RegisterResult("Error: bad request");
            }
            try {
                inMemoryDataAccess.getUser(request.getUsername());
                return new RegisterResult("Error: already taken");
            } catch (DataAccessException ignored) {
            }
            UserData newUser = new UserData(request.getUsername(), request.getPassword(), request.getEmail());
            inMemoryDataAccess.createUser(newUser);
            String token = UUID.randomUUID().toString();
            inMemoryDataAccess.createAuth(new AuthData(token, request.getUsername()));
            return new RegisterResult(request.getUsername(), token);
        } catch (DataAccessException e) {
            return new RegisterResult("Error: database failure - " + e.getMessage());
        } catch (Exception e) {
            return new RegisterResult("Error: unexpected failure - " + e.getMessage());
        }
    }

    public LoginResult login(LoginRequest request) {
        try {
            if (request.getUsername() == null || request.getPassword() == null) {
                return new LoginResult("Error: bad request");
            }
            UserData user = inMemoryDataAccess.getUser(request.getUsername());
            if (!user.password().equals(request.getPassword())) {
                return new LoginResult("Error: unauthorized");
            }
            String token = UUID.randomUUID().toString();
            inMemoryDataAccess.createAuth(new AuthData(token, request.getUsername()));
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
            if (request.getAuthToken() == null) {
                return new LogoutResult("Error: bad request");
            }
            inMemoryDataAccess.deleteAuth(request.getAuthToken());
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
