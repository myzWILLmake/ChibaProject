package moe.akagi.chibaproject.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;

import java.util.List;

/**
 * Created by sinkerine on 5/11/16.
 */
public class PlaceMapCreate extends PlaceMap {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bListener = new MyLocationCreateListener();
        bLocationClient.registerLocationListener(bListener);
        bLocationClient.start();
    }

    private class MyLocationCreateListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bLocation) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(bLocation.getTime());
            sb.append("\nerror code : ");
            sb.append(bLocation.getLocType());
            sb.append("\nlatitude : ");
            sb.append(bLocation.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(bLocation.getLongitude());
            sb.append("\nradius : ");
            sb.append(bLocation.getRadius());
            if (bLocation.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(bLocation.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(bLocation.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(bLocation.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(bLocation.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(bLocation.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (bLocation.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(bLocation.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(bLocation.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (bLocation.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (bLocation.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (bLocation.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (bLocation.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(bLocation.getLocationDescribe());// 位置语义化信息
            List<Poi> list = bLocation.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.d("BaiduLocationApiDem", sb.toString());
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PlaceMapCreate.class);
        Bundle bundle = new Bundle();
        context.startActivity(intent);
    }
}
