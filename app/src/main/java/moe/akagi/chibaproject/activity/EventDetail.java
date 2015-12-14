package moe.akagi.chibaproject.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardViewNative;
import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.card.EventDetailInfo;
import moe.akagi.chibaproject.database.API;
import moe.akagi.chibaproject.datatype.Event;

/**
 * Created by a15 on 12/12/15.
 */
public class EventDetail extends AppCompatActivity {

    private RecyclerView memberList;
    private MemberAdapter adapter;
    private FloatingActionButton fabAddDecision;
    private Event event;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        Intent intent = getIntent();
        this.event = (Event) intent.getSerializableExtra("event");
        initLayout();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private void initLayout() {
        setContentView(R.layout.event_layout);

        // event detail card init
        Card card = new EventDetailInfo(this, event);
        CardViewNative cardView = (CardViewNative) findViewById(R.id.event_detail_card);
        cardView.setCard(card);
        //memberList init
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        memberList = (RecyclerView) findViewById(R.id.event_member_list);
        memberList.setLayoutManager(layoutManager);
        this.adapter = new MemberAdapter(this, API.getPartInPeopleByEventId(event.getId()));
        memberList.setAdapter(this.adapter);
        // floating action button init
        fabAddDecision = (FloatingActionButton) findViewById(R.id.event_detail_fab);
        fabAddDecision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pop a dialog to create a decision
            }
        });
    }

    public static void actionStart(Context context, Event event) {
        Intent intent = new Intent(context, EventDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("event",event);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
