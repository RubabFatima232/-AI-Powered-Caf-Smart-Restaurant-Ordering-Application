package com.example.smartcafe;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigationrail.NavigationRailView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavView = findViewById(R.id.bottom_navigation);
        NavigationRailView railNavView = findViewById(R.id.navigation_rail);

        if (bottomNavView != null) {
            bottomNavView.setOnItemSelectedListener(navListener);
        } else if (railNavView != null) {
            railNavView.setOnItemSelectedListener(navListener);
        }

        // Load the default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
    }

    private final NavigationBarView.OnItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();

        if (itemId == R.id.navigation_home) {
            selectedFragment = new HomeFragment();
        } else if (itemId == R.id.navigation_menu) {
            selectedFragment = new MenuFragment();
        } else if (itemId == R.id.navigation_cart) {
            selectedFragment = new CartFragment();
        } else if (itemId == R.id.navigation_reservations) {
            selectedFragment = new TableReservationFragment();
        } else if (itemId == R.id.navigation_profile) {
            selectedFragment = new ProfileFragment();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
        return false;
    };
}
