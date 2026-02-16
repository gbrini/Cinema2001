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

    @Test
    void testLogoutFlowUpdateSession() throws SQLException {
        LoginController loginController = LoginController.getInstance();

        loginController.logout(null);

        assertNull(UserSession.getInstance().getCurrentUser(), "La sessione deve essere nulla dopo il logout utente");
    }
}