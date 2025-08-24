package com.example.borgo.data.model;

import java.util.List;

public class SignUpResponse {
    private int userId;
    private String firstName;
    private String lastName;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String createdAt;
    private String updatedAt;
    private boolean enabled;
    private boolean accountNonLocked;
    private List<Object> authorities;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private String username;
    private String address;
    private String dob;
    private String gender;

    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public List<Object> getAuthorities() {
        return authorities;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return address;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }
}
