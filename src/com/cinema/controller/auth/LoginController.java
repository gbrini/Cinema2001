package com.cinema.controller.auth;

import com.cinema.model.auth.IAuthenticationService;
import com.cinema.model.auth.core.AuthenticationService;
import com.cinema.model.auth.security.SecurityAuthenticationProxy;
import com.cinema.model.User;

import java.sql.SQLException;

public class LoginController {
    private static final LoginController instance = new LoginController();
    private final IAuthenticationService proxyAuth;

    private LoginController() {
        IAuthenticationService realAuth = new AuthenticationService();
        proxyAuth = new SecurityAuthenticationProxy(realAuth);
    }

    public static LoginController getInstance() {
        return instance;
    }

    public User login(String email, String password) throws SQLException {
        return proxyAuth.login(email, password);
    }
}
