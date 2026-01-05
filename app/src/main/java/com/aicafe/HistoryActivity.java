package com.aicafe;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setSupportActionBar(findViewById(R.id.toolbarHistory));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Order History");
        }

        RecyclerView rv = findViewById(R.id.rvHistory);
        rv.setLayoutManager(new LinearLayoutManager(this));
        Adapters.HistoryAdapter adapter = new Adapters.HistoryAdapter();
        rv.setAdapter(adapter);
        adapter.submitList(OrderRepository.getHistory(this));
    }

    @Override
    public boolean onSupportNavigateUp() { 
        finish(); 
        return true; 
    }
}
