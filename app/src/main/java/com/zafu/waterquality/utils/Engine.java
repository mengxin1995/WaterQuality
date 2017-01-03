package com.zafu.waterquality.utils;

import android.text.TextUtils;
import android.util.Log;

import com.zafu.waterquality.db.City;
import com.zafu.waterquality.db.County;
import com.zafu.waterquality.db.Province;
import com.zafu.waterquality.global.GlobalConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by mengxin on 16-12-29.
 */

public class Engine {
    private static final String TAG = "Engine";
    private static String mProvince;
    private static String mCity;
    private static String mCounty;
    private static Province mSelectProvince;
    private static City mSelectCity;
    private static WeatherUrlFinded mWeatherUrlFinded;
    /**
     * 解析和处理服务器返回的省级数据
     * @param response
     * @return
     */
    public static boolean handleProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvinces = new JSONArray(response);
                for(int i=0; i<allProvinces.length(); i++){
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProviceCode(provinceObject.getInt("id"));
                    //储存进数据库
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     * @param response
     * @return
     */
    public static boolean handleCityResponse(String response, int provinceId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCitys = new JSONArray(response);
                for(int i=0; i<allCitys.length(); i++){
                    JSONObject cityObject = allCitys.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     * @param response
     * @return
     */
    public static boolean handleCountyResponse(String response, int cityId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCountys = new JSONArray(response);
                for(int i=0; i<allCountys.length(); i++){
                    JSONObject countyObject = allCountys.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void findWeatherURL(String province, String city, String county, WeatherUrlFinded wuf){
        mWeatherUrlFinded = wuf;
        mProvince = province;
        mCity = city;
        mCounty = county;
        queryProvince();
    }


    private static void queryProvince(){
        List<Province> provinceLists = DataSupport.findAll(Province.class);
        if(provinceLists.size() > 0){
            for (Province p :
                    provinceLists) {
                if (mProvince.contains(p.getProvinceName())) {
                    List<Province> pro = DataSupport.select("provicecode").where("provincename = ?", p.getProvinceName()).find(Province.class);
                    mSelectProvince = pro.get(0);
                    queryCity(mSelectProvince.getProviceCode());
                }
            }
        }else{
            queryFromServer(GlobalConstants.PROVINCE_URL, "Province");
        }
    }

    private static void queryCity(int provinceId) {
        List<City> provinceLists = DataSupport.where("provinceid = ?", String.valueOf(provinceId)).find(City.class);
       if(provinceLists.size() > 0){
           for (City c:
                provinceLists) {
               if(mCity.contains(c.getCityName())){
                   List<City> city = DataSupport.select("citycode").where("cityname = ?", c.getCityName()).find(City.class);
                   mSelectCity = city.get(0);
                   queryCounty(mSelectCity.getCityCode());
               }
           }
       }else{
           queryFromServer(GlobalConstants.PROVINCE_URL + "/" + provinceId, "City");
       }
    }

    private static void queryCounty(int cityCode) {
        List<County> countyList = DataSupport.where("cityid = ?", String.valueOf(cityCode)).find(County.class);
        if(countyList.size() > 0){
            for (County c :
                    countyList) {
                if (mCounty.contains(c.getCountyName())){
                    //这里找到最终的url
                    String finalUrl = GlobalConstants.PROVINCE_URL + "/" + mSelectProvince.getProviceCode()
                            + "/" + mSelectCity.getCityCode()
                            + "/" + c.getWeatherId();
                    Log.d(TAG, "queryCounty: " + finalUrl);
                    mWeatherUrlFinded.success(finalUrl);
                }
            }
        }else{
            queryFromServer(GlobalConstants.PROVINCE_URL + "/" + mSelectProvince.getProviceCode()
            + "/" + mSelectCity.getCityCode(), "County");
        }
    }

    private static void queryFromServer(String adress, final String type) {
        HttpUtil.sendOkHttpRequest(adress, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                boolean result = false;
                if(type.equals("Province")){
                    result = Engine.handleProvinceResponse(response.body().string());
                    if(result == true){
                        queryProvince();
                    }
                }else if(type.equals("City")){
                    result = Engine.handleCityResponse(response.body().string(), mSelectProvince.getProviceCode());
                    if(result == true){
                        queryCity(mSelectProvince.getProviceCode());
                    }
                }else if(type.equals("County")){
                    result = Engine.handleCountyResponse(response.body().string(), mSelectCity.getCityCode());
                    if(result == true){
                        queryCounty(mSelectCity.getCityCode());
                    }
                }
            }
        });
    }



    public interface WeatherUrlFinded{
        public void success(String finalUrl);
    }
}
