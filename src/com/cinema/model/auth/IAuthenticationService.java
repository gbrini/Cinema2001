package com.cinema.model.auth;

import com.cinema.model.User;

import java.sql.SQLException;

public interface IAuthenticationService {
    User login(String email, String password) throws SQLException;
    boolean logout();
}
