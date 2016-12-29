package com.zafu.waterquality.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zafu.waterquality.global.GlobalConstants;


/**
 * Created by mengxin on 16-9-23.
 */
public class SpUtil {

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(GlobalConstants.CONFIG,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void setBoolean(Context context, String key, boolean value){
        SharedPreferences sp = context.getSharedPreferences(GlobalConstants.CONFIG,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(GlobalConstants.CONFIG,
                Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static void setString(Context context, String key, String value){
        SharedPreferences sp = context.getSharedPreferences(GlobalConstants.CONFIG,
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }
}
