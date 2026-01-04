package com.aicafe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.aicafe.model.MenuItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CartManager {
    private static CartManager instance;
    public final Map<Integer, MenuItem> map = new ConcurrentHashMap<>();

    private final MutableLiveData<List<MenuItem>> cart = new MutableLiveData<>();
    private final MutableLiveData<Integer> count = new MutableLiveData<>();
    private final MutableLiveData<Double> total = new MutableLiveData<>();

    private CartManager() {
        updateLiveData();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public LiveData<List<MenuItem>> getCart() { return cart; }
    public LiveData<Integer> getCount() { return count; }
    public LiveData<Double> getTotal() { return total; }

    public void add(MenuItem item) {
        if (item == null) return;
        map.put(item.getId(), item);
        updateLiveData();
    }

    public void remove(int itemId) {
        map.remove(itemId);
        updateLiveData();
    }

    public void remove(MenuItem item) {
        if (item == null) return;
        remove(item.getId());
    }

    public void clear() {
        map.clear();
        updateLiveData();
    }

    public List<Integer> getItemIds() {
        return new ArrayList<>(map.keySet());
    }

    private void updateLiveData() {
        List<MenuItem> items = new ArrayList<>(map.values());
        cart.postValue(items);
        count.postValue(items.size());
        
        double currentTotal = 0;
        for (MenuItem item : items) {
            currentTotal += item.getPrice();
        }
        total.postValue(currentTotal);
    }
}