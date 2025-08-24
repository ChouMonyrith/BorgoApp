package com.example.borgo.data.remote;

import android.util.Log;

import com.example.borgo.manager.TokenManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.18.234:8081/";
    private static final String TAG = "RetrofitClient";

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(new AuthInterceptor())
                    .addInterceptor(loggingInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    // Inner class for Authentication interceptor
    private static class AuthInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            
            // Log detailed information about what we're retrieving
            Log.d(TAG, "=== AUTH INTERCEPTOR ===");
            Log.d(TAG, "Request URL: " + original.url());
            
            Request.Builder builder = original.newBuilder();
            
            // Get token from TokenManager
            String token = TokenManager.getToken();
            Log.d(TAG, "Retrieved token from TokenManager: " + (token != null ? "'" + (token.length() > 10 ? token.substring(0, 10) + "..." : token) + "'" : "null"));
            
            // Add Bearer token header if we have a token
            if (token != null && !token.isEmpty()) {
                String bearerToken = "Bearer " + token;
                builder.header("Authorization", bearerToken);
                Log.d(TAG, "Adding Bearer Auth header: " + bearerToken);
            } else {
                Log.d(TAG, "No token available for Bearer Auth");
            }

            Request request = builder.build();
            Log.d(TAG, "Final request URL: " + request.url());
            Log.d(TAG, "==============================");
            
            return chain.proceed(request);
        }
    }
}