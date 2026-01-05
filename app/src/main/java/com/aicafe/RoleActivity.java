package com.aicafe;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class RoleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);
        findViewById(R.id.btnCustomer).setOnClickListener(v -> {
            RoleManager.setRole(this,"customer");
            startActivity(new Intent(this, CustomerActivity.class));
            finish();
        });
        findViewById(R.id.btnAdmin).setOnClickListener(v -> {
            RoleManager.setRole(this,"admin");
            startActivity(new Intent(this, AdminActivity.class));
            finish();
        });
    }
}
