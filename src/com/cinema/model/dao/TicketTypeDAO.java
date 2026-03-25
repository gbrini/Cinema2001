package com.cinema.model.dao;

import com.cinema.model.TicketType;
import com.cinema.model.dao.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TicketTypeDAO {
    public static ArrayList<TicketType> getTicketTypes() {
        ArrayList<TicketType> ticketTypes = new ArrayList<>();

        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement("select * from ticket_type");

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                ticketTypes.add(new TicketType(
                        result.getInt("type_id"),
                        result.getString("type_name"),
                        result.getFloat("base_discount_percent"),
                        result.getFloat("base_price_addendum")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error getting ticket types " + ex);
        }

        return ticketTypes;
    }

    public static TicketType getTicketTypeById(int id) {
        return null;
    }
}
