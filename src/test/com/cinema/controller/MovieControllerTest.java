package test.com.cinema.controller;

import com.cinema.controller.MovieController;
import com.cinema.controller.auth.LoginController;
import com.cinema.model.Movie;
import com.cinema.model.User;
import com.cinema.service.auth.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MovieControllerTest {
    private Movie testMovie;

    @BeforeEach
    void setup() {
        testMovie = new Movie(9999,
        "Prova",
        99,
        LocalDate.now(),
        "action",
        "PG-13",
        "",
        "",
        false);
        UserSession.getInstance().cleanSession();
    }

    @Test
    void testControllerInitialization() throws SQLException {
        User user = LoginController.getInstance().login("admin@me.com", "password");
        MovieController controller = new MovieController(testMovie);
        assertNotNull(controller.getView(), "Il pannello ci deve essere");
    }

    @Test
    void testAddMovie() throws SQLException {
        //test add con employee -> si
        User user = LoginController.getInstance().login("employee@me.com", "password");
        MovieController controller = new MovieController(testMovie);
        //controller.getView().
    }

    @Test
    void testAddMovieUser() throws SQLException {
        //test add con user -> no
        User user = LoginController.getInstance().login("user@me.com", "password");
        MovieController controller = new MovieController(testMovie);
    }
}
