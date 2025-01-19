package main.java.com.javastock.viewmodel;

import main.java.com.javastock.dao.ProductDAO;
import main.java.com.javastock.model.Product;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class InventoryVM {
    private ProductDAO productDAO;

    public InventoryVM() {
        this.productDAO = new ProductDAO();
    }

    public DefaultTableModel getProductTableModel() {
        List<Product> products = productDAO.getAllProducts();
        String[] columnNames = {"ID", "Name", "Description", "Unit", "Category", "Supplier", "Reorder Level", "Price", "Active"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Product product : products) {
            model.addRow(new Object[]{
                    product.getProductId(),
                    product.getProductName(),
                    product.getDescription(),
                    product.getUnitOfMeasure(),
                    product.getCategoryId(),
                    product.getSupplierId(),
                    product.getReorderLevel(),
                    product.getUnitPrice(),
                    product.isActive() ? "Yes" : "No"
            });
        }
        return model;
    }
}
