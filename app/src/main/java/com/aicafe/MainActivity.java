package com.aicafe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.*;
import com.aicafe.Adapters.CategoryAdapter;
import com.aicafe.Adapters.FeaturedAdapter;
import com.aicafe.Adapters.RecommendedAdapter;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private OrderViewModel viewModel;
    private final CartManager cart = CartManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        // ---- AI RECOMMENDS ----
        RecyclerView rvRec = findViewById(R.id.rvRecommended);
        rvRec.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        RecommendedAdapter recAdapter = new RecommendedAdapter(item -> {
            cart.add(item);
            Toast.makeText(this, item.getName() + " added", Toast.LENGTH_SHORT).show();
        });
        rvRec.setAdapter(recAdapter);

        // ---- FEATURED ----
        RecyclerView rvFeat = findViewById(R.id.rvFeatured);
        rvFeat.setLayoutManager(new LinearLayoutManager(this));
        FeaturedAdapter featAdapter = new FeaturedAdapter(item -> {
            cart.add(item);
            Toast.makeText(this, item.getName() + " added", Toast.LENGTH_SHORT).show();
        });
        rvFeat.setAdapter(featAdapter);

        // ---- CATEGORIES ----
        RecyclerView rvCat = findViewById(R.id.rvCategories);
        rvCat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        CategoryAdapter catAdapter = new CategoryAdapter(cat -> Toast.makeText(this, cat, Toast.LENGTH_SHORT).show());
        rvCat.setAdapter(catAdapter);

        // observe API data
        viewModel.getRecommendedDishes().observe(this, items -> {
            recAdapter.submitList(items);
            featAdapter.submitList(items);
        });

        // ---- CART FAB ----
        ExtendedFloatingActionButton fab = findViewById(R.id.fabCart);
        cart.getCount().observe(this, count -> fab.setText("Cart (" + count + ")"));
        fab.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
    }
}