package com.example.borgo.data.model;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("categoryId")
    private Long categoryId;
    @SerializedName("name")
    private String name;

    public Category(Long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    // Getters and Setters
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
