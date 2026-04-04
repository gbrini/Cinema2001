package test.com.cinema.service;

import com.cinema.controller.auth.LoginController;
import com.cinema.model.*;
import com.cinema.model.dao.MovieDAO;
import com.cinema.model.dao.ScreenDAO;
import com.cinema.model.dao.database.DatabaseConnection;
import com.cinema.service.ScreeningService;
import com.cinema.service.auth.UserSession;
import com.cinema.util.EnvConfig;
import com.cinema.util.UnauthorizedAccessException;
import org.junit.jupiter.api.*;
import test.com.cinema.BaseTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ScreeningServiceTest extends BaseTest {
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

        Screening screening = new Screening(movie.getMovieId(), screenId, startTime, 10.00F, false);

        Screen screen = new Screen(screenId, "Screen " + screenId, 100, false);

        return new ScreeningRecord(screening, movie, startTime.toLocalDate(),
                startTime.toLocalTime(), screen, 0, 100);
    }

    @BeforeAll
    static void start() throws SQLException {
        LoginController.getInstance().login("admin@me.com", EnvConfig.getInstance().get("password"));
    }

    @AfterAll
    static void end() throws SQLException {
        LoginController.getInstance().logout(null);
    }

    @BeforeEach
    void setup() throws SQLException {
        Connection conn = DatabaseConnection.getInstance();
        PreparedStatement stmt = conn.prepareStatement("TRUNCATE TABLE ticket, ticket_type, screening, seat, screen, movie RESTART IDENTITY CASCADE");
        stmt.execute();

        Movie movie = new Movie.Builder()
                .setMovieId(1)
                .setTitle("Test Movie")
                .setDurationMinutes(120)
                .setReleaseDate(LocalDate.now())
                .setGenre("Action")
                .setRating("PG")
                .setDescription("Test description")
                .setDirector("Test Director")
                .setIsDeleted(false)
                .build();

        MovieDAO.addMovie(movie);

        Screen screen = new Screen(1, "Screen 1", 100, false);
        ScreenDAO.addScreen(screen);
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

            ScreeningRecord second = buildRecord(LocalDateTime.now().plusDays(1).withHour(11), 60, 1);
            assertFalse(ScreeningService.validateAndSchedule(second));
        }

        @Test
        void shouldReturnTrueForBackToBackScreening() {
            ScreeningRecord first = buildRecord(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0), 120, 1);
            ScreeningService.validateAndSchedule(first);

            ScreeningRecord second = buildRecord(LocalDateTime.now().plusDays(1).withHour(12).withMinute(15).withSecond(0), 60, 1);
            assertTrue(ScreeningService.validateAndSchedule(second));
        }

        @Test
        void shouldAllowSameTimeOnDifferentScreen() {
            Screen screen = new Screen(2, "Screen 2", 100, false);
            ScreenDAO.addScreen(screen);

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
        void shouldReturnFalseWhenProposedStartIsInsideExisting() {
            ScreeningRecord first = buildRecord(LocalDateTime.now().plusDays(1).withHour(10), 120, 1);
            ScreeningService.validateAndSchedule(first);

            ScreeningRecord second = buildRecord(LocalDateTime.now().plusDays(1).withHour(10).withMinute(30), 120, 1);
            assertFalse(ScreeningService.validateAndSchedule(second));
        }

        @Test
        void shouldReturnFalseWhenProposedEnglobsExisting() {
            ScreeningRecord first = buildRecord(LocalDateTime.now().plusDays(1).withHour(11), 60, 1);
            ScreeningService.validateAndSchedule(first);

            ScreeningRecord second = buildRecord(LocalDateTime.now().plusDays(1).withHour(10), 180, 1);
            assertFalse(ScreeningService.validateAndSchedule(second));
        }

        @Test
        void shouldNotConflictWhenExistingMovieIsNull() {
            ScreeningRecord record = buildRecord(LocalDateTime.now().plusDays(1).withHour(10), 120, 1);
            Movie newMovie = new Movie.Builder()
                    .setMovieId(1)
                    .setTitle("Test Movie")
                    .setDurationMinutes(200)
                    .setReleaseDate(LocalDate.now())
                    .setGenre("Action")
                    .setRating("PG")
                    .setDescription("Test description")
                    .setDirector("Test Director")
                    .setIsDeleted(false)
                    .build();
            record = new ScreeningRecord(record.screening(), newMovie, record.date(), record.slot(), record.screen(), 0, 0);

            assertFalse(ScreeningService.validateAndSchedule(record));
        }

        @Test
        void shouldReturnFalseOneMinuteBeforeBufferEnds() {
            ScreeningRecord first = buildRecord(LocalDateTime.now().plusDays(1).withHour(10), 120, 1);
            ScreeningService.validateAndSchedule(first);

            ScreeningRecord second = buildRecord(LocalDateTime.now().plusDays(1).withHour(12).withMinute(14), 60, 1);
            assertFalse(ScreeningService.validateAndSchedule(second));
        }
    }

}