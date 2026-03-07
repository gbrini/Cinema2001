package test.com.cinema.service;

import com.cinema.controller.auth.LoginController;
import com.cinema.model.Movie;
import com.cinema.model.User;
import com.cinema.service.MovieService;
import com.cinema.util.EnvConfig;
import com.cinema.util.UnauthorizedAccessException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MovieServiceTest {
    @Test
    void getAllMoviesSuccess() throws SQLException {
        User user = LoginController.getInstance().login("admin@me.com", EnvConfig.getInstance().get("password"));
        assertDoesNotThrow(MovieService::getAllMovies);
    }

    @Test
    void getAllMoviesF() throws SQLException {
        User user = LoginController.getInstance().login("admin@me.com", EnvConfig.getInstance().get("password"));
        assertThrows(UnauthorizedAccessException.class, MovieService::getAllMovies);
    }

    @Test
    void addMovie() throws SQLException {
        User user = LoginController.getInstance().login("admin@me.com", EnvConfig.getInstance().get("password"));
        Movie movie = null;
        MovieService.addMovie(movie);
    }
}
