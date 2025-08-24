package com.example.borgo.data.api;

import android.util.Log;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BasicAuthInterceptor implements Interceptor {
    private static final String TAG = "BasicAuthInterceptor";
    private String credentials;

    public BasicAuthInterceptor(String username, String password) {
        this.credentials = Credentials.basic(username, password);
        Log.d(TAG, "Basic Auth credentials created: " + credentials);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                .header("Authorization", credentials)
                .build();
        Log.d(TAG, "Added Basic Auth header to request: " + request.url());
        return chain.proceed(authenticatedRequest);
    }
}