package com.cinema.service;

import com.cinema.model.Movie;
import com.cinema.model.dao.MovieDAO;
import com.cinema.util.UnauthorizedAccessException;

import java.util.ArrayList;

public class MovieService {
    public static int addMovie(Movie movie) {
        if (!PermissionService.hasPermission("movie:add"))
            throw new UnauthorizedAccessException("Accesso neagto");
        return MovieDAO.addMovie(movie);
    }

    public static boolean updateMovie(Movie movie) {
        if (PermissionService.hasPermission("movie:edit"))
            return MovieDAO.updateMovie(movie);
        return false;
    }

    public static boolean deleteMovie(int movieId) {
        if (PermissionService.hasPermission("movie:delete"))
            return MovieDAO.deleteMovie(movieId);
        return false;
    }

    public static Movie getMovieById(int movieId) {
        if (PermissionService.hasPermission("movie:view"))
            return MovieDAO.getMovieById(movieId);
        return null;
    }

    public static ArrayList<Movie> getAllMovies() {
        if (!PermissionService.hasPermission("movie:view"))
            throw new UnauthorizedAccessException("Accesso negato");
        return MovieDAO.getAllMovies();
    }
}
