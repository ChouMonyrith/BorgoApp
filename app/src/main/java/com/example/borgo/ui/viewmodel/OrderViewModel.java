package com.example.borgo.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.borgo.data.model.Order;
import com.example.borgo.data.model.OrderRequest;
import com.example.borgo.data.repository.OrderRepository;
import com.example.borgo.manager.OrderManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderViewModel extends ViewModel {
    private final OrderRepository repository;
    private static final String TAG = "OrderViewModel";

    private final MutableLiveData<Order> placedOrder = new MutableLiveData<>();
    private final MutableLiveData<Boolean> placing = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<List<Order>> userOrders = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public LiveData<Order> getPlacedOrder() { return placedOrder; }
    public LiveData<Boolean> isPlacing() { return placing; }
    public LiveData<String> getError() { return error; }
    public LiveData<List<Order>> getUserOrders() { return userOrders; }
    public LiveData<Boolean> isLoading() { return loading; }

    public OrderViewModel() {
        repository = new OrderRepository();
    }

    public void placeOrder(OrderRequest order) {
        placing.setValue(true);
        repository.placeOrder(order).enqueue(new Callback<Order>() {
            @Override public void onResponse(Call<Order> call, Response<Order> res) {
                placing.postValue(false);
                if (res.isSuccessful() && res.body() != null) placedOrder.postValue(res.body());
                else error.postValue("Failed to place order");
            }
            @Override public void onFailure(Call<Order> call, Throwable t) {
                placing.postValue(false);
                error.postValue(t.getMessage());
            }
        });
    }
    
    public void fetchUserOrders() {
        // Only show local orders, remove API orders
        List<Order> localOrders = OrderManager.getInstance().getOrders();
        Log.d(TAG, "Showing " + localOrders.size() + " local orders only");
        userOrders.postValue(localOrders);
    }
}