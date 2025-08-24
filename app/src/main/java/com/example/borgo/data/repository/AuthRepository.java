package com.example.borgo.data.repository;

import android.util.Log;

import com.example.borgo.data.api.AuthService;
import com.example.borgo.data.model.LoginRequest;
import com.example.borgo.data.model.LoginResponse;
import com.example.borgo.data.model.SignUpRequest;
import com.example.borgo.data.model.SignUpResponse;
import com.example.borgo.data.model.UpdateProfileRequest;
import com.example.borgo.data.remote.BasicAuthRetrofitClient;
import com.example.borgo.data.remote.RetrofitClient;

import retrofit2.Call;

public class AuthRepository {
    private final AuthService authService;
    private AuthService basicAuthAuthService;
    private static final String TAG = "AuthRepository";

    public AuthRepository() {
        authService = RetrofitClient.getRetrofitInstance().create(AuthService.class);
    }

    // Constructor for Basic Auth login
    public AuthRepository(String username, String password) {
        this();
        basicAuthAuthService = BasicAuthRetrofitClient.getBasicAuthRetrofitInstance(username, password)
                .create(AuthService.class);
    }

    public Call<SignUpResponse> signup(SignUpRequest signUpRequest) {
        return authService.signup(signUpRequest);
    }

    public Call<LoginResponse> login(LoginRequest loginRequest) {
        // Use Basic Auth service if available, otherwise use regular service
        if (basicAuthAuthService != null) {
            Log.d(TAG, "Using Basic Auth for login");
            return basicAuthAuthService.login(loginRequest);
        } else {
            Log.d(TAG, "Using regular auth for login");
            return authService.login(loginRequest);
        }
    }

    public Call<SignUpResponse> getProfile(int userId) {
        Log.d(TAG, "Getting profile for user ID: " + userId);
        return authService.getProfile(userId);
    }

    public Call<SignUpResponse> updateProfile(int userId, UpdateProfileRequest updateProfileRequest) {
        Log.d(TAG, "Updating profile for user ID: " + userId);
        return authService.updateProfile(userId, updateProfileRequest);
    }
}