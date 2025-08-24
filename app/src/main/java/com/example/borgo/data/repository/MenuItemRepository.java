package com.example.borgo.data.repository;

import com.example.borgo.data.api.MenuItemService;
import com.example.borgo.data.model.MenuItem;
import com.example.borgo.data.remote.RetrofitClient;

import java.util.List;

import retrofit2.Call;

public class MenuItemRepository {

    private final MenuItemService menuItemService;

    public MenuItemRepository() {
        menuItemService = RetrofitClient.getRetrofitInstance().create(MenuItemService.class);
    }

    public Call<List<MenuItem>> getMenuItems() {
        return menuItemService.getMenuItems();
    }
    
    public Call<List<MenuItem>> getMenuItemsByCategory(Long categoryId) {
        return menuItemService.getMenuItemsByCategory(categoryId);
    }
    
    public Call<List<MenuItem>> searchMenuItems(String query) {
        // If the API supported search, we would use it here
        // For now, we'll fetch all items and filter client-side
        return menuItemService.getMenuItems();
    }
}