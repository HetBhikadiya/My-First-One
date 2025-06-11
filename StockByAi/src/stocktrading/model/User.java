package stocktrading.model;

import java.util.UUID;

public class User {
    private final String userId;
    private String name;
    private String email;
    private String password;
    private int birthDate;
    private int birthMonth;
    private int birthYear;
    private String mobile;

    public User(String name, String email, String password,
                int birthDate, int birthMonth, int birthYear,
                String mobile) {
        this.userId = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.birthMonth = birthMonth;
        this.birthYear = birthYear;
        this.mobile = mobile;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getBirthDate() {
        return birthDate;
    }

    public int getBirthMonth() {
        return birthMonth;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public String getMobile() {
        return mobile;
    }

    // Setters (only for fields that might need to be updated)
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}