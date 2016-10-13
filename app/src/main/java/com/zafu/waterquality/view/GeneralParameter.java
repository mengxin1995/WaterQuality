package com.zafu.waterquality.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zafu.waterquality.global.GlobalConstants;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mengxin on 16-10-9.
 */
public class GeneralParameter extends View{

    private static final String TAG = "GeneralParameter";
    private Paint paint ;
    private Paint greenLinePaint ;
    private Paint yellowLinePaint ;
    private int viewWidth ;
    private int viewHeight ;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            //canvas.drawText(str.toString(), 340, 440, paint);
            //paint.setStrokeWidth((float) 6.0);
            invalidate() ;
        }
    };;
    private double phValue ;
    private double tempValue ;
    private double oxyGasValue ;
    private Timer mTimer ;
    private TimerTask mTimerTask ;
    private DecimalFormat df = new DecimalFormat("######0.0");

    public GeneralParameter(Context context) {
        super(context);
    }

    public GeneralParameter(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        greenLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        yellowLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        init() ;
    }

    public GeneralParameter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(){
        mTimer = new Timer() ;
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                DoTask() ;
            }
        };
        mTimer.schedule(mTimerTask, 0, 5*60*1000) ;
    }

    @Override
    protected void onDraw(Canvas canvas){
        //得到长宽
        viewWidth = getWidth() ;
        viewHeight = getHeight() ;
        Log.i("ViewHei", viewHeight+"GeneralParameter") ;
        //画透明背景
        int color = Color.rgb(110,112,120) ;
        paint.setColor(color);
        paint.setAlpha(0x60);
        canvas.drawRect((float)(viewWidth*0.05), 0, (float) (viewWidth*0.95), viewHeight,paint);
        //写字划线
        paint.setColor(Color.WHITE) ;
        paint.setTextSize(viewWidth/22) ;
        canvas.drawText("常规参数", (float)(viewWidth*0.05), (float) (viewHeight*0.071), paint);
        canvas.drawLine((float)(viewWidth*0.05), (float) (viewHeight*0.095), (float)(viewWidth*0.92), (float) (viewHeight*0.095), paint);
        ////////浊度，ph值，电导率,溶解率，水温
        greenLinePaint.setColor(Color.GREEN) ;
        yellowLinePaint.setColor(Color.YELLOW) ;
        greenLinePaint.setStrokeWidth((float) 5.0);
        yellowLinePaint.setStrokeWidth((float) 5.0);
        //浊度
        canvas.drawText("浊度", (float)(viewWidth*0.06), (float) (viewHeight*0.238), paint);
        canvas.drawText("5.0", (float)(viewWidth*0.26), (float) (viewHeight*0.238), paint);
        canvas.drawLine((float)(viewWidth*0.06), (float) (viewHeight*0.32), (float)(viewWidth*0.36),  (float) (viewHeight*0.32), yellowLinePaint);
        //PH值
        canvas.drawText("PH值", (float)(viewWidth*0.59),  (float) (viewHeight*0.238), paint);
        canvas.drawText(""+df.format( phValue), (float)(viewWidth*0.79),  (float) (viewHeight*0.238), paint);
        canvas.drawLine((float)(viewWidth*0.59), (float) (viewHeight*0.32), (float)(viewWidth*0.89), (float) (viewHeight*0.32), greenLinePaint);
        //-------------
        //电导率
        canvas.drawText("电导率", (float)(viewWidth*0.06), (float) (viewHeight*0.535), paint);
        canvas.drawText("0.02", (float)(viewWidth*0.26), (float) (viewHeight*0.535), paint);
        canvas.drawLine((float)(viewWidth*0.06), (float) (viewHeight*0.62), (float)(viewWidth*0.36), (float) (viewHeight*0.62), yellowLinePaint);
        //溶解氧
        canvas.drawText("溶解氧", (float)(viewWidth*0.59), (float) (viewHeight*0.535), paint);
        canvas.drawText(""+df.format( oxyGasValue), (float)(viewWidth*0.79), (float) (viewHeight*0.535), paint);
        canvas.drawLine((float)(viewWidth*0.59), (float) (viewHeight*0.62), (float)(viewWidth*0.89), (float) (viewHeight*0.62), greenLinePaint);
        //------------------
        //水温
        canvas.drawText("水温", (float)(viewWidth*0.06), (float) (viewHeight*0.785), paint);
        canvas.drawText(""+df.format( tempValue), (float)(viewWidth*0.79), (float) (viewHeight*0.785), paint);
        canvas.drawLine((float)(viewWidth*0.06), (float) (viewHeight*0.87), (float)(viewWidth*0.89), (float) (viewHeight*0.87), yellowLinePaint);
    }

    public void DoTask(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    URL url =new URL(GlobalConstants.SITE_URL) ;
                    HttpURLConnection connection =(HttpURLConnection) url.openConnection() ;
                    //服务器的常用的两个方法,post,get
                    connection.setRequestMethod("GET") ;
                    //链接超时,读取超时，根据自己情况定
                    connection.setConnectTimeout(8000) ;
                    connection.setReadTimeout(8000) ;
                    //下面是读取，可以用connection带的方法，获取输入流
                    InputStream inStream = connection.getInputStream() ;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inStream)) ;
                    Log.i(TAG, "我到了");
                    String line = "";
                    int count = 0 ;
                    double phCount =0 ,tempCount = 0 ,oxyGasCount = 0 ;
                    while((line=reader.readLine())!=null && count <= 24*12){
                        if(count == 0)
                        {
                            count++;
                            continue;
                        }
                        Log.i(TAG, line);
                        String item[] = line.split(",");
                        count++ ;
                        oxyGasCount += Double.parseDouble(item[1]) ;
                        phCount += Double.parseDouble(item[3]) ;
                        tempCount += Double.parseDouble(item[4]) ;
                    }
                    oxyGasValue = oxyGasCount/count ;
                    phValue = phCount/count ;
                    tempValue = tempCount/count ;
                    handler.sendEmptyMessage(0) ;
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace() ;
                }
            }
        }).start() ;
    }
}
