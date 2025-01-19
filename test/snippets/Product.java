public class Product {
    private int productId;
    private String productName;
    private String description;
    private String unitOfMeasure;
    private double unitPrice;

    // Constructor
    public Product(int productId, String productName, String description, String unitOfMeasure, double unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.unitOfMeasure = unitOfMeasure;
        this.unitPrice = unitPrice;
    }

    // Getters
    public String getProductName() {
        return productName;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s, Description: %s, Unit: %s, Price: %.2f",
                productId, productName, description, unitOfMeasure, unitPrice);
    }
}


