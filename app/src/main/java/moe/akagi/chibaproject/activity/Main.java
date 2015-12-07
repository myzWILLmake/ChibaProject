package moe.akagi.chibaproject.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import moe.akagi.chibaproject.MyApplication;
import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.card.EventBriefInfo;
import moe.akagi.chibaproject.database.API;
import moe.akagi.chibaproject.datatype.Event;
import moe.akagi.chibaproject.datatype.Time;
import moe.akagi.chibaproject.datatype.User;

/**
 * Created by yunze on 11/30/15.
 */
public class Main extends AppCompatActivity {

    public static final int LOGIN_ACTIVITY = 1;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigation;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actionbar_menu, menu);
        // Set the icons
        menu.findItem(R.id.main_activity_action_bar_add_event).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_plus)
                        .actionBarSize()
        );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            // to do: add event
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        layoutInit();
        SharedPreferences pref = getSharedPreferences("AppData", MODE_PRIVATE);
        if (!pref.getBoolean("logged", false)) {
            Intent intent = new Intent(Main.this, Login.class);
            startActivityForResult(intent, LOGIN_ACTIVITY);
        } else {
            String phone = pref.getString("phone", null);
            String password = pref.getString("password", null);
            MyApplication.user = API.getUserByAuth(phone, password);
            User user = MyApplication.user;
            user.setPartInEventIds((API.getPartInEventsByPersonId(user.getId())));
            createCardList();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LOGIN_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    boolean returnedData = data.getBooleanExtra("login_succeed", false);
                    if (returnedData) {
                        User user = MyApplication.user;
                        user.setPartInEventIds((API.getPartInEventsByPersonId(user.getId())));
                        createCardList();
                        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                    }
                }
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void createCardList() {

        ArrayList<Card> cards = new ArrayList<Card>();

        List<String> partInEvents = MyApplication.user.getPartInEventIds();
        for (String partInEvent : partInEvents) {
            Event event = API.getEventById(partInEvent);
            EventBriefInfo card = new EventBriefInfo(getContext());
            card.setTitle(event.getTitle());
            Date date = new Date(event.getTime());
            Time time = new Time(date);
            card.setTime(time.formatTime());
            card.setPlace(event.getLocation());
            card.setImage(getDrawable(R.drawable.test_profile));
            cards.add(card);
        }

        //Set card in the cardView
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);
        CardListView listView = (CardListView) getActivity().findViewById(R.id.event_brief_info_card_list);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }
    }

    private void layoutInit() {
        setContentView(R.layout.main_layout);
        // Add action bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(myToolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.main_activity_drawer);
        drawerToggle = new ActionBarDrawerToggle(Main.this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigation = (NavigationView) findViewById(R.id.main_activity_navi);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.navi_item_profile:
                        break;
                    case R.id.navi_item_friends:
                        break;
                    case R.id.navi_item_logout:
                        SharedPreferences.Editor editor = getSharedPreferences("AppData", MODE_PRIVATE).edit();
                        editor.putBoolean("logged", false);
                        editor.putString("phone", null);
                        editor.putString("password", null);
                        editor.apply();
                        MyApplication.user = null;
                        Intent intent = new Intent(Main.this, Login.class);
                        startActivityForResult(intent, LOGIN_ACTIVITY);
                        break;
                }
                return false;
            }
        });
    }

    private Context getContext() {
        return this;
    }

    private Activity getActivity() {
        return this;
    }
}
