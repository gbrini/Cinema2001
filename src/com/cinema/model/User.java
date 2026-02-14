package com.cinema.model;

import com.cinema.model.Role;

import java.util.Date;

public class User {
    private final int userId;
    private final String firstName;
    private final String lastName;
    private final Date birthDate;
    private final String email;
    private final Role role;

    public User(int userId, String firstName, String lastName, Date birthDate, String email, Role role) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}
