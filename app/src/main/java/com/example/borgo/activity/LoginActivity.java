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
import com.example.borgo.manager.TokenManager;
import com.example.borgo.ui.viewmodel.AuthViewModel;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText email;
    private EditText password;
    private Button login, createAccount;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        TokenManager.init(this);

        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);
        login = findViewById(R.id.login_account_button);
        createAccount = findViewById(R.id.create_account_button);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.getLoginResponseLiveData().observe(this, loginResponse -> {
            if (loginResponse != null) {
                Log.d(TAG, "Received login response with token: " + 
                    (loginResponse.getToken() != null ? loginResponse.getToken().substring(0, Math.min(10, loginResponse.getToken().length())) + "..." : "null"));
                Log.d(TAG, "UserId from response: " + loginResponse.getUserId());
                
                // Save token and user ID if they exist in the response
                if (loginResponse.getToken() != null) {
                    TokenManager.saveToken(loginResponse.getToken());
                }
                TokenManager.saveUserId(loginResponse.getUserId());
                
                // Log token for debugging
                Log.d(TAG, "Token saved: " + (loginResponse.getToken() != null ? 
                    (loginResponse.getToken().length() > 10 ? loginResponse.getToken().substring(0, 10) + "..." : loginResponse.getToken()) : "null"));
                
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Log.e(TAG, "Login failed: Response was null");
                Toast.makeText(LoginActivity.this, "Login failed: Response was null", Toast.LENGTH_SHORT).show();
            }
        });

        authViewModel.getErrorLiveData().observe(this, error -> {
            if (error != null) {
                Log.e(TAG, "Login error: " + error);
                Toast.makeText(LoginActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        createAccount.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SigninActivity.class);
            startActivity(intent);
            finish();
        });

        login.setOnClickListener(view -> {
            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();

            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Email and password are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Use Basic Auth for login - send credentials in the request body as well
            LoginRequest loginRequest = new LoginRequest(emailText, passwordText);
            authViewModel.loginWithBasicAuth(emailText, passwordText, loginRequest);
        });
    }
}