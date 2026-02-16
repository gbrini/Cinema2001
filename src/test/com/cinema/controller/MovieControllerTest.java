package test.com.cinema.controller;

import com.cinema.controller.MovieController;
import com.cinema.model.Movie;
import com.cinema.service.auth.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MovieControllerTest {
    private Movie testMovie;

    @BeforeEach
    void setup() {
        //testMovie = new Movie();
        UserSession.getInstance().cleanSession();
    }

    @Test
    void testControllerInitialization() {
        //test con admin
        MovieController controller = new MovieController(testMovie);
        assertNotNull(controller.getView(), "Il pannello ci deve essere");
    }

    @Test
    void testAddMovie() {
        //test add con employee -> si
    }

    @Test
    void testAddMovieUser() {
        //test add con user -> no
    }
}
