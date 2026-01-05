package com.aicafe;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.*;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class AdminActivity extends AppCompatActivity {

    private MenuViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_admin);

        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        findViewById(R.id.btnAdd).setOnClickListener(v -> {
            String name = Objects.requireNonNull(((TextInputEditText)findViewById(R.id.etName)).getText()).toString().trim();
            String priceStr = Objects.requireNonNull(((TextInputEditText)findViewById(R.id.etPrice)).getText()).toString().trim();
            String desc = Objects.requireNonNull(((TextInputEditText)findViewById(R.id.etDesc)).getText()).toString().trim();
            String img = Objects.requireNonNull(((TextInputEditText)findViewById(R.id.etImg)).getText()).toString().trim();
            String cat = Objects.requireNonNull(((TextInputEditText)findViewById(R.id.etCat)).getText()).toString().trim();
            if (name.isEmpty() || priceStr.isEmpty() || desc.isEmpty() || cat.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            double price = Double.parseDouble(priceStr);
            viewModel.addItem(name, price, desc, img.isEmpty() ? "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=400&q=60" : img, cat);
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
            ((TextInputEditText)findViewById(R.id.etName)).setText("");
            ((TextInputEditText)findViewById(R.id.etPrice)).setText("");
            ((TextInputEditText)findViewById(R.id.etDesc)).setText("");
            ((TextInputEditText)findViewById(R.id.etImg)).setText("");
            ((TextInputEditText)findViewById(R.id.etCat)).setText("");
        });

        RecyclerView rv = findViewById(R.id.rvAdmin);
        rv.setLayoutManager(new LinearLayoutManager(this));
        Adapters.AdminAdapter adapter = new Adapters.AdminAdapter(item -> viewModel.deleteItem(item));
        rv.setAdapter(adapter);
        viewModel.getMenu().observe(this, adapter::submitList);
        viewModel.loadMenu();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
