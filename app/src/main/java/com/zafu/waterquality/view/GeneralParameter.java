package com.zafu.waterquality.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.utils.TimeUtils;
import com.zafu.waterquality.RxjavaRetrofit.entity.WaterData;
import com.zafu.waterquality.RxjavaRetrofit.http.HttpMethods;
import com.zafu.waterquality.RxjavaRetrofit.subscribers.SimpleHttpSubscriber;
import com.zafu.waterquality.RxjavaRetrofit.subscribers.SubscriberOnNextListener;
import com.zafu.waterquality.global.Event;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mengxin on 16-10-9.
 */
public class GeneralParameter extends View{

    private static final String TAG = "GeneralParameter";
    private static final String DEFAULT_PATTERN = "yyyy:MM:dd";
    private Paint paint ;
    private Paint greenLinePaint ;
    private Paint yellowLinePaint ;
    private int viewWidth ;
    private int viewHeight ;
    private Timer mTimer ;
    private TimerTask mTimerTask ;
    private DecimalFormat df = new DecimalFormat("######0.0");

    private WaterData waterData = new WaterData();

    public GeneralParameter(Context context) {
        this(context, null);
    }

    public GeneralParameter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GeneralParameter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        greenLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        yellowLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        init() ;
        EventBus.getDefault().register(this);
    }


    public void init(){
        mTimer = new Timer() ;
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                DoTask() ;
            }
        };
        mTimer.schedule(mTimerTask, 0, 5*60*1000) ;
    }

    private void DoTask() {
        HttpMethods.getInstance().getWaterData(new SimpleHttpSubscriber<List<WaterData>>(new SubscriberOnNextListener<List<WaterData>>() {
            @Override
            public void onNext(List<WaterData> waterDatas) {
                waterData = waterDatas.get(waterDatas.size()-1);
                invalidate();
            }
        }), 0, TimeUtils.getNowTimeString(DEFAULT_PATTERN));
    }

    @Override
    protected void onDraw(Canvas canvas){
        //得到长宽
        viewWidth = getWidth() ;
        viewHeight = getHeight() ;
        //Log.i("ViewHei", viewHeight+"GeneralParameter") ;
        //画透明背景
        int color = Color.rgb(110,112,120);
        paint.setColor(color);
        paint.setAlpha(0x60);
        canvas.drawRect((float)(viewWidth*0.05), 0, (float) (viewWidth*0.95), viewHeight,paint);
        //写字划线
        paint.setColor(Color.WHITE) ;
        paint.setTextSize(viewWidth/22) ;
        canvas.drawText("常规参数", (float)(viewWidth*0.05), (float) (viewHeight*0.2), paint);
        paint.setTextSize(viewWidth/11) ;
        canvas.drawLine((float)(viewWidth*0.05), (float) (viewHeight*0.23), (float)(viewWidth*0.92), (float) (viewHeight*0.23), paint);
        paint.setTextSize(viewWidth/22) ;
        ////////浊度，ph值，电导率,溶解率，水温
        greenLinePaint.setColor(Color.GREEN) ;
        yellowLinePaint.setColor(Color.YELLOW) ;
        greenLinePaint.setStrokeWidth((float) 5.0);
        yellowLinePaint.setStrokeWidth((float) 5.0);
        //浊度
        canvas.drawText("水温", (float)(viewWidth*0.06), (float) (viewHeight*0.438), paint);
        canvas.drawText("" + waterData.getShuiWen(), (float)(viewWidth*0.26), (float) (viewHeight*0.438), paint);
        canvas.drawLine((float)(viewWidth*0.06), (float) (viewHeight*0.52), (float)(viewWidth*0.36),  (float) (viewHeight*0.52), yellowLinePaint);
        //PH值
        canvas.drawText("PH值", (float)(viewWidth*0.59),  (float) (viewHeight*0.438), paint);
        canvas.drawText("" + waterData.getPh(), (float)(viewWidth*0.79),  (float) (viewHeight*0.438), paint);
        canvas.drawLine((float)(viewWidth*0.59), (float) (viewHeight*0.52), (float)(viewWidth*0.89), (float) (viewHeight*0.52), greenLinePaint);
        //-------------
        //电导率
        canvas.drawText("电导率", (float)(viewWidth*0.06), (float) (viewHeight*0.735), paint);
        canvas.drawText("" + waterData.getDianDaoLv(), (float)(viewWidth*0.26), (float) (viewHeight*0.735), paint);
        canvas.drawLine((float)(viewWidth*0.06), (float) (viewHeight*0.85), (float)(viewWidth*0.36), (float) (viewHeight*0.85), yellowLinePaint);
        //溶解氧
        canvas.drawText("溶解氧", (float)(viewWidth*0.59), (float) (viewHeight*0.735), paint);
        canvas.drawText("" + waterData.getRongJieYang(), (float)(viewWidth*0.79), (float) (viewHeight*0.735), paint);
        canvas.drawLine((float)(viewWidth*0.59), (float) (viewHeight*0.85), (float)(viewWidth*0.89), (float) (viewHeight*0.85), greenLinePaint);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.RefreshEventGeneralParameter event) {
        DoTask();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }
}
