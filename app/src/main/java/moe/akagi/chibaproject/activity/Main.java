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
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import moe.akagi.chibaproject.MyApplication;
import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.card.EventBriefInfo;
import moe.akagi.chibaproject.database.API;
import moe.akagi.chibaproject.datatype.Event;
import moe.akagi.chibaproject.datatype.User;

/**
 * Created by yunze on 11/30/15.
 */
public class Main extends AppCompatActivity {

    public static final int LOGIN_ACTIVITY = 1;
    public static final int ADD_EVENT_ACTIVITY = 2;
    public static final int FRIEND_ACTIVITY = 3;

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
            case R.id.main_activity_action_bar_add_event:
                Intent intent = new Intent(Main.this, AddEvent.class);
                startActivityForResult(intent, ADD_EVENT_ACTIVITY);
                break;
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
        initLayout();
        SharedPreferences pref = getSharedPreferences("AppData", MODE_PRIVATE);
        if (!pref.getBoolean("logged", false)) {
            Intent intent = new Intent(Main.this, Login.class);
            startActivityForResult(intent, LOGIN_ACTIVITY);
        } else {
            String phone = pref.getString("phone", null);
            String password = pref.getString("password", null);
            MyApplication.user = API.getUserByAuth(phone, password);
            refreshUserInfo();
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
                        createCardList();
                        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                    }
                    refreshUserInfo();
                }
                break;
            case ADD_EVENT_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    createCardList();
                } else {
                    // Nothing
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

        User user = MyApplication.user;
        user.setPartInEventIds((API.getPartInEventsByPersonId(user.getId())));

        ArrayList<Card> cards = new ArrayList<Card>();

        List<String> partInEvents = MyApplication.user.getPartInEventIds();
        for (String partInEvent : partInEvents) {
            Event event = API.getEventById(partInEvent);
            EventBriefInfo card = new EventBriefInfo(getContext(),event);
            cards.add(card);
        }

        //Set card in the cardView
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);
        CardListView listView = (CardListView) getActivity().findViewById(R.id.event_brief_info_card_list);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }

        //bind click listener on 'more info'
        IconTextView moreInfo = (IconTextView) findViewById(R.id.event_brief_info_more);

    }

    private void initLayout() {
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
        navigation.inflateHeaderView(R.layout.main_navi_header);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.navi_item_profile:
                        break;
                    case R.id.navi_item_friends:
                        Intent intentFriend = new Intent(Main.this, AllFriends.class);
                        startActivityForResult(intentFriend, FRIEND_ACTIVITY);
                        break;
                    case R.id.navi_item_logout:
                        SharedPreferences.Editor editor = getSharedPreferences("AppData", MODE_PRIVATE).edit();
                        editor.putBoolean("logged", false);
                        editor.putString("phone", null);
                        editor.putString("password", null);
                        editor.apply();
                        MyApplication.user = null;
                        Intent intentLogout = new Intent(Main.this, Login.class);
                        startActivityForResult(intentLogout, LOGIN_ACTIVITY);
                        break;
                }
                return false;
            }
        });
    }

    private void refreshUserInfo() {
        CircleImageView naviImage = (CircleImageView) navigation.getHeaderView(0).findViewById(R.id.main_navi_profile_image);
        TextView naviNickname = (TextView) navigation.getHeaderView(0).findViewById(R.id.main_navi_profile_nickname);
        String resString = "profile_image_" + MyApplication.user.getPhone();
        int resId = getResources().getIdentifier(resString, "drawable", getPackageName());
        naviImage.setImageResource(resId);
        naviNickname.setText(MyApplication.user.getNickname());
    }

    private Context getContext() {
        return this;
    }

    private Activity getActivity() {
        return this;
    }
}
