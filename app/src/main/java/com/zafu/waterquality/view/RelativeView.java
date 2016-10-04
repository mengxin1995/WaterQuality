package com.zafu.waterquality.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zafu.waterquality.R;

/**
 * Created by mengxin on 16-10-4.
 */
public class RelativeView extends View{

    private String str1;
    private String str2;

    private Paint backPaint ;
    private Paint linePaint ;
    private Paint textpaint ;

    public RelativeView(Context context) {
        super(context);
        initPaint();
    }

    public RelativeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.relativeView);
        str1 = ta.getString(R.styleable.relativeView_attr1);
        str2 = ta.getString(R.styleable.relativeView_attr2);
        ta.recycle();
    }
    public RelativeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        textpaint = new Paint() ;
        backPaint = new Paint() ;

        int color = Color.rgb(255,255,255) ;
        backPaint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        int viewWidth = getWidth() ;
        int viewHeight = getHeight() ;
        ////////画背景
        canvas.drawRect((float)(0), 0, (float) (viewWidth), viewHeight,backPaint);
        //////////画线
        int colorx = Color.rgb(110,112,120) ;
        linePaint.setColor(colorx);
        linePaint.setAlpha(0x60);
        canvas.drawLine((float)(0), (float) (viewHeight*0.995), (float)(viewWidth), (float) (viewHeight*0.995), linePaint);
        ////////////写字
        textpaint.setColor(Color.BLACK);
        textpaint.setTextSize(viewWidth/20) ;
        ///////加粗
        textpaint.setTypeface(Typeface.DEFAULT_BOLD) ;
        canvas.drawText(str1, (float)(viewWidth*0.03), (float) (viewHeight*0.55), textpaint);
        textpaint.setTypeface(Typeface.DEFAULT) ;
        textpaint.setTextSize(viewWidth/22) ;
        canvas.drawText(str2, (float)(viewWidth-viewWidth/22*(str2.length()+1)), (float) (viewHeight*0.55), textpaint);
        //canvas.drawText("非常规参数-氨氮量", (float)(viewWidth*0.05), (float) (viewHeight*0.071), paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int colorx = Color.rgb(110,112,120) ;
                backPaint.setColor(colorx);
                backPaint.setAlpha(0x60);

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                backPaint.setColor(Color.WHITE);
                invalidate();
                break;
            default:
                break;
        }
        return true ;
    }
}
