package com.zafu.waterquality.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zafu.waterquality.domain.MyPoint;
import com.zafu.waterquality.global.GlobalConstants;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mengxin on 16-10-9.
 */
public class UnGeneralParameter extends View {
    ArrayList<MyPoint> dataPoints = new ArrayList<MyPoint>() ;
    ArrayList<Point> dataChangePoint = new ArrayList<Point>() ;
    private Paint paint;
    private Paint textPaint ;
    private Handler handler ;
    private Timer mTimer ;
    private TimerTask mTimerTask ;
    private int viewWidth ;
    private int viewHeight ;
    public UnGeneralParameter(Context context) {
        super(context);
    }

    public UnGeneralParameter(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        //Log.i("xValue", "waiWidth"+"-"+getWidth()) ;
        init() ;
    }

    public UnGeneralParameter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //定时去调用 DoTask
    private void init() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                DoTask() ;
            }

        };
        //开始一个定时任务
        mTimer.schedule(mTimerTask, 1000,5*60*1000);
    }

    @Override
    protected void onDraw(final Canvas canvas){

        ///画字和横线
        viewWidth = getWidth() ;
        viewHeight = getHeight() ;

        Log.i("ViewHei", viewHeight+"UnGeneralParameter") ;

        //int viewHeight = getHeight() ;
        //Log.i("viewWidValue", viewWidth+"-"+getWidth()) ;
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                //changePoint() ;
                //canvas.drawText(str.toString(), 340, 440, paint);
                paint.setStrokeWidth((float) 6.0);
                invalidate() ;
                Log.i("XValue", "VIEWoneINit") ;
            }
        };

        paint.setColor(Color.WHITE) ;
        paint.setTextSize(viewWidth/22) ;
        canvas.drawText("非常规参数-氨氮量", (float)(viewWidth*0.05), (float) (viewHeight*0.071), paint);
        canvas.drawLine((float)(viewWidth*0.05), (float) (viewHeight*0.095), (float)(viewWidth*0.92), (float) (viewHeight*0.095), paint);
        //paint.setAntiAlias(false);
        //画透明的背景

        int color = Color.rgb(110,112,120) ;
        //canvas.drawColor(Color.WHITE) ;
        paint.setColor(color);
        paint.setAlpha(0x60);
        canvas.drawRect((float)(viewWidth*0.05), 0, (float) (getWidth()*0.95), getHeight(),paint);
        ///（120，720）是坐标原点，Y上差量为100，X140
        //画Y坐标
        paint.setColor(Color.rgb(64, 0, 0)) ;
        textPaint.setTextSize(viewWidth/25) ;
        textPaint.setColor(Color.WHITE) ;
        canvas.drawText("36", (float)(viewWidth*0.06), (float) (viewHeight*0.142), textPaint);
        canvas.drawRect((float)(viewWidth*0.11), (float) (viewHeight*0.142), (float)(viewWidth*0.12), (float) (viewHeight*0.262),paint);
        //-----------------------------------
        paint.setColor(Color.rgb(128, 0, 0)) ;
        canvas.drawText("30", (float)(viewWidth*0.06), (float) (viewHeight*0.262), textPaint);
        canvas.drawRect((float)(viewWidth*0.11), (float) (viewHeight*0.262), (float)(viewWidth*0.12), (float) (viewHeight*0.38),paint);
        //-----------------------------------
        paint.setColor(Color.RED) ;
        canvas.drawText("24", (float)(viewWidth*0.06), (float) (viewHeight*0.38), textPaint);
        canvas.drawRect((float)(viewWidth*0.11), (float) (viewHeight*0.38), (float)(viewWidth*0.12), (float) (viewHeight*0.5),paint);
        //-----------------------------------
        paint.setColor(Color.rgb(255, 90, 11)) ;
        canvas.drawText("18", (float)(viewWidth*0.06), (float) (viewHeight*0.5), textPaint);
        canvas.drawRect((float)(viewWidth*0.11), (float) (viewHeight*0.5), (float)(viewWidth*0.12), (float) (viewHeight*0.62),paint);
        //-----------------------------------
        paint.setColor(Color.YELLOW) ;
        canvas.drawText("12", (float)(viewWidth*0.06), (float) (viewHeight*0.62), textPaint);
        canvas.drawRect((float)(viewWidth*0.11), (float) (viewHeight*0.62), (float)(viewWidth*0.12), (float) (viewHeight*0.74),paint);
        //-----------------------------------
        paint.setColor(Color.rgb(0, 204, 0)) ;
        canvas.drawText("6", (float)(viewWidth*0.07), (float) (viewHeight*0.74), textPaint);
        canvas.drawRect((float)(viewWidth*0.11), (float) (viewHeight*0.74), (float)(viewWidth*0.12), (float) (viewHeight*0.86),paint);
        //-----------------------------------
        canvas.drawText("0", (float)(viewWidth*0.07), (float) (viewHeight*0.86), textPaint);
        ///画Y轴的矩形边框

        paint.setColor(color) ;
        paint.setAlpha(35) ;
        canvas.drawRect((float)(viewWidth*0.12),  (float) (viewHeight*0.142), (float)(viewWidth*0.91),  (float) (viewHeight*0.262),paint);
        canvas.drawRect((float)(viewWidth*0.12),  (float) (viewHeight*0.38),(float)(viewWidth*0.91),  (float) (viewHeight*0.5),paint);
        canvas.drawRect((float)(viewWidth*0.12),  (float) (viewHeight*0.62), (float)(viewWidth*0.91),  (float) (viewHeight*0.74),paint);
        ///画X轴
        paint.setColor(Color.WHITE) ;
        canvas.drawLine((float)(viewWidth*0.11), (float) (viewHeight*0.86), (float)(viewWidth*0.91), (float) (viewHeight*0.86), paint);
        canvas.drawText("00时", (float)(viewWidth*0.09), (float) (viewHeight*0.934), textPaint);
        canvas.drawText("05时", (float)(viewWidth*0.25),  (float) (viewHeight*0.934), textPaint);
        canvas.drawText("10时", (float)(viewWidth*0.41),  (float) (viewHeight*0.934), textPaint);
        canvas.drawText("15时", (float)(viewWidth*0.56),  (float) (viewHeight*0.934), textPaint);
        canvas.drawText("20时", (float)(viewWidth*0.72),  (float) (viewHeight*0.934), textPaint);
        //画X轴矩形边框
        paint.setAlpha(35) ;
        canvas.drawLine((float)(viewWidth*0.27), (float) (viewHeight*0.86), (float)(viewWidth*0.27), (float) (viewHeight*0.142),paint);
        canvas.drawLine((float)(viewWidth*0.43), (float) (viewHeight*0.86), (float)(viewWidth*0.43), (float) (viewHeight*0.142),paint);
        canvas.drawLine((float)(viewWidth*0.58), (float) (viewHeight*0.86), (float)(viewWidth*0.58), (float) (viewHeight*0.142),paint);
        canvas.drawLine((float)(viewWidth*0.74), (float) (viewHeight*0.86), (float)(viewWidth*0.74), (float) (viewHeight*0.142),paint);
        //canvas.drawLine((float)(viewWidth*0.91), 720, (float)(viewWidth*0.91), 120,paint);
        //画数据曲线
        color = Color.rgb(0, 255, 255) ;
        paint.setColor(color) ;
        paint.setStrokeWidth((float) 5.0);

        for(int i=0 ;i<dataChangePoint.size()-1 ;i++){
            canvas.drawLine(dataChangePoint.get(i).x, dataChangePoint.get(i).y,
                    dataChangePoint.get(i+1).x, dataChangePoint.get(i+1).y,paint) ; //画线
        }

    }
    ///得到的点根据XY的长度，将值转成对应的XY值
    public void changePoint(){
    }
    ///去服务器拿数据
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
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inStream,"GB2312")) ;

                    String line = null;
                    int count = 0 ;
                    dataPoints.clear() ;
                    dataChangePoint.clear() ;
                    double elemX = ( viewWidth*0.8 )*1.0 / 288 ;
                    Log.i("xValue", elemX+"") ;
                    double everY = viewHeight*0.718/36 ;
                    Log.i("asd", everY+"") ;
                    while((line=reader.readLine())!=null && count <24*12){
                        if(count == 0)
                        {
                            count++;
                            continue;
                        }
                        System.out.println(line);
                        String item[] = line.split(",");

                        double elemY = Double.parseDouble(item[2]) ;

                        int x = (int) (viewWidth*0.11 + elemX*count);
                        //Log.i("xValue", x+"-"+count+"-"+getWidth()) ;
                        int y = (int) (	viewHeight*0.857 - elemY*1000*everY ) ;
                        Point point = new Point(x,y) ;
                        dataChangePoint.add(point) ;

                        count++ ;
                    }
                    handler.sendEmptyMessage(0) ;
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace() ;
                }
            }
        }).start() ;
    }
}
