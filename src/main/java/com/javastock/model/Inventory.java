package main.java.com.javastock.model;

public class Inventory {
    private int inventoryId;
    private int productId;
    private int warehouseId;
    private int quantity;

    // Constructors
    public Inventory() {}

    public Inventory(int inventoryId, int productId, int warehouseId, int quantity) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}