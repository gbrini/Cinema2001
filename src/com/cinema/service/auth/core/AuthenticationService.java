package com.cinema.service.auth.core;

import com.cinema.model.dao.UserDAO;
import com.cinema.service.auth.IAuthenticationService;
import com.cinema.model.Role;
import com.cinema.model.User;
import com.cinema.model.dao.database.QueryBuilder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class AuthenticationService implements IAuthenticationService {
    @Override
    public User login(String email, String password) throws SQLException {
        return UserDAO.login(email, password);
    }

    @Override
    public boolean logout() {
        return false;
    }
}
