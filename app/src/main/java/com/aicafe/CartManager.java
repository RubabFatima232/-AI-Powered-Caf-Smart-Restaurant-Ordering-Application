package com.aicafe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartManager {
    private static CartManager instance;
    private final Map<Integer, CartItem> map = new HashMap<>();
    private final MutableLiveData<List<CartItem>> liveItems = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Double> liveTotal = new MutableLiveData<>(0.0);
    private final MutableLiveData<Integer> liveCount = new MutableLiveData<>(0);

    public static synchronized CartManager getInstance() {
        if (instance == null) instance = new CartManager();
        return instance;
    }

    public void add(MenuItem item) {
        if (map.containsKey(item.getId())) {
            CartItem cartItem = map.get(item.getId());
            if (cartItem != null) {
                cartItem.increment();
            }
        } else {
            map.put(item.getId(), new CartItem(item));
        }
        update();
    }

    public void remove(MenuItem item) {
        if (map.containsKey(item.getId())) {
            CartItem cartItem = map.get(item.getId());
            if (cartItem != null) {
                cartItem.decrement();
                if (cartItem.getQuantity() == 0) {
                    map.remove(item.getId());
                }
            }
        }
        update();
    }

    public void clear() {
        map.clear();
        update();
    }

    public List<Integer> getItemIds() {
        return new ArrayList<>(map.keySet());
    }

    public LiveData<List<CartItem>> getItems() {
        return liveItems;
    }

    public LiveData<Double> getTotal() {
        return liveTotal;
    }

    public LiveData<Integer> getCount() {
        return liveCount;
    }

    private void update() {
        List<CartItem> list = new ArrayList<>(map.values());
        liveItems.postValue(list);
        int count = 0;
        double total = 0;
        for (CartItem i : list) {
            count += i.getQuantity();
            total += i.getSubtotal();
        }
        liveCount.postValue(count);
        liveTotal.postValue(total);
    }
}
