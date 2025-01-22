package main.java.com.javastock.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Utility class for managing database connections.
 * <p>
 * This class reads database configuration from a properties file and provides
 * a method to establish a connection to the database.
 * </p>
 */
public class DatabaseConnector {

    private static final String CONFIG_FILE = "src/main/java/com/javastock/config/application.properties"; // Path to the configuration file
    private static String dbUrl;    // Database URL
    private static String username; // Database username
    private static String password; // Database password

    // Static initializer block for loading database configurations
    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            // Load properties from the configuration file
            Properties properties = new Properties();
            properties.load(fis);

            // Construct the full database URL
            dbUrl = properties.getProperty("db.url") + "errjava_invdb";
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");

            // Validate that all required properties are present
            if (dbUrl == null || username == null || password == null) {
                throw new IllegalStateException("Database properties are missing.");
            }
        } catch (IOException e) {
            // Wrap and rethrow exception for easier debugging
            throw new RuntimeException("Failed to load database config: " + CONFIG_FILE, e);
        }
    }

    /**
     * Establishes and returns a connection to the database.
     *
     * @return a {@link Connection} object to interact with the database.
     * @throws RuntimeException if the connection to the database cannot be established.
     */
    public static Connection getConnection() {
        try {
            // Establish and return the database connection
            return DriverManager.getConnection(dbUrl, username, password);
        } catch (SQLException e) {
            // Wrap and rethrow exception with a meaningful message
            throw new RuntimeException("Database connection error.", e);
        }
    }
}
