package test.com.cinema.service;

import com.cinema.controller.auth.LoginController;
import com.cinema.model.Movie;
import com.cinema.model.User;
import com.cinema.service.MovieService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class MovieServiceTest {

    @Test
    void addMovie() throws SQLException {
        User user = LoginController.getInstance().login("admin@me.com", "password");
        Movie movie = null;
        MovieService.addMovie(movie);
    }
}
