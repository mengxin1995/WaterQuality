package com.zafu.waterquality.base.impl;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zafu.waterquality.R;
import com.zafu.waterquality.base.BasePager;
import com.zafu.waterquality.domain.DataPoint;
import com.zafu.waterquality.global.GlobalConstants;
import com.zafu.waterquality.gson.Forecast;
import com.zafu.waterquality.gson.Weather;
import com.zafu.waterquality.utils.Engine;
import com.zafu.waterquality.utils.HttpUtil;
import com.zafu.waterquality.utils.SpUtil;
import com.zafu.waterquality.utils.ToastUtil;
import com.zafu.waterquality.utils.blurredview.BlurredView;
import com.zafu.waterquality.view.RefreshListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by mengxin on 16-10-2.
 */
public class DataTab extends BasePager {

    private static final String TAG = "DataTab";
    private ViewHolder mHolder;
    private RefreshListView mLvSiteData;
    private SiteDataAdapter mSiteDataAdapter;
    private ArrayList<DataPoint> mSiteDataLists = new ArrayList<DataPoint>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mLvSiteData.refreshComplete(true);
        }
    };
    private BlurredView mBlurredView;
    private String mProvince;
    private String mCity;
    private String mCounty;
    private View mWeatherNowView;
    private View mWeatherForecastView;
    private View mWeatherAqiView;
    private View mWeatherSuggestion;

    private TextView titleCity;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;
    private View mMainview;

    public DataTab(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("水质检测");
        tvTitle.setText("水质检测");
        ibMenu.setVisibility(View.INVISIBLE);
        //tvTitle.setVisibility(View.GONE);
        rlTitle.setVisibility(View.GONE);
        mMainview = View.inflate(mActivity, R.layout.view_data_tab, null);
        mLvSiteData = (RefreshListView) mMainview.findViewById(R.id.lv_site_data);
        mBlurredView = (BlurredView) mMainview.findViewById(R.id.yahooweather_blurredview);
        //获取数据
        getDataFromService();
        //刷新做什么
        initLvRefresh();

        mSiteDataAdapter = new SiteDataAdapter();

        //天气预报板块添加
        initWeatherUI();
        mLvSiteData.addHeaderView(View.inflate(mActivity, R.layout.view_general_parameter, null));
        mLvSiteData.addHeaderView(View.inflate(mActivity, R.layout.view_ungeneral_parameter, null));
        mLvSiteData.addHeaderView(View.inflate(mActivity, R.layout.listview_site_data_item_head, null));
        mLvSiteData.setAdapter(mSiteDataAdapter);

        mLvSiteData.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem == 0 || firstVisibleItem == 1){
                    mBlurredView.setBlurredLevel(0);
                }else{
                    mBlurredView.setBlurredLevel(100);
                }
            }
        });
        flContent.addView(mMainview);
    }

    /**
     * 把天气条目一个个加进来
     */
    private void initWeatherUI() {
        mWeatherNowView = View.inflate(mActivity, R.layout.weather_now, null);
        mWeatherForecastView = View.inflate(mActivity, R.layout.weather_forecast, null);
        mWeatherAqiView = View.inflate(mActivity, R.layout.weather_aqi, null);
        mWeatherSuggestion = View.inflate(mActivity, R.layout.weather_suggestion, null);
        setPadding(mWeatherNowView);
        setPadding(mWeatherForecastView);
        setPadding(mWeatherAqiView);
        setPadding(mWeatherSuggestion);
        mLvSiteData.addHeaderView(mWeatherNowView);
        mLvSiteData.addHeaderView(mWeatherForecastView);
        mLvSiteData.addHeaderView(mWeatherAqiView);
        mLvSiteData.addHeaderView(mWeatherSuggestion);
        // 初始化各控件
        titleCity = (TextView) mMainview.findViewById(R.id.title_city);
        degreeText = (TextView) mWeatherNowView.findViewById(R.id.degree_text);
        weatherInfoText = (TextView) mWeatherNowView.findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) mWeatherForecastView.findViewById(R.id.forecast_layout);
        aqiText = (TextView) mWeatherAqiView.findViewById(R.id.aqi_text);
        pm25Text = (TextView) mWeatherAqiView.findViewById(R.id.pm25_text);
        comfortText = (TextView) mWeatherSuggestion.findViewById(R.id.comfort_text);
        carWashText = (TextView) mWeatherSuggestion.findViewById(R.id.car_wash_text);
        sportText = (TextView) mWeatherSuggestion.findViewById(R.id.sport_text);
        String weatherString = SpUtil.getString(mActivity, GlobalConstants.WEATHER, null);
        if(weatherString != null){
            //有缓存直接解析天气数据
            Weather weather = Engine.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }else{
            //获取当地天气信息
            getLocalWeather();
        }
    }

    /**
     * 把天气数据展示出来
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.weather_forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }

        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运行建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
//        weatherLayout.setVisibility(View.VISIBLE);
//        Intent intent = new Intent(this, AutoUpdateService.class);
//        startService(intent);
    }

    /**
     * 获取当地天气信息
     */
    private void getLocalWeather() {
        mProvince = SpUtil.getString(mActivity, GlobalConstants.PROVINCE, "浙江省");
        mCity = SpUtil.getString(mActivity, GlobalConstants.CITY, "杭州市");
        mCounty = SpUtil.getString(mActivity, GlobalConstants.DISTRICT, "临安");
        Engine.findWeatherURL(mProvince, mCity, mCounty, new Engine.WeatherIdFinded() {
            @Override
            public void success(String weatherId) {
                String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=a615da100d8e4631a3e80367a5c046f8";
                HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show(mActivity, "获取天气信息失败");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseText = response.body().string();
                        final Weather weather = Engine.handleWeatherResponse(responseText);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(weather != null && "ok".equals(weather.status)){
                                    SpUtil.setString(mActivity, GlobalConstants.WEATHER, responseText);
                                    showWeatherInfo(weather);
                                }else{
                                    ToastUtil.show(mActivity, "获取天气信息失败");
                                }
                            }
                        });
                    }
                });
            }
        });
    }


    private void initLvRefresh() {
        mLvSiteData.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.sendEmptyMessageDelayed(111, 2000);
            }
        });
    }

    private void getDataFromService() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(GlobalConstants.SITE_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //服务器的常用的两个方法,post,get
                    connection.setRequestMethod("GET");
                    //链接超时,读取超时，根据自己情况定
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    //下面是读取，可以用connection带的方法，获取输入流
                    InputStream inStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
                    mSiteDataLists.clear();
                    String line = null;
                    int count = 0;
                    while ((line = reader.readLine()) != null) {
                        if (count == 0) {
                            count++;
                            continue;
                        }
                        String item[] = line.split(",");
                        DataPoint elem = new DataPoint();
                        elem.setPonitName(item[0]);
                        Log.i("name", item[0]);
                        elem.setOxgasValue(Double.parseDouble(item[1]));
                        elem.setPhValue(Double.parseDouble(item[3]));
                        elem.setTempVale(Double.parseDouble(item[4]));
                        elem.setnValue(Double.parseDouble(item[2]) * 1000);
                        elem.setZhouValue(Double.parseDouble(item[5]));
                        mSiteDataLists.add(elem);
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class SiteDataAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return mSiteDataLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.listview_site_data_item, null);
                //设置内边距
                setPadding(convertView);
                mHolder = new ViewHolder();
                mHolder.tv_col_name1 = (TextView) convertView.findViewById(R.id.tv_col_name1);
                mHolder.tv_col_name2 = (TextView) convertView.findViewById(R.id.tv_col_name2);
                mHolder.tv_col_name3 = (TextView) convertView.findViewById(R.id.tv_col_name3);
                mHolder.tv_col_name4 = (TextView) convertView.findViewById(R.id.tv_col_name4);
                mHolder.tv_col_name5 = (TextView) convertView.findViewById(R.id.tv_col_name5);
                mHolder.tv_col_name6 = (TextView) convertView.findViewById(R.id.tv_col_name6);
                mHolder.tv_col_name7 = (TextView) convertView.findViewById(R.id.tv_col_name7);
                mHolder.tv_col_name8 = (TextView) convertView.findViewById(R.id.tv_col_name8);

                mHolder.tv_col1 = (TextView) convertView.findViewById(R.id.tv_col1);
                mHolder.tv_col2 = (TextView) convertView.findViewById(R.id.tv_col2);
                mHolder.tv_col3 = (TextView) convertView.findViewById(R.id.tv_col3);
                mHolder.tv_col4 = (TextView) convertView.findViewById(R.id.tv_col4);
                mHolder.tv_col5 = (TextView) convertView.findViewById(R.id.tv_col5);
                mHolder.tv_col6 = (TextView) convertView.findViewById(R.id.tv_col6);
                mHolder.tv_col7 = (TextView) convertView.findViewById(R.id.tv_col7);
                mHolder.tv_col8 = (TextView) convertView.findViewById(R.id.tv_col8);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            DataPoint dataPoint = mSiteDataLists.get(position);

            mHolder.tv_col_name1.setText("ZAFU");
            mHolder.tv_col_name2.setText("THM");
            mHolder.tv_col_name3.setText("PH");
            mHolder.tv_col_name4.setText("CTD");
            mHolder.tv_col_name5.setText("O2");
           // mHolder.tv_col_name6.setText("WTMP");
           // mHolder.tv_col_name7.setText("WTMP");
          //  mHolder.tv_col_name8.setText("WTMP");

            //mHolder.tv_col1.setText("");
            mHolder.tv_col2.setText(dataPoint.getnValue() + "");
            mHolder.tv_col3.setText(dataPoint.getOxgasValue() + "");
            mHolder.tv_col4.setText(dataPoint.getPhValue() + "");
            mHolder.tv_col5.setText(dataPoint.getTempVale() + "");
            mHolder.tv_col6.setText(dataPoint.getZhouValue() + "");
            mHolder.tv_col7.setText(dataPoint.getZhouValue() + "");
            mHolder.tv_col8.setText(dataPoint.getZhouValue() + "");
            return convertView;
        }

    }

    private void setPadding(View convertView) {
        WindowManager wm = mActivity.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int padding = (int) (width * 0.05);
        convertView.setPadding(padding, 0, padding, 0);
    }
    static class ViewHolder {
        public TextView tv_col1;
        public TextView tv_col2;
        public TextView tv_col3;
        public TextView tv_col4;
        public TextView tv_col5;
        public TextView tv_col6;
        public TextView tv_col7;
        public TextView tv_col8;
        public TextView tv_col_name1;
        public TextView tv_col_name2;
        public TextView tv_col_name3;
        public TextView tv_col_name4;
        public TextView tv_col_name5;
        public TextView tv_col_name6;
        public TextView tv_col_name7;
        public TextView tv_col_name8;

    }
}
