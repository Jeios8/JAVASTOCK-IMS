import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryManagement {
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT product_id, product_name, description, unit_of_measure, unit_price " +
                "FROM products WHERE is_active = TRUE ORDER BY product_name ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("description"),
                        rs.getString("unit_of_measure"),
                        rs.getDouble("unit_price")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}

