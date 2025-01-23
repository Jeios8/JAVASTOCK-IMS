package main.java.com.javastock.model;

public class Warehouse {
    private int warehouseId;
    private String warehouseName;
    private String location;
    private String contactName;
    private String phone;
    private boolean isActive;

    // Constructors
    public Warehouse() {}

    public Warehouse(int warehouseId, String warehouseName, String location,String contactName, String phone,  boolean isActive) {
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.location = location;
        this.contactName = contactName;
        this.phone = phone;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContactName() {
        return contactName;
    }


    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}