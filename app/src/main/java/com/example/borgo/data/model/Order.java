package com.example.borgo.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<CartItem> items;
    private double subtotal;
    private double tax;
    @SerializedName("totalAmount")
    private double total;

    public Order(List<CartItem> items, double subtotal, double tax, double total) {
        this.items = items != null ? items : new ArrayList<>();
        this.subtotal = subtotal;
        this.tax = tax;
        this.total = total;
    }

    public List<CartItem> getItems() { 
        return items != null ? items : new ArrayList<>();
    }
    public double getSubtotal() { return subtotal; }
    public double getTax() { return tax; }
    public double getTotal() { return total; }
}