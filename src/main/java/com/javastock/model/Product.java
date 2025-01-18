package main.java.com.javastock.model;

public class Product {
    private int productId;
    private String productName;
    private String description;
    private String unitOfMeasure;
    private int categoryId;
    private int supplierId;
    private int reorderLevel;
    private double unitPrice;
    private String imagePath;
    private boolean isActive;

    // Constructors
    public Product() {}

    public Product(int productId, String productName, String description, String unitOfMeasure, int categoryId, int supplierId, int reorderLevel, double unitPrice, String imagePath, boolean isActive) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.unitOfMeasure = unitOfMeasure;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.reorderLevel = reorderLevel;
        this.unitPrice = unitPrice;
        this.imagePath = imagePath;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}