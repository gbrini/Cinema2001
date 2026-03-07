package com.cinema.util;

import com.cinema.model.dao.database.DatabaseConfig;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class EnvConfig {
    private static EnvConfig instance;
    private final Properties properties = new Properties();

    private EnvConfig() {
        Path envFile = Paths.get("env.properties");

        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("env.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find env.properties");
                System.exit(1);
            }

            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static EnvConfig getInstance() {
        if (instance == null) {
            instance = new EnvConfig();
        }
        return instance;
    }

    public String get(String key) {
        return properties.getProperty(key);
    }
}
