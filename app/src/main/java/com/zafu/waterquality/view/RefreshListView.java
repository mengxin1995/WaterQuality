package com.zafu.waterquality.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zafu.waterquality.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mengxin on 16-11-1.
 */
public class RefreshListView extends ListView {

    /**
     * 下拉刷新状态
     */
    private static final int DOWN_STATE = 1;
    /**
     * 松开刷新
     */
    private static final int UN_DOWN_STATE = 2;
    /**
     * 正在刷新
     */
    private static final int DOING_STATE = 3;
    private static final String TAG = "RefreshListView";
    private ImageView mIvRefresh;
    private ProgressBar mPbRefreshHeader;
    private TextView mTvRefresh;
    private TextView mTvDate;
    private View mHeaderView;
    private int mMeasuredHeight;
    private RotateAnimation mRotateAnimationUP;
    private RotateAnimation mRotateAnimationDown;
    private float startY = -1;
    private float moveY;
    private float dis;
    private int mRefreshState = DOWN_STATE;

    public RefreshListView(Context context) {
        super(context);
        initHeaderView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
    }

    /**
     *  初始化头布局
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.refresh_header_listview, null);
        this.addHeaderView(mHeaderView);
        mIvRefresh = (ImageView) mHeaderView.findViewById(R.id.iv_refresh);
        mPbRefreshHeader = (ProgressBar) mHeaderView.findViewById(R.id.pb_refresh_header);
        mTvRefresh = (TextView) mHeaderView.findViewById(R.id.tv_refresh);
        mTvDate = (TextView) mHeaderView.findViewById(R.id.tv_date);

        mHeaderView.measure(0, 0);
        mMeasuredHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mMeasuredHeight, 0, 0);

        initAnim();
        initCurrentTime();
    }

    /**
     *  设置当前时间
     */
    private void initCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String CurrentTime = format.format(new Date());
        mTvDate.setText(CurrentTime);
    }

    /**
     *  初始化箭头动画
     */
    private void initAnim() {
        mRotateAnimationUP = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimationUP.setDuration(200);
        mRotateAnimationUP.setFillAfter(true);

        mRotateAnimationDown = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimationDown.setDuration(200);
        mRotateAnimationDown.setFillAfter(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                if(startY == -1){
                    startY = getY();
                }
                moveY = ev.getY();
                dis = moveY - startY;

                if(mRefreshState == DOING_STATE){
                    break;
                }

                int firstVisiblePosition = getFirstVisiblePosition();

                if(firstVisiblePosition == 0 && dis > 0){
                    int padding = (int) (-mMeasuredHeight + dis);
                    mHeaderView.setPadding(0, (int) (-mMeasuredHeight + dis), 0, 0);

                    if(padding >= 0 && mRefreshState != UN_DOWN_STATE){
                        stateChange(UN_DOWN_STATE);
                        mRefreshState = UN_DOWN_STATE;
                    }else if(padding < 0 && mRefreshState != DOWN_STATE){
                        stateChange(DOWN_STATE);
                        mRefreshState = DOWN_STATE;
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;

                if(mRefreshState == DOWN_STATE){
                    mHeaderView.setPadding(0, -mMeasuredHeight, 0, 0);
                }else if(mRefreshState == UN_DOWN_STATE){
                    mRefreshState = DOING_STATE;
                    stateChange(DOING_STATE);
                    mHeaderView.setPadding(0, 0, 0, 0);
                    if(mListener != null){
                        mListener.onRefresh();
                    }
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void refreshComplete(boolean success){
        stateChange(DOWN_STATE);
        mRefreshState = DOWN_STATE;
        mHeaderView.setPadding(0, -mMeasuredHeight, 0, 0);
        if(success){
            initCurrentTime();
        }
    }

    private OnRefreshListener mListener;

    public void setOnRefreshListener(OnRefreshListener listener){
        mListener = listener;
    }
    /**
     * 刷新回调，完成后调用refreshComplete完成初始化
     */
    public interface OnRefreshListener{
        public void onRefresh();
    }

    private void stateChange(int state) {
        switch(state){
            case DOWN_STATE:
                //下拉刷新
                mPbRefreshHeader.setVisibility(View.INVISIBLE);
                mIvRefresh.setVisibility(View.VISIBLE);
                mTvRefresh.setText("下拉刷新");
                mIvRefresh.startAnimation(mRotateAnimationDown);
                break;

            case UN_DOWN_STATE:
                //松开刷新
                mPbRefreshHeader.setVisibility(View.INVISIBLE);
                mIvRefresh.setVisibility(View.VISIBLE);
                mTvRefresh.setText("松开刷新");
                mIvRefresh.startAnimation(mRotateAnimationUP);

                break;

            case DOING_STATE:
                //正在刷新刷新
                mIvRefresh.clearAnimation();
                mTvRefresh.setText("正在刷新");
                mPbRefreshHeader.setVisibility(View.VISIBLE);
                mIvRefresh.setVisibility(View.INVISIBLE);
                break;

            default:
                break;
        }
    }
}
