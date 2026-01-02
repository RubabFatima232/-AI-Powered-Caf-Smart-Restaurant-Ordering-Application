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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

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

        addSampleDishes();

        // Categories
        RecyclerView categoriesRecyclerView = view.findViewById(R.id.categories_recycler_view);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(getString(R.string.fast_food), R.drawable.burger));
        categories.add(new Category(getString(R.string.drinks), R.drawable.images__1_));
        categories.add(new Category(getString(R.string.desserts), R.drawable.desserts));
        categories.add(new Category(getString(R.string.main_course), R.drawable.grilled_chicken));
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

    private void addSampleDishes() {
        db.collection("menu").get().addOnSuccessListener(queryDocumentSnapshots -> {
            WriteBatch batch = db.batch();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                batch.delete(document.getReference());
            }
            batch.commit().addOnSuccessListener(aVoid -> {
                List<Dish> newDishes = new ArrayList<>();
                newDishes.add(new Dish("Classic Burger", "9.99", "Fast Food", "A delicious beef burger on a toasted bun.", String.valueOf(R.drawable.burger)));
                newDishes.add(new Dish("Deluxe Burger", "12.99", "Fast Food", "A gourmet burger with all the toppings.", String.valueOf(R.drawable.images__3_)));
                newDishes.add(new Dish("Crispy Fries", "4.49", "Fast Food", "Perfectly golden and crispy french fries.", String.valueOf(R.drawable.crispy_fries)));
                newDishes.add(new Dish("Grilled Chicken", "16.99", "Main Course", "A healthy and flavorful grilled chicken breast.", String.valueOf(R.drawable.grilled_chicken)));
                newDishes.add(new Dish("Grilled Salmon", "18.99", "Main Course", "Fresh salmon fillet, grilled to perfection.", String.valueOf(R.drawable.grilled_salmon_final_2_800x1200)));
                newDishes.add(new Dish("Spaghetti Bolognese", "14.99", "Main Course", "A classic Italian pasta dish with a rich meat sauce.", String.valueOf(R.drawable.images)));
                newDishes.add(new Dish("Chicken Salad", "11.99", "Salads", "A light and refreshing chicken salad.", String.valueOf(R.drawable.images__2_)));
                newDishes.add(new Dish("Cola", "2.49", "Drinks", "A cold and refreshing glass of cola.", String.valueOf(R.drawable.images__1_)));
                newDishes.add(new Dish("Chocolate Lava Cake", "6.99", "Desserts", "A decadent chocolate cake with a molten center.", String.valueOf(R.drawable.desserts)));

                for (Dish dish : newDishes) {
                    db.collection("menu").add(dish);
                }

                if (isAdded()) {
                    Toast.makeText(requireContext(), "Menu has been refreshed with new items.", Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void loadFeaturedDishes() {
        if (featuredDishesListener != null) {
            featuredDishesListener.remove();
        }
        featuredDishesListener = db.collection("menu").addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Error loading menu items.", Toast.LENGTH_SHORT).show();
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
                            if (dc.getOldIndex() < featuredDishes.size() && dc.getNewIndex() < featuredDishes.size()) {
                                if (dc.getOldIndex() == dc.getNewIndex()) {
                                    featuredDishes.set(dc.getNewIndex(), dish);
                                    featuredDishAdapter.notifyItemChanged(dc.getNewIndex());
                                } else {
                                    featuredDishes.remove(dc.getOldIndex());
                                    featuredDishes.add(dc.getNewIndex(), dish);
                                    featuredDishAdapter.notifyItemMoved(dc.getOldIndex(), dc.getNewIndex());
                                }
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
