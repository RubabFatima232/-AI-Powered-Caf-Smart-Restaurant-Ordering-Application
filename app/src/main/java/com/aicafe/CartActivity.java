package com.aicafe;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.*;

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

        cart.getTotal().observe(this, total ->
                ((android.widget.TextView) findViewById(R.id.tvTotal)).setText("Total  $" + String.format("%.2f", total)));

        cart.getCart().observe(this, items ->
                adapter.submitList(new java.util.ArrayList<>(items)));

        findViewById(R.id.btnCheckout).setOnClickListener(v -> {
            if (cart.getItemIds().isEmpty()) {
                Toast.makeText(this, "Cart empty", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.placeOrder(cart.getItemIds());
        });

        viewModel.orderLive.observe(this, resp -> {
            Toast.makeText(this, "Order #" + resp.getOrderId() + " placed", Toast.LENGTH_LONG).show();
            cart.clear();
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }
}