package test.com.cinema.service;

import com.cinema.controller.auth.LoginController;
import com.cinema.model.dao.database.DatabaseConnection;
import com.cinema.util.EnvConfig;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ScreeningServiceTest {

    @BeforeEach
    void setup() throws SQLException {
        Connection conn = DatabaseConnection.getInstance();
        PreparedStatement stmt = conn.prepareStatement("TRUNCATE TABLE ticket, ticket_type, screening, seat, screen, movie CASCADE");
        stmt.execute();

        LoginController.getInstance().logout(null);
        LoginController.getInstance().login("employee@me.com", EnvConfig.getInstance().get("password"));
    }

    @Nested
    @DisplayName("Black Box Tests")
    class BlackBoxTests {
        @Test
        void shouldReturnFalseOnOverlap() {
            // schedule screening A, then try to schedule B overlapping A
        }

        @Test
        void shouldReturnTrueOnValidNonOverlappingScreening() {
            // schedule A, then schedule B starting after A + duration + 15min
        }

        @Test
        void shouldReturnFalseWhenSameStartTime() {
            // two screenings on same screen at exact same time
        }
    }

    @Nested
    @DisplayName("White Box Tests")
    class WhiteBoxTests {
        @Test
        void shouldThrowWhenUserHasNoPermission() {
            // mock PermissionService.hasPermission("screening:add") = false
//            assertThrows(UnauthorizedAccessException.class, () ->
//                    ScreeningService.validateAndSchedule(screeningRecord));
        }

        @Test
        void shouldSkipScheduledScreeningWhenMovieIsNull() {
            // existing screening in DB whose movie_id returns null from MovieService
            // proposed screening should NOT conflict and should return true
        }

        @Test
        void shouldReturnFalseWhenProposedStartIsInsideExistingScreening() {
            // proposedStart > existingStart && proposedStart < existingEnd
        }

        @Test
        void shouldReturnFalseWhenProposedScreeningEnglobsExistingOne() {
            // proposedStart < existingStart && proposedEnd > existingEnd
        }

        @Test
        void shouldReturnFalseWhenProposedEndIsInsideExistingScreening() {
            // proposedStart < existingStart && proposedEnd > existingStart
        }

        @Test
        void shouldAllowBackToBackScreening() {
            // proposedStart == existingEnd exactly — the +15 buffer makes this an edge case
        }

        @Test
        void shouldReturnTrueWhenNoScreeningsExistForThatDayAndScreen() {
            // ScreeningDAO.getScreeningByDateAndScreen returns empty list
        }
    }

}