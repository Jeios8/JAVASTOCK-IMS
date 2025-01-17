package main.java.com.javastock.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {
    private static final String CONFIG_FILE = "src/main/java/com/javastock/config/application.properties";
    private static String dbUrl;
    private static String username;
    private static String password;

    static {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(CONFIG_FILE));

            dbUrl = properties.getProperty("db.url");
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");

            if (dbUrl == null || username == null || password == null) {
                throw new IllegalArgumentException("Database properties not properly configured");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }

    public static Connection getConnection(String dbName) {
        if (dbName == null || dbName.trim().isEmpty()) {
            throw new IllegalArgumentException("Database name cannot be null or empty");
        }

        String fullDbUrl = dbUrl + dbName;
        try {
            return DriverManager.getConnection(fullDbUrl, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to the database: " + dbName, e);
        }
    }
}
