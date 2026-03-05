package com.cinema.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class EnvConfig {
    private static EnvConfig instance;
    private final Properties properties = new Properties();

    private EnvConfig() {
        Path envFile = Paths.get(System.getProperty("user.home"), ".cinema.util", "env.properties");
        try(var inputStream = Files.newInputStream(envFile)) {
            properties.load(inputStream);
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    private EnvConfig getInstance() {
        if (instance == null) {
            instance = new EnvConfig();
        }
        return instance;
    }

    public String get(String key) {
        return properties.getProperty(key);
    }
}
