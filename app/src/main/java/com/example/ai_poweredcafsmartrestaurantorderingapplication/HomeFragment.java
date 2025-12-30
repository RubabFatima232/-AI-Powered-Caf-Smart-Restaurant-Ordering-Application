package com.example.ai_poweredcafsmartrestaurantorderingapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Categories
        RecyclerView categoriesRecyclerView = view.findViewById(R.id.categories_recycler_view);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Fast Food", R.drawable.ic_placeholder_category));
        categories.add(new Category("Drinks", R.drawable.ic_placeholder_category));
        categories.add(new Category("Desserts", R.drawable.ic_placeholder_category));
        categories.add(new Category("Main Course", R.drawable.ic_placeholder_category));
        CategoryAdapter categoryAdapter = new CategoryAdapter(categories);
        categoriesRecyclerView.setAdapter(categoryAdapter);

        // Featured Dishes
        RecyclerView featuredDishesRecyclerView = view.findViewById(R.id.featured_dishes_recycler_view);
        featuredDishesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Dish> dishes = new ArrayList<>();
        dishes.add(new Dish("Cheese Pizza", "$8.99", R.drawable.ic_placeholder_dish));
        dishes.add(new Dish("Chocolate Shake", "$4.50", R.drawable.ic_placeholder_dish));
        dishes.add(new Dish("Spaghetti Bolognese", "$12.00", R.drawable.ic_placeholder_dish));
        FeaturedDishAdapter featuredDishAdapter = new FeaturedDishAdapter(getContext(), dishes);
        featuredDishesRecyclerView.setAdapter(featuredDishAdapter);

        return view;
    }
}
