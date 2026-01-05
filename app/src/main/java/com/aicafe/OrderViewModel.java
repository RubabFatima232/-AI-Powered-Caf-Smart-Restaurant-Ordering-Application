package com.aicafe;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderViewModel extends ViewModel {
    public final MutableLiveData<Order> orderLive = new MutableLiveData<>();

    public void placeOrder(Context context, List<Integer> itemIds) {
        List<MenuItem> allItems = MenuRepository.getMenu();
        List<MenuItem> orderItems = new ArrayList<>();
        double total = 0;
        for (int id : itemIds) {
            for (MenuItem item : allItems) {
                if (item.getId() == id) {
                    orderItems.add(item);
                    total += item.getPrice();
                    break;
                }
            }
        }

        String date = new SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(new Date());
        Order order = new Order(String.valueOf(System.currentTimeMillis()), orderItems, total, date);
        OrderRepository.saveOrder(context, order);
        orderLive.postValue(order);
    }
}