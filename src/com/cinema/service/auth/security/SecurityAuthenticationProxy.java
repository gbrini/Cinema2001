package com.cinema.service.auth.security;

import com.cinema.service.auth.IAuthenticationService;
import com.cinema.model.User;

import java.sql.SQLException;

public class SecurityAuthenticationProxy implements IAuthenticationService {
    private final IAuthenticationService realService;

    public SecurityAuthenticationProxy(IAuthenticationService realService) {
        this.realService = realService;
    }

    @Override
    public User login(String email, String password) throws SQLException {
        if(email == null || email.isBlank() || password == null || password.isBlank()) {
            System.out.println("[Security] Invalid input data for login");
            return null;
        }

        return realService.login(email, password);
    }

    @Override
    public boolean logout() { return realService.logout(); }
}
