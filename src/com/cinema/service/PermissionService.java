package com.cinema.service;

import com.cinema.model.User;
import com.cinema.model.dao.PermissionDAO;

import java.sql.SQLException;
import java.util.Set;

public class PermissionService {
    public static boolean hasPermission(User user, String permissionName) {
        if (user == null || user.getRole() == null) {
            return false;
        }

        try {
            Set<String> permissions = PermissionDAO.getPermissionByRole(user.getRole().getRoleId());
            return permissions.contains(permissionName);
        } catch (SQLException ex) {
            System.out.println("Error retrieving permission: " + ex);
        }

        return false;
    }
}
