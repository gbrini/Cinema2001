package com.cinema.model.dao;

import com.cinema.model.dao.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class PermissionDAO {
    public static Set<String> getPermissionByRole(int roleId) throws SQLException {
        Set<String> permissions = new HashSet<>();

        String sql = "SELECT p.permission_name FROM permission p JOIN role_permission rp on rp.permission_id = p.permission_id AND rp.role_id = ? ";

        Connection conn = DatabaseConnection.getInstance();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, roleId);

        ResultSet results = stmt.executeQuery();

        while(results.next()) {
            permissions.add(results.getString("permission_name"));
        }

        return permissions;
    }
}
