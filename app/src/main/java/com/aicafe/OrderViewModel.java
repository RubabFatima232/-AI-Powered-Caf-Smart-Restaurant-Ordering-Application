package com.aicafe;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.aicafe.model.MenuItem;
import com.aicafe.model.Order;
import java.util.List;

public class OrderViewModel extends ViewModel {

    public final MutableLiveData<List<MenuItem>> menuLive = new MutableLiveData<>();
    public final MutableLiveData<Order> orderLive = new MutableLiveData<>();
    private final Repository repository = new Repository();

    public void loadMenu() {
        repository.getMenuItems(new Repository.Callback() {
            @Override
            public void onSuccess(List<MenuItem> items) {
                menuLive.postValue(items);
            }

            @Override
            public void onError(Exception e) {
                // For now, do nothing
            }
        });
    }

    public void placeOrder(List<Integer> itemIds) {
        // In a real app, you would have a proper callback
        Order order = repository.placeOrder(itemIds);
        orderLive.postValue(order);
    }
}