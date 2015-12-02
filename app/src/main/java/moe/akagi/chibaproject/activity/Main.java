package moe.akagi.chibaproject.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardViewNative;
import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.card.EventBriefInfo;

/**
 * Created by yunze on 11/30/15.
 */
public class Main extends AppCompatActivity {
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

        test();
    }


    public void test() {
        EventBriefInfo card = new EventBriefInfo(getContext());
        card.setTitle("这是活动题目");
        card.setTime("20:52");
        card.setPlace("紫金港");
        card.setImage(getDrawable(R.drawable.test_profile));
        //Set card in the cardView
        CardViewNative cardView = (CardViewNative) getActivity().findViewById(R.id.event_brief_info_card);
        cardView.setCard(card);
    }

    public Context getContext() {
        return this;
    }

    public Activity getActivity() {
        return this;
    }
}
