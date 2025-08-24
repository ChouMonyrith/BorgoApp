package com.example.borgo.data.api;

import com.example.borgo.data.model.LoginRequest;
import com.example.borgo.data.model.LoginResponse;
import com.example.borgo.data.model.SignUpRequest;
import com.example.borgo.data.model.SignUpResponse;
import com.example.borgo.data.model.UpdateProfileRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AuthService {
    @POST("api/auth/signup")
    Call<SignUpResponse> signup(@Body SignUpRequest signUpRequest);

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("api/users/{id}")
    Call<SignUpResponse> getProfile(@Path("id") int userId);

    @PUT("api/users/{id}")
    Call<SignUpResponse> updateProfile(@Path("id") int userId, @Body UpdateProfileRequest updateProfileRequest);
}
