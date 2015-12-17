package moe.akagi.chibaproject.card;

import android.content.Context;

import it.gmariotti.cardslib.library.internal.Card;
import moe.akagi.chibaproject.R;
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
}
