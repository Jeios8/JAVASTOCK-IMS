package main.java.com.javastock.model;

public class PurchaseOrder {
    private int purchaseOrderId;
    private int supplierId;
    private String orderDate;
    private String expectedArrivalDate;
    private String status;

    // Constructors
    public PurchaseOrder() {}

    public PurchaseOrder(int purchaseOrderId, int supplierId, String orderDate, String expectedArrivalDate, String status) {
        this.purchaseOrderId = purchaseOrderId;
        this.supplierId = supplierId;
        this.orderDate = orderDate;
        this.expectedArrivalDate = expectedArrivalDate;
        this.status = status;
    }

    // Getters and Setters
    public int getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(int purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getExpectedArrivalDate() {
        return expectedArrivalDate;
    }

    public void setExpectedArrivalDate(String expectedArrivalDate) {
        this.expectedArrivalDate = expectedArrivalDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}