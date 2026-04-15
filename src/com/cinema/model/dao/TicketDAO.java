package com.cinema.model.dao;

import com.cinema.model.*;
import com.cinema.model.dao.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;

public class TicketDAO {
    public static boolean saveTicket(Ticket ticket) {
        String sql = "INSERT INTO ticket (screening_id, type_id, seat_id, user_id, purchase_time, final_price) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, ticket.getScreeningId());
            stmt.setInt(2, ticket.getTypeId());
            stmt.setInt(3, ticket.getSeatId());
            stmt.setInt(4, ticket.getUserId());

            stmt.setTimestamp(5, Timestamp.valueOf(ticket.getPurchaseTime()));
            stmt.setFloat(6, ticket.getFinalPrice());

            stmt.executeUpdate();
            return true;

        } catch (SQLException ex) {
            System.out.println("Error saving ticket: " + ex);
            return false;
        }
    }

    public static boolean saveBatchTickets(ArrayList<Ticket> tickets) {
        if (tickets == null || tickets.isEmpty()) return false;

        String sql = "INSERT INTO ticket (screening_id, type_id, seat_id, user_id, purchase_time, final_price) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance();
            conn.setAutoCommit(false);

            PreparedStatement stmt = conn.prepareStatement(sql);

            for (Ticket ticket : tickets) {
                stmt.setInt(1, ticket.getScreeningId());
                stmt.setInt(2, ticket.getTypeId());
                stmt.setInt(3, ticket.getSeatId());
                stmt.setInt(4, ticket.getUserId());
                stmt.setTimestamp(5, Timestamp.valueOf(ticket.getPurchaseTime()));
                stmt.setFloat(6, ticket.getFinalPrice());
                stmt.addBatch();
            }

            stmt.executeBatch();
            conn.commit();
            return true;

        } catch (SQLException ex) {
            System.out.println("Error saving batch tickets: " + ex);
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Error during rollback: " + rollbackEx);
            }
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Error restoring autocommit: " + ex);
            }
        }
    }

    public static ArrayList<Ticket> getTicketsByUserId(int userId) {
        ArrayList<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE user_id = ? ORDER BY purchase_time DESC";

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, userId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                tickets.add(mapRow(result));
            }

        } catch (SQLException ex) {
            System.out.println("Error getting tickets by user: " + ex);
        }

        return tickets;
    }

    public static ArrayList<TicketRecord> getTicketRecordsByUserId(int userId) {
        ArrayList<TicketRecord> records = new ArrayList<>();

        String sql = """
                SELECT
                    t.ticket_id, t.screening_id, t.type_id, t.seat_id,
                    t.user_id, t.purchase_time, t.final_price,
                    s.screen_id, s.start_time, s.ticket_price, s.is_deleted AS screening_deleted,
                    m.movie_id, m.title, m.duration_minutes, m.release_date,
                    m.genre, m.rating, m.director, m.description, m.is_deleted AS movie_deleted,
                    se.seat_id AS seat_id_join, se.screen_id AS seat_screen_id,
                    se.seat_row, se.seat_number, se.is_vip, se.is_handicap, se.is_active,
                    tt.type_id AS type_id_join, tt.type_name,
                    tt.base_discount_percent, tt.base_price_addendum, sc.screen_name
                FROM ticket t
                JOIN screening s   ON t.screening_id = s.screening_id
                JOIN movie m       ON s.movie_id     = m.movie_id
                JOIN seat se       ON t.seat_id      = se.seat_id
                JOIN ticket_type tt ON t.type_id     = tt.type_id
                JOIN screen sc on sc.screen_id = s.screen_id
                WHERE t.user_id = ?
                ORDER BY t.purchase_time DESC
        """;

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Ticket ticket = new Ticket.Builder()
                        .setTicketId(rs.getInt("ticket_id"))
                        .setScreeningId(rs.getInt("screening_id"))
                        .setTypeId(rs.getInt("type_id"))
                        .setSeatId(rs.getInt("seat_id"))
                        .setUserId(rs.getInt("user_id"))
                        .setPurchaseTime(rs.getTimestamp("purchase_time").toLocalDateTime())
                        .setFinalPrice(rs.getFloat("final_price"))
                        .build();

                Screening screening = new Screening(
                        rs.getInt("screening_id"),
                        rs.getInt("movie_id"),
                        rs.getInt("screen_id"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getFloat("ticket_price"),
                        rs.getBoolean("screening_deleted")
                );

                Movie movie = new Movie.Builder()
                        .setMovieId(rs.getInt("movie_id"))
                        .setTitle(rs.getString("title"))
                        .setDurationMinutes(rs.getInt("duration_minutes"))
                        .setReleaseDate(rs.getDate("release_date").toLocalDate())
                        .setGenre(rs.getString("genre"))
                        .setRating(rs.getString("rating"))
                        .setDirector(rs.getString("director"))
                        .setDescription(rs.getString("description"))
                        .build();

                Seat seat = new Seat(
                        rs.getInt("seat_id_join"),
                        rs.getInt("seat_screen_id"),
                        rs.getString("seat_row"),
                        rs.getInt("seat_number"),
                        rs.getBoolean("is_vip"),
                        rs.getBoolean("is_handicap"),
                        rs.getBoolean("is_active")
                );

                TicketType ticketType = new TicketType(
                        rs.getInt("type_id_join"),
                        rs.getString("type_name"),
                        rs.getFloat("base_discount_percent"),
                        rs.getFloat("base_price_addendum")
                );

                records.add(new TicketRecord(ticket, screening, movie, seat, ticketType, rs.getString("screen_name")));
            }

        } catch (SQLException ex) {
            System.out.println("Error getting ticket records: " + ex);
        }

        return records;
    }

    public static boolean deleteTicket(int ticketId) {
        String sql = "DELETE FROM ticket WHERE ticket_id = ?";

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, ticketId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException ex) {
            System.out.println("Error deleting ticket: " + ex);
            return false;
        }
    }

    private static Ticket mapRow(ResultSet result) throws SQLException {
        int userId = result.getInt("user_id");

        return new Ticket.Builder()
                .setTicketId(result.getInt("ticket_id"))
                .setScreeningId(result.getInt("screening_id"))
                .setTypeId(result.getInt("type_id"))
                .setSeatId(result.getInt("seat_id"))
                .setUserId(userId)
                .setPurchaseTime(result.getTimestamp("purchase_time").toLocalDateTime())
                .setFinalPrice(result.getFloat("final_price"))
                .build();
    }
}