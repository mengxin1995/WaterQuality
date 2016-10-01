package com.zafu.waterquality.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by mengxin on 16-9-23.
 */
public class ToastUtil {
    public static void show(Context ctx, String msg){
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }
}
