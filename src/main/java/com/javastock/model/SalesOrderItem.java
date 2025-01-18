package main.java.com.javastock.model;

public class SalesOrderItem {
    private int salesOrderItemId;
    private int salesOrderId;
    private int productId;
    private int quantity;
    private double unitPrice;

    // Constructors
    public SalesOrderItem() {}

    public SalesOrderItem(int salesOrderItemId, int salesOrderId, int productId, int quantity, double unitPrice) {
        this.salesOrderItemId = salesOrderItemId;
        this.salesOrderId = salesOrderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters and Setters
    public int getSalesOrderItemId() {
        return salesOrderItemId;
    }

    public void setSalesOrderItemId(int salesOrderItemId) {
        this.salesOrderItemId = salesOrderItemId;
    }

    public int getSalesOrderId() {
        return salesOrderId;
    }

    public void setSalesOrderId(int salesOrderId) {
        this.salesOrderId = salesOrderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}