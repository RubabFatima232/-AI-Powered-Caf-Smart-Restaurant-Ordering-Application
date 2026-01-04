package com.aicafe.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.aicafe.model.MenuItem;
import com.aicafe.CartManager;
import com.aicafe.Repository;
import java.util.List;

public class OrderViewModel extends ViewModel {
    private final Repository repository = new Repository();
    private final CartManager cartManager = CartManager.getInstance();
    private final MutableLiveData<List<MenuItem>> recommendedItems = new MutableLiveData<>();
    private final MutableLiveData<List<MenuItem>> featuredItems = new MutableLiveData<>();

    public void fetchMenuItems() {
        repository.getMenuItems(new Repository.Callback() {
            @Override
            public void onSuccess(List<MenuItem> items) {
                recommendedItems.postValue(items);
                featuredItems.postValue(items);
            }
            @Override
            public void onError(Exception e) {
                // Handle error
            }
        });
    }

    public LiveData<List<MenuItem>> getRecommendedItems() {
        return recommendedItems;
    }

    public LiveData<List<MenuItem>> getFeaturedItems() {
        return featuredItems;
    }

    public LiveData<List<MenuItem>> getCart() {
        return cartManager.getCart();
    }

    public void addToCart(MenuItem item) {
        cartManager.add(item);
    }

    public void removeFromCart(MenuItem item) {
        cartManager.remove(item);
    }

    public void setCategory(String category) {
        // In a real app, you would filter the items by category
    }
}