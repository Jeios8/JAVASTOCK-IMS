package main.java.com.javastock.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnector {
    private static final String CONFIG_FILE = "src/main/java/com/javastock/config/application.properties";
    private static String dbUrl;
    private static String username;
    private static String password;

    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            Properties properties = new Properties();
            properties.load(fis);

            dbUrl = properties.getProperty("db.url") + "errjava_invdb"; // Full URL constructed once
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");

            if (dbUrl == null || username == null || password == null) {
                throw new IllegalStateException("Database properties are missing.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database config: " + CONFIG_FILE, e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(dbUrl, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error.", e);
        }
    }
}
