package main.java.com.javastock.dao;

import main.java.com.javastock.model.Product;
import main.java.com.javastock.utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Fetch all products from the database
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products"; // Ensure your table name is correct

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("description"),
                        rs.getString("unit_of_measure"),
                        rs.getInt("category_id"),
                        rs.getInt("supplier_id"),
                        rs.getInt("reorder_level"),
                        rs.getDouble("unit_price"),
                        rs.getString("image_path"),
                        rs.getBoolean("is_active")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch products from database", e);
        }
        return products;
    }

    // Fetch a single product by ID
    public Product getProductById(int productId) {
        String query = "SELECT * FROM products WHERE product_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("description"),
                        rs.getString("unit_of_measure"),
                        rs.getInt("category_id"),
                        rs.getInt("supplier_id"),
                        rs.getInt("reorder_level"),
                        rs.getDouble("unit_price"),
                        rs.getString("image_path"),
                        rs.getBoolean("is_active")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch product from database", e);
        }
        return null;
    }

    // Insert a new product into the database
    public boolean insertProduct(Product product) {
        String query = "INSERT INTO products (product_name, description, unit_of_measure, category_id, supplier_id, reorder_level, unit_price, image_path, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setString(3, product.getUnitOfMeasure());
            stmt.setInt(4, product.getCategoryId());
            stmt.setInt(5, product.getSupplierId());
            stmt.setInt(6, product.getReorderLevel());
            stmt.setDouble(7, product.getUnitPrice());
            stmt.setString(8, product.getImagePath());
            stmt.setBoolean(9, product.isActive());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert product into database", e);
        }
    }

    // Update an existing product
    public boolean updateProduct(Product product) {
        String query = "UPDATE products SET product_name=?, description=?, unit_of_measure=?, category_id=?, supplier_id=?, reorder_level=?, unit_price=?, image_path=?, is_active=? WHERE product_id=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setString(3, product.getUnitOfMeasure());
            stmt.setInt(4, product.getCategoryId());
            stmt.setInt(5, product.getSupplierId());
            stmt.setInt(6, product.getReorderLevel());
            stmt.setDouble(7, product.getUnitPrice());
            stmt.setString(8, product.getImagePath());
            stmt.setBoolean(9, product.isActive());
            stmt.setInt(10, product.getProductId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update product in database", e);
        }
    }

    // Delete a product by ID
    public boolean deleteProduct(int productId) {
        String query = "DELETE FROM products WHERE product_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete product from database", e);
        }
    }
}
