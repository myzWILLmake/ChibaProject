package moe.akagi.chibaproject.card;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import moe.akagi.chibaproject.R;

/**
 * Created by yunze on 12/2/15.
 */
public class EventBriefInfo extends Card {

    private Context mContext;
    private CardHeader header;
    private String time;
    private String place;
    private Drawable image;

    protected ImageView infoImage;
    protected TextView timeText;
    protected TextView placeText;

    public EventBriefInfo(Context context) {
        this(context, R.layout.event_brief_info_content);
        mContext = context;
        header = new CardHeader(mContext);
        this.addCardHeader(header);
        this.time = "待定";
        this.place = "待定";
    }

    public EventBriefInfo(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        timeText = (TextView) parent.findViewById(R.id.event_brief_info_time);
        placeText = (TextView) parent.findViewById(R.id.event_brief_info_place);
        infoImage = (ImageView) parent.findViewById(R.id.event_brief_info_image);

        if (timeText != null)
            timeText.setText(time);

        if (placeText != null)
            placeText.setText(place);

        if (infoImage != null)
            infoImage.setImageDrawable(image);
    }

    public void setTitle(String s) {
        header.setTitle(s);
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
