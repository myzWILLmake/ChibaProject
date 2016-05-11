package moe.akagi.chibaproject.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import moe.akagi.chibaproject.R;

/**
 * Created by sinkerine on 5/9/16.
 */
abstract public class PlaceMap extends AppCompatActivity {
    MapView bView;
    BaiduMap bMap;
    BitmapDescriptor bIconMarker;
    LocationClient bLocationClient;
    BDLocationListener bListener;
    BDLocation bCurrentLocation = null;
    MyOrientationListener myOrientationListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        bLocationClient = new LocationClient(getApplicationContext());
        setContentView(R.layout.place_display);

        // Init map
        bView = (MapView) findViewById(R.id.map_view_display);
        bMap = bView.getMap();
        bMap.setMyLocationEnabled(true);
        bIconMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        initLocationConfig();
        initMarkerClickEvent();
        initOrientationListener();
    }

    abstract class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bLocation) {
            bCurrentLocation = bLocation;
            refreshMapLocation();
        }
    }
    public static class MyOrientationListener implements SensorEventListener
    {

        private Context context;
        private SensorManager sensorManager;
        private Sensor sensor;

        private float lastX ;

        private OnOrientationListener onOrientationListener ;

        public MyOrientationListener(Context context)
        {
            this.context = context;
        }

        // 开始
        public void start()
        {
            // 获得传感器管理器
            sensorManager = (SensorManager) context
                    .getSystemService(Context.SENSOR_SERVICE);
            if (sensorManager != null)
            {
                // 获得方向传感器
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            }
            // 注册
            if (sensor != null)
            {//SensorManager.SENSOR_DELAY_UI
                sensorManager.registerListener(this, sensor,
                        SensorManager.SENSOR_DELAY_UI);
            }

        }

        // 停止检测
        public void stop()
        {
            sensorManager.unregisterListener(this);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy)
        {

        }

        @Override
        public void onSensorChanged(SensorEvent event)
        {
            // 接受方向感应器的类型
            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
            {
                // 这里我们可以得到数据，然后根据需要来处理
                float x = event.values[SensorManager.DATA_X];

                if( Math.abs(x- lastX) > 1.0 )
                {
                    onOrientationListener.onOrientationChanged(x);
                }
//            Log.e("DATA_X", x+"");
                lastX = x ;

            }
        }

        public void setOnOrientationListener(OnOrientationListener onOrientationListener)
        {
            this.onOrientationListener = onOrientationListener ;
        }


        public interface OnOrientationListener
        {
            void onOrientationChanged(float x);
        }
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

    void initOrientationListener() {
        myOrientationListener = new MyOrientationListener(
                getApplicationContext());
        myOrientationListener
                .setOnOrientationListener(new MyOrientationListener.OnOrientationListener()
                {
                    @Override
                    public void onOrientationChanged(float x)
                    {
                        // 构造定位数据
                        if (bCurrentLocation != null) {
                            refreshMapLocation();
                        }
                    }
                });
    }

    void refreshMapLocation() {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bCurrentLocation.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(bCurrentLocation.getDirection())
                .latitude(bCurrentLocation.getLatitude())
                .longitude(bCurrentLocation.getLongitude()).build();
        // 设置定位数据
        bMap.setMyLocationData(locData);
        // 设置自定义图标
        MyLocationConfiguration config = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.COMPASS, true, null);
        bMap.setMyLocationConfigeration(config);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bView.onDestroy();
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
