package com.zafu.waterquality.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.zafu.waterquality.R;
import com.zafu.waterquality.global.GlobalConstants;
import com.zafu.waterquality.utils.SpUtil;


public class SplashActivity extends MyActivity {

    private AnimationSet mAnimationSet;
    private RelativeLayout rl_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initAnimation();
        initUI();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
//        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        rl_root = $(R.id.rl_root);
        this.rl_root.setAnimation(mAnimationSet);
        mAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            private Intent intent;
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boolean is_first_enter = SpUtil.getBoolean(getApplicationContext(), GlobalConstants.IS_FIRST_ENTER, true);
                if(is_first_enter){
                    intent = new Intent(getApplicationContext(), GuideActivity.class);
                }else {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        //旋转
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
//        rotateAnimation.setDuration(1000);
        rotateAnimation.setDuration(1);
        rotateAnimation.setFillAfter(true);

        //缩放
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
//        scaleAnimation.setDuration(1000);
        scaleAnimation.setDuration(1);
        scaleAnimation.setFillAfter(true);

        //渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
//        alphaAnimation.setDuration(2000);
        alphaAnimation.setDuration(2);
        alphaAnimation.setFillAfter(true);

        mAnimationSet = new AnimationSet(true);
        mAnimationSet.addAnimation(rotateAnimation);
        mAnimationSet.addAnimation(scaleAnimation);
        mAnimationSet.addAnimation(alphaAnimation);
    }
}
