package com.example.borgo.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.borgo.R;
import com.example.borgo.fragment.CartFragment;
import com.example.borgo.fragment.OrderFragment;
import com.example.borgo.fragment.HomeFragment;
import com.example.borgo.fragment.ProfileFragment;
import com.example.borgo.manager.TokenManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize TokenManager
        TokenManager.init(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        String fragmentToLoad = getIntent().getStringExtra("fragment_to_load");
        if (fragmentToLoad != null && !fragmentToLoad.isEmpty()) {
            if (fragmentToLoad.equals("CartFragment")) {
                loadFragment(new CartFragment());
            } else if (fragmentToLoad.equals("OrderFragment")) {
                loadFragment(new OrderFragment());
            } else if (fragmentToLoad.equals("ProfileFragment")) {
                loadFragment(new ProfileFragment());
            }
        }else{
            loadHomeFragment();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if(item.getItemId() == R.id.homeFragment) {
                selectedFragment = new HomeFragment();
            } else if(item.getItemId() == R.id.cartFragment) {
                selectedFragment = new CartFragment();
            } else if(item.getItemId() == R.id.favoriteFragment) {
                selectedFragment = new OrderFragment();
            }else if(item.getItemId() == R.id.profileFragment){
                selectedFragment = new ProfileFragment();
            }
            return loadFragment(selectedFragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void loadHomeFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new HomeFragment())
                .commit();
    }
}