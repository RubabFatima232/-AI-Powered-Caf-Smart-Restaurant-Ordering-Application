package com.example.smartcafe;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private FirebaseFirestore db;
    private FeaturedDishAdapter featuredDishAdapter;
    private List<Dish> featuredDishes;
    private ListenerRegistration featuredDishesListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        // Categories
        RecyclerView categoriesRecyclerView = view.findViewById(R.id.categories_recycler_view);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(getString(R.string.fast_food), R.drawable.ic_placeholder_category));
        categories.add(new Category(getString(R.string.drinks), R.drawable.ic_placeholder_category));
        categories.add(new Category(getString(R.string.desserts), R.drawable.ic_placeholder_category));
        categories.add(new Category(getString(R.string.main_course), R.drawable.ic_placeholder_category));
        CategoryAdapter categoryAdapter = new CategoryAdapter(categories);
        categoriesRecyclerView.setAdapter(categoryAdapter);

        // Featured Dishes
        RecyclerView featuredDishesRecyclerView = view.findViewById(R.id.featured_dishes_recycler_view);
        featuredDishesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        featuredDishes = new ArrayList<>();
        featuredDishAdapter = new FeaturedDishAdapter(requireContext(), featuredDishes);
        featuredDishesRecyclerView.setAdapter(featuredDishAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadFeaturedDishes();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (featuredDishesListener != null) {
            featuredDishesListener.remove();
        }
    }

    private void loadFeaturedDishes() {
        featuredDishesListener = db.collection("menu").limit(5)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        if (isAdded()) {
                            Toast.makeText(requireContext(), "Error loading featured dishes.", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                    if (snapshots != null && isAdded()) {
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            Dish dish = dc.getDocument().toObject(Dish.class);
                            dish.setDocumentId(dc.getDocument().getId());

                            switch (dc.getType()) {
                                case ADDED:
                                    featuredDishes.add(dc.getNewIndex(), dish);
                                    featuredDishAdapter.notifyItemInserted(dc.getNewIndex());
                                    break;
                                case MODIFIED:
                                    if (dc.getOldIndex() == dc.getNewIndex()) {
                                        featuredDishes.set(dc.getNewIndex(), dish);
                                        featuredDishAdapter.notifyItemChanged(dc.getNewIndex());
                                    } else {
                                        featuredDishes.remove(dc.getOldIndex());
                                        featuredDishes.add(dc.getNewIndex(), dish);
                                        featuredDishAdapter.notifyItemMoved(dc.getOldIndex(), dc.getNewIndex());
                                    }
                                    break;
                                case REMOVED:
                                    if (dc.getOldIndex() < featuredDishes.size()) {
                                        featuredDishes.remove(dc.getOldIndex());
                                        featuredDishAdapter.notifyItemRemoved(dc.getOldIndex());
                                    }
                                    break;
                            }
                        }
                    }
                });
    }
}
