package moe.akagi.chibaproject.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import de.hdodenhof.circleimageview.CircleImageView;
import it.gmariotti.cardslib.library.internal.Card;
import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.database.API;
import moe.akagi.chibaproject.datatype.Decision;

/**
 * Created by a15 on 12/16/15.
 */
public class DecisionCard extends Card{
    private Context context;
    private Decision decision;
    private int sponsorId;


    public DecisionCard(Context context,Decision decision) {
        super(context, R.layout.decision_card);
        this.context = context;
        this.decision = decision;
        sponsorId = decision.getSponsorId();
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);
        CircleImageView sponsorImage = (CircleImageView) parent.findViewById(R.id.decision_card_sponsor_image);
        TextView nicknameTextView  = (TextView) parent.findViewById(R.id.decision_card_sponsor_nickname);
        TextView typeTextView = (TextView) parent.findViewById(R.id.decision_card_type);
        TextView contentTextView = (TextView) parent.findViewById(R.id.decision_card_content);
        TextView agreeNumTextView = (TextView) parent.findViewById(R.id.decision_card_agree_num);
        TextView disagreeNumTextView = (TextView) parent.findViewById(R.id.decision_card_disagree_num);

        sponsorImage.setImageResource(getSponsorImageId());
        nicknameTextView.setText(getSponsorNickName());
        switch (decision.getType()) {
            case Decision.TYPE_DATE:
                typeTextView.setText("修改日期");
                break;
            case Decision.TYPE_LOCA:
                typeTextView.setText("修改地点");
                break;
            case Decision.TYPE_TIME:
                typeTextView.setText("修改时间");
                break;
        }
        contentTextView.setText(decision.getContent());
        agreeNumTextView.setText(Integer.toString(decision.getAgreePersonNum()));
        disagreeNumTextView.setText(Integer.toString(decision.getRejectPersonNum()));
    }

    public String getSponsorNickName() {
        return API.getPersonByPersonId(sponsorId).getNickname();
    }

    public int getSponsorImageId() {
        String resId  = "profile_image_" + API.getPersonByPersonId(sponsorId).getPhone();
        return context.getResources().getIdentifier(resId, "drawable", context.getPackageName());
    }
}
