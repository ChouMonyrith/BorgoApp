package com.example.borgo.data.remote;

import android.util.Log;

import com.example.borgo.data.api.BasicAuthInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BasicAuthRetrofitClient {
    private static final String BASE_URL = "http://192.168.18.234:8081/";
    private static final String TAG = "BasicAuthRetrofitClient";

    public static Retrofit getBasicAuthRetrofitInstance(String username, String password) {
        Log.d(TAG, "Creating Basic Auth Retrofit instance for user: " + username);
        
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new BasicAuthInterceptor(username, password))
                .addInterceptor(loggingInterceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
}