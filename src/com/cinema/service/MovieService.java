package com.cinema.service;

import com.cinema.model.Movie;
import com.cinema.model.User;
import com.cinema.model.dao.MovieDAO;

import java.util.ArrayList;

public class MovieService {
    public static int addMovie(Movie movie, User user) {
        if (PermissionService.hasPermission(user, "movie:add"))
            return MovieDAO.addMovie(movie);
        return -1;
    }

    public static boolean updateMovie(Movie movie, User user) {
        if (PermissionService.hasPermission(user, "movie:edit"))
            return MovieDAO.updateMovie(movie);
        return false;
    }

    public static boolean deleteMovie(int movieId, User user) {
        if (PermissionService.hasPermission(user, "movie:delete"))
            return MovieDAO.deleteMovie(movieId);
        return false;
    }

    public static Movie getMovieById(int movieId, User user) {
        if (PermissionService.hasPermission(user, "movie:view"))
            return MovieDAO.getMovieById(movieId);
        return null;
    }

    public static ArrayList<Movie> getAllMovies(User user) {
        if (PermissionService.hasPermission(user, "movie:view"))
            return MovieDAO.getAllMovies();
        return new ArrayList<>();
    }
}
