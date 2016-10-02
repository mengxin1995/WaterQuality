package com.zafu.waterquality.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zafu.waterquality.R;
import com.zafu.waterquality.fragment.ContentFragment;
import com.zafu.waterquality.fragment.LeftMenuFragment;
import com.zafu.waterquality.global.GlobalConstants;
import com.zafu.waterquality.utils.LogUtil;


public class MainActivity extends SlidingFragmentActivity {

    private SlidingMenu mSlidingMenu;
    private FragmentManager supportFragmentManager;

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
        FragmentManager supportFragmentManager =  getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_leftMenu,
                new LeftMenuFragment(), GlobalConstants.TAG_LEFT_MENU);
        fragmentTransaction.replace(R.id.fl_content,
                new ContentFragment(), GlobalConstants.TAG_CONTENT);
        fragmentTransaction.commit();
    }
}
