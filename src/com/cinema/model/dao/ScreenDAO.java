package com.cinema.model.dao;

import com.cinema.model.Screen;
import com.cinema.model.dao.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;

public class ScreenDAO {
    public static int addScreen(Screen screen) {
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO screen (screen_name, capacity) VALUES (?, ?) RETURNING screen_id");

            stmt.setString(1, screen.getScreenName());
            stmt.setInt(2, screen.getCapacity());

            stmt.execute();

            ResultSet last_insert = stmt.getResultSet();

            if (last_insert.next()) {
                return last_insert.getInt(1);
            }

            return 0;
        } catch (SQLException ex) {
            System.out.println("Error getting latest screen id " + ex);
        }

        return 0;
    }

    public static int getLastScreenId() {
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement("SELECT screen_id FROM screen ORDER BY screen_id DESC LIMIT 1 ");

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                return result.getInt("screen_id");
            }

            return 0;
        } catch (SQLException ex) {
            System.out.println("Error getting latest screen id " + ex);
        }

        return 0;
    }

    public static ArrayList<Screen> getAllScreen() {
        ArrayList<Screen> screens = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM screen ORDER BY screen_id DESC ");

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                screens.add(new Screen(
                        result.getInt("screen_id"),
                        result.getString("screen_name"),
                        result.getInt("capacity"),
                        result.getBoolean("is_deleted")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error getting all screens " + ex);
        }

        return screens;
    }
}
