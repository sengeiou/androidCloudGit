package com.cloudtenant.yunmenkeji.cloudtenant.activity;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cloudtenant.yunmenkeji.cloudtenant.R;
import com.cloudtenant.yunmenkeji.cloudtenant.base.YzsBaseActivity;
import com.cloudtenant.yunmenkeji.cloudtenant.model.HouseDetil;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.CameraPosition;
import com.tencent.mapsdk.raster.model.Circle;
import com.tencent.mapsdk.raster.model.CircleOptions;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.CameraUpdate;
import com.tencent.tencentmap.mapsdk.map.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;


import com.yzs.yzsbaseactivitylib.entity.EventCenter;

import java.util.List;



/**
 * Created by 72984 on 2018/7/11.
 */

public class DisMapActivity extends YzsBaseActivity  implements TencentLocationListener{
    private HouseDetil houseDetil;
    MapView mapview=null;

    TencentMap tencentMap;
    private TencentLocationManager locationManager;
    private TencentLocationRequest locationRequest;
    private Marker myLocation;
    private Circle accuracy;
    private MapView mapView;

    @Override
    protected void initContentView(Bundle var1) {
        setContentView(R.layout.activity_distribution_map);

    }

    @Override
    protected void initView() {

        mapview=(MapView)findViewById(R.id.map);
//获取TencentMap实例
        tencentMap = mapview.getMap();



//设置实时路况开启
        tencentMap.setTrafficEnabled(true);

        tencentMap.setZoom(12);

//移动地图




    }
    protected void init() {

        locationManager = TencentLocationManager.getInstance(this);
        locationRequest = TencentLocationRequest.create();




        bindListener();


    }
    protected void bindListener() {

            // TODO Auto-generated method stub
            int error = locationManager.requestLocationUpdates(
                    locationRequest,DisMapActivity.this);
            switch (error) {
                case 0:
                    Log.e("location", "成功注册监听器");
                    break;
                case 1:
                    Log.e("location", "设备缺少使用腾讯定位服务需要的基本条件");
                    break;
                case 2:
                    Log.e("location", "manifest 中配置的 key 不正确");
                    break;
                case 3:
                    Log.e("location", "自动加载libtencentloc.so失败");
                    break;

                default:
                    break;
            }


    }
    @Override
    protected void initLogic() {
        setSmellText("房屋分布");
        getBtn1().setVisibility(View.INVISIBLE);
        getBtn2().setVisibility(View.INVISIBLE);
        getTv_out().setVisibility(View.VISIBLE);
        init();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapview.onResume();
        try {

            getBundleExtras(getIntent().getExtras());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mapview.onPause();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        mapview.onStop();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        mapview.onRestart();
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    @Override
    protected void getBundleExtras(Bundle var1) throws Exception {
        houseDetil= (HouseDetil) var1.getSerializable("bean");
        List<HouseDetil.ViewDataBean>  list= houseDetil.getViewDataX();
        for(HouseDetil.ViewDataBean bean :list){
            LatLng latLng = new LatLng(Double.valueOf(bean.getCellLatitude()),Double.valueOf(bean.getCellLongitude()));
            final Marker marker = tencentMap.addMarker(new MarkerOptions().
                    position(latLng).
                    title(bean.getCellName()).
                    snippet(bean.getCellAddress()));


        }
    }

    @Override
    protected void onEventComing(EventCenter var1) {

    }

    @Override
    public void onLocationChanged(TencentLocation arg0, int arg1, String s) {
        if (arg1 == TencentLocation.ERROR_OK) {
            LatLng latLng = new LatLng(arg0.getLatitude(), arg0.getLongitude());
            if (myLocation == null) {
                myLocation = tencentMap.addMarker(new MarkerOptions().
                        position(latLng).
                        icon(BitmapDescriptorFactory.fromResource(R.mipmap.navigation)).

                        anchor(0.5f, 0.5f));
            }
            tencentMap.setCenter(latLng);
            if (accuracy == null) {
                accuracy = tencentMap.addCircle(new CircleOptions().
                        center(latLng).
                        radius((double)arg0.getAccuracy()).
                        fillColor(0x440000ff).
                        strokeWidth(0f));
            }
            myLocation.setPosition(latLng);
            myLocation.setRotation(arg0.getBearing()); //仅当定位来源于gps有效，或者使用方向传感器
            accuracy.setCenter(latLng);
            accuracy.setRadius(arg0.getAccuracy());

        } else {

        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }


}