package moe.akagi.chibaproject.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import it.gmariotti.cardslib.library.internal.Card;
import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.activity.PlaceDisplay;
import moe.akagi.chibaproject.datatype.Event;

/**
 * Created by a15 on 12/11/15.
 */
public class EventDetailInfo extends EventInfo {

    public EventDetailInfo(Context context, Event event) {
        super(context, R.layout.event_detail_info_content, event);
        this.addCardHeader(null);
        this.imageViewId = R.id.event_detail_info_image;
        this.timeViewId = R.id.event_detail_info_time;
        this.placeViewId = R.id.event_detail_info_place;
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);
        showMapText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceDisplay.actionStart(v.getContext(),location);
            }
        });
    }
}
