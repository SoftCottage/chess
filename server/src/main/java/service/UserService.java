package service;

import dataaccess.DataAccess;
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
import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    /**
     * Registers a new user with bcrypt password hashing.
     */
    public RegisterResult register(RegisterRequest request) {
        try {
            if (request.getUsername() == null || request.getPassword() == null || request.getEmail() == null) {
                return new RegisterResult("Error: bad request");
            }

            try {
                dataAccess.getUser(request.getUsername());
                return new RegisterResult("Error: already taken");
            } catch (DataAccessException ignored) {
                // User does not exist — continue
            }

            // Hash password before storing
            String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

            UserData newUser = new UserData(request.getUsername(), hashedPassword, request.getEmail());
            dataAccess.createUser(newUser);

            String token = UUID.randomUUID().toString();
            dataAccess.createAuth(new AuthData(token, request.getUsername()));

            return new RegisterResult(request.getUsername(), token);
        } catch (DataAccessException e) {
            return new RegisterResult("Error: database failure - " + e.getMessage());
        } catch (Exception e) {
            return new RegisterResult("Error: unexpected failure - " + e.getMessage());
        }
    }

    /**
     * Logs in a user and checks bcrypt password.
     */
    public LoginResult login(LoginRequest request) {
        try {
            if (request.getUsername() == null || request.getPassword() == null) {
                return new LoginResult("Error: bad request");
            }

            UserData user = dataAccess.getUser(request.getUsername());

            // Use bcrypt to check password
            if (!BCrypt.checkpw(request.getPassword(), user.password())) {
                return new LoginResult("Error: unauthorized");
            }

            String token = UUID.randomUUID().toString();
            dataAccess.createAuth(new AuthData(token, request.getUsername()));

            return new LoginResult(request.getUsername(), token);
        } catch (DataAccessException e) {
            if (e.getMessage().toLowerCase().contains("not found")) {
                return new LoginResult("Error: unauthorized");
            }
            return new LoginResult("Error: database failure - " + e.getMessage());
        } catch (Exception e) {
            return new LoginResult("Error: unexpected failure - " + e.getMessage());
        }
    }

    /**
     * Logs out a user.
     */
    public LogoutResult logout(LogoutRequest request) {
        try {
            if (request.getAuthToken() == null) {
                return new LogoutResult("Error: bad request");
            }

            dataAccess.deleteAuth(request.getAuthToken());
            return new LogoutResult(null);
        } catch (DataAccessException e) {
            if (e.getMessage().toLowerCase().contains("auth not found")) {
                return new LogoutResult("Error: unauthorized");
            }
            return new LogoutResult("Error: database failure - " + e.getMessage());
        } catch (Exception e) {
            return new LogoutResult("Error: unexpected failure - " + e.getMessage());
        }
    }
}
