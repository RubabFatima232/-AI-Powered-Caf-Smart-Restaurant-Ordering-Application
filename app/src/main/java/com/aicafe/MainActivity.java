package com.aicafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private OrderViewModel viewModel;
    private final CartManager cart = CartManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.topAppBar));

        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        // AI Recommends horizontal
        RecyclerView rvRec = findViewById(R.id.rvRecommended);
        rvRec.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        Adapters.RecommendedAdapter recAdapter = new Adapters.RecommendedAdapter(item -> {
            cart.add(item);
            Toast.makeText(this, item.getName() + " added", Toast.LENGTH_SHORT).show();
        });
        rvRec.setAdapter(recAdapter);

        // Featured vertical
        RecyclerView rvFeat = findViewById(R.id.rvFeatured);
        rvFeat.setLayoutManager(new LinearLayoutManager(this));
        Adapters.FeaturedAdapter featAdapter = new Adapters.FeaturedAdapter(item -> {
            cart.add(item);
            Toast.makeText(this, item.getName() + " added", Toast.LENGTH_SHORT).show();
        });
        rvFeat.setAdapter(featAdapter);

        // Categories chips
        RecyclerView rvCat = findViewById(R.id.rvCategories);
        rvCat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        Adapters.CategoryAdapter catAdapter = new Adapters.CategoryAdapter(cat ->
                Toast.makeText(this, cat, Toast.LENGTH_SHORT).show());
        rvCat.setAdapter(catAdapter);

        // observe real API
        viewModel.menuLive.observe(this, items -> {
            recAdapter.submitList(items);
            featAdapter.submitList(items);
        });
        viewModel.loadMenu();

        // cart FAB
        ExtendedFloatingActionButton fab = findViewById(R.id.fabCart);
        cart.getCount().observe(this, count -> fab.setText("Cart (" + count + ")"));
        fab.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}