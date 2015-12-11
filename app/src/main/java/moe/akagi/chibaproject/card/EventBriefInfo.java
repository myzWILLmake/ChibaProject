package moe.akagi.chibaproject.card;

import android.content.Context;
import moe.akagi.chibaproject.R;

/**
 * Created by a15 on 12/11/15.
 */
public class EventBriefInfo extends EventInfo {
    public EventBriefInfo(Context context) {
        super(context, R.layout.event_brief_info_content);
        timeViewId = R.id.event_brief_info_time;
        placeViewId = R.id.event_brief_info_place;
        imageViewId = R.id.event_brief_info_image;
    }
}
