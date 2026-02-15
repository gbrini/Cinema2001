package test;

import com.cinema.controller.auth.LoginController;
import com.cinema.model.User;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {
    private LoginController loginController;

    @BeforeAll
    static void beforeAll() {
        //create new users '...@test.it'
    }

    @BeforeEach
    void setUp() {
        loginController = LoginController.getInstance();
    }

    @Test
    void simpleLogin() throws SQLException {
        User user = loginController.login("utente@me.it", "password");
        assertNotNull(user);
    }

    @Test
    void simpleLoginWrong() throws SQLException {
        User user = loginController.login("utente@me.it", "prova2");
        assertNull(user);
    }

    @AfterAll
    static void afterAll() {
        //load reset.sql
    }
}