package com.cinema.model.dao.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static Properties properties = null;

    private static void load(String fileName) {
        properties = new Properties();

        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                System.out.println("Sorry, unable to find db.properties");
                System.exit(1);
            }

            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Properties get() {
        if(properties == null) load("db.properties");
        return properties;
    }

    public static void useTestDB() {
        load("db-test.properties");
        DatabaseConnection.resetInstance();
    }

    public static String getDbUrl() {
        return get().getProperty("db.url");
    }

    public static String getDbUsername() {
        return get().getProperty("db.username");
    }

    public static String getDbPassword() {
        return get().getProperty("db.password");
    }
}
