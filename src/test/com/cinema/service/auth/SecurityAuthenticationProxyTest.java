package test.com.cinema.service.auth;

import com.cinema.model.Role;
import com.cinema.model.User;
import com.cinema.service.auth.IAuthenticationService;
import com.cinema.service.auth.security.SecurityAuthenticationProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SecurityAuthenticationProxy — White Box")
public class SecurityAuthenticationProxyTest {

    //Stub del servizio reale, non mi serve il db
    private final IAuthenticationService stubReal = new IAuthenticationService() {
        @Override
        public User login(String email, String password) {
            Role role = new Role(3, "client");
            return new User(1, "Mario", "Rossi", LocalDate.now(), email, role);
        }
        @Override
        public boolean logout() {
            return true;
        }
    };

    private SecurityAuthenticationProxy proxy;

    @BeforeEach
    void setUp() {
        proxy = new SecurityAuthenticationProxy(stubReal);
    }

    @Test
    @DisplayName("Email null → restituisce null")
    void testEmailNull() throws Exception {
        assertNull(proxy.login(null, "password"));
    }

    @Test
    @DisplayName("Password null → restituisce null")
    void testPasswordNull() throws Exception {
        assertNull(proxy.login("mario@test.it", null));
    }

    @Test
    @DisplayName("Email blank → restituisce null")
    void testEmailBlank() throws Exception {
        assertNull(proxy.login("   ", "password"));
    }

    @Test
    @DisplayName("Password blank → restituisce null")
    void testPasswordBlank() throws Exception {
        assertNull(proxy.login("mario@test.it", "   "));
    }

    @Test
    @DisplayName("Email vuota → restituisce null")
    void testEmailVuota() throws Exception {
        assertNull(proxy.login("", "password"));
    }

    @Test
    @DisplayName("Password vuota → restituisce null")
    void testPasswordVuota() throws Exception {
        assertNull(proxy.login("mario@test.it", ""));
    }

    @Test
    @DisplayName("Entrambi null → restituisce null")
    void testEntrambiNull() throws Exception {
        assertNull(proxy.login(null, null));
    }

    @Test
    @DisplayName("Entrambi blank → restituisce null")
    void testEntrambiBlank() throws Exception {
        assertNull(proxy.login("", ""));
    }

    @Test
    @DisplayName("Credenziali valide → restituisce utente dal servizio reale")
    void testCredenzialiValide() throws Exception {
        User result = proxy.login("mario@test.it", "password");
        assertNotNull(result);
        assertEquals("Mario", result.getFirstName());
    }

    @Test
    @DisplayName("logout() delega al servizio reale e restituisce true")
    void testLogout() throws Exception {
        // Stub che verifica che logout sia chiamato
        final boolean[] logoutCalled = {false};
        IAuthenticationService stubLogout = new IAuthenticationService() {
            @Override
            public User login(String email, String password) { return null; }
            @Override
            public boolean logout() {
                logoutCalled[0] = true;
                return true;
            }
        };

        SecurityAuthenticationProxy proxyLogout = new SecurityAuthenticationProxy(stubLogout);
        assertTrue(proxyLogout.logout());
        assertTrue(logoutCalled[0], "logout() deve delegare al servizio reale");
    }
}