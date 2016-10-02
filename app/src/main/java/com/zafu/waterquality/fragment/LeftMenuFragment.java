package com.zafu.waterquality.fragment;

import android.view.View;

import com.zafu.waterquality.R;

/**
 * Created by mengxin on 16-10-1.
 */
public class LeftMenuFragment extends BaseFragment {
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        return view;
    }

    @Override
    protected void initData() {

    }
}
