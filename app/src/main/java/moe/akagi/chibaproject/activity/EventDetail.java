package moe.akagi.chibaproject.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;
import it.gmariotti.cardslib.library.view.CardViewNative;
import moe.akagi.chibaproject.MyApplication;
import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.card.DecisionCard;
import moe.akagi.chibaproject.card.EventDetailInfo;
import moe.akagi.chibaproject.database.API;
import moe.akagi.chibaproject.datatype.Decision;
import moe.akagi.chibaproject.datatype.Event;
import moe.akagi.chibaproject.datatype.Location;
import moe.akagi.chibaproject.datatype.Time;
import moe.akagi.chibaproject.datatype.Vote;
import moe.akagi.chibaproject.dialog.DateDialogAdapter;
import moe.akagi.chibaproject.dialog.DatePickerUtil;
import moe.akagi.chibaproject.dialog.LocationDialogAdapter;
import moe.akagi.chibaproject.dialog.LocationDialogUtil;
import moe.akagi.chibaproject.dialog.TimeDialogAdapter;
import moe.akagi.chibaproject.dialog.TimePickerUtil;

/**
 * Created by a15 on 12/12/15.
 */
public class EventDetail extends AppCompatActivity implements DateDialogAdapter, TimeDialogAdapter, LocationDialogAdapter {

    private RecyclerView memberList;
    private Event event;
    protected Time date;
    protected Time time;
    protected Location location;
    private EventDetailInfo eventDetailCard;
    private CardViewNative  cardView;
    ArrayList<DecisionCard> decisionCardList;
    ArrayList<Card>  cardList;
    CardArrayRecyclerViewAdapter decisionArrayAdapter;

    private ObservableBoolean adminMode = new ObservableBoolean(false);
    private boolean isAdmin;

    private static class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MViewHolder> {
        private LayoutInflater layoutInflater;
        private List<String> imageResourceIds;

        public class MViewHolder extends RecyclerView.ViewHolder{
            private CircleImageView circleImageView;

            public MViewHolder(View view) {
                super(view);
                circleImageView = (CircleImageView) view.findViewById(R.id.event_member_circle_image);
            }

        }

        public MemberAdapter(Context context, List<String> memberIds) {
            imageResourceIds = new ArrayList<>();
            for (String member_id : memberIds) {
                String phone = (API.getPersonByPersonId(Integer.valueOf(member_id)).getPhone());
                int resId = context.getResources().getIdentifier("profile_image_" + phone, "drawable", context.getPackageName());
                imageResourceIds.add(String.valueOf(resId));
            }
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MViewHolder(layoutInflater.inflate(R.layout.event_member_image, parent, false));
        }

        @Override
        public void onBindViewHolder(MViewHolder holder, int position) {
            holder.circleImageView.setImageResource(Integer.valueOf(imageResourceIds.get(position)));
        }

        @Override
        public int getItemCount() {
            return imageResourceIds.isEmpty() ? 0 : imageResourceIds.size();
        }
    }

//    private static class DecisionAdapter extends

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        Intent intent = getIntent();
        this.event = (Event) intent.getSerializableExtra("event");
        event.setDecisionIds(API.getDecisionsByEventId(event.getId()));

        // Init date time location tmp variable for creating new decision
        Date dateTmp = new Date(event.getTime());
        date = new Time(dateTmp);
        time = new Time(dateTmp);
        location = new Location();

        date.setHour(-1);
        time.setHour(-1);
        location.setInfo(null);

        //judge admin
        if (MyApplication.user.getPhone().equals(API.getPersonByPersonId(event.getManegerId()).getPhone())) {
            isAdmin = true;
        } else {
            isAdmin = false;
        }
        initLayout();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isAdmin) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.event_detail_toolbar_menu, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.event_detail_toolbar_menu:
                toggleAdmin(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }


    private void initLayout() {
        setContentView(R.layout.event_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.event_detail_activity_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(event.getTitle());
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    setResult(RESULT_CANCELED, intent);
                    ActivityCollector.removeActivity(EventDetail.this);
                }
            });
        }

        // event detail card init
        eventDetailCard = new EventDetailInfo(this, event);
        cardView = (CardViewNative) findViewById(R.id.event_detail_card);
        cardView.setCard(eventDetailCard);

        // memberList init
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        memberList = (RecyclerView) findViewById(R.id.event_member_list);
        memberList.setLayoutManager(layoutManager);
        MemberAdapter memberAdapter = new MemberAdapter(this, API.getPartInPeopleByEventId(event.getId()));
        memberList.setAdapter(memberAdapter);

        // decisionList init
        List<String> decisionIdList = API.getDecisionsByEventId(event.getId());
        decisionCardList = new ArrayList<>();
        for (String decisionId : decisionIdList) {
            decisionCardList.add(new DecisionCard(this, API.getDecisionById(Integer.valueOf(decisionId))));
        }
        initDecisionList();

        // fab init
        FloatingActionButton fabDate = (FloatingActionButton) findViewById(R.id.fab_modify_date);
        FloatingActionButton fabTime = (FloatingActionButton) findViewById(R.id.fab_modify_time);
        FloatingActionButton fabLocation = (FloatingActionButton) findViewById(R.id.fab_modify_location);

        fabDate.setIconDrawable(new IconDrawable(this, FontAwesomeIcons.fa_calendar_o));
        fabTime.setIconDrawable(new IconDrawable(this, FontAwesomeIcons.fa_clock_o));
        fabLocation.setIconDrawable(new IconDrawable(this, FontAwesomeIcons.fa_map_marker));

        fabDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerUtil datePickerUtil = new DatePickerUtil(EventDetail.this, EventDetail.this, -1, 0, 0);
                datePickerUtil.datePickDialog(date);
            }
        });

        fabTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerUtil timePickerUtil = new TimePickerUtil(EventDetail.this, EventDetail.this, -1, 0);
                timePickerUtil.timePickDialog(time);
            }
        });

        fabLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationDialogUtil locationDialogUtil = new LocationDialogUtil(EventDetail.this, EventDetail.this, "");
                locationDialogUtil.locationDialog(location);
            }
        });
    }

    private void initDecisionList() {
        cardList = new ArrayList<>();
        cardList.addAll(decisionCardList);
        decisionArrayAdapter = new CardArrayRecyclerViewAdapter(this, cardList);
        CardRecyclerView decisionListView = (CardRecyclerView) this.findViewById(R.id.event_decision_card_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        decisionListView.setLayoutManager(layoutManager);
        if (decisionListView != null) {
            decisionListView.setAdapter(decisionArrayAdapter);
        }
    }

    private void toggleAdmin(MenuItem item) {
        toogleAdminMode();
        if (mIsAdminMode()) {
            item.setTitle(R.string.menu_admin_mode);
        } else {
            item.setTitle(R.string.menu_not_admin_mode);
        }
        for (DecisionCard card : decisionCardList) {
            card.setUpViewByIsAdmin(mIsAdminMode());
        }
    }

    private void deleteDecisionCard(int decisionId) {
        for (int i = 0; i < decisionCardList.size(); i++) {
            DecisionCard decisionCard = decisionCardList.get(i);
            if (decisionCard.getDecisionId()  == decisionId) {
                decisionCardList.remove(i);
                cardList.remove(i);
                decisionArrayAdapter.notifyItemRemoved(i);
                break;
            }
        }
        API.deleteDecisionById(decisionId);
    }

    private void addDecisionCard(Decision decision) {
        //add decision card
        DecisionCard card = new DecisionCard(this,decision);
        decisionCardList.add(card);
        cardList.add(card);
        decisionArrayAdapter.notifyItemInserted(cardList.size() - 1);
    }

    public void passDecision(int decisionId) {
            Decision tDecision = API.getDecisionById(decisionId);
            String content = tDecision.getContent();
            Event tEvent = this.event;
            switch (tDecision.getType()) {
                case Decision.TYPE_TIME: tEvent.setTimeStat(true);
                case Decision.TYPE_DATE:
                    tEvent.setTime(Long.valueOf(content));
                    break;
                case Decision.TYPE_LOCA:
                    tEvent.setLocation(content);
                    break;
            }
            API.updateEventByDecision(tDecision);
            eventDetailCard = new EventDetailInfo(this, tEvent);
            cardView.refreshCard(eventDetailCard);
            deleteDecisionCard(decisionId);
    }

    public void denyDecision(int decisionId) {
            deleteDecisionCard(decisionId);
    }

    @Override
    public void refreshTimeInfo() {
        // To do: add decision time type card
        if (time.getHour() != -1) {
            Decision decision = new Decision();
            decision.setEventId(event.getId());
            decision.setSponsorId(MyApplication.user.getId());
            decision.setType(Decision.TYPE_TIME);
            decision.setContent(Long.toString(time.formatLong()));
            decision.setAgreePersonNum(0);
            decision.setRejectPersonNum(0);
            API.insertDecision(decision);
            addDecisionCard(decision);
       }
    }

    @Override
    public void refreshDateInfo() {
        // To do: add decision date type card
        if (date.getYear() != -1) {
            Decision decision = new Decision();
            decision.setEventId(event.getId());
            decision.setSponsorId(MyApplication.user.getId());
            decision.setType(Decision.TYPE_DATE);
            decision.setContent(Long.toString(date.formatLong()));
            decision.setAgreePersonNum(0);
            decision.setRejectPersonNum(0);
            API.insertDecision(decision);
            addDecisionCard(decision);
        }
        // refreshCards();
    }

    @Override
    public void refreshLocationInfo() {
        // To do: add decision location type card
        if (location.getInfo() != null) {
            Decision decision = new Decision();
            decision.setEventId(event.getId());
            decision.setSponsorId(MyApplication.user.getId());
            decision.setType(Decision.TYPE_LOCA);
            decision.setContent(location.getInfo());
            decision.setAgreePersonNum(0);
            decision.setRejectPersonNum(0);
            API.insertDecision(decision);
            addDecisionCard(decision);
        }
        // refreshCards();
    }

    public static void actionStart(Context context, Event event) {
        Intent intent = new Intent(context, EventDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("event",event);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public ObservableBoolean isAdminMode() {
        return adminMode;
    }

    public void setAdminMode(ObservableBoolean adminMode) {
        this.adminMode = adminMode;
    }

    public boolean mIsAdminMode(){
        return adminMode.get();
    }

    public void mSetAdminMode(boolean adminMode) {
        this.adminMode.set(adminMode);
    }

    public void toogleAdminMode(){
        this.adminMode.set(!mIsAdminMode());
    }
}
