package main.java.com.javastock.model;
public class User {
    private int userId;
    private String username;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private int roleId;
    private boolean isActive;

    // Constructors
    public User() {}

    public User(int userId, String username, String passwordHash, String firstName, String lastName, int roleId, boolean isActive) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleId = roleId;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
