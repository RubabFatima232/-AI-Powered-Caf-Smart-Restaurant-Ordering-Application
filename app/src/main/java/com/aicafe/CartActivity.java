package com.aicafe;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.*;
import com.aicafe.Adapters;
import com.aicafe.model.MenuItem;
import java.util.ArrayList;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {

    private final CartManager cart = CartManager.getInstance();
    private OrderViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setSupportActionBar(findViewById(R.id.toolbarCart));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        RecyclerView rvCart = findViewById(R.id.rvCart);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        Adapters.CartAdapter adapter = new Adapters.CartAdapter(item -> cart.remove(item.getId()));
        rvCart.setAdapter(adapter);

        cart.getTotal().observe(this, total -> ((TextView) findViewById(R.id.tvTotal)).setText("Total  $" + String.format("%.2f", total)));

        // observe cart items
        cart.getCount().observe(this, count -> {
            if (count == 0) {
                adapter.submitList(new ArrayList<>());
            } else {
                adapter.submitList(new ArrayList<>(cart.getMap().values()));
            }
        });

        findViewById(R.id.btnCheckout).setOnClickListener(v -> {
            if (cart.getItemIds().isEmpty()) {
                Toast.makeText(this, "Cart empty", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.placeOrder(cart.getItemIds());
        });

        viewModel.getRecommendedDishes().observe(this, resp -> {
            Toast.makeText(this, "Order #" + resp.get(0).getId() + " placed", Toast.LENGTH_LONG).show();
            cart.clear();
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}