package com.aicafe;

import android.content.Context;
import android.content.SharedPreferences;

public class RoleManager {
    private static final String PREF = "aiCafeRole";
    public static void setRole(Context ctx, String role) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        prefs.edit().putString("role", role).apply();
    }
    public static String getRole(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return prefs.getString("role", "customer");
    }
}
