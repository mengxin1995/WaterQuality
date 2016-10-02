package com.zafu.waterquality.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zafu.waterquality.R;
import com.zafu.waterquality.activity.MainActivity;

/**
 * Created by mengxin on 16-10-1.
 */
public class BasePager {

    public final Activity mActivity;
    public final View mBaseView;
    public ImageButton ibMenu;
    public TextView tvTitle;
    public FrameLayout flContent;

    public BasePager(Activity activity) {
        mActivity = activity;
        mBaseView = initView();
    }

    private View initView() {
        View view = View.inflate(mActivity, R.layout.base_pager, null);
        ibMenu = (ImageButton) view.findViewById(R.id.ib_menu);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        flContent = (FrameLayout) view.findViewById(R.id.fl_content);

        ibMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        return view;
    }

    private void toggle() {
        MainActivity mainUI = (MainActivity) this.mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();
    }

    /**
     * 初始化数据
     */
    public void initData() {

    }
}
