package com.example.borgo.data.model;

public class SignUpRequest {
    private String firstName;
    private String lastName;
    private String name;
    private String email;
    private String phone;
    private String password;

    public SignUpRequest(String firstName, String lastName, String name, String email, String phone, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    // Getters and setters
}
