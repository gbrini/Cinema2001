package test.com.cinema.service;

import com.cinema.controller.auth.LoginController;
import com.cinema.model.Movie;
import com.cinema.model.User;
import com.cinema.model.dao.database.DatabaseConfig;
import com.cinema.service.MovieService;
import com.cinema.util.EnvConfig;
import com.cinema.util.UnauthorizedAccessException;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MovieService - BlackBox")
public class MovieServiceTest {
    @BeforeAll
    static void setUp() {
        DatabaseConfig.useTestDB();
    }

    private Movie getMovie() {
        return new Movie.Builder()
                .setTitle("Test Film - " + System.currentTimeMillis())
                .setGenre("Azione")
                .setRating("T")
                .setReleaseDate(LocalDate.now())
                .setDurationMinutes(120)
                .setDescription("Test descrizione")
                .setDirector("Regista test")
                .setIsDeleted(false)
                .build();
    }

    @AfterEach
    void clearSession() {
        LoginController.getInstance().logout(null);
    }

    @Test
    void getAllMoviesSuccess() throws SQLException {
        LoginController.getInstance().login("admin@me.com", EnvConfig.getInstance().get("password"));
        assertDoesNotThrow(MovieService::getAllMovies);
    }

    @Test
    void addMovie() throws SQLException {
        LoginController.getInstance().login("admin@me.com", EnvConfig.getInstance().get("password"));
        int id = MovieService.addMovie(getMovie());
        assertTrue(id > 0);
    }

    @Test
    void addMovieF() throws SQLException {
        LoginController.getInstance().login("user@me.com", EnvConfig.getInstance().get("password"));
        assertThrows(UnauthorizedAccessException.class, () -> MovieService.addMovie(getMovie()));
    }

    @Test
    void getMovieById() throws SQLException {
        LoginController.getInstance().login("admin@me.com", EnvConfig.getInstance().get("password"));
        Movie film = MovieService.getMovieById(5);
        assertNotNull(film);
    }

    @Test
    void getMovieByIdF() throws SQLException {
        LoginController.getInstance().login("admin@me.com", EnvConfig.getInstance().get("password"));
        Movie film = MovieService.getMovieById(-1);
        assertNull(film);
    }
}
