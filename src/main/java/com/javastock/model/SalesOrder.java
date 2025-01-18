package main.java.com.javastock.model;

public class SalesOrder {
    private int salesOrderId;
    private int customerId;
    private String orderDate;
    private String status;

    // Constructors
    public SalesOrder() {}

    public SalesOrder(int salesOrderId, int customerId, String orderDate, String status) {
        this.salesOrderId = salesOrderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.status = status;
    }

    // Getters and Setters
    public int getSalesOrderId() {
        return salesOrderId;
    }

    public void setSalesOrderId(int salesOrderId) {
        this.salesOrderId = salesOrderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}