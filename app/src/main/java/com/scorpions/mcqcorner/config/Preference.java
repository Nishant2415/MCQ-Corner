package com.scorpions.mcqcorner.config;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {

    public static final String USER_PREF = "UserPreference";
    public static SharedPreferences sp;

    public static void setString(Context context, String key, String value) {
        sp = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public static String getString(Context context, String key) {
        sp = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        return sp.getString(key, null);
    }

    public static void setInt(Context context, String key, int value) {
        sp = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    public static int getInt(Context context, String key) {
        sp = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        sp = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key) {
        sp = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

}
