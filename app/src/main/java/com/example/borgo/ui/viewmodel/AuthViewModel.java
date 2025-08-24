package com.example.borgo.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.borgo.data.model.LoginRequest;
import com.example.borgo.data.model.LoginResponse;
import com.example.borgo.data.model.SignUpRequest;
import com.example.borgo.data.model.SignUpResponse;
import com.example.borgo.data.model.UpdateProfileRequest;
import com.example.borgo.data.repository.AuthRepository;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends ViewModel {
    private AuthRepository repository;
    private AuthRepository basicAuthRepository;
    private final MutableLiveData<LoginResponse> loginResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<SignUpResponse> signUpResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<SignUpResponse> profileLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public AuthViewModel () {
        repository = new AuthRepository();
    }

    public LiveData<LoginResponse> getLoginResponseLiveData() {
        return loginResponseLiveData;
    }

    public LiveData<SignUpResponse> getSignUpResponseLiveData() {
        return signUpResponseLiveData;
    }

    public LiveData<SignUpResponse> getProfileLiveData() {
        return profileLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void loginWithBasicAuth(String username, String password, LoginRequest loginRequest) {
        // Create a repository with Basic Auth
        basicAuthRepository = new AuthRepository(username, password);
        basicAuthRepository.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    loginResponseLiveData.postValue(response.body());
                } else {
                    String errorMessage = "Login failed: " + response.code() + " " + response.message();
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += ", Error Body: " + response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    errorLiveData.postValue(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                errorLiveData.postValue(t.getMessage());
            }
        });
    }

    public void login(LoginRequest loginRequest) {
        // Use the regular repository for login (with token)
        if (repository == null) {
            repository = new AuthRepository();
        }
        repository.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    loginResponseLiveData.postValue(response.body());
                } else {
                    String errorMessage = "Login failed: " + response.code() + " " + response.message();
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += ", Error Body: " + response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    errorLiveData.postValue(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                errorLiveData.postValue(t.getMessage());
            }
        });
    }

    public void signup(SignUpRequest signUpRequest) {
        if (repository == null) {
            repository = new AuthRepository();
        }
        repository.signup(signUpRequest).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if (response.isSuccessful()) {
                    signUpResponseLiveData.postValue(response.body());
                } else {
                    String errorMessage = "Signup failed: " + response.code() + " " + response.message();
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += ", Error Body: " + response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    errorLiveData.postValue(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                errorLiveData.postValue(t.getMessage());
            }
        });
    }

    public void getProfile(int userId) {
        if (repository == null) {
            repository = new AuthRepository();
        }
        repository.getProfile(userId).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if (response.isSuccessful()) {
                    profileLiveData.postValue(response.body());
                } else {
                    errorLiveData.postValue("Failed to load profile: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                errorLiveData.postValue("Network error while loading profile: " + t.getMessage());
            }
        });
    }

    public void updateProfile(int userId, UpdateProfileRequest updateProfileRequest) {
        if (repository == null) {
            repository = new AuthRepository();
        }
        repository.updateProfile(userId, updateProfileRequest).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if (response.isSuccessful()) {
                    // Refresh the profile data to get the updated information
                    getProfile(userId);
                } else {
                    String errorMessage = "Failed to update profile: " + response.code() + " " + response.message();
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += ", Error Body: " + response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    errorLiveData.postValue(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                errorLiveData.postValue("Network error while updating profile: " + t.getMessage());
            }
        });
    }
}