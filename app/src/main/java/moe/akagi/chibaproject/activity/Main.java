package moe.akagi.chibaproject.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.card.EventBriefInfo;
import moe.akagi.chibaproject.database.API;
import moe.akagi.chibaproject.datatype.Event;
import moe.akagi.chibaproject.datatype.User;

/**
 * Created by yunze on 11/30/15.
 */
public class Main extends AppCompatActivity {

    User user;
    EditText phoneEdit;
    EditText passwdEdit;

    // Create action bar menu
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
        switch (item.getItemId()) {
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        // Add action bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(myToolbar);

        API.init(getActivity());
        SharedPreferences pref = getSharedPreferences("AppData", MODE_PRIVATE);
        if (!pref.getBoolean("init", false)) {
            API.initInsert();
            SharedPreferences.Editor editor = getSharedPreferences("AppData", MODE_PRIVATE).edit();
            editor.putBoolean("init", true);
            editor.commit();
        }
        phoneEdit = (EditText) findViewById(R.id.phone);
        passwdEdit = (EditText) findViewById(R.id.password);
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneEdit.getText().toString();
                String password = passwdEdit.getText().toString();
                user = API.getUserByAuth(phone, password);
                if (user != null) {
                    Log.d("USER", "id: " + user.getId());
                    Log.d("USER", "phone: " + user.getPhone());
                    Log.d("USER", "password: " + user.getPassword());
                    Log.d("USER", "nickname: " + user.getNickname());
                    user.setPartInEventIds((API.getPartInEventsByPersonId(user.getId())));
                }
                test();
            }
        });
    }


    public void test() {

        ArrayList<Card> cards = new ArrayList<Card>();

        List<String> partInEvents = user.getPartInEventIds();
        for (String partInEvent : partInEvents) {
            Log.d("PARTIN", "event_id: " + partInEvent);
            Event event = API.getEventById(partInEvent);
            EventBriefInfo card = new EventBriefInfo(getContext());
            card.setTitle("这是活动题目");
            card.setTime(Long.toString(event.getTime()));
            card.setPlace(event.getLocation());
            card.setImage(getDrawable(R.drawable.test_profile));
            cards.add(card);
        }

        //Set card in the cardView


        //cards.add(card);
        //cards.add(card1);
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);
        CardListView listView = (CardListView) getActivity().findViewById(R.id.event_brief_info_card_list);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }

        //CardViewNative cardView = (CardViewNative) getActivity().findViewById(R.id.event_brief_info_card);
        //cardView.setCard(card);
    }

    public Context getContext() {
        return this;
    }

    public Activity getActivity() {
        return this;
    }
}
