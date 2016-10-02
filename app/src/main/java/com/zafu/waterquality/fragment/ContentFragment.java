package com.zafu.waterquality.fragment;

import android.view.View;

import com.zafu.waterquality.R;
import com.zafu.waterquality.base.BasePager;
import com.zafu.waterquality.base.impl.DataTab;
import com.zafu.waterquality.base.impl.FormTab;
import com.zafu.waterquality.base.impl.PointTab;
import com.zafu.waterquality.base.impl.Relative;

import java.util.ArrayList;

/**
 * Created by mengxin on 16-10-1.
 */
public class ContentFragment extends BaseFragment{

    private ArrayList<BasePager> mPagers;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        return view;
    }

    @Override
    protected void initData() {
        mPagers = new ArrayList<>();
        mPagers.add(new DataTab(mActivity));
        mPagers.add(new PointTab(mActivity));
        mPagers.add(new FormTab(mActivity));
        mPagers.add(new Relative(mActivity));
    }
}
