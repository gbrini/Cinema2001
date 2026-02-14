package com.cinema.model.dao.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance = null;
    private Connection conn = null;

    private DatabaseConnection() {}

    private void init() throws SQLException {
        conn = DriverManager.getConnection(DatabaseConfig.getDbUrl(), DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
        System.out.println("Database connected!");
    }

    public Connection getConnection() {
        return conn;
    }

    public static Connection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
            instance.init();
        }

        return instance.getConnection();
    }
}
