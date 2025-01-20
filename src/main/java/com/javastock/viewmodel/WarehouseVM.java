package main.java.com.javastock.viewmodel;

import main.java.com.javastock.dao.WarehouseDAO;
import main.java.com.javastock.model.Warehouse;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class WarehouseVM {
    private WarehouseDAO warehouseDAO;

    public WarehouseVM() {
        this.warehouseDAO = new WarehouseDAO();
    }

    public DefaultTableModel getWarehouseTableModel() {
        List<Warehouse> warehouses = warehouseDAO.getAllWarehouses();
        String[] columnNames = {"ID", "Name", "Location", "Active"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Warehouse warehouse : warehouses) {
            model.addRow(new Object[]{
                    warehouse.getWarehouseId(),
                    warehouse.getWarehouseName(),
                    warehouse.getLocation(),
                    warehouse.isActive() ? "Yes" : "No"
            });
        }
        return model;
    }
}
