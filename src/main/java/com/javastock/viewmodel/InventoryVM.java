package main.java.com.javastock.viewmodel;

import main.java.com.javastock.model.Inventory;
import main.java.com.javastock.utils.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InventoryVM {
    public List<Inventory> fetchInventory() {
        List<Inventory> inventoryList = new ArrayList<>();
        String query = "SELECT * FROM inventory";

        try (Connection conn = DatabaseConnector.getConnection("inventorydb");
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Inventory item = new Inventory(
                        rs.getInt("inventory_id"),
                        rs.getInt("product_id"),
                        rs.getInt("warehouse_id"),
                        rs.getInt("quantity")
                );
                inventoryList.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inventoryList;
    }
}
