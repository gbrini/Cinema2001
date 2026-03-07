package com.cinema.service;

import com.cinema.model.Movie;
import com.cinema.model.dao.MovieDAO;
import com.cinema.util.UnauthorizedAccessException;

import java.util.ArrayList;

public class MovieService {
    public static int addMovie(Movie movie) {
        if (!PermissionService.hasPermission("movie:add"))
            throw new UnauthorizedAccessException("Accesso non consentito");
        return MovieDAO.addMovie(movie);
    }

    public static boolean updateMovie(Movie movie) {
        if (!PermissionService.hasPermission("movie:edit"))
            throw new UnauthorizedAccessException("Accesso non consentito");
        return MovieDAO.updateMovie(movie);
    }

    public static boolean deleteMovie(int movieId) {
        if (!PermissionService.hasPermission("movie:delete"))
            throw new UnauthorizedAccessException("Accesso non consentito");
        return MovieDAO.deleteMovie(movieId);
    }

    public static Movie getMovieById(int movieId) {
        if (!PermissionService.hasPermission("movie:view"))
            throw new UnauthorizedAccessException("Accesso non consentito");
        return MovieDAO.getMovieById(movieId);
    }

    public static ArrayList<Movie> getAllMovies() {
        if (!PermissionService.hasPermission("movie:view"))
            throw new UnauthorizedAccessException("Accesso negato");
        return MovieDAO.getAllMovies();
    }
}
