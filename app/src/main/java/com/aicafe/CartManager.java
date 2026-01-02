package com.aicafe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.aicafe.model.MenuItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartManager {
    private static CartManager instance;
    private final HashMap<Integer, MenuItem> map = new HashMap<>();
    private final MutableLiveData<Integer> liveCount = new MutableLiveData<>(0);
    private final MutableLiveData<Double> liveTotal = new MutableLiveData<>(0.0);

    public static synchronized CartManager getInstance() {
        if (instance == null) instance = new CartManager();
        return instance;
    }
    public void add(MenuItem item) {
        map.put(item.getId(), item);
        update();
    }
    public void remove(int id) {
        map.remove(id);
        update();
    }
    public List<Integer> getItemIds() { return new ArrayList<>(map.keySet()); }
    public LiveData<Integer> getCount() { return liveCount; }
    public LiveData<Double> getTotal() { return liveTotal; }
    public Map<Integer, MenuItem> getMap() { return map; }
    public void clear() { map.clear(); update(); }
    private void update() {
        liveCount.postValue(map.size());
        double total = 0;
        for (MenuItem i : map.values()) total += i.getPrice();
        liveTotal.postValue(total);
    }
}