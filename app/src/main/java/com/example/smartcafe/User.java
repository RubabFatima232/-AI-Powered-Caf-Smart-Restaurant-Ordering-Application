package com.example.smartcafe;

public class User {
    private String name;
    private String email;
    private String role;

    // Required for Firestore data mapping
    @SuppressWarnings("unused")
    public User() {
    }

    public User(String name, String email, String role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public String getEmail() {
        return email;
    }

    @SuppressWarnings("unused")
    public String getRole() {
        return role;
    }
}
