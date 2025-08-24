package com.example.borgo.data.api;

import com.example.borgo.data.model.Order;
import com.example.borgo.data.model.OrderRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface OrderService {
    @POST("api/orders")
    Call<Order> placeOrder(@Body OrderRequest request);
    
    @GET("api/orders")
    Call<List<Order>> getUserOrders();
}