import java.sql.*;
import java.util.Scanner;

public class UserManagement {
    private static final String URL = "jdbc:mysql://localhost:3306/inventory"; // replace with your database name
    private static final String USER = "root"; // replace with your MySQL username
    private static final String PASSWORD = "root"; // replace with your MySQL password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("User Management System");
            System.out.println("1. Add User");
            System.out.println("2. Edit User");
            System.out.println("3. Delete User");
            System.out.println("4. View All Users");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addUser(scanner);
                    break;
                case 2:
                    editUser(scanner);
                    break;
                case 3:
                    deleteUser(scanner);
                    break;
                case 4:
                    viewAllUsers();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine(); // Hash for production
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter role (admin/user): ");
        String role = scanner.nextLine();

        String query = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // Hash the password in production
            pstmt.setString(3, email);
            pstmt.setString(4, role);
            pstmt.executeUpdate();
            System.out.println("User added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    private static void editUser(Scanner scanner) {
        System.out.print("Enter user ID to edit: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new username: ");
        String username = scanner.nextLine();
        System.out.print("Enter new password: ");
        String password = scanner.nextLine(); // Hash for production
        System.out.print("Enter new email: ");
        String email = scanner.nextLine();
        System.out.print("Enter new role (admin/user): ");
        String role = scanner.nextLine();

        String query = "UPDATE users SET username = ?, password = ?, email = ?, role = ? WHERE user_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // Hash the password in production
            pstmt.setString(3, email);
            pstmt.setString(4, role);
            pstmt.setInt(5, userId);
            pstmt.executeUpdate();
            System.out.println("User updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    private static void deleteUser(Scanner scanner) {
        System.out.print("Enter user ID to delete: ");
        int userId = scanner.nextInt();

        String query = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            System.out.println("User deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    private static void viewAllUsers() {
        String query = "SELECT * FROM users";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("user_id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String role = rs.getString("role");
                Timestamp createdAt = rs.getTimestamp("created_at");

                System.out.println("ID: " + id + ", Username: " + username +
                        ", Email: " + email + ", Role: " + role +
                        ", Created At: " + createdAt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving users: " + e.getMessage());
        }
    }
}