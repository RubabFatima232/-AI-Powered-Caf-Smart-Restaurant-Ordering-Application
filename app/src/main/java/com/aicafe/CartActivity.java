package com.aicafe;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private final CartManager cart = CartManager.getInstance();
    private OrderViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setSupportActionBar(findViewById(R.id.toolbarCart));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        RecyclerView rv = findViewById(R.id.rvCart);
        rv.setLayoutManager(new LinearLayoutManager(this));
        Adapters.CartAdapter adapter = new Adapters.CartAdapter(cart);
        rv.setAdapter(adapter);

        TextView tvTotal = findViewById(R.id.tvTotal);
        cart.getTotal().observe(this, total -> tvTotal.setText(String.format(Locale.getDefault(), "Total $%.2f", total)));
        cart.getItems().observe(this, items -> adapter.submitList(new ArrayList<>(items)));

        findViewById(R.id.btnCheckout).setOnClickListener(v -> {
            if (cart.getItems().getValue() == null || cart.getItems().getValue().isEmpty()) {
                Toast.makeText(this, "Cart empty", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.placeOrder(this, cart.getItemIds());
        });

        viewModel.orderLive.observe(this, resp -> {
            Toast.makeText(this, String.format("Order #%s placed", resp.getId()), Toast.LENGTH_LONG).show();
            cart.clear();
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
