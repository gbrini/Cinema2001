package test;

import com.cinema.controller.auth.LoginController;
import com.cinema.model.User;
import com.cinema.service.auth.UserSession;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {
    @Test
    void testLoginFlowUpdateSession() throws SQLException {
        LoginController loginController = LoginController.getInstance();

        User user = loginController.login("employee@me.com", "password");

        assertNotNull(user);
        assertEquals(user, UserSession.getInstance().getCurrentUser());
    }
}