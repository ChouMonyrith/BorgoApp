package com.example.borgo.data.model;

public class UpdateProfileRequest {
    private String firstName;
    private String lastName;
    private String name;

    private String email;

    private String phone;
    private String address;
    private String dob;
    private String gender;

    public UpdateProfileRequest(String firstName, String lastName, String name,String email, String phone, String address, String dob, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
        this.gender = gender;
    }

    // Getters and setters
}
