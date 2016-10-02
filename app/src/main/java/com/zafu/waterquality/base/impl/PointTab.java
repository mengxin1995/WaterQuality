package com.zafu.waterquality.base.impl;

import android.app.Activity;
import android.view.View;

import com.zafu.waterquality.base.BasePager;

/**
 * Created by mengxin on 16-10-2.
 */
public class PointTab extends BasePager {
    public PointTab(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tvTitle.setText("站点信息");
        ibMenu.setVisibility(View.INVISIBLE);
    }
}
