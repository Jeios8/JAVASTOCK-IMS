import java.sql.*;
import java.util.Scanner;

public class Login {

    // Database credentials (Replace with your actual credentials)
    private static final String url = "jdbc:mysql://localhost:3306/InventoryDB";
    private static final String user = "root";
    private static final String password = "root";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String passwordInput = scanner.nextLine();

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, passwordInput);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Login Successful!");
                // Proceed to other operations here
            } else {
                System.out.println("Invalid username or password.");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}