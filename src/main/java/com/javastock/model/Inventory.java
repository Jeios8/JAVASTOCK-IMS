package main.java.com.javastock.model;


import java.sql.Date;

public class Inventory {
    private int inventoryId;
    private int productId;
    private int warehouseId;
    private int quantity;
    private Date expirationDate;

    // Default constructor
    public Inventory() {}

    // Constructor for fetching from database (includes inventoryId)
    public Inventory(int inventoryId, int productId, int warehouseId, int quantity) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
    }

    // Constructor for inserting new inventory (without inventoryId)
    public Inventory(int productId, int warehouseId, int quantity, Date expirationDate) {
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
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

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}