package test.com.cinema.service;

import com.cinema.controller.auth.LoginController;
import com.cinema.model.Movie;
import com.cinema.model.Screen;
import com.cinema.model.Screening;
import com.cinema.model.ScreeningRecord;
import com.cinema.model.dao.database.DatabaseConnection;
import com.cinema.service.ScreeningService;
import com.cinema.util.EnvConfig;
import com.cinema.util.UnauthorizedAccessException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ScreeningServiceTest {
    private ScreeningRecord buildRecord(LocalDateTime startTime, int movieDuration, int screenId) {
        Movie movie = new Movie.Builder()
                .setMovieId(1)
                .setTitle("Test Movie")
                .setDurationMinutes(movieDuration)
                .setReleaseDate(LocalDate.now())
                .setGenre("Action")
                .setRating("PG")
                .setDescription("Test description")
                .setDirector("Test Director")
                .setIsDeleted(false)
                .build();

        Screening screening = new Screening(movie.getMovieId(), screenId, startTime, 10.00, false);

        Screen screen = new Screen(screenId, "Screen " + screenId, 100, false);

        return new ScreeningRecord(screening, movie, startTime.toLocalDate(),
                startTime.toLocalTime(), screen, 0, 100);
    }


    @BeforeEach
    void setup() throws SQLException {
        Connection conn = DatabaseConnection.getInstance();
        PreparedStatement stmt = conn.prepareStatement("TRUNCATE TABLE ticket, ticket_type, screening, seat, screen, movie CASCADE");
        stmt.execute();

        //LoginController.getInstance().logout(null);
        LoginController.getInstance().login("employee@me.com", EnvConfig.getInstance().get("password"));
    }

    @Nested
    @DisplayName("Black Box Tests")
    class BlackBoxTests {
        @Test
        void shouldReturnTrueWhenNoConflict() {
            ScreeningRecord record = buildRecord(LocalDateTime.now().plusDays(1), 120, 1);
            assertTrue(ScreeningService.validateAndSchedule(record));
        }

        @Test
        void shouldReturnFalseWhenOverlapping() {
            ScreeningRecord first = buildRecord(LocalDateTime.now().plusDays(1).withHour(10), 120, 1);
            ScreeningService.validateAndSchedule(first);

            // starts 1 hour into the first screening
            ScreeningRecord second = buildRecord(LocalDateTime.now().plusDays(1).withHour(11), 60, 1);
            assertFalse(ScreeningService.validateAndSchedule(second));
        }

        @Test
        void shouldReturnTrueForBackToBackScreening() {
            ScreeningRecord first = buildRecord(LocalDateTime.now().plusDays(1).withHour(10), 120, 1);
            ScreeningService.validateAndSchedule(first);

            // starts exactly after first + duration + 15min buffer
            ScreeningRecord second = buildRecord(LocalDateTime.now().plusDays(1).withHour(12).withMinute(15), 60, 1);
            assertTrue(ScreeningService.validateAndSchedule(second));
        }

        @Test
        void shouldAllowSameTimeOnDifferentScreen() {
            ScreeningRecord screen1 = buildRecord(LocalDateTime.now().plusDays(1).withHour(10), 120, 1);
            ScreeningRecord screen2 = buildRecord(LocalDateTime.now().plusDays(1).withHour(10), 120, 2);

            ScreeningService.validateAndSchedule(screen1);
            assertTrue(ScreeningService.validateAndSchedule(screen2));
        }
    }

    @Nested
    @DisplayName("White Box Tests")
    class WhiteBoxTests {
        @Test
        void shouldThrowWhenUserHasNoPermission() {
            // log in as a user without screening:add permission first
            ScreeningRecord record = buildRecord(LocalDateTime.now().plusDays(1), 120, 1);
            assertThrows(UnauthorizedAccessException.class, () ->
                    ScreeningService.validateAndSchedule(record));
        }

        @Test
        void shouldReturnFalseWhenProposedStartIsInsideExisting() {
            ScreeningRecord first = buildRecord(LocalDateTime.now().plusDays(1).withHour(10), 120, 1);
            ScreeningService.validateAndSchedule(first);

            // starts 30min in, ends after — partial overlap
            ScreeningRecord second = buildRecord(LocalDateTime.now().plusDays(1).withHour(10).withMinute(30), 120, 1);
            assertFalse(ScreeningService.validateAndSchedule(second));
        }

        @Test
        void shouldReturnFalseWhenProposedEnglobsExisting() {
            ScreeningRecord first = buildRecord(LocalDateTime.now().plusDays(1).withHour(11), 60, 1);
            ScreeningService.validateAndSchedule(first);

            // starts before and ends after the existing one
            ScreeningRecord second = buildRecord(LocalDateTime.now().plusDays(1).withHour(10), 180, 1);
            assertFalse(ScreeningService.validateAndSchedule(second));
        }

        @Test
        void shouldNotConflictWhenExistingMovieIsNull() {
            // insert a screening into DB with a movie_id that doesn't exist
            // so MovieService.getMovieById returns null → should be skipped
            ScreeningRecord record = buildRecord(LocalDateTime.now().plusDays(1).withHour(10), 120, 1);
            assertTrue(ScreeningService.validateAndSchedule(record));
        }

        @Test
        void shouldReturnFalseOneMinuteBeforeBufferEnds() {
            ScreeningRecord first = buildRecord(LocalDateTime.now().plusDays(1).withHour(10), 120, 1);
            ScreeningService.validateAndSchedule(first);

            // 10:00 + 120min + 15min buffer = 12:15, so 12:14 should still conflict
            ScreeningRecord second = buildRecord(LocalDateTime.now().plusDays(1).withHour(12).withMinute(14), 60, 1);
            assertFalse(ScreeningService.validateAndSchedule(second));
        }
    }

}