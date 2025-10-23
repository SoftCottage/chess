package passoff.service;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import service.UserService;
import model.UserData;
import model.RegisterRequest;
import model.RegisterResult;
import model.LoginRequest;
import model.LoginResult;
import model.LogoutRequest;
import model.LogoutResult;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {

    private static DataAccess dataAccess;
    private static UserService userService;

    @BeforeAll
    public static void setup() {
        dataAccess = new DataAccess();
        userService = new UserService(dataAccess);
    }

    @BeforeEach
    public void beforeEach() throws DataAccessException {
        // ensure clean state
        dataAccess.clearDatabase();
    }

    // ----------------- REGISTER TESTS -----------------

    @Test
    @Order(1)
    @DisplayName("Register success")
    public void registerSuccess() {
        RegisterRequest req = new RegisterRequest("alice", "pw", "a@mail.com");
        RegisterResult res = userService.register(req);

        assertNull(res.getMessage(), "Success response should have null message");
        assertEquals("alice", res.getUsername(), "Username should match");
        assertNotNull(res.getAuthToken(), "Auth token should be present");
    }

    @Test
    @Order(2)
    @DisplayName("Register bad request (missing fields)")
    public void registerBadRequest() {
        RegisterRequest req = new RegisterRequest(null, "pw", "a@mail.com"); // missing username
        RegisterResult res = userService.register(req);

        assertNotNull(res.getMessage());
        assertTrue(res.getMessage().toLowerCase().contains("bad request"));
        assertNull(res.getUsername());
        assertNull(res.getAuthToken());
    }

    @Test
    @Order(3)
    @DisplayName("Register duplicate username")
    public void registerDuplicate() throws DataAccessException {
        // Pre-create user in dataAccess
        dataAccess.createUser(new UserData("bob", "pw", "b@mail.com"));

        RegisterRequest req = new RegisterRequest("bob", "pw2", "b2@mail.com");
        RegisterResult res = userService.register(req);

        assertNotNull(res.getMessage());
        assertTrue(res.getMessage().toLowerCase().contains("already taken"));
        assertNull(res.getUsername());
        assertNull(res.getAuthToken());
    }

    // ----------------- LOGIN TESTS -----------------

    @Test
    @Order(4)
    @DisplayName("Login success")
    public void loginSuccess() {
        // First register a user
        RegisterRequest regReq = new RegisterRequest("charlie", "mypw", "c@mail.com");
        userService.register(regReq);

        // Now login
        LoginRequest loginReq = new LoginRequest("charlie", "mypw");
        LoginResult loginRes = userService.login(loginReq);

        assertNull(loginRes.getMessage(), "Success response should have null message");
        assertEquals("charlie", loginRes.getUsername(), "Username should match");
        assertNotNull(loginRes.getAuthToken(), "Auth token should be present");
    }

    @Test
    @Order(5)
    @DisplayName("Login bad request (missing fields)")
    public void loginBadRequest() {
        LoginRequest req = new LoginRequest(null, "pw"); // missing username
        LoginResult res = userService.login(req);

        assertNotNull(res.getMessage());
        assertTrue(res.getMessage().toLowerCase().contains("bad request"));
        assertNull(res.getUsername());
        assertNull(res.getAuthToken());
    }

    @Test
    @Order(6)
    @DisplayName("Login unauthorized (wrong password)")
    public void loginUnauthorizedWrongPassword() {
        // Register user first
        userService.register(new RegisterRequest("david", "rightpw", "d@mail.com"));

        // Try login with wrong password
        LoginRequest req = new LoginRequest("david", "wrongpw");
        LoginResult res = userService.login(req);

        assertNotNull(res.getMessage());
        assertTrue(res.getMessage().toLowerCase().contains("unauthorized"));
        assertNull(res.getUsername());
        assertNull(res.getAuthToken());
    }

    @Test
    @Order(7)
    @DisplayName("Login unauthorized (nonexistent user)")
    public void loginUnauthorizedNoUser() {
        // Attempt login for user that doesn't exist
        LoginRequest req = new LoginRequest("eve", "pw");
        LoginResult res = userService.login(req);

        assertNotNull(res.getMessage());
        assertTrue(res.getMessage().toLowerCase().contains("unauthorized"));
        assertNull(res.getUsername());
        assertNull(res.getAuthToken());
    }

    // ----------------- LOGOUT TESTS -----------------

    @Test
    @Order(8)
    @DisplayName("Logout success")
    public void logoutSuccess() {
        // Register and login
        RegisterRequest regReq = new RegisterRequest("frank", "pw", "f@mail.com");
        userService.register(regReq);

        LoginRequest loginReq = new LoginRequest("frank", "pw");
        LoginResult loginRes = userService.login(loginReq);
        assertNotNull(loginRes.getAuthToken(), "Auth token should exist after login");

        // Now logout
        LogoutRequest logoutReq = new LogoutRequest(loginRes.getAuthToken());
        LogoutResult logoutRes = userService.logout(logoutReq);

        assertNull(logoutRes.getMessage(), "Success response should have null message");

        // Verify token is invalidated
        LogoutResult secondLogout = userService.logout(logoutReq);
        assertNotNull(secondLogout.getMessage(), "Second logout should fail");
        assertTrue(secondLogout.getMessage().toLowerCase().contains("unauthorized"));
    }

    @Test
    @Order(9)
    @DisplayName("Logout unauthorized (invalid token)")
    public void logoutUnauthorized() {
        // Attempt to logout with invalid token
        LogoutRequest logoutReq = new LogoutRequest("fakeToken");
        LogoutResult logoutRes = userService.logout(logoutReq);

        assertNotNull(logoutRes.getMessage());
        assertTrue(logoutRes.getMessage().toLowerCase().contains("unauthorized"));
    }
}
