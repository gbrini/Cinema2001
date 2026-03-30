package test.com.cinema.service;

import com.cinema.controller.auth.LoginController;
import com.cinema.model.Movie;
import com.cinema.model.User;
import com.cinema.service.MovieService;
import com.cinema.util.EnvConfig;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MovieService - BlackBox")
public class MovieServiceTest {
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

    @Test
    void getAllMoviesSuccess() throws SQLException {
        User user = LoginController.getInstance().login("admin@me.com", EnvConfig.getInstance().get("password"));
        assertDoesNotThrow(MovieService::getAllMovies);
    }

    @Test
    void addMovie() throws SQLException {
        User user = LoginController.getInstance().login("admin@me.com", EnvConfig.getInstance().get("password"));
        int id = MovieService.addMovie(getMovie());
        assertTrue(id > 0);
    }
}
