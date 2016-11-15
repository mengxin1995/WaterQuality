package com.zafu.waterquality.base.impl;

import android.app.Activity;
import android.view.View;

import com.zafu.waterquality.R;
import com.zafu.waterquality.base.BasePager;

/**
 * Created by mengxin on 16-10-2.
 */
public class Relative extends BasePager {
    public Relative(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("关于我们");
        tvTitle.setText("关于我们");
        ibMenu.setVisibility(View.INVISIBLE);
        View view = View.inflate(mActivity, R.layout.view_relative, null);
        flContent.addView(view);
    }
}
