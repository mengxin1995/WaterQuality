package com.zafu.waterquality.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by mengxin on 16-10-13.
 */
public class LvSiteDataHead extends View {
    private Paint mPaint;

    public LvSiteDataHead(Context context) {
        super(context);
        init();
    }

    public LvSiteDataHead(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LvSiteDataHead(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        //画背景
        int color = Color.rgb(110, 112, 120);
        mPaint.setColor(color);
        mPaint.setAlpha(0x60);
        canvas.drawRect((float) (width * 0.05), 0, (float) (width * 0.95), height, mPaint);

        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(width / 22);
        canvas.drawText("站点数据", (float) (width * 0.05), 60, mPaint);
        canvas.drawLine((float) (width * 0.05), 80, (float) (width * 0.92), 80, mPaint);
    }
}
