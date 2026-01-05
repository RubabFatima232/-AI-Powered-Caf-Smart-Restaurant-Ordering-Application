package com.aicafe;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private static final String PREF = "aiCafeHistory";
    private static final Gson gson = new Gson();
    private static final Type TYPE = new TypeToken<List<Order>>(){}.getType();

    public static List<Order> getHistory(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String json = prefs.getString("history", "[]");
        List<Order> orders = gson.fromJson(json, TYPE);
        if (orders == null) {
            return new ArrayList<>();
        }
        return orders;
    }
    public static void saveOrder(Context ctx, Order order) {
        List<Order> list = getHistory(ctx);
        list.add(order);
        SharedPreferences prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        prefs.edit().putString("history", gson.toJson(list)).apply();
    }
}
