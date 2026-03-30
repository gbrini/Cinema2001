package com.cinema.model.dao.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static final Properties properties = new Properties();
    private static String propertiesFile = "db.properties";

    public static void useTestDB() {
        propertiesFile = "db-test.properties";
    }

    static {
        reload();
    }

    private static void reload() {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream(propertiesFile)) {
            if (input == null) {
                System.out.println("Sorry, unable to find db.properties");
                System.exit(1);
            }

            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDbUrl() {
        return properties.getProperty("db.url");
    }

    public static String getDbUsername() {
        return properties.getProperty("db.username");
    }

    public static String getDbPassword() {
        return properties.getProperty("db.password");
    }
}
