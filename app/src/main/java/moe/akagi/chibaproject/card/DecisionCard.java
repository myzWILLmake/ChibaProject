package moe.akagi.chibaproject.card;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import it.gmariotti.cardslib.library.internal.Card;
import moe.akagi.chibaproject.MyApplication;
import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.activity.EventDetail;
import moe.akagi.chibaproject.button.DecisionCardAgreeButton;
import moe.akagi.chibaproject.button.DecisionCardDisagreeButton;
import moe.akagi.chibaproject.database.API;
import moe.akagi.chibaproject.datatype.Decision;
import moe.akagi.chibaproject.datatype.Time;
import moe.akagi.chibaproject.datatype.Vote;

/**
 * Created by a15 on 12/16/15.
 */
public class DecisionCard extends Card{
    private Context context;
    private Decision decision;
    private int sponsorId;

    TextView nicknameTextView;
    TextView typeTextView;
    TextView contentTextView;
    TextView agreeNumTextView;
    TextView disagreeNumTextView;
    DecisionCardAgreeButton agreeButton;
    DecisionCardDisagreeButton disagreeButton;

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
        nicknameTextView  = (TextView) parent.findViewById(R.id.decision_card_sponsor_nickname);
        typeTextView = (TextView) parent.findViewById(R.id.decision_card_type);
        contentTextView = (TextView) parent.findViewById(R.id.decision_card_content);
        agreeNumTextView = (TextView) parent.findViewById(R.id.decision_card_agree_num);
        disagreeNumTextView = (TextView) parent.findViewById(R.id.decision_card_disagree_num);
        agreeButton = (DecisionCardAgreeButton) parent.findViewById(R.id.decision_card_agree_button);
        disagreeButton = (DecisionCardDisagreeButton) parent.findViewById(R.id.decision_card_disagree_button);

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

        String content = decision.getContent();
        if (decision.getType() == Decision.TYPE_DATE || decision.getType() == Decision.TYPE_TIME) {
            Date date = new Date(Long.valueOf(content));
            Time time = new Time(date);
            if (decision.getType() == Decision.TYPE_DATE) {
                content = time.formatDate();
            } else {
                content = time.formatTime();
            }
        }

        contentTextView.setText(content);

        setUpViewFromVoteData(((EventDetail)getContext()).isAdmin());

        // bind button onClickListener
        agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!disagreeButton.isClicked()) {
                    agreeButton.toggleClicked();
                    ((EventDetail) getContext()).toggleAgree(agreeButton.isClicked(), decision.getId());
                    decision = API.getDecisionById(decision.getId());
                    agreeNumTextView.setText(Integer.toString(decision.getAgreePersonNum()));
                    agreeButton.setUpView();
                }
            }
        });
        disagreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!agreeButton.isClicked()) {
                    disagreeButton.toggleClicked();
                    ((EventDetail) getContext()).toggleDisagree(disagreeButton.isClicked(), decision.getId());
                    decision = API.getDecisionById(decision.getId());
                    disagreeNumTextView.setText(Integer.toString(decision.getRejectPersonNum()));
                    disagreeButton.setUpView();
                }
            }
        });
    }

    public void setUpViewFromVoteData(boolean toggleAdmin) {
        if (!toggleAdmin) {
            Vote vote = API.getVoteByUsrIdDecisionId(MyApplication.user.getId(), decision.getId());
            if (vote != null) {
                if (vote.getType() == Vote.TYPE_AGREE)
                    agreeButton.setIsClicked(true);
                else if (vote.getType() == Vote.TYPE_REJECT)
                    disagreeButton.setIsClicked(true);
            }
            agreeNumTextView.setText(Integer.toString(decision.getAgreePersonNum()));
            disagreeNumTextView.setText(Integer.toString(decision.getRejectPersonNum()));
        }else{
            agreeNumTextView.setText("");
            disagreeNumTextView.setText("");
            agreeButton.setIsClicked(false);
            disagreeButton.setIsClicked(false);
        }
        agreeButton.setUpView(toggleAdmin);
        disagreeButton.setUpView(toggleAdmin);
    }

    public String getSponsorNickName() {
        return API.getPersonByPersonId(sponsorId).getNickname();
    }

    public int getSponsorImageId() {
        String resId  = "profile_image_" + API.getPersonByPersonId(sponsorId).getPhone();
        return context.getResources().getIdentifier(resId, "drawable", context.getPackageName());
    }

    public void toggleView(boolean toggleAdmin) {
        agreeButton.toggleText(toggleAdmin);
        disagreeButton.toggleText(toggleAdmin);
        agreeButton.setUpView(toggleAdmin);
        disagreeButton.setUpView(toggleAdmin);
        setUpViewFromVoteData(toggleAdmin);
        if (toggleAdmin) {
            agreeButton.setIsClicked(false);
            disagreeButton.setIsClicked(false);
        }
    }

    public int getDecisionId() {
        return decision.getId();
    }
}
