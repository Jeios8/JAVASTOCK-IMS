package main.java.com.javastock.viewmodel;

import main.java.com.javastock.viewmodel.DashboardVM;
import main.java.com.javastock.viewmodel.InventoryVM;

public class MainVM {
    private DashboardVM dashboardVM;
    private InventoryVM inventoryVM;

    public MainVM() {
        // Initialize sub-viewmodels
        this.dashboardVM = new DashboardVM();
        this.inventoryVM = new InventoryVM();
    }

    public DashboardVM getDashboardVM() {
        return dashboardVM;
    }

    public InventoryVM getInventoryVM() {
        return inventoryVM;
    }

    public String fetchContent(String section) {
        switch (section) {
            case "dashboard":
                return "Dashboard Content Loaded";
            case "inventory":
                return "Inventory Content Loaded";
            default:
                return "Unknown Section";
        }
    }
}
