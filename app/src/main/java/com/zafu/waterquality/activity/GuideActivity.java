package com.zafu.waterquality.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zafu.waterquality.R;
import com.zafu.waterquality.global.GlobalConstants;
import com.zafu.waterquality.utils.SpUtil;

import java.util.ArrayList;

public class GuideActivity extends MyActivity {

    private ViewPager mVp_guide;
    private Button bt_guide;
    private LinearLayout ll_container;
    private ImageView iv_red_point;
    private ArrayList<ImageView> mImageViews;
    private int mImageIds[] = new int[]{R.drawable.guide1, R.drawable.guide2, R.drawable.guide3};
    private int mPointDis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);

        initUI();

        initData();

        initAdapter();
    }

    private void initAdapter() {
        mVp_guide.setAdapter(new GuideAdapter());
        mVp_guide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
                int dis = (int) (position * mPointDis + mPointDis * positionOffset);
                params.leftMargin = dis;
                iv_red_point.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                if(position == mImageIds.length - 1){
                    bt_guide.setVisibility(View.VISIBLE);
                }else{
                    bt_guide.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //得到两个圆点的距离
        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                iv_red_point.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mPointDis = ll_container.getChildAt(1).getLeft() -
                        ll_container.getChildAt(0).getLeft();
            }
        });
    }

    /**
     * viewpage适配器实现
     */
    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = mImageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void initData() {
        mImageViews = new ArrayList<ImageView>();
        for(int i=0; i<mImageIds.length; i++){
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(mImageIds[i]);
            mImageViews.add(imageView);

            //初始化圆点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.shape_point_grey);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            if(i > 0){
                params.leftMargin = 10;
            }
            point.setLayoutParams(params);
            ll_container.addView(point);
        }
    }

    private void initUI() {
        mVp_guide = $(R.id.vp_guide);
        bt_guide = $(R.id.bt_guide);
        ll_container = $(R.id.ll_container);
        iv_red_point = $(R.id.iv_red_point);
        bt_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtil.setBoolean(getApplicationContext(), GlobalConstants.IS_FIRST_ENTER, false);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
