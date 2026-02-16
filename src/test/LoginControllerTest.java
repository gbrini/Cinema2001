package test;

import com.cinema.controller.auth.LoginController;
import com.cinema.model.User;
import com.cinema.service.auth.UserSession;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {
    @Test
    void testLoginErrorFlowUpdateSession() throws SQLException {
        LoginController loginController = LoginController.getInstance();

        User user = loginController.login("employee@me.com", "sbagliata");

        assertNull(user);
        assertNull(UserSession.getInstance().getCurrentUser(), "La sessione deve essere nulla con login errato");
    }

    @Test
    void testLoginFlowUpdateSession() throws SQLException {
        LoginController loginController = LoginController.getInstance();

        User user = loginController.login("employee@me.com", "password");

        assertNotNull(user);
        assertEquals(user, UserSession.getInstance().getCurrentUser());
    }

    @Test
    void testLogoutFlowUpdateSession() {
        LoginController loginController = LoginController.getInstance();

        loginController.logout(null);

        assertNull(UserSession.getInstance().getCurrentUser(), "La sessione deve essere nulla dopo il logout utente");
    }

    @Test
    void testAdminLogin() throws SQLException {
        LoginController loginController = LoginController.getInstance();

        User user = loginController.login("admin@me.com", "password");

        assertEquals(1, user.getRole().getRoleId());
        assertEquals(1, UserSession.getInstance().getCurrentUser().getRole().getRoleId());
    }

    @Test
    void testEmpoyeeLogin() throws SQLException {
        LoginController loginController = LoginController.getInstance();

        User user = loginController.login("employee@me.com", "password");

        assertEquals(2, user.getRole().getRoleId());
        assertEquals(2, UserSession.getInstance().getCurrentUser().getRole().getRoleId());
    }

    @Test
    void testUserLogin() throws SQLException {
        LoginController loginController = LoginController.getInstance();

        User user = loginController.login("user@me.com", "password");

        assertEquals(3, user.getRole().getRoleId());
        assertEquals(3, UserSession.getInstance().getCurrentUser().getRole().getRoleId());
    }

    @AfterEach
    void clearSession() {
        LoginController loginController = LoginController.getInstance();
        loginController.logout(null);
    }
}