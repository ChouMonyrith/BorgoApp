package com.example.borgo.data.repository;

import com.example.borgo.data.api.CategoryService;
import com.example.borgo.data.model.Category;
import com.example.borgo.data.remote.RetrofitClient;

import java.util.List;

import retrofit2.Call;

public class CategoryRepository {

    private final CategoryService categoryService;

    public CategoryRepository() {
        categoryService = RetrofitClient.getRetrofitInstance().create(CategoryService.class);
    }

    public Call<List<Category>> getCategories() {
        return categoryService.getCategories();
    }
}
