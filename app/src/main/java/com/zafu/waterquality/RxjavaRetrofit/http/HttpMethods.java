package com.zafu.waterquality.RxjavaRetrofit.http;

import com.zafu.waterquality.RxjavaRetrofit.entity.HttpResult;
import com.zafu.waterquality.RxjavaRetrofit.entity.WaterData;
import com.zafu.waterquality.global.GlobalConstants;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by mengxin on 17-2-12.
 */

public class HttpMethods {

    private static final int DEFAULT_TIMEOUT = 10;
    private final Retrofit mRetrofit;
    private final WaterQualityService mWaterQualityService;
    private static HttpMethods httpMethods = null;

    private HttpMethods() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(GlobalConstants.SITE_BASE_URL)
                .build();

        mWaterQualityService = mRetrofit.create(WaterQualityService.class);
    }

    public static HttpMethods getInstance(){
        if(httpMethods == null){
            synchronized (HttpMethods.class){
                if(httpMethods == null){
                    httpMethods = new HttpMethods();
                }
                return httpMethods;
            }
        }
        return httpMethods;
    }


    /**
     * 获得今天的水质数据
     * @return
     */
    public void getTodayWaterData(Subscriber<List<WaterData>> subscriber){
        Observable<List<WaterData>> observable = mWaterQualityService.getTodayWaterData()
                .map(new HttpResultFunc<List<WaterData>>());

        toSubscribe(observable, subscriber);
    }


    /**
     * 获得指定日期一天的水质数据
     * @return
     */
    public void getWaterData(Subscriber<List<WaterData>> subscriber, int flag, String data){
        Observable<List<WaterData>> observable = mWaterQualityService.getWaterData(flag, data)
                .map(new HttpResultFunc<List<WaterData>>());
        toSubscribe(observable, subscriber);
    }

    private <T> void toSubscribe(Observable<T> observable, Subscriber<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T>   Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (!httpResult.isSuccess()) {
                throw new ApiException(100);
            }
            return httpResult.getSubjects();
        }
    }
}
