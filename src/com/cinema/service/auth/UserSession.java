package com.cinema.service.auth;

import com.cinema.model.User;

public class UserSession {
    private static UserSession instance;
    private User currentUser;

    private UserSession() {}

    public static UserSession getInstance() {
        if(instance == null) {
            instance = new UserSession();
        }

        return instance;
    }

    public void createSession(User user) { this.currentUser = user; }
    public void cleanSession() { this.currentUser = null; }
    public User getCurrentUser() { return this.currentUser; }
}