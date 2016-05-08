package moe.akagi.chibaproject.card;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
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
import moe.akagi.chibaproject.button.DecisionCardButton;
import moe.akagi.chibaproject.database.API;
import moe.akagi.chibaproject.databinding.DecisionCardBinding;
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

    // Data binding member
    private ObservableInt agreeNum;
    private ObservableInt disagreeNum;

    TextView nicknameTextView;
    TextView typeTextView;
    TextView contentTextView;
    DecisionCardButton agreeButton;
    DecisionCardButton disagreeButton;

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
        agreeButton = (DecisionCardButton) parent.findViewById(R.id.decision_card_agree_button);
        disagreeButton = (DecisionCardButton) parent.findViewById(R.id.decision_card_disagree_button);
        decision = API.getDecisionById(decision.getId());
        this.agreeNum = new ObservableInt(decision.getAgreePersonNum());
        this.disagreeNum = new ObservableInt(decision.getRejectPersonNum());

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

        setUpViewByIsAdmin(((EventDetail)getContext()).mIsAdminMode());

//         Databinding for button
        DecisionCardBinding decisionCardBinding = DataBindingUtil.bind(view);
        decisionCardBinding.setAgreeBtn(agreeButton);
        decisionCardBinding.setDisagreeBtn(disagreeButton);
        decisionCardBinding.setAgreeNum(this.agreeNum);
        decisionCardBinding.setDisagreeNum(this.disagreeNum);
        decisionCardBinding.setIsAdminMode(((EventDetail) getContext()).isAdminMode());

        // bind button onClickListener
        agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!((EventDetail) getContext()).mIsAdminMode()) {
                    if (!disagreeButton.mIsClicked()) {
                        if(!agreeButton.mIsClicked()){
                            mSetAgreeNum(mGetAgreeNum()+1);
                            API.insertVote(new Vote(getDecisionId(),sponsorId,Vote.TYPE_AGREE));
                        }else{
                            mSetAgreeNum(mGetAgreeNum()-1);
                            API.deleteVote(new Vote(getDecisionId(),sponsorId,Vote.TYPE_AGREE));
                        }
                        agreeButton.toggleClicked();
                    }
                }else{
                    ((EventDetail) getContext()).passDecision(getDecisionId());
                }
            }
        });
        disagreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!((EventDetail) getContext()).mIsAdminMode()) {
                    if (!agreeButton.mIsClicked()) {
                        if(!disagreeButton.mIsClicked()){
                            mSetDisagreeNum(mGetDisagreeNum()+1);
                            API.insertVote(new Vote(getDecisionId(),sponsorId,Vote.TYPE_REJECT));
                        }else{
                            mSetDisagreeNum(mGetDisagreeNum()-1);
                            API.deleteVote(new Vote(getDecisionId(),sponsorId,Vote.TYPE_REJECT));
                        }
                        disagreeButton.toggleClicked();
                    }
                }else{
                    ((EventDetail) getContext()).denyDecision(getDecisionId());
                }
            }
        });
    }

    public void setUpViewByIsAdmin(boolean adminMode) {
        if (adminMode) {
            agreeButton.mSetClicked(false);
            disagreeButton.mSetClicked(false);
        }else{
            // Set up view from vote record
            Vote vote = API.getVoteByUsrIdDecisionId(MyApplication.user.getId(), decision.getId());
            if (vote != null) {
                if (vote.getType() == Vote.TYPE_AGREE)
                    agreeButton.mSetClicked(true);
                else if (vote.getType() == Vote.TYPE_REJECT)
                    disagreeButton.mSetClicked(true);
            }
        }
    }

    public String getSponsorNickName() {
        return API.getPersonByPersonId(sponsorId).getNickname();
    }

    public int getSponsorImageId() {
        String resId  = "profile_image_" + API.getPersonByPersonId(sponsorId).getPhone();
        return context.getResources().getIdentifier(resId, "drawable", context.getPackageName());
    }

    public int getDecisionId() {
        return decision.getId();
    }

    // Data Binding Getter / Setter

    public ObservableInt getAgreeNum() {
        return agreeNum;
    }

    public ObservableInt getDisagreeNum() {
        return disagreeNum;
    }

    public void setAgreeNum(ObservableInt agreeNum) {
        this.agreeNum = agreeNum;
    }

    public void setDisagreeNum(ObservableInt disagreeNum) {
        this.disagreeNum = disagreeNum;
    }

    public int mGetAgreeNum() {
        return agreeNum.get();
    }

    public int mGetDisagreeNum() {
        return disagreeNum.get();
    }

    public void mSetAgreeNum(int agreeNum) {
        this.agreeNum.set(agreeNum);
    }

    public void mSetDisagreeNum(int disagreeNum) {
        this.disagreeNum.set(disagreeNum);
    }
}
