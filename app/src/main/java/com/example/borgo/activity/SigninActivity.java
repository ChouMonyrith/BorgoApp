package com.example.borgo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.borgo.R;
import com.example.borgo.data.model.LoginRequest;
import com.example.borgo.data.model.LoginResponse;
import com.example.borgo.data.model.SignUpRequest;
import com.example.borgo.data.model.SignUpResponse;
import com.example.borgo.manager.TokenManager;
import com.example.borgo.ui.viewmodel.AuthViewModel;

public class SigninActivity extends AppCompatActivity {
    private static final String TAG = "SigninActivity";

    private EditText email, password, confirmPassword;
    private Button createAccount, gotoLoginButton;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);
        confirmPassword = findViewById(R.id.confirm_password);
        createAccount = findViewById(R.id.create_account_button);
        gotoLoginButton = findViewById(R.id.goto_login_button);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.getSignUpResponseLiveData().observe(this, signUpResponse -> {
            if (signUpResponse != null) {
                Log.d(TAG, "Received signup response");
                Toast.makeText(SigninActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                // Automatically log the user in after successful signup
                String emailText = email.getText().toString().trim();
                String passwordText = password.getText().toString().trim();
                
                // Use Basic Auth for login - send credentials in the request body as well
                LoginRequest loginRequest = new LoginRequest(emailText, passwordText);
                authViewModel.loginWithBasicAuth(emailText, passwordText, loginRequest);
            }
        });

        authViewModel.getLoginResponseLiveData().observe(this, loginResponse -> {
            if (loginResponse != null) {
                Log.d(TAG, "Received login response after signup with token: " + 
                    (loginResponse.getToken() != null ? loginResponse.getToken().substring(0, Math.min(10, loginResponse.getToken().length())) + "..." : "null"));
                Log.d(TAG, "UserId from response: " + loginResponse.getUserId());
                
                // Save the token and user ID if they exist
                if (loginResponse.getToken() != null) {
                    TokenManager.saveToken(loginResponse.getToken());
                }
                TokenManager.saveUserId(loginResponse.getUserId());
                
                // Log token for debugging
                Log.d(TAG, "Token saved: " + (loginResponse.getToken() != null ? 
                    (loginResponse.getToken().length() > 10 ? loginResponse.getToken().substring(0, 10) + "..." : loginResponse.getToken()) : "null"));
                
                Toast.makeText(SigninActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SigninActivity.this, MainActivity.class));
                finish();
            }
        });

        authViewModel.getErrorLiveData().observe(this, error -> {
            if (error != null) {
                Log.e(TAG, "Error: " + error);
                Toast.makeText(SigninActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        gotoLoginButton.setOnClickListener(view -> {
            Intent intent = new Intent(SigninActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        createAccount.setOnClickListener(view -> {
            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();
            String confirmPasswordText = confirmPassword.getText().toString().trim();

            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Email and password are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!passwordText.equals(confirmPasswordText)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Use empty strings for missing fields
            SignUpRequest signUpRequest = new SignUpRequest("", "", "", emailText, "", passwordText);
            authViewModel.signup(signUpRequest);
        });
    }
}