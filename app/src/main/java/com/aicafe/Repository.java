package com.aicafe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.aicafe.model.Category;
import com.aicafe.model.MenuItem;
import com.aicafe.model.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private static Repository instance;
    private final ApiService apiService;

    private final MutableLiveData<List<MenuItem>> recommendedDishes = new MutableLiveData<>();
    private final MutableLiveData<List<MenuItem>> featuredDishes = new MutableLiveData<>();
    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<Order> orderConfirmation = new MutableLiveData<>();

    private Repository() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public static synchronized Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public LiveData<List<MenuItem>> getRecommendedDishes() {
        fetchRecommendedDishes();
        return recommendedDishes;
    }

    public LiveData<List<MenuItem>> getFeaturedDishes() {
        fetchFeaturedDishes();
        return featuredDishes;
    }

    public LiveData<List<Category>> getCategories() {
        fetchCategories();
        return categories;
    }

    public LiveData<Order> placeOrder(List<Integer> itemIds) {
        apiService.placeOrder(itemIds).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    orderConfirmation.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                // Handle failure
                orderConfirmation.setValue(null);
            }
        });
        return orderConfirmation;
    }

    private void fetchRecommendedDishes() {
        apiService.getRecommendedDishes().enqueue(new Callback<List<MenuItem>>() {
            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                if (response.isSuccessful()) {
                    recommendedDishes.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<MenuItem>> call, Throwable t) { /* Handle failure */ }
        });
    }

    private void fetchFeaturedDishes() {
        apiService.getFeaturedDishes().enqueue(new Callback<List<MenuItem>>() {
            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                if (response.isSuccessful()) {
                    featuredDishes.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<MenuItem>> call, Throwable t) { /* Handle failure */ }
        });
    }

    private void fetchCategories() {
        apiService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    categories.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) { /* Handle failure */ }
        });
    }
}