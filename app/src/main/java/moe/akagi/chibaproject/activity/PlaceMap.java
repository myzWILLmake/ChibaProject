package moe.akagi.chibaproject.activity;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.datatype.Location;

/**
 * Created by sinkerine on 5/9/16.
 */
abstract public class PlaceMap extends AppCompatActivity {
    MapView bView;
    BaiduMap bMap;
    BitmapDescriptor bIconMarker;
    LocationClient bLocationClient;
    BDLocationListener bListener;
    LatLng llCurrentLocation;
    Boolean firstLocated = true;
    int mLayoutRes;
    int mMapviewRes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        SDKInitializer.initialize(getApplicationContext());
        bLocationClient = new LocationClient(getApplicationContext());
        setContentView(mLayoutRes);

        // Init map
        bView = (MapView) findViewById(mMapviewRes);
        bMap = bView.getMap();
        bMap.setMyLocationEnabled(true);
        bIconMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
        initLocationConfig();
        initMarkerClickEvent();
    }

    void initLayout(int layoutRes,int mapViewRes) {
        this.mLayoutRes = layoutRes;
        this.mMapviewRes = mapViewRes;
    }

    class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bLocation) {
            llCurrentLocation = new LatLng(bLocation.getLatitude(), bLocation.getLongitude());
            if (firstLocated == true) {
                refreshLocation(bLocation);
                firstLocated = false;
            }
        }
    }

    void refreshLocation(BDLocation bLocation) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bLocation.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(bLocation.getDirection())
                .latitude(bLocation.getLatitude())
                .longitude(bLocation.getLongitude()).build();
        // 设置定位数据
        bMap.setMyLocationData(locData);
        // 设置自定义图标
        MyLocationConfiguration config = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.COMPASS, true, bIconMarker);
        bMap.setMyLocationConfigeration(config);
    }

    void initLocationConfig(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=3000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        bLocationClient.setLocOption(option);
    }

    void initMarkerClickEvent(){
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bView.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bView.onPause();
    }
}
