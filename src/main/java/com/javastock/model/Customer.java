package main.java.com.javastock.model;

public class Customer {
    private int customerId;
    private String firstName;
    private String lastName;
    private boolean isActive;

    // Constructors
    public Customer() {}

    public Customer(int customerId, String firstName, String lastName, boolean isActive) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}