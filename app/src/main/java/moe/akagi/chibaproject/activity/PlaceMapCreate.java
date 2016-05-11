package moe.akagi.chibaproject.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.baidu.location.Poi;

import java.util.List;

import moe.akagi.chibaproject.R;

/**
 * Created by sinkerine on 5/11/16.
 */
public class PlaceMapCreate extends PlaceMap {
    int mSearchRadius = 500;

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
        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initLayout(R.layout.place_map_create,R.id.map_view_create);
        super.onCreate(savedInstanceState);
        bListener = new MyLocationListener();
        bLocationClient.registerLocationListener(bListener);
        bLocationClient.start();

        Toolbar toolbar = (Toolbar) findViewById(R.id.place_map_create_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PlaceMapCreate.class);
        context.startActivity(intent);
    }

    void searchAround(){
        Log.d("Test Bar: ", String.valueOf(mSearchRadius));
    }

}
