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

import com.zafu.waterquality.domain.DataPoint;
import com.zafu.waterquality.global.GlobalConstants;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mengxin on 16-10-9.
 */
public class SiteData extends View{

    private Handler handler  ;
    private String strUrl = GlobalConstants.SITE_URL;
    private Paint warPaint ;
    private Paint paint ;
    private int pointCount = 1 ;
    private ArrayList<DataPoint> points = new ArrayList<DataPoint>() ;

    public SiteData(Context context) {
        super(context);
    }

    public SiteData(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        warPaint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        warPaint.setColor(Color.RED) ;
        //warPaint.setTextSize(width/24) ;

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                requestLayout();//////View.requestLayout() 请求重新布局,重新调用：onMeasure，onLayout，onDraw；
            }
        };
        DoTask() ;
    }

    public SiteData(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onDraw( Canvas canvas) {
        // TODO Auto-generated method stub
        int width = getWidth() ;
        int height = getHeight() ;
        //画背景
        int color = Color.rgb(110,112,120) ;
        paint.setColor(color);
        paint.setAlpha(0x60);
        canvas.drawRect((float)(width*0.05), 0, (float) (width*0.95), height,paint);

        paint.setColor(Color.WHITE) ;
        paint.setTextSize(width/22) ;
        canvas.drawText("站点数据", (float)(width*0.05), 60, paint);
        canvas.drawLine((float)(width*0.05), 80, (float)(width*0.92), 80, paint);

        paint.setTextSize(width/25) ;
        for (int i = 0; i < points.size(); i++) {

            DataPoint elem = points.get(i);
            canvas.drawText("An", (float)(width*0.23), i*200+140, paint);
            canvas.drawText("Oxv", (float)(width*0.38), i*200+140, paint);
            canvas.drawText("Ph", (float)(width*0.53), i*200+140, paint);
            canvas.drawText("Temp", (float)(width*0.68), i*200+140, paint);
            canvas.drawText("Turb", (float)(width*0.83), i*200+140, paint);


            canvas.drawText(elem.getPonitName()+"", (float)(width*0.08), i*200+140+80, paint);
            canvas.drawText(elem.getnValue()+"", (float)(width*0.23), i*200+140+80, paint);
            canvas.drawText(elem.getOxgasValue()+"", (float)(width*0.38), i*200+140+80, paint);
            canvas.drawText(elem.getPhValue()+"", (float)(width*0.53), i*200+140+80, paint);
            canvas.drawText(elem.getTempVale()+"", (float)(width*0.68), i*200+140+80, paint);
            canvas.drawText(elem.getZhouValue()+"", (float)(width*0.83), i*200+140+80, paint);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,pointCount*200) ;
    }

    public void DoTask(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    URL url =new URL(strUrl) ;
                    HttpURLConnection connection =(HttpURLConnection) url.openConnection() ;
                    //服务器的常用的两个方法,post,get
                    connection.setRequestMethod("GET") ;
                    //链接超时,读取超时，根据自己情况定
                    connection.setConnectTimeout(8000) ;
                    connection.setReadTimeout(8000) ;
                    //下面是读取，可以用connection带的方法，获取输入流
                    InputStream inStream = connection.getInputStream() ;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inStream,"GB2312")) ;
                    points.clear() ;
                    pointCount = 1 ;
                    String line = null;
                    int count = 0;
                    while((line=reader.readLine())!=null){
                        if(count == 0)
                        {
                            count++;
                            continue;
                        }
                        String item[] = line.split(",");
                        DataPoint elem = new DataPoint() ;
                        elem.setPonitName( item[0]) ;
                        Log.i("name", item[0]) ;
                        elem.setOxgasValue(Double.parseDouble(item[1])) ;
                        elem.setPhValue(Double.parseDouble(item[3])) ;
                        elem.setTempVale(Double.parseDouble(item[4])) ;
                        elem.setnValue(Double.parseDouble(item[2])*1000) ;
                        elem.setZhouValue(Double.parseDouble(item[5])) ;
                        pointCount++ ;
                        points.add(elem) ;
                    }
						/*Message msg =new Message() ;
						msg.what = index ;
						handler.sendMessage(msg) ;	*/
                    handler.sendEmptyMessage(0);
                    connection.disconnect();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace() ;
                }
            }
        }).start() ;
    }
}
