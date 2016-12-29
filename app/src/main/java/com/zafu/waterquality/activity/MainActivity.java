package com.zafu.waterquality.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zafu.waterquality.R;
import com.zafu.waterquality.fragment.ContentFragment;
import com.zafu.waterquality.fragment.LeftMenuFragment;
import com.zafu.waterquality.global.GlobalConstants;
import com.zafu.waterquality.utils.LogUtil;
import com.zafu.waterquality.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends SlidingFragmentActivity {
    private static final String TAG = "MainActivity";
    private static final int REFRESH_COMPLETE = 1;
    private SlidingMenu mSlidingMenu;
    private FragmentManager mSupportFragmentManager;

    public LocationClient mLocationClient;
    private MapView mMapView = null;
    private View mViewPointTab;
    private SwipeRefreshLayout mSwipeLayout;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case REFRESH_COMPLETE:
                    mSwipeLayout.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.activity_main);
        //获取当前位置
        getLocalAddress();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        setBehindContentView(R.layout.left_menu);
        mSlidingMenu = getSlidingMenu();
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        WindowManager windowManager = getWindowManager();
        int width = windowManager.getDefaultDisplay().getWidth();
        LogUtil.getInstance().debug(width + "");
        mSlidingMenu.setBehindOffset(width * 200 / 320);

        initFragment();
    }

    /**
     * 获取当前位置
     */
    private void getLocalAddress() {

        //申请权限
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            requestLocation();
        }
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(1000 * 60 * 5);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }


    //监听位置变化
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            SpUtil.setString(MainActivity.this, GlobalConstants.PROVINCE, location.getProvince());
            SpUtil.setString(MainActivity.this, GlobalConstants.CITY, location.getCity());
            SpUtil.setString(MainActivity.this, GlobalConstants.DISTRICT, location.getDistrict());
//            StringBuilder currentPosition = new StringBuilder();
//            currentPosition.append("省：").append(location.getProvince()).append("\n");
//            currentPosition.append("市：").append(location.getCity()).append("\n");
//            currentPosition.append("区：").append(location.getDistrict()).append("\n");
//            Log.d(TAG, currentPosition.toString());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能精确提供天气预报", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    /**
     * 初始化碎片
     */
    private void initFragment() {
        mSupportFragmentManager =  getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                mSupportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_leftMenu,
                new LeftMenuFragment(), GlobalConstants.TAG_LEFT_MENU);
        fragmentTransaction.replace(R.id.fl_content,
                new ContentFragment(), GlobalConstants.TAG_CONTENT);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        MapView mMapView = (MapView) findViewById(R.id.bmapView);
        System.out.println("onDestroy mMapView : " + mMapView);
        if(mMapView != null) {
            mMapView.onDestroy();
        }
        if(mLocationClient != null){
            mLocationClient.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        MapView mMapView = (MapView) findViewById(R.id.bmapView);
        System.out.println("onResume mMapView : " + mMapView);
        if(mMapView != null) {
            mMapView.setVisibility(View.VISIBLE);
            mMapView.onResume();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        MapView mMapView = (MapView) findViewById(R.id.bmapView);
        System.out.println("onPause mMapView : " + mMapView);
        if(mMapView != null) {
            mMapView.setVisibility(View.INVISIBLE);
            mMapView.onPause();
        }
    }
}
