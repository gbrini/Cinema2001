package com.cinema.service.auth.core;

import com.cinema.model.dao.UserDAO;
import com.cinema.service.auth.IAuthenticationService;
import com.cinema.model.Role;
import com.cinema.model.User;
import com.cinema.model.dao.database.QueryBuilder;
import com.cinema.service.auth.UserSession;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class AuthenticationService implements IAuthenticationService {
    @Override
    public User login(String email, String password) throws SQLException {
        User user = UserDAO.login(email, password);
        UserSession.getInstance().createSession(user);
        return user;
    }

    @Override
    public boolean logout() {
        UserSession.getInstance().cleanSession();
        return true;
    }
}
