package com.example.smartcafe;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class ProfileSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_selection);

        MaterialCardView adminCard = findViewById(R.id.card_admin);
        MaterialCardView customerCard = findViewById(R.id.card_customer);

        adminCard.setOnClickListener(v -> startActivity(new Intent(ProfileSelectionActivity.this, LoginActivity.class)));

        customerCard.setOnClickListener(v -> startActivity(new Intent(ProfileSelectionActivity.this, HomeActivity.class)));
    }
}
