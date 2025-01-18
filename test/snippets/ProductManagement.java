import java.sql.*;
import java.util.Scanner;

public class ProductManagement {
    private static final String URL = "jdbc:mysql://localhost:3306/inventory"; // replace with your database name
    private static final String USER = "root"; // replace with your MySQL username
    private static final String PASSWORD = "root"; // replace with your MySQL password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nProduct Management System");
            System.out.println("1. Add Product");
            System.out.println("2. Edit Product");
            System.out.println("3. Delete Product");
            System.out.println("4. View All Products");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addProduct(scanner);
                    break;
                case 2:
                    editProduct(scanner);
                    break;
                case 3:
                    deleteProduct(scanner);
                    break;
                case 4:
                    viewAllProducts();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addProduct(Scanner scanner) {
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter unit price: ");
        double unitPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter expiry date (YYYY-MM-DD): ");
        String expiryDate = scanner.nextLine();
        System.out.print("Enter threshold: ");
        int threshold = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter image path: ");
        String imagePath = scanner.nextLine();

        String query = "INSERT INTO products (product_name, category, unit_price, expiry_date, threshold, image_path) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, productName);
            pstmt.setString(2, category);
            pstmt.setDouble(3, unitPrice);
            pstmt.setDate(4, Date.valueOf(expiryDate));
            pstmt.setInt(5, threshold);
            pstmt.setString(6, imagePath);
            pstmt.executeUpdate();
            System.out.println("Product added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    private static void editProduct(Scanner scanner) {
        System.out.print("Enter product ID to edit: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new product name: ");
        String productName = scanner.nextLine();
        System.out.print("Enter new category: ");
        String category = scanner.nextLine();
        System.out.print("Enter new unit price: ");
        double unitPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new expiry date (YYYY-MM-DD): ");
        String expiryDate = scanner.nextLine();
        System.out.print("Enter new threshold: ");
        int threshold = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new image path: ");
        String imagePath = scanner.nextLine();

        String query = "UPDATE products SET product_name = ?, category = ?, unit_price = ?, expiry_date = ?, threshold = ?, image_path = ? WHERE product_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, productName);
            pstmt.setString(2, category);
            pstmt.setDouble(3, unitPrice);
            pstmt.setDate(4, Date.valueOf(expiryDate));
            pstmt.setInt(5, threshold);
            pstmt.setString(6, imagePath);
            pstmt.setInt(7, productId);
            pstmt.executeUpdate();
            System.out.println("Product updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    private static void deleteProduct(Scanner scanner) {
        System.out.print("Enter product ID to delete: ");
        int productId = scanner.nextInt();

        String query = "DELETE FROM products WHERE product_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
            System.out.println("Product deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }

    private static void viewAllProducts() {
        String query = "SELECT * FROM products";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nAll Products:");
            while (rs.next()) {
                int id = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                String category = rs.getString("category");
                double unitPrice = rs.getDouble("unit_price");
                Date expiryDate = rs.getDate("expiry_date");
                int threshold = rs.getInt("threshold");
                String imagePath = rs.getString("image_path");

                System.out.println("ID: " + id + ", Name: " + productName +
                        ", Category: " + category +
                        ", Unit Price: " + unitPrice +
                        ", Expiry Date: " + expiryDate +
                        ", Threshold: " + threshold +
                        ", Image Path: " + imagePath);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving products: " + e.getMessage());
        }
    }
}





/*
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.*;

public class ProductManagement extends JFrame {
    private static final String URL = "jdbc:mysql://localhost:3306/inventory";
    private static final String USER = "root"; // replace with your MySQL username
    private static final String PASSWORD = "root"; // replace with your MySQL password

    private JTextField productIdField, productNameField, categoryField, unitPriceField, expiryDateField, thresholdField, imagePathField;
    private JTextArea outputArea;

    public ProductManagement() {
        setTitle("Inventory Management System");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input fields
        JPanel inputPanel = new JPanel(new GridLayout(7, 2));
        inputPanel.add(new JLabel("Product ID:"));
        productIdField = new JTextField();
        inputPanel.add(productIdField);

        inputPanel.add(new JLabel("Product Name:"));
        productNameField = new JTextField();
        inputPanel.add(productNameField);

        inputPanel.add(new JLabel("Category:"));
        categoryField = new JTextField();
        inputPanel.add(categoryField);

        inputPanel.add(new JLabel("Unit Price:"));
        unitPriceField = new JTextField();
        inputPanel.add(unitPriceField);

        inputPanel.add(new JLabel("Expiry Date (YYYY-MM-DD):"));
        expiryDateField = new JTextField();
        inputPanel.add(expiryDateField);

        inputPanel.add(new JLabel("Threshold:"));
        thresholdField = new JTextField();
        inputPanel.add(thresholdField);

        inputPanel.add(new JLabel("Image Path:"));
        imagePathField = new JTextField();
        inputPanel.add(imagePathField);

        add(inputPanel, BorderLayout.NORTH);

        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Product");
        JButton editButton = new JButton("Edit Product");
        JButton deleteButton = new JButton("Delete Product");
        JButton viewButton = new JButton("View All Products");

        addButton.addActionListener(e -> addProduct());
        editButton.addActionListener(e -> editProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        viewButton.addActionListener(e -> viewAllProducts());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addProduct() {
       // int productId = Integer.parseInt(productIdField.getText());
        String productId = productIdField.getText();
        String productName = productNameField.getText();
        String category = categoryField.getText();
        String unitPrice = unitPriceField.getText();
        String expiryDate = expiryDateField.getText();
        String threshold = thresholdField.getText();
        String imagePath = imagePathField.getText();

        String query = "INSERT INTO products (product_id, product_name, category, unit_price, expiry_date, threshold, image_path) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, productId);
            pstmt.setString(2, productName);
            pstmt.setString(3, category);
            pstmt.setBigDecimal(4, new BigDecimal(unitPrice));
            pstmt.setDate(5, Date.valueOf(expiryDate));
            pstmt.setInt(6, Integer.parseInt(threshold));
            pstmt.setString(7, imagePath);
            pstmt.executeUpdate();
            outputArea.append("Product added successfully.\n");
        } catch (SQLException e) {
            e.printStackTrace();
            outputArea.append("Error adding product: " + e.getMessage() + "\n");
        }
    }

    private void editProduct() {
        int productId = Integer.parseInt(productIdField.getText());
     //   String productId = productIdField.getText();
        String productName = productNameField.getText();
        String category = categoryField.getText();
        String unitPrice = unitPriceField.getText();
        String expiryDate = expiryDateField.getText();
        String threshold = thresholdField.getText();
        String imagePath = imagePathField.getText();

        String query = "UPDATE products SET product_id = ?, product_name = ?, category = ?, unit_price = ?, expiry_date = ?, threshold = ?, image_path = ? WHERE product_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
        //    pstmt.setString(1, productId);
            pstmt.setString(1, productName);
            pstmt.setString(2, category);
            pstmt.setBigDecimal(3, new BigDecimal(unitPrice));
            pstmt.setDate(4, Date.valueOf(expiryDate));
            pstmt.setInt(5, Integer.parseInt(threshold));
            pstmt.setString(6, imagePath);
            pstmt.setInt(7, productId);
            pstmt.executeUpdate();
            outputArea.append("Product updated successfully.\n");
        } catch (SQLException e) {
            e.printStackTrace();
            outputArea.append("Error updating product: " + e.getMessage() + "\n");
        }
    }

    private void deleteProduct() {
        int productId = Integer.parseInt(productIdField.getText());

        String query = "DELETE FROM products WHERE product_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
            outputArea.append("Product deleted successfully.\n");
        } catch (SQLException e) {
            e.printStackTrace();
            outputArea.append("Error deleting product: " + e.getMessage() + "\n");
        }
    }

    private void viewAllProducts() {
        String query = "SELECT * FROM products";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            outputArea.setText(""); // Clear previous output
            while (rs.next()) {
                int id = rs.getInt("product_id");
                String name = rs.getString("product_name");
                String category = rs.getString("category");
                BigDecimal price = rs.getBigDecimal("unit_price");
                Date expiry = rs.getDate("expiry_date");
                int threshold = rs.getInt("threshold");
                String imagePath = rs.getString("image_path");

                outputArea.append("ID: " + id + ", Name: " + name + ", Category: " + category +
                        ", Price: " + price + ", Expiry Date: " + expiry +
                        ", Threshold: " + threshold + ", Image Path: " + imagePath + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            outputArea.append("Error retrieving products: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProductManagement frame = new ProductManagement();
            frame.setVisible(true);
        });
    }
}*/
