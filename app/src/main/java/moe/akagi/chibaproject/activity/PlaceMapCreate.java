package moe.akagi.chibaproject.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.datatype.Location;

/**
 * Created by sinkerine on 5/11/16.
 */
public class PlaceMapCreate extends PlaceMap {
    int mSearchRadius = 500;
    PoiSearch mPoiSearch;
    List<BitmapDescriptor> poiMarkList = new ArrayList<>();
    MyPoiLocation mLocationSelected;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.place_map_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map_search_radius_set_500:
                mSearchRadius = 500;
                break;
            case R.id.map_search_radius_set_1000:
                mSearchRadius = 1000;
                break;
            case R.id.map_search_radius_set_2000:
                mSearchRadius = 2000;
                break;
            case R.id.map_action_search:
                searchAround();
                break;
            case R.id.map_action_choose:
                if(mLocationSelected != null){
                    Intent resIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("location", mLocationSelected);
                    resIntent.putExtras(bundle);
                    setResult(Activity.RESULT_OK,resIntent);
                    super.onBackPressed();
                }else{
                    Toast.makeText(this, "还没有选定地点哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.map_action_detail:
                if(mLocationSelected != null){
                    mPoiSearch.searchPoiDetail(new PoiDetailSearchOption()
                            .poiUid(mLocationSelected.getUid())
                    );
                }else{
                    Toast.makeText(this, "还没有选定地点哦", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    class MyPoiLocation extends Location implements Serializable{
        String uid;

        MyPoiLocation(String uid, String name, double latitude, double longtitude) {
            super(name,latitude,longtitude);
            this.uid = uid;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initLayout(R.layout.place_map_create,R.id.map_view_create);
        super.onCreate(savedInstanceState);
        bListener = new MyLocationListener();
        bLocationClient.registerLocationListener(bListener);
        bLocationClient.start();

        poiMarkList.add(BitmapDescriptorFactory.fromResource(R.drawable.icon_mark1));
        poiMarkList.add(BitmapDescriptorFactory.fromResource(R.drawable.icon_mark2));
        poiMarkList.add(BitmapDescriptorFactory.fromResource(R.drawable.icon_mark3));
        poiMarkList.add(BitmapDescriptorFactory.fromResource(R.drawable.icon_mark4));
        poiMarkList.add(BitmapDescriptorFactory.fromResource(R.drawable.icon_mark5));
        poiMarkList.add(BitmapDescriptorFactory.fromResource(R.drawable.icon_mark6));
        poiMarkList.add(BitmapDescriptorFactory.fromResource(R.drawable.icon_mark7));
        poiMarkList.add(BitmapDescriptorFactory.fromResource(R.drawable.icon_mark8));
        poiMarkList.add(BitmapDescriptorFactory.fromResource(R.drawable.icon_mark9));
        poiMarkList.add(BitmapDescriptorFactory.fromResource(R.drawable.icon_mark10));

        Toolbar toolbar = (Toolbar) findViewById(R.id.place_map_create_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        OnGetPoiSearchResultListener poiSearchResultListener = new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                bMap.clear();
                for (int i = 0; i < poiResult.getAllPoi().size(); i ++) {
                    PoiInfo poiInfo = poiResult.getAllPoi().get(i);
                    Bundle poiBundle = new Bundle();
                    poiBundle.putSerializable("location",new MyPoiLocation(
                            poiInfo.uid,
                            poiInfo.name,
                            poiInfo.location.latitude,
                            poiInfo.location.longitude
                    ));
                    OverlayOptions markerOverlayOptions = new MarkerOptions()
                            .icon(poiMarkList.get(i))
                            .position(poiInfo.location)
                            .extraInfo(poiBundle);
                    bMap.addOverlay(markerOverlayOptions);
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                Uri uri = Uri.parse(poiDetailResult.getDetailUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        };
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(poiSearchResultListener);
    }

    @Override
    void initMarkerClickEvent() {
        super.initMarkerClickEvent();
        bMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mLocationSelected = (MyPoiLocation) marker.getExtraInfo().get("location");
                Point point = bMap.getProjection().toScreenLocation(marker.getPosition());
                point.y -= 10;
                LatLng llInfo = bMap.getProjection().fromScreenLocation(point);

                TextView infoText = new TextView(getApplicationContext());
                infoText.setPadding(30,20,30,50);
                infoText.setText(mLocationSelected.getName());
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

    void searchAround(){
        mPoiSearch.searchNearby(new PoiNearbySearchOption()
                .keyword("美食")
                .location(llCurrentLocation)
                .pageCapacity(10)
                .pageNum(1)
                .radius(mSearchRadius)
        );
    }
    /*
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PlaceMapCreate.class);
        context.startActivity(intent);
    }*/
}
