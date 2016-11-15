package com.zafu.waterquality.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.mapapi.map.MapView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zafu.waterquality.R;
import com.zafu.waterquality.fragment.ContentFragment;
import com.zafu.waterquality.fragment.LeftMenuFragment;
import com.zafu.waterquality.global.GlobalConstants;
import com.zafu.waterquality.utils.LogUtil;


public class MainActivity extends SlidingFragmentActivity {

    private static final int REFRESH_COMPLETE = 1;
    private SlidingMenu mSlidingMenu;
    private FragmentManager mSupportFragmentManager;

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

        setContentView(R.layout.activity_main);
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
