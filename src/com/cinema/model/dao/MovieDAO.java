package com.cinema.model.dao;

import com.cinema.model.dao.database.DatabaseConnection;
import com.cinema.model.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieDAO {
    public static int addMovie(Movie movie) {
        String sql = "INSERT INTO movie (title, duration_minutes, release_date, genre, rating, description, director) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING movie_id";

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, movie.getTitle());
            stmt.setInt(2, movie.getDurationMinutes());
            stmt.setDate(3, java.sql.Date.valueOf(movie.getReleaseDate().toString()));
            stmt.setString(4, movie.getGenre());
            stmt.setString(5, movie.getRating());
            stmt.setString(6, movie.getDescription());
            stmt.setString(7, movie.getDirector());

            stmt.execute();

            ResultSet last_insert = stmt.getResultSet();

            if (last_insert.next()) {
                return last_insert.getInt(1);
            }

            return 0;
        } catch (SQLException ex) {
            System.out.println("Error inserting movie " + ex);
        }

        return 0;
    }

    public static boolean updateMovie(Movie movie) {
        String sql = "UPDATE movie SET title = ?, duration_minutes = ?, release_date = ?, genre = ?, rating = ?, description = ?, director = ? WHERE movie_id = ? ";

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, movie.getTitle());
            stmt.setInt(2, movie.getDurationMinutes());
            stmt.setDate(3, java.sql.Date.valueOf(movie.getReleaseDate().toString()));
            stmt.setString(4, movie.getGenre());
            stmt.setString(5, movie.getRating());
            stmt.setString(6, movie.getDescription());
            stmt.setString(7, movie.getDirector());
            stmt.setInt(8, movie.getMovieId());

            return stmt.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println("Error updating movie " + ex);
        }

        return false;
    }

    public static boolean deleteMovie(int movieId) {
        Movie movie = MovieDAO.getMovieById(movieId);

        if (movie == null)
            return false;

        movie.setDeleted(true);

        return MovieDAO.updateMovie(movie);
    }

    public static Movie getMovieById(int movieId) {
        Movie movie = null;
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM movie WHERE movie_id = ? ");
            stmt.setInt(1, movieId);

            stmt.execute();

            ResultSet results = stmt.getResultSet();

            if (results.next()) {
                movie = new Movie(
                    results.getInt("movie_id"),
                    results.getString("title"),
                    results.getInt("duration_minutes"),
                    LocalDate.parse(results.getDate("release_date").toString()),
                    results.getString("genre"),
                    results.getString("rating"),
                    results.getString("description"),
                    results.getString("director"),
                    results.getBoolean("is_deleted")
                );
            }

            return movie;
        } catch (SQLException ex) {
            System.out.println("Error deleting movie " + ex);
        }

        return movie;
    }

    public static ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM movie WHERE is_deleted = false ORDER BY movie_id DESC");

            stmt.execute();

            ResultSet results = stmt.getResultSet();

            while (results.next()) {
                movies.add(new Movie(
                    results.getInt("movie_id"),
                    results.getString("title"),
                    results.getInt("duration_minutes"),
                    LocalDate.parse(results.getDate("release_date").toString()),
                    results.getString("genre"),
                    results.getString("rating"),
                    results.getString("description"),
                    results.getString("director"),
                    results.getBoolean("is_deleted")
                ));
            }

        } catch (SQLException ex) {
            System.out.println("Error getting all movies " + ex);
        }

        return movies;
    }

    public static List<Movie> getMoviesByScreenDates(Date from, Date to) {
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM movie LEFT JOIN screening on screening.movie_id = movie.movie_id WHERE x");
            //WHERE start_time >= '2025-11-14T00:00:00' and start_time <= '2025-11-14T:23:59:59'
        } catch (SQLException ex) {

        }

        return movies;
    }
}
