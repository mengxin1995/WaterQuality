package com.zafu.waterquality.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.zafu.waterquality.R;
import com.zafu.waterquality.utils.LogUtil;

/**
 * Created by mengxin on 16-10-2.
 */
public class DataDescription extends View {

    private int index;
    private Paint mBgPaint;
    private Paint mLinePaint;
    private Paint mLevelBgPaint;
    private Paint mTextPaint;
    private String mTitle;
    private String[] contents = new String[]{"", "", "", ""};

    public DataDescription(Context context) {
        super(context);
        initPaint();
    }

    public DataDescription(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DataDescription);
        mTitle = ta.getString(R.styleable.DataDescription_title);
        LogUtil.getInstance().debug(mTitle);
        contents[0] = ta.getString(R.styleable.DataDescription_content1);
        contents[1] = ta.getString(R.styleable.DataDescription_content2);
        contents[2] = ta.getString(R.styleable.DataDescription_content3);
        contents[3] = ta.getString(R.styleable.DataDescription_content4);
        index = ta.getInteger(R.styleable.DataDescription_select, 0) ;
        ta.recycle();
        LogUtil.getInstance().debug(contents[0]);
    }

    public DataDescription(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mBgPaint = new Paint();
        mLinePaint = new Paint();
        mLevelBgPaint = new Paint();
        mTextPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWidth = getWidth() ;
        int viewHeight = getHeight() ;
        ////////画背景
        mBgPaint.setColor(Color.WHITE);
        canvas.drawRect((float)(0), 0, (float) (viewWidth), viewHeight,mBgPaint);
        //////////画线
        int colorx = Color.rgb(110,112,120) ;
        mLinePaint.setColor(colorx);
        mLinePaint.setAlpha(0x60);
        canvas.drawLine((float)(0), (float) (viewHeight*0.995), (float)(viewWidth), (float) (viewHeight*0.995), mLinePaint);

        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(viewWidth/25) ;
        ///////加粗
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD) ;
        float halfLen = (float) (mTitle.length()*1.0/2) ;
        float elemLen = (float) (viewWidth*1.0/25) ;
        /////////小背景
        selectBack() ;
        canvas.drawRect((float)(viewWidth*0.15-2.2*elemLen), (float) (viewHeight*0.15-elemLen*1.5),
                (float)(viewWidth*0.15+2.2*elemLen), (float) (viewHeight*0.15+elemLen*0.5),mLevelBgPaint);
        canvas.drawText(mTitle, (float)(viewWidth*0.15-halfLen*elemLen),
                (float) (viewHeight*0.15), mTextPaint);
        canvas.drawLine((float)(viewWidth*0.15), 0, (float) (viewWidth*0.15), viewHeight, mTextPaint);
        //canvas.drawCircle((float)(viewWidth*0.2-halfLen*elemLen), (float) (viewHeight*0.15), 4, backPaint);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(viewWidth/24) ;
        canvas.drawText(contents[0], (float)(viewWidth*0.15+2.2*elemLen), (float) (viewHeight*0.20+elemLen*2), mTextPaint);
        canvas.drawText(contents[1], (float)(viewWidth*0.15+2.2*elemLen), (float) (viewHeight*0.20+elemLen*4), mTextPaint);
        canvas.drawText(contents[2], (float)(viewWidth*0.15+2.2*elemLen), (float) (viewHeight*0.20+elemLen*6), mTextPaint);
        canvas.drawText(contents[3], (float)(viewWidth*0.15+2.2*elemLen), (float) (viewHeight*0.20+elemLen*8), mTextPaint);
    }

    private void selectBack() {
        switch (index) {
            case 1:
                mLevelBgPaint.setColor(Color.rgb(0, 204, 0)) ;
                break;
            case 2:
                mLevelBgPaint.setColor(Color.rgb(241,196,173)) ;
                break;
            case 3:
                mLevelBgPaint.setColor(Color.rgb(255, 90, 11)) ;
                break;
            case 4:
                mLevelBgPaint.setColor(Color.RED) ;
                break;
            case 5:
                mLevelBgPaint.setColor(Color.rgb(128, 0, 0)) ;
                break;
            default:
                break;
        }
    }
}
