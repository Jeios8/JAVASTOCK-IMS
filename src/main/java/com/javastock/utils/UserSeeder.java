package main.java.com.javastock.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserSeeder {

    public UserSeeder() {
    }

    public void seedUsers() {
        String query = """
            INSERT INTO users (username, password_hash, first_name, last_name, role_id, is_active)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        // Seed data
        Object[][] users = {
                {"johndoe", "1234", "John", "Doe", 2, true},    // Admin
                {"janedoe", "1234", "Jane", "Doe", 3, true},    // Manager
                {"bobsmith", "1234", "Bob", "Smith", 4, true},  // Staff
                {"alicebrown", "1234", "Alice", "Brown", 1, true},  // Moderator
                {"charliewhite", "1234", "Charlie", "White", 5, true},  // Viewer
                {"emilygreen", "1234", "Emily", "Green", 3, true},  // Manager
                {"davidblue", "1234", "David", "Blue", 4, true},  // Staff
                {"susanpink", "1234", "Susan", "Pink", 2, true},  // Admin
                {"willgray", "1234", "Will", "Gray", 1, true},  // Moderator
                {"rachelyellow", "1234", "Rachel", "Yellow", 5, true}  // Viewer
        };

        try (Connection connection = DatabaseConnector.getConnection("errjava_invdb");
             PreparedStatement stmt = connection.prepareStatement(query)) {

            for (Object[] user : users) {
                String username = (String) user[0];
                String plainPassword = (String) user[1];
                String firstName = (String) user[2];
                String lastName = (String) user[3];
                int roleId = (int) user[4];
                boolean isActive = (boolean) user[5];

                // Hash the password
                String hashedPassword = Hasher.hashPassword(plainPassword);

                // Bind parameters
                stmt.setString(1, username);
                stmt.setString(2, hashedPassword);
                stmt.setString(3, firstName);
                stmt.setString(4, lastName);
                stmt.setInt(5, roleId);
                stmt.setBoolean(6, isActive);

                // Add to batch
                stmt.addBatch();
            }

            // Execute batch
            stmt.executeBatch();
            System.out.println("Users seeded successfully!");

        } catch (SQLException e) {
            System.err.println("Error during database seeding: " + e.getMessage());
        }
    }
}
