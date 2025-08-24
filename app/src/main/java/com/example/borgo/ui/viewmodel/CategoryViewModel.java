package com.example.borgo.ui.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.borgo.data.model.Category;
import com.example.borgo.data.repository.CategoryRepository;
import com.example.borgo.manager.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryViewModel extends ViewModel {
    private final CategoryRepository repository;
    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private static final String TAG = "CategoryViewModel";

    public LiveData<List<Category>> getCategories() { return categories; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }

    public CategoryViewModel() {
        repository = new CategoryRepository();
    }

    public void fetchCategories() {
        loading.setValue(true);
        Call<List<Category>> call = repository.getCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override public void onResponse(Call<List<Category>> call, Response<List<Category>> res) {
                loading.postValue(false);
                Log.d(TAG, "Response code: " + res.code());
                Log.d(TAG, "Response message: " + res.message());
                Log.d(TAG, "Response headers: " + res.headers());
                
                if (res.isSuccessful() && res.body() != null) {
                    Log.d(TAG, "Successfully fetched " + res.body().size() + " categories");
                    categories.postValue(res.body());
                } else {
                    String errorMsg = "Failed to load categories: " + res.code() + " " + res.message();
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
            @Override public void onFailure(Call<List<Category>> call, Throwable t) {
                loading.postValue(false);
                String errorMsg = "Network error: " + t.getMessage();
                Log.e(TAG, errorMsg);
                Log.e(TAG, "Network error cause: " + t.getCause());
                t.printStackTrace();
                error.postValue(errorMsg);
            }
        });
    }
}
