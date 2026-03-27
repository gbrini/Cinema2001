package com.cinema.model.dao;

import com.cinema.model.Seat;
import com.cinema.model.SeatEditor;
import com.cinema.model.dao.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SeatDAO {
    public static boolean addSeats(ArrayList<Seat> seats) {
        String sql = " INSERT INTO seat (screen_id, seat_row, seat_number, is_vip, is_handicap) VALUES (?, ?, ?, ?, ?) ";

        try {
            Connection conn = DatabaseConnection.getInstance();
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement(sql);

            int i = 0;
            for (Seat seat: seats) {
                stmt.setInt(1, seat.getScreenId());
                stmt.setString(2, seat.getSeatRow());
                stmt.setInt(3, seat.getSeatNumber());
                stmt.setBoolean(4, seat.isVip());
                stmt.setBoolean(5, seat.isHandicap());

                stmt.addBatch();

                if (++i % 1000 == 0 || i == seats.size()) {
                    stmt.executeBatch();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException ex) {
            System.out.println("Error inserting seats " + ex);
        }

        return false;
    }

    public static boolean upsertSeats(ArrayList<Seat> seats) {
        String sql = "INSERT INTO seat (screen_id, seat_row, seat_number, is_vip, is_handicap, is_active) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (screen_id, seat_row, seat_number) " +
                "DO UPDATE SET is_vip = EXCLUDED.is_vip, is_handicap = EXCLUDED.is_handicap, is_active = EXCLUDED.is_active";

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(sql);

            int i = 0;
            for (Seat seat: seats) {
                stmt.setInt(1, seat.getScreenId());
                stmt.setString(2, seat.getSeatRow());
                stmt.setInt(3, seat.getSeatNumber());
                stmt.setBoolean(4, seat.isVip());
                stmt.setBoolean(5, seat.isHandicap());
                stmt.setBoolean(6, seat.isActive());

                stmt.addBatch();

                if (++i % 1000 == 0 || i == seats.size()) {
                    stmt.executeBatch();
                }
            }

            return true;
        } catch (SQLException ex) {
            System.out.println("Error upserting seats " + ex);
        }

        return false;
    }

    public static boolean removeSeatsByScreenId(int screenId) {
        String sql = " DELETE FROM seat WHERE screen_id = ? ";

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, screenId);

            return stmt.execute();
        } catch (SQLException ex) {
            System.out.println("Error removing seats " + ex);
        }

        return false;
    }

    public static ArrayList<Seat> getSeatsByScreenId(int screenId) {
        ArrayList<Seat> seats = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM seat WHERE screen_id = ? ORDER BY seat_id ASC ");

            stmt.setInt(1, screenId);

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                seats.add(new Seat(
                    result.getInt("seat_id"),
                    result.getInt("screen_id"),
                    result.getString("seat_row"),
                    result.getInt("seat_number"),
                    result.getBoolean("is_vip"),
                    result.getBoolean("is_handicap"),
                    result.getBoolean("is_active")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error getting seats by screenId " + ex);
        }

        return seats;
    }

    public static ArrayList<SeatEditor> getSeatsStatusByScreeningId(int screeningId) {
        ArrayList<SeatEditor> seats = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstance();
            String query = """
                SELECT
                    seat.*,
                    CASE WHEN ticket.ticket_id IS NULL THEN FALSE ELSE TRUE END AS taken
                FROM seat
                LEFT JOIN ticket
                    ON ticket.seat_id = seat.seat_id
                    AND ticket.screening_id = ?
                WHERE seat.screen_id = (
                    SELECT screen_id FROM screening WHERE screening_id = ?
                )
            """;

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, screeningId);
            stmt.setInt(2, screeningId);

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                seats.add(new SeatEditor(
                    result.getInt("seat_id"),
                    result.getString("seat_row"),
                    result.getInt("seat_number"),
                    result.getBoolean("is_vip"),
                    result.getBoolean("is_handicap"),
                    result.getBoolean("is_active"),
                    false,
                    result.getBoolean("taken")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error getting seats by screeningId " + ex);
        }

        return seats;
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
}
