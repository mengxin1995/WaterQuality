package com.zafu.waterquality.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zafu.waterquality.R;
import com.zafu.waterquality.activity.MainActivity;
import com.zafu.waterquality.base.BasePager;
import com.zafu.waterquality.base.impl.DataTab;
import com.zafu.waterquality.base.impl.FormTab;
import com.zafu.waterquality.base.impl.PointTab;
import com.zafu.waterquality.base.impl.Relative;
import com.zafu.waterquality.utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by mengxin on 16-10-1.
 */
public class ContentFragment extends BaseFragment{

    private ArrayList<BasePager> mPagers;
    private ViewPager vpContent;
    private RadioGroup radioGroup;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        vpContent = (ViewPager) view.findViewById(R.id.vp_content);
        radioGroup = (RadioGroup) view.findViewById(R.id.rg_tab);
        return view;
    }

    @Override
    protected void initData() {
        mPagers = new ArrayList<>();
        mPagers.add(new DataTab(mActivity));
        mPagers.add(new PointTab(mActivity));
        mPagers.add(new FormTab(mActivity));
        mPagers.add(new Relative(mActivity));

        setSlidingMenuEnable(false);
        //先加载一页数据
        mPagers.get(0).initData();
        vpContent.setAdapter(new VpContent());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.tab_data :
                        vpContent.setCurrentItem(0, false);
                        break;
                    case R.id.tab_point :
                        vpContent.setCurrentItem(1, false);
                        break;
                    case R.id.tab_form :
                        vpContent.setCurrentItem(2, false);
                        break;
                    case R.id.tab_relative :
                        vpContent.setCurrentItem(3, false);
                        break;
                    default:
                        break;
                }
            }
        });

        vpContent.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePager pager = mPagers.get(position);
                pager.initData();
                LogUtil.getInstance().debug("当前初始化页面" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 开启或者关闭侧边栏
     * @param enable
     */
    private void setSlidingMenuEnable(boolean enable) {
        MainActivity mainUI = (MainActivity) this.mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        if(enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else{
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    private class VpContent extends PagerAdapter {
        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager Pager = mPagers.get(position);
            View view = Pager.mBaseView;
            container.addView(view);
            LogUtil.getInstance().debug("加载viewpager的页码"+position);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
