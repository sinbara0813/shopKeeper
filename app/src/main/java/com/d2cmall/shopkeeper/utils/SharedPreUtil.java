package com.d2cmall.shopkeeper.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by WuXiaolong
 * on 2016/3/31.
 * 微信公众号：吴小龙同学
 * 个人博客：http://wuxiaolong.me/
 */
public class SharedPreUtil {
    public static String getString(String key,
                                   final String defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(App.getContext());
        return settings.getString(key, defaultValue);
    }

    public static void setString(String key,
                                 String value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(App.getContext());
        settings.edit().putString(key, value).apply();
    }

    public static boolean getBoolean(String key,
                                     boolean defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(App.getContext());
        return settings.getBoolean(key, defaultValue);
    }

    public static boolean hasKey(String key) {
        return PreferenceManager.getDefaultSharedPreferences(App.getContext()).contains(
                key);
    }

    public static void setBoolean(String key,
                                  boolean value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(App.getContext());
        settings.edit().putBoolean(key, value).apply();
    }

    public static void setInt(String key,
                              int value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(App.getContext());
        settings.edit().putInt(key, value).apply();
    }

    public static int getInt(String key,
                             int defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(App.getContext());
        return settings.getInt(key, defaultValue);
    }

    public static void setFloat(String key,
                                float value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(App.getContext());
        settings.edit().putFloat(key, value).apply();
    }

    public static float getFloat(String key,
                                 float defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(App.getContext());
        return settings.getFloat(key, defaultValue);
    }

    public static void setLong(String key,
                               long value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(App.getContext());
        settings.edit().putLong(key, value).apply();
    }

    public static long getLong(String key,
                               long defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(App.getContext());
        return settings.getLong(key, defaultValue);
    }

    public static void clear(SharedPreferences p) {
        final SharedPreferences.Editor editor = p.edit();
        editor.clear();
        editor.apply();
    }
}
