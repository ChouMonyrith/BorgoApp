package com.example.borgo.data.repository;

import android.util.Log;

import com.example.borgo.data.api.OrderService;
import com.example.borgo.data.model.Order;
import com.example.borgo.data.model.OrderRequest;
import com.example.borgo.data.remote.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {
    private final OrderService orderService;
    private static final String TAG = "OrderRepository";

    public interface OrderCallback {
        void onSuccess(Order order);
        void onError(String error);
    }
    
    public interface OrdersCallback {
        void onSuccess(List<Order> orders);
        void onError(String error);
    }

    public OrderRepository() {
        orderService = RetrofitClient.getRetrofitInstance().create(OrderService.class);
    }

    public void placeOrder(OrderRequest request, OrderCallback callback) {
        // Add validation for request
        if (request == null) {
            callback.onError("Order request is null");
            return;
        }
        
        if (request.getItems() == null || request.getItems().isEmpty()) {
            callback.onError("Order items are null or empty");
            return;
        }

        Log.d(TAG, "Placing order with " + request.getItems().size() + " items");

        orderService.placeOrder(request).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Order placement successful");
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "Order placement failed with code: " + response.code());
                    callback.onError("Failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.e(TAG, "Order placement failed with exception: " + t.getMessage(), t);
                callback.onError(t.getMessage());
            }
        });
    }
    
    public Call<Order> placeOrder(OrderRequest order) {
        return orderService.placeOrder(order);
    }
    
    public void getUserOrders(OrdersCallback callback) {
        orderService.getUserOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to fetch orders: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.e(TAG, "Failed to fetch orders", t);
                callback.onError("Failed to fetch orders: " + t.getMessage());
            }
        });
    }
    
    public Call<List<Order>> getUserOrders() {
        return orderService.getUserOrders();
    }
}