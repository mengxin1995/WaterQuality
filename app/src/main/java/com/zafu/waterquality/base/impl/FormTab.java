package com.zafu.waterquality.base.impl;

import android.app.Activity;
import android.view.View;

import com.zafu.waterquality.base.BasePager;

/**
 * Created by mengxin on 16-10-2.
 */
public class FormTab extends BasePager {
    public FormTab(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tvTitle.setText("水质数据说明");
        ibMenu.setVisibility(View.INVISIBLE);
    }
}
