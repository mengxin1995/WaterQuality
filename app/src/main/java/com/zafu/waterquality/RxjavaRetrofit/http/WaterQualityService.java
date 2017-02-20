package com.zafu.waterquality.RxjavaRetrofit.http;

import com.zafu.waterquality.RxjavaRetrofit.entity.HttpResult;
import com.zafu.waterquality.RxjavaRetrofit.entity.WaterData;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by mengxin on 17-2-12.
 */

public interface WaterQualityService {

    /**
     * 获得今天的水质数据
     * @return
     */
    @GET("Option/fordata.php")
    Observable<HttpResult<List<WaterData>>> getTodayWaterData();

    /**
     * 获得指定日期一天的水质数据
     * @return
     */
    @FormUrlEncoded
    @POST("Option/fordata.php")
    Observable<HttpResult<List<WaterData>>> getWaterData(@Field("flag") int flag, @Field("day") String data);
}
