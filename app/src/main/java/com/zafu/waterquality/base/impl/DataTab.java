package com.zafu.waterquality.base.impl;

import android.app.Activity;
import android.view.View;

import com.zafu.waterquality.R;
import com.zafu.waterquality.base.BasePager;

/**
 * Created by mengxin on 16-10-2.
 */
public class DataTab extends BasePager {
    public DataTab(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tvTitle.setText("水质检测");
        ibMenu.setVisibility(View.INVISIBLE);
        View view = View.inflate(mActivity, R.layout.view_data_tab, null);
        flContent.addView(view);
    }
}
