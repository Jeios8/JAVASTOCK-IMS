package main.java.com.javastock.model;

public class User {
    private String employeeId;
    private String firstName;
    private String lastName;
    private String username;
    private String passwordHash;
    private int roleId;

    // Constructor
    public User(String employeeId, String firstName, String lastName, String username, String passwordHash, int roleId) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.passwordHash = passwordHash;
        this.roleId = roleId;
    }

    // Getters
    public String getEmployeeId() {
        return employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public int getRoleId() {
        return roleId;
    }
}
