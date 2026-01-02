package com.aicafe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.aicafe.model.Category;
import com.aicafe.model.MenuItem;
import com.aicafe.model.Order;

import java.util.List;

public class OrderViewModel extends ViewModel {
    private final Repository repository;
    private final LiveData<List<MenuItem>> recommendedDishes;
    private final LiveData<List<MenuItem>> featuredDishes;
    private final LiveData<List<Category>> categories;

    public OrderViewModel() {
        repository = Repository.getInstance();
        recommendedDishes = repository.getRecommendedDishes();
        featuredDishes = repository.getFeaturedDishes();
        categories = repository.getCategories();
    }

    public LiveData<List<MenuItem>> getRecommendedDishes() {
        return recommendedDishes;
    }

    public LiveData<List<MenuItem>> getFeaturedDishes() {
        return featuredDishes;
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public LiveData<Order> placeOrder(List<Integer> itemIds) {
        return repository.placeOrder(itemIds);
    }
}
