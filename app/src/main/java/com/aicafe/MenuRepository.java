package com.aicafe;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MenuRepository {
    private static final String PREF = "aiCafeMenu";
    private static final Gson gson = new Gson();
    private static final Type TYPE = new TypeToken<List<MenuItem>>(){}.getType();

    public static List<MenuItem> getMenu() {
        // default seed if nothing saved
        List<MenuItem> seed = new ArrayList<>();
        seed.add(new MenuItem(11,"AI Latte",3.5,"Creamy latte with AI foam art","https://images.unsplash.com/photo-1561882468-9110e03e0f16?auto=format&fit=crop&w=400&q=60","Beverages"));
        seed.add(new MenuItem(21,"Smart Burger",7.9,"Juicy patty with smart sauce","https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=400&q=60","Fast Food"));
        seed.add(new MenuItem(31,"AI Cheesecake",4.5,"Perfect texture every time","https://images.unsplash.com/photo-1565958011703-44f9829ba187?auto=format&fit=crop&w=400&q=60","Desserts"));
        seed.add(new MenuItem(41,"AI Caesar",5.9,"Balanced dressing, AI-mixed","https://images.unsplash.com/photo-1546793665-c74683f339c1?auto=format&fit=crop&w=400&q=60","Salads"));

        SharedPreferences prefs = MyApp.getContext().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String json = prefs.getString("menu", null);
        if (json == null) {
            return seed;
        }
        List<MenuItem> savedMenu = gson.fromJson(json, TYPE);
        if (savedMenu == null || savedMenu.isEmpty()) {
            return seed;
        }
        return savedMenu;
    }
    public static void saveMenu(List<MenuItem> list) {
        SharedPreferences prefs = MyApp.getContext().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        prefs.edit().putString("menu", gson.toJson(list)).apply();
    }
}