package moe.akagi.chibaproject.card;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.joanzapata.iconify.widget.IconTextView;

import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.activity.EventDetail;
import moe.akagi.chibaproject.datatype.Event;

/**
 * Created by a15 on 12/11/15.
 */
public class EventBriefInfo extends EventInfo {

    private int moreInfoId;

    public EventBriefInfo(Context context, Event event) {
        super(context, R.layout.event_brief_info_content,event );
        timeViewId = R.id.event_brief_info_time;
        placeViewId = R.id.event_brief_info_place;
        imageViewId = R.id.event_brief_info_image;
        this.moreInfoId = R.id.event_brief_info_more;
    }


    @Override
    public void setupInnerViewElements(final ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);
        IconTextView moreInfo = (IconTextView) parent.findViewById(this.moreInfoId);
        if (event != null) {
            moreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventDetail.actionStart(v.getContext(),event);
                }
            });
        }
    }

}
