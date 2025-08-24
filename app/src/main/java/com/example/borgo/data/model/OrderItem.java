package com.example.borgo.data.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class OrderItem {
    private int foodId;
    private int quantity;

    public OrderItem(int foodId, int quantity) {
        this.foodId = foodId;
        this.quantity = quantity;
    }

    public int getFoodId() {
        return foodId;
    }

    public int getQuantity() {
        return quantity;
    }
}
