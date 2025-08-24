package com.example.borgo.data.api;

import com.example.borgo.data.model.Category;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;

public interface CategoryService {
    @GET("api/categories")
    Call<List<Category>> getCategories();

    @GET("api/categories/{id}")
    Call<Category> getCategoryById(@Path("id") Long id);

    @POST("api/categories")
    Call<Category> createCategory(@Body Category category);

    @PUT("api/categories/{id}")
    Call<Category> updateCategory(@Path("id") Long id, @Body Category category);

    @DELETE("api/categories/{id}")
    Call<Void> deleteCategory(@Path("id") Long id);
}
