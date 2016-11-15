package com.zafu.waterquality.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by mengxin on 16-10-26.
 */
public class WaterQualityApplication extends Application{
    private static Context context;
    private static Handler handler;
    private static int mainThreadId;

    @Override
    public void onCreate() {
        //百度地图初始化
        SDKInitializer.initialize(getApplicationContext());
        super.onCreate();
        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = android.os.Process.myTid();
    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }
}
