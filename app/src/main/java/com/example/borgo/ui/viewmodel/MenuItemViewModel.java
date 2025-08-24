package com.example.borgo.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.borgo.data.model.MenuItem;
import com.example.borgo.data.repository.MenuItemRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuItemViewModel extends AndroidViewModel {
    private final MenuItemRepository repository;
    private final MutableLiveData<List<MenuItem>> menuItems = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private static final String TAG = "MenuItemViewModel";
    
    // Store all menu items for filtering
    private List<MenuItem> allMenuItems = new ArrayList<>();
    
    // Current filter states
    private String currentSearchQuery = "";
    private Long currentCategoryId = null;

    public MenuItemViewModel(@NonNull Application application) {
        super(application);
        repository = new MenuItemRepository();
    }

    public LiveData<List<MenuItem>> getMenuItems() { return menuItems; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }

    public void fetchMenuItems() {
        loading.setValue(true);
        Call<List<MenuItem>> call = repository.getMenuItems();
        call.enqueue(new Callback<List<MenuItem>>() {
            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> res) {
                loading.postValue(false);
                Log.d(TAG, "Response code: " + res.code());
                Log.d(TAG, "Response message: " + res.message());
                Log.d(TAG, "Response headers: " + res.headers());
                
                if (res.isSuccessful() && res.body() != null) {
                    Log.d(TAG, "Successfully fetched " + res.body().size() + " menu items");
                    allMenuItems = new ArrayList<>(res.body());
                    applyFilters(); // Apply current filters to the fetched data
                } else {
                    String errorMsg = "Failed: " + res.code() + " " + res.message();
                    Log.e(TAG, errorMsg);
                    if (res.errorBody() != null) {
                        try {
                            String errorBody = res.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                            errorMsg += " - " + errorBody;
                        } catch (Exception e) {
                            Log.e(TAG, "Error reading error body: " + e.getMessage());
                        }
                    }
                    error.postValue(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<List<MenuItem>> call, Throwable t) {
                loading.postValue(false);
                String errorMsg = "Network error: " + t.getMessage();
                Log.e(TAG, errorMsg);
                Log.e(TAG, "Network error cause: " + t.getCause());
                t.printStackTrace();
                error.postValue(errorMsg);
            }
        });
    }
    
    public void fetchMenuItemsByCategory(Long categoryId) {
        loading.setValue(true);
        Call<List<MenuItem>> call = repository.getMenuItemsByCategory(categoryId);
        call.enqueue(new Callback<List<MenuItem>>() {
            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> res) {
                loading.postValue(false);
                Log.d(TAG, "Response code: " + res.code());
                Log.d(TAG, "Response message: " + res.message());
                Log.d(TAG, "Response headers: " + res.headers());
                
                if (res.isSuccessful() && res.body() != null) {
                    Log.d(TAG, "Successfully fetched " + res.body().size() + " menu items for category " + categoryId);
                    allMenuItems = new ArrayList<>(res.body());
                    applyFilters(); // Apply current filters to the fetched data
                } else {
                    String errorMsg = "Failed: " + res.code() + " " + res.message();
                    Log.e(TAG, errorMsg);
                    if (res.errorBody() != null) {
                        try {
                            String errorBody = res.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                            errorMsg += " - " + errorBody;
                        } catch (Exception e) {
                            Log.e(TAG, "Error reading error body: " + e.getMessage());
                        }
                    }
                    error.postValue(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<List<MenuItem>> call, Throwable t) {
                loading.postValue(false);
                String errorMsg = "Network error: " + t.getMessage();
                Log.e(TAG, errorMsg);
                Log.e(TAG, "Network error cause: " + t.getCause());
                t.printStackTrace();
                error.postValue(errorMsg);
            }
        });
    }
    
    public void searchMenuItems(String query) {
        this.currentSearchQuery = query != null ? query.trim() : "";
        applyFilters();
    }
    
    public void filterByCategory(Long categoryId) {
        this.currentCategoryId = categoryId;
        if (categoryId == null) {
            // Fetch all items
            fetchMenuItems();
        } else {
            // Fetch items by category
            fetchMenuItemsByCategory(categoryId);
        }
    }
    
    private void applyFilters() {
        List<MenuItem> filteredItems = new ArrayList<>(allMenuItems);
        
        // Apply search filter
        if (currentSearchQuery != null && !currentSearchQuery.isEmpty()) {
            String query = currentSearchQuery.toLowerCase();
            filteredItems.removeIf(item -> 
                (item.getName() == null || !item.getName().toLowerCase().contains(query)) && 
                (item.getDescription() == null || !item.getDescription().toLowerCase().contains(query)));
        }
        
        menuItems.postValue(filteredItems);
    }
    
    public List<MenuItem> getAllMenuItems() {
        return new ArrayList<>(allMenuItems);
    }
}