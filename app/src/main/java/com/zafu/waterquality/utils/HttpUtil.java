package com.zafu.waterquality.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by mengxin on 16-12-29.
 */

public class HttpUtil {

    /**
     * 异步GET请求
     * @param address
     * @param callback
     */
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
