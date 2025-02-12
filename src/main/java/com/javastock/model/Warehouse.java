package main.java.com.javastock.model;

public class Warehouse {
    private int warehouseId;
    private String warehouseName;
    private String contactName;
    private String phone;
  //  private String email;
    private String location;
    private boolean isActive;

    // Constructors
    public Warehouse() {}

    public Warehouse(int warehouseId, String warehouseName, String contactName, String phone, String location, boolean isActive) {
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.contactName = contactName;
        this.phone = phone;
        this.location = location;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}