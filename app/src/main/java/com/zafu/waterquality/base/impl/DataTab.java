package com.zafu.waterquality.base.impl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.TimeUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.zafu.waterquality.R;
import com.zafu.waterquality.RxjavaRetrofit.entity.WaterData;
import com.zafu.waterquality.RxjavaRetrofit.http.HttpMethods;
import com.zafu.waterquality.RxjavaRetrofit.subscribers.SimpleHttpSubscriber;
import com.zafu.waterquality.RxjavaRetrofit.subscribers.SubscriberOnNextListener;
import com.zafu.waterquality.base.BasePager;
import com.zafu.waterquality.domain.DataSite;
import com.zafu.waterquality.global.GlobalConstants;
import com.zafu.waterquality.gson.Forecast;
import com.zafu.waterquality.gson.Weather;
import com.zafu.waterquality.service.AutoUpdateService;
import com.zafu.waterquality.utils.Engine;
import com.zafu.waterquality.utils.HttpUtil;
import com.zafu.waterquality.utils.SpUtil;
import com.zafu.waterquality.utils.ToastUtil;
import com.zafu.waterquality.utils.blurredview.BlurredView;
import com.zafu.waterquality.view.RefreshListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zafu.waterquality.R.id.chart;

/**
 * Created by mengxin on 16-10-2.
 */
public class DataTab extends BasePager {

    public static final String DEFAULT_PATTERN = "yyyy:MM:dd";
    private static final String TAG = "DataTab";
    private static final int REFRESHFINISH = 1;
    private ViewHolder mHolder;
    private RefreshListView mLvSiteData;
    private SiteDataAdapter mSiteDataAdapter;
    private ArrayList<DataSite> mSiteDataLists = new ArrayList<DataSite>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case REFRESHFINISH:
                    mLvSiteData.refreshComplete(true);
                    break;
                default:
                    break;
            }
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
    private LineChart mChart;

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
        View chartView = View.inflate(mActivity, R.layout.view_ungeneral_parameter, null);
        setPadding(chartView);
        mLvSiteData.addHeaderView(chartView);
        mChart = (LineChart) chartView.findViewById(chart);
        fillChart();
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

    private void fillChart() {
        HttpMethods.getInstance().getWaterData(new SimpleHttpSubscriber<List<WaterData>>(new SubscriberOnNextListener<List<WaterData>>() {
            @Override
            public void onNext(List<WaterData> waterDatas) {
                List<Entry> entries = new ArrayList<Entry>();
                for (WaterData w :
                        waterDatas) {
                    String t = w.getTime();
                    String[] split = t.split(":");
                    float time = Float.parseFloat(split[0]) * 60 + Float.parseFloat(split[1]);
                    float anDan = w.getAnDan();
                    entries.add(new Entry(time, anDan));
                }

                LineDataSet dataSet = new LineDataSet(entries, "非常规参数--氨氮"); // add entries to dataset
                dataSet.setColor(Color.BLUE);
                dataSet.setValueTextColor(Color.WHITE); // styling, ...
                LineData lineData = new LineData(dataSet);
                mChart.setData(lineData);
                //设置描述信息
                Description mChartDescription = new Description();
//                mChartDescription.setText("非常规参数--氨氮");
//                mChartDescription.setTextSize(15);
//                mChartDescription.setTextColor(mActivity.getResources().getColor(R.color.textColor));
                mChartDescription.setEnabled(false);
                mChart.setDescription(mChartDescription);


                //设置x轴坐标
                XAxis xAxis = mChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawAxisLine(true);
                xAxis.setDrawGridLines(true);
                xAxis.setDrawAxisLine(true);
                xAxis.setEnabled(true);
                xAxis.setAxisLineWidth(2f);
                xAxis.setTextColor(mActivity.getResources().getColor(R.color.textColor));
                xAxis.setLabelCount(7);
                // the labels that should be drawn on the XAxis
                final String[] quarters = new String[] { "0h", "1h", "2h", "3h",
                "4h", "5h", "6h", "7h", "8h", "9h", "10h", "11h", "12h", "13h", "14h", "15h", "16h", "17h", "18h",
                "19h", "20h", "21h", "22h", "23h", "24h"};
                IAxisValueFormatter formatter = new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return quarters[(int) value / 60];
                    }
                };
                xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
                xAxis.setValueFormatter(formatter);

                //设置Y轴属性
                YAxis leftAxis = mChart.getAxisLeft();
                leftAxis.setAxisLineWidth(2f);
                leftAxis.setTextColor(mActivity.getResources().getColor(R.color.textColor));
                YAxis rightAxis = mChart.getAxisRight();
                rightAxis.setAxisLineWidth(2f);
                rightAxis.setTextColor(mActivity.getResources().getColor(R.color.textColor));

                mChart.invalidate(); // refresh
            }
        }), 0, TimeUtils.getNowTimeString(DEFAULT_PATTERN));
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
        //Log.d(TAG, "getLocalWeather: " + mCounty);
        Intent intent = new Intent(mActivity, AutoUpdateService.class);
        mActivity.startService(intent);
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
                //重新获取天气信息
                getLocalWeather();
                //重新获取常规参数

                //重新获取非常规参数
                fillChart();
                //重新获取站点数据
                getDataFromService();
                handler.sendEmptyMessageDelayed(REFRESHFINISH, 2000);
            }
        });
    }

    private void getDataFromService() {
        HttpMethods.getInstance().getWaterData(new SimpleHttpSubscriber<List<WaterData>>(new SubscriberOnNextListener<List<WaterData>>() {
            @Override
            public void onNext(List<WaterData> waterDatas) {
                DataSite elem = new DataSite();
                WaterData waterData = waterDatas.get(waterDatas.size() - 1);
                elem.setSiteName("浙农大");
                elem.setPh(waterData.getPh());
                elem.setDianDaoLv(waterData.getDianDaoLv());
                elem.setShuiWen(waterData.getShuiWen());
                elem.setAnDan(waterData.getAnDan());
                elem.setRongJieYang(waterData.getRongJieYang());
                mSiteDataLists.clear();
                mSiteDataLists.add(elem);
                mSiteDataLists.add(elem);
                mSiteDataAdapter.notifyDataSetChanged();
            }
        }), 0, TimeUtils.getNowTimeString(DEFAULT_PATTERN));
    }

    private class SiteDataAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mSiteDataLists.size();
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

                mHolder.tv_col1 = (TextView) convertView.findViewById(R.id.tv_col1);
                mHolder.tv_col2 = (TextView) convertView.findViewById(R.id.tv_col2);
                mHolder.tv_col3 = (TextView) convertView.findViewById(R.id.tv_col3);
                mHolder.tv_col4 = (TextView) convertView.findViewById(R.id.tv_col4);
                mHolder.tv_col5 = (TextView) convertView.findViewById(R.id.tv_col5);
                mHolder.tv_col6 = (TextView) convertView.findViewById(R.id.tv_col6);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            DataSite dataSite = mSiteDataLists.get(position);

            //站点
            mHolder.tv_col_name1.setText("站点");
            //PH
            mHolder.tv_col_name2.setText("PH");
            //电导率
            mHolder.tv_col_name3.setText("电导率");
            //水温
            mHolder.tv_col_name4.setText("水温");
            //氨氮
            mHolder.tv_col_name5.setText("氨氮");
            //溶解氧
            mHolder.tv_col_name6.setText("溶解氧");


            mHolder.tv_col1.setText(dataSite.getSiteName() + "");
            mHolder.tv_col2.setText(dataSite.getPh() + "");
            mHolder.tv_col3.setText(dataSite.getDianDaoLv() + "");
            mHolder.tv_col4.setText(dataSite.getShuiWen() + "");
            mHolder.tv_col5.setText(dataSite.getAnDan() + "");
            mHolder.tv_col6.setText(dataSite.getRongJieYang() + "");
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
        public TextView tv_col_name1;
        public TextView tv_col_name2;
        public TextView tv_col_name3;
        public TextView tv_col_name4;
        public TextView tv_col_name5;
        public TextView tv_col_name6;
    }
}
