package com.zafu.waterquality.base.impl;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.zafu.waterquality.R;
import com.zafu.waterquality.base.BasePager;

/**
 * Created by mengxin on 16-10-2.
 */
public class PointTab extends BasePager {

    public int temp = 0;
    private BaiduMap baiduMap;
    private View pop;
    private TextView tv_title;

    public PointTab(Activity activity) {
        super(activity);
    }
    /**
     * 浙江农林大学东湖校区
     */
    private LatLng mZafu_donghu = new LatLng(30.2609970000,119.7355840000);
    public MapView mMapView = null;

    @Override
    public void initData() {
        System.out.println("站点信息");
        tvTitle.setText("站点信息(点击切换)");
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("我被点击了");
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setIcon(R.drawable.round);
                builder.setTitle("选择一个城市");
                //    指定下拉列表的显示数据
                final String[] cities = {"广州", "上海", "北京", "香港", "澳门"};
                //    设置一个下拉的列表选择项
                builder.setItems(cities, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(mActivity, "选择的城市为：" + cities[which], Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
        ibMenu.setVisibility(View.INVISIBLE);
        View view = View.inflate(mActivity, R.layout.view_point_tab, null);
        flContent.addView(view);

        mMapView = (MapView) view.findViewById(R.id.bmapView);
        baiduMap = mMapView.getMap();

        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setTrafficEnabled(false);

        //设置地图中心点为浙江农林大学
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(mZafu_donghu);
        baiduMap.animateMapStatus(mapStatusUpdate);
        mapStatusUpdate = MapStatusUpdateFactory.zoomTo(18);
        baiduMap.setMapStatus(mapStatusUpdate);

        initMarker();

        setOnClickMarker();
    }

    /**
     * 设置标志物的掉级时间
     */
    private void setOnClickMarker() {
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // 显示一个泡泡
                if (pop == null) {
                    pop = View.inflate(mActivity, R.layout.pop, null);
                    tv_title = (TextView) pop.findViewById(R.id.tv_title);
                    mMapView.addView(pop, createLayoutParams(marker.getPosition()));
                } else {
                    mMapView.updateViewLayout(pop, createLayoutParams(marker.getPosition()));
                }
                tv_title.setText(marker.getTitle());
                return true;
            }
        });
    }

    /**
     * 初始化标志物
     */
    private void initMarker() {
        MarkerOptions options = new MarkerOptions();
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_eat);
        options.position(mZafu_donghu)      // 位置
                .title("农大站点")      // title
                .icon(icon)         // 图标
                .draggable(false);   // 设置图标可以拖动
        baiduMap.addOverlay(options);
    }

    /**
     * 创建一个布局参数
     * @param position
     * @return
     */
    private MapViewLayoutParams createLayoutParams(LatLng position) {
        MapViewLayoutParams.Builder buidler = new MapViewLayoutParams.Builder();
        buidler.layoutMode(MapViewLayoutParams.ELayoutMode.mapMode);    // 指定坐标类型为经纬度
        buidler.position(position);     // 设置标志的位置
        buidler.yOffset(-25);           // 设置View往上偏移
        MapViewLayoutParams params = buidler.build();
        return params;
    }
}
