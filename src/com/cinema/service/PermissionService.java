package com.cinema.service;

import com.cinema.model.User;
import com.cinema.model.dao.PermissionDAO;
import com.cinema.service.auth.UserSession;

import java.sql.SQLException;
import java.util.Set;

public class PermissionService {
    public static boolean hasPermission(String permissionName) {
        User user = UserSession.getInstance().getCurrentUser();

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
