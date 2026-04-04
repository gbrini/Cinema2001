package test.com.cinema.service.auth;

import com.cinema.model.Role;
import com.cinema.model.User;
import com.cinema.service.auth.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserSession - WhiteBox Singleton")
public class UserSessionTest {
    private User testUser;

    @BeforeEach
    void setUp() {
        UserSession.getInstance().cleanSession();

        Role role = new Role(3, "client");
        testUser = new User(1, "Mario", "Rossi", LocalDate.now(), "mario@test.it", role);
    }

    @Test
    @DisplayName("getInstance() restituisce sempre la stessa istanza")
    void testSingleton() {
        UserSession a = UserSession.getInstance();
        UserSession b = UserSession.getInstance();
        assertSame(a, b);
    }

    @Test
    @DisplayName("Sessione vuota → getCurrentUser() è null")
    void testEmptySession() {
        assertNull(UserSession.getInstance().getCurrentUser());
    }

    @Test
    @DisplayName("createSession() imposta l'utente corrente")
    void testCreateSession() {
        UserSession.getInstance().createSession(testUser);
        assertEquals(testUser, UserSession.getInstance().getCurrentUser());
    }

    @Test
    @DisplayName("createSession() con utente diverso sovrascrive il precedente")
    void testCreateSessionOverwrite() {
        Role role = new Role(1, "admin");
        User admin = new User(2, "Luigi", "Bianchi", LocalDate.now(), "luigi@test.it", role);

        UserSession.getInstance().createSession(testUser);
        UserSession.getInstance().createSession(admin);

        assertEquals(admin, UserSession.getInstance().getCurrentUser());
    }

    @Test
    @DisplayName("cleanSession() rimuove l'utente corrente")
    void testCleanSession() {
        UserSession.getInstance().createSession(testUser);
        UserSession.getInstance().cleanSession();
        assertNull(UserSession.getInstance().getCurrentUser());
    }

    @Test
    @DisplayName("cleanSession() su sessione già vuota non lancia eccezioni")
    void testCleanSessionEmpty() {
        assertDoesNotThrow(() -> UserSession.getInstance().cleanSession());
    }
}
