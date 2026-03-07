package com.cinema.model.dao;

import com.cinema.model.Role;
import com.cinema.model.User;
import com.cinema.model.dao.database.QueryBuilder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class UserDAO {
    public static User login(String email, String password) throws SQLException {
        String sql = "SELECT user_id, first_name, last_name, email, birth_date, role.role_id, role_name FROM users LEFT JOIN role on role.role_id = users.role_id WHERE email = ? AND password = ?";
        User user = null;

        try (
                PreparedStatement stmt = QueryBuilder.prepareStatement(sql, Arrays.asList(email, password));
                ResultSet result = stmt.executeQuery()
        ) {
            if (result.next()) {
                user = new User(
                        result.getInt("user_id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getDate("birth_date"),
                        result.getString("email"),
                        new Role(result.getInt("role_id"), result.getString("role_name"))
                );
            }
        }

        return user;
    }
}
