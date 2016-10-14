package com.zafu.waterquality.base.impl;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zafu.waterquality.R;
import com.zafu.waterquality.base.BasePager;
import com.zafu.waterquality.domain.DataPoint;
import com.zafu.waterquality.global.GlobalConstants;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mengxin on 16-10-2.
 */
public class DataTab extends BasePager {

    private ViewHolder mHolder;
    private ListView mLvSiteData;
    private SiteDataAdapter mSiteDataAdapter;
    private ArrayList<DataPoint> mSiteDataLists = new ArrayList<DataPoint>() ;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(mSiteDataAdapter != null){
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
//                        mLvSiteData.getLayoutParams();
//                View view = View.inflate(mActivity, R.layout.listview_site_data_item, null);

                mSiteDataAdapter.notifyDataSetChanged();
            }
        }
    };

    public DataTab(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tvTitle.setText("水质检测");
        ibMenu.setVisibility(View.INVISIBLE);
        View view = View.inflate(mActivity, R.layout.view_data_tab, null);
        mLvSiteData = (ListView) view.findViewById(R.id.lv_site_data);

        //获取数据
        getDataFromService();

        mSiteDataAdapter = new SiteDataAdapter();
        mLvSiteData.setAdapter(mSiteDataAdapter);

        mLvSiteData.addHeaderView(View.inflate(mActivity, R.layout.view_general_parameter, null));
        mLvSiteData.addHeaderView(View.inflate(mActivity, R.layout.view_ungeneral_parameter, null));
        mLvSiteData.addHeaderView(View.inflate(mActivity, R.layout.listview_site_data_item_head, null));
        flContent.addView(view);
    }

    private void getDataFromService() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url =new URL(GlobalConstants.SITE_URL) ;
                    HttpURLConnection connection =(HttpURLConnection) url.openConnection() ;
                    //服务器的常用的两个方法,post,get
                    connection.setRequestMethod("GET") ;
                    //链接超时,读取超时，根据自己情况定
                    connection.setConnectTimeout(8000) ;
                    connection.setReadTimeout(8000) ;
                    //下面是读取，可以用connection带的方法，获取输入流
                    InputStream inStream = connection.getInputStream() ;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inStream)) ;
                    mSiteDataLists.clear() ;
                    String line = null;
                    int count = 0;
                    while((line=reader.readLine())!=null){
                        if(count == 0)
                        {
                            count++;
                            continue;
                        }
                        String item[] = line.split(",");
                        DataPoint elem = new DataPoint() ;
                        elem.setPonitName( item[0]) ;
                        Log.i("name", item[0]) ;
                        elem.setOxgasValue(Double.parseDouble(item[1])) ;
                        elem.setPhValue(Double.parseDouble(item[3])) ;
                        elem.setTempVale(Double.parseDouble(item[4])) ;
                        elem.setnValue(Double.parseDouble(item[2])*1000) ;
                        elem.setZhouValue(Double.parseDouble(item[5])) ;
                        mSiteDataLists.add(elem) ;
                    }
                    handler.sendEmptyMessage(0);
                    connection.disconnect();
                }
                catch (Exception e) {
                    e.printStackTrace() ;
                }
            }
        }).start();
    }


    private class SiteDataAdapter extends BaseAdapter{
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
            if(convertView == null){
                convertView = View.inflate(mActivity, R.layout.listview_site_data_item, null);
                mHolder = new ViewHolder();
                mHolder.tv_col1 = (TextView) convertView.findViewById(R.id.tv_col1);
                mHolder.tv_col2 = (TextView) convertView.findViewById(R.id.tv_col2);
                mHolder.tv_col3 = (TextView) convertView.findViewById(R.id.tv_col3);
                mHolder.tv_col4 = (TextView) convertView.findViewById(R.id.tv_col4);
                mHolder.tv_col5 = (TextView) convertView.findViewById(R.id.tv_col5);
                mHolder.tv_col6 = (TextView) convertView.findViewById(R.id.tv_col6);
                convertView.setTag(mHolder);
            }else{
                mHolder = (ViewHolder) convertView.getTag();
            }
            DataPoint dataPoint = mSiteDataLists.get(position);
            mHolder.tv_col1.setText(dataPoint.getPonitName() + "");
            mHolder.tv_col2.setText(dataPoint.getnValue() + "");
            mHolder.tv_col3.setText(dataPoint.getOxgasValue() + "");
            mHolder.tv_col4.setText(dataPoint.getPhValue() + "");
            mHolder.tv_col5.setText(dataPoint.getTempVale() + "");
            mHolder.tv_col6.setText(dataPoint.getZhouValue() + "");
            return convertView;
        }
    }

    static class ViewHolder{
        public TextView tv_col1;
        public TextView tv_col2;
        public TextView tv_col3;
        public TextView tv_col4;
        public TextView tv_col5;
        public TextView tv_col6;
    }
}
