// Updated DatabaseConnector.java
package main.java.com.javastock.utils;

import main.java.com.javastock.model.User;
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

    public static User getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = getConnection("inventorydb");
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userid = rs.getInt("user_id");
                String passwordHash = rs.getString("password_hash");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int roleId = rs.getInt("role_id");
                boolean isActive = rs.getBoolean("is_active");
                return new User(userid, username, passwordHash, firstName, lastName, roleId, isActive);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch user from database", e);
        }
        return null;
    }
}
