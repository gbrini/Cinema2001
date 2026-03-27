package com.cinema.model.dao;

import com.cinema.controller.MovieController;
import com.cinema.model.*;
import com.cinema.model.dao.database.DatabaseConnection;
import com.cinema.service.MovieService;
import com.sun.net.httpserver.Authenticator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ScreeningDAO {

    public static boolean addScreening(Screening screening) {
        String sql = "INSERT INTO screening (movie_id, screen_id, start_time, ticket_price, is_deleted) VALUES (?, ?, ?, ?, ?)";

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, screening.getMovieId());
            stmt.setInt(2, screening.getScreenId());
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(screening.getStartTime()));
            stmt.setDouble(4, screening.getTicketPrice());
            stmt.setBoolean(5, screening.isDeleted());

            stmt.execute();

            return true;
        } catch (SQLException ex) {
            System.out.println("Error inserting screening " + ex);
        }

        return false;
    }

    public static ArrayList<Screening> getScreeningByDateAndScreen(LocalDate date, int screenId) {
        ArrayList<Screening> screenings = new ArrayList<>();
        String sql = "SELECT * FROM screening WHERE start_time::text LIKE ? AND screen_id = ? ";

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + date.toString() + "%");
            stmt.setInt(2, screenId);

            stmt.execute();
            ResultSet results = stmt.getResultSet();

            while (results.next()) {
                screenings.add(new Screening(
                    results.getInt("screening_id"),
                    results.getInt("movie_id"),
                    results.getInt("screen_id"),
                    results.getTimestamp("start_time").toLocalDateTime(),
                    results.getFloat("ticket_price"),
                    results.getBoolean("is_deleted")
                ));
            }

            return screenings;
        } catch (SQLException ex) {
            System.out.println("Error retrieving screenings " + ex);
        }

        return screenings;
    }

    public static HashMap<LocalDate, ArrayList<ScreeningRecord>> getScreeningByDateRange(Date from, Date to) {
        HashMap<LocalDate, ArrayList<ScreeningRecord>> screenings = new HashMap<>();
        String sql = """ 
                SELECT
                    screening.screening_id AS scr_id, screening.movie_id AS mid, screen.screen_id AS sid, start_time, ticket_price, screening.is_deleted AS scr_is_del,
                    title, duration_minutes, releASe_date, genre, rating, description, director, movie.is_deleted AS mv_is_del,
                    screen_name, capacity, screen.is_deleted AS scr_is_del,
                    (start_time + (duration_minutes * INTERVAL '1 minute')) AS end_time,
                    COALESCE(t.sold_count, 0) AS tickets_sold,
                    (capacity - COALESCE(t.sold_count, 0)) AS seats_remaining 
                    FROM screening 
                    JOIN movie ON movie.movie_id = screening.movie_id 
                    JOIN screen ON screen.screen_id = screening.screen_id 
                    LEFT JOIN ( SELECT 
                    screening_id,
                    COUNT(ticket_id) AS sold_count 
                FROM ticket 
                GROUP BY screening_id) AS t ON t.screening_id = screening.screening_id
                WHERE screening.start_time >= ? AND screening.start_time <= ? AND screening.is_deleted = false
        """;

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setDate(1, new java.sql.Date(from.getTime()));
            stmt.setDate(2, new java.sql.Date(to.getTime()));

            stmt.execute();
            ResultSet results = stmt.getResultSet();

            while (results.next()) {
                Screening s = new Screening(
                    results.getInt("scr_id"),
                    results.getInt("mid"),
                    results.getInt("sid"),
                    results.getTimestamp("start_time").toLocalDateTime(),
                    results.getFloat("ticket_price"),
                    results.getBoolean("scr_is_del")
                );

                Movie movie = new Movie.Builder()
                        .setMovieId(results.getInt("mid"))
                        .setTitle(results.getString("title"))
                        .setDurationMinutes(results.getInt("duration_minutes"))
                        .setReleaseDate(results.getTimestamp("start_time").toLocalDateTime().toLocalDate())
                        .setGenre(results.getString("genre"))
                        .setRating(results.getString("rating"))
                        .setDescription(results.getString("description"))
                        .setDirector(results.getString("director"))
                        .setIsDeleted(results.getBoolean("mv_is_del"))
                        .build();

                Screen screen = new Screen(
                    results.getInt("sid"),
                    results.getString("screen_name"),
                    results.getInt("capacity"),
                    results.getBoolean("scr_is_del")
                );

                ScreeningRecord sr = new ScreeningRecord(s, movie, s.getStartTimeDate(), s.getStartTimeT(), screen, results.getInt("tickets_sold"), results.getInt("seats_remaining"));

                screenings.computeIfAbsent(s.getStartTimeDate(), k -> new ArrayList<>()).add(sr);
            }

            return screenings;
        } catch (SQLException ex) {
            System.out.println("Error retrieving screenings by date range " + ex);
        }

        return screenings;
    }
}
