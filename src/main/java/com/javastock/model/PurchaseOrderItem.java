public class PurchaseOrderItem {
    private int purchaseOrderItemId;
    private int purchaseOrderId;
    private int productId;
    private int quantity;
    private double unitPrice;

    // Constructors
    public PurchaseOrderItem() {}

    public PurchaseOrderItem(int purchaseOrderItemId, int purchaseOrderId, int productId, int quantity, double unitPrice) {
        this.purchaseOrderItemId = purchaseOrderItemId;
        this.purchaseOrderId = purchaseOrderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters and Setters
    public int getPurchaseOrderItemId() {
        return purchaseOrderItemId;
    }

    public void setPurchaseOrderItemId(int purchaseOrderItemId) {
        this.purchaseOrderItemId = purchaseOrderItemId;
    }

    public int getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(int purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
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