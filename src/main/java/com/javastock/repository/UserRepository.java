package main.java.com.javastock.repository;

import main.java.com.javastock.model.User;
import main.java.com.javastock.utils.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
    private static final Map<String, User> userCache = new ConcurrentHashMap<>();
    private static final long CACHE_EXPIRATION_MS = 10 * 60 * 1000; // 10 minutes
    private static final Map<String, Long> cacheTimestamps = new ConcurrentHashMap<>();

    public User getUserByUsername(String username) {
        long currentTime = System.currentTimeMillis();

        // Check if user exists in cache & hasn't expired
        if (userCache.containsKey(username) && (currentTime - cacheTimestamps.get(username) < CACHE_EXPIRATION_MS)) {
            return userCache.get(username);
        }

        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getInt("role_id"),
                            rs.getBoolean("is_active")
                    );

                    // Store user in cache
                    userCache.put(username, user);
                    cacheTimestamps.put(username, currentTime);

                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user: " + username, e);
        }
        return null;
    }

    public void clearCache() {
        userCache.clear();
        cacheTimestamps.clear();
    }
}
