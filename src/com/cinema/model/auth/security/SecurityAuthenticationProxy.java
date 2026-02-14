package com.cinema.model.auth.security;

import com.cinema.model.auth.IAuthenticationService;
import com.cinema.model.User;

import java.sql.SQLException;

public class SecurityAuthenticationProxy implements IAuthenticationService {
    private final IAuthenticationService realService;

    public SecurityAuthenticationProxy(IAuthenticationService realService) {
        this.realService = realService;
    }

    @Override
    public User login(String email, String password) throws SQLException {
        return realService.login(email, password);
    }

    @Override
    public boolean logout() {
        return false;
    }
}
