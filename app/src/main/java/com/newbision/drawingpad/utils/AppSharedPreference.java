package com.newbision.drawingpad.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPreference {
    private static SharedPreferences app_preference;

    public static void addStringPreference(Context context, String key, String value){
        app_preference = context.getSharedPreferences(AppConstant.APP_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = app_preference.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void addIntegerPreference(Context context, String key, int value){
        app_preference = context.getSharedPreferences(AppConstant.APP_SHARED_PREFERENCE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = app_preference.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static String getStringPreference(Context context, String key, String default_value){
        app_preference = context.getSharedPreferences(AppConstant.APP_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return app_preference.getString(key, default_value);
    }

    public static int getIntegerPreference(Context context, String key, int default_value){
        app_preference = context.getSharedPreferences(AppConstant.APP_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return app_preference.getInt(key, default_value);
    }
}
