package moe.akagi.chibaproject.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.datatype.Location;

/**
 * Created by sinkerine on 5/11/16.
 */
public class PlaceMapDisplay extends PlaceMap{
    Location mLocation;

    class MylocationDisplayListener extends MyLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bLocation) {
            super.onReceiveLocation(bLocation);
            LatLng llPlace = new LatLng(mLocation.getLatitude(), mLocation.getLongtitude());
            double distance = DistanceUtil.getDistance(llCurrentLocation,llPlace);
            Log.d("Test disrance: ", String.valueOf(distance));
            Toast.makeText(
                    getApplicationContext(),
                    "您和集合地点的距离是: " + String.format("%1f", distance) + " 米",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initLayout(R.layout.place_map_display,R.id.map_view_display);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mLocation = (Location)intent.getSerializableExtra("location");
        bListener = new MylocationDisplayListener();
        bLocationClient.registerLocationListener(bListener);
        bLocationClient.start();

        // Mark current Location
        Bundle infoBundle = new Bundle();
        infoBundle.putSerializable("name",mLocation.getName());
        OverlayOptions markerOverlayOptions = new MarkerOptions()
                .icon(bIconMarker)
                .position(new LatLng(mLocation.getLatitude(), mLocation.getLongtitude()))
                .extraInfo(infoBundle);
        bMap.addOverlay(markerOverlayOptions);
    }


    @Override
    void initMarkerClickEvent() {
        super.initMarkerClickEvent();
        bMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Point point = bMap.getProjection().toScreenLocation(marker.getPosition());
                point.y -= 10;
                LatLng llInfo = bMap.getProjection().fromScreenLocation(point);

                TextView infoText = new TextView(getApplicationContext());
                infoText.setPadding(30,20,30,50);
                infoText.setText(marker.getExtraInfo().getString("name"));
                infoText.setTextColor(getResources().getColor(R.color.black));
                infoText.setTextSize(24);

                InfoWindow infoWindow = new InfoWindow(
                        infoText,
                        llInfo,
                        1
                );
                bMap.showInfoWindow(infoWindow);
                return true;
            }
        });
    }

    public static void actionStart(Context context, Location location) {
        Intent intent = new Intent(context,PlaceMapDisplay.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("location",location);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
