package com.example.borgo.data.model;

import com.example.borgo.data.model.OrderItem;

import java.util.List;

public class OrderRequest {
    private List<OrderItem> items;
    private double totalAmount;

    public OrderRequest(List<OrderItem> items, double totalAmount) {
        // Add null check for items
        if (items == null) {
            throw new IllegalArgumentException("Order items cannot be null");
        }
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
