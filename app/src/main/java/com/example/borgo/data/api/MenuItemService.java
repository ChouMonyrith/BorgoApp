package com.example.borgo.data.api;

import com.example.borgo.data.model.MenuItem;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;

public interface MenuItemService {
    @GET("api/menu-items")
    Call<List<MenuItem>> getMenuItems();

    @GET("api/menu-items/category/{categoryId}")
    Call<List<MenuItem>> getMenuItemsByCategory(@Path("categoryId") Long categoryId);

    @GET("api/menu-items/{id}")
    Call<MenuItem> getMenuItemById(@Path("id") Long id);

    @POST("api/menu-items")
    Call<MenuItem> createMenuItem(@Body MenuItem menuItem);

    @PUT("api/menu-items/{id}")
    Call<MenuItem> updateMenuItem(@Path("id") Long id, @Body MenuItem menuItem);

    @DELETE("api/menu-items/{id}")
    Call<Void> deleteMenuItem(@Path("id") Long id);
}
