package passoff.service;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.RegisterRequest;
import model.RegisterResult;
import model.UserData;
import service.UserService;

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

    @Test
    @DisplayName("Register success")
    public void registerSuccess() {
        RegisterRequest req = new RegisterRequest("alice", "pw", "a@mail.com");
        RegisterResult res = userService.register(req);

        assertNull(res.getMessage(), "Success response should have null message");
        assertEquals("alice", res.getUsername(), "Username should match");
        assertNotNull(res.getAuthToken(), "Auth token should be present");
    }

    @Test
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
}
