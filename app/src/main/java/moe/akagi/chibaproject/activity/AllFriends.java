package moe.akagi.chibaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import moe.akagi.chibaproject.MyApplication;
import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.card.FriendCardListItem;
import moe.akagi.chibaproject.datatype.Person;
import moe.akagi.chibaproject.datatype.User;

/**
 * Created by yunze on 12/10/15.
 */
public class AllFriends extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        initLayout();
        createCardList();
    }

    private void initLayout() {
        setContentView(R.layout.friends_layout);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.friends_activity_toolbar);
        if (myToolbar != null) {
            setSupportActionBar(myToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            String allFriendsStr = getResources().getString(R.string.my_friends);
            getSupportActionBar().setTitle(allFriendsStr);
            myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    setResult(RESULT_CANCELED, intent);
                    ActivityCollector.removeActivity(AllFriends.this);
                }
            });
        }
    }

    private void createCardList() {
        User user = MyApplication.user;
        List<Card> cards = new ArrayList<Card>();
        Iterator it = user.getFriendsMap().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Person person = (Person)pair.getValue();

            FriendCardListItem card = new FriendCardListItem(this);
            String resString = "profile_image_" + person.getPhone();
            int resId = getResources().getIdentifier(resString, "drawable", getPackageName());
            card.setImageResId(resId);
            card.setNickname(person.getNickname());
            cards.add(card);
        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);
        CardListView listView = (CardListView) this.findViewById(R.id.friends_activity_cardlist);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        ActivityCollector.removeActivity(AllFriends.this);
    }
}
