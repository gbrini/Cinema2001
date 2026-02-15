package com.cinema.controller.auth;

import com.cinema.service.auth.IAuthenticationService;
import com.cinema.service.auth.core.AuthenticationService;
import com.cinema.service.auth.security.SecurityAuthenticationProxy;
import com.cinema.model.User;
import com.cinema.view.auth.LoginFrame;

import javax.swing.*;
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

    public void logout(JFrame currentFrame) {
        boolean isLogout = proxyAuth.logout();

        if(currentFrame != null) {
            currentFrame.dispose();
        }

        new LoginFrame().setVisible(true);
    }
}
