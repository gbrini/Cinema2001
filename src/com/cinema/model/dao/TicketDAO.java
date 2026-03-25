package com.cinema.model.dao;

import com.cinema.model.Ticket;
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

    public static boolean isSeatTaken(int screeningId, int seatId) {
        String sql = "SELECT 1 FROM ticket WHERE screening_id = ? AND seat_id = ?";

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, screeningId);
            stmt.setInt(2, seatId);
            ResultSet result = stmt.executeQuery();

            return result.next();

        } catch (SQLException ex) {
            System.out.println("Error checking seat availability: " + ex);
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