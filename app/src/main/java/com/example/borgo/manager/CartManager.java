package com.example.borgo.manager;

import com.example.borgo.adapter.CartAdapter;
import com.example.borgo.data.model.CartItem;
import com.example.borgo.data.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static CartManager instance;
    private List<CartItem> cartItems;
    private CartAdapter.OnCartChangeListener cartChangeListener;

    private static final double TAX_RATE = 0.1;
    private static final double DELIVERY_FEE = 1.5;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void setCartChangeListener(CartAdapter.OnCartChangeListener listener) {
        this.cartChangeListener = listener;
    }

    private void notifyCartChanged() {
        if (cartChangeListener != null) {
            cartChangeListener.onCartChanged(getSubtotal(), getTax(TAX_RATE), getTotal(TAX_RATE));
        }
    }

    public void addItemToCart(MenuItem food, int quantity) {
        // Check if item already exists in cart (comparing ID, name, and price)
        for (CartItem item : cartItems) {
            if (item.getFood().getId() == food.getId() && 
                item.getFood().getName().equals(food.getName()) && 
                item.getFood().getPrice() == food.getPrice()) {
                item.setQuantity(item.getQuantity() + quantity);
                notifyCartChanged();
                return;
            }
        }
        
        // If item doesn't exist, add new item to cart
        cartItems.add(new CartItem(food, quantity));
        notifyCartChanged();
    }
    public void increaseQuantity(int foodId, String foodName, double foodPrice) {
        for (CartItem item : cartItems) {
            if (item.getFood().getId() == foodId && 
                item.getFood().getName().equals(foodName) && 
                item.getFood().getPrice() == foodPrice) {
                item.setQuantity(item.getQuantity() + 1);
                notifyCartChanged();
                return;
            }
        }
    }

    public void decreaseQuantity(int foodId, String foodName, double foodPrice) {
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);
            if (item.getFood().getId() == foodId && 
                item.getFood().getName().equals(foodName) && 
                item.getFood().getPrice() == foodPrice) {
                item.setQuantity(item.getQuantity() - 1);
                if (item.getQuantity() <= 0) {
                    cartItems.remove(i);
                }
                notifyCartChanged();
                return;
            }
        }
    }

    public List<CartItem> getCartItems() {
        if (cartItems == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(cartItems);
    }

    public void clearCart() {
        cartItems.clear();
        notifyCartChanged();
    }

    public double getSubtotal() {
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getSubtotal();
        }
        return subtotal;
    }

    public double getTax(double taxRate) {
        return getSubtotal() * taxRate;
    }

    public double getTotal(double taxRate) {
        if (getSubtotal() == 0) return 0;
        return getSubtotal() + getTax(taxRate) + DELIVERY_FEE;
    }

    public double getDeliveryFee() {
        return getSubtotal() > 0 ? DELIVERY_FEE : 0;
    }
}