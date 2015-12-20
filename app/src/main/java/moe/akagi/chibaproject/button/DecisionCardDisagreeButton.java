package moe.akagi.chibaproject.button;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import moe.akagi.chibaproject.R;

/**
 * Created by a15 on 12/18/15.
 */
public class DecisionCardDisagreeButton extends DecisionCardButton {
    public DecisionCardDisagreeButton(Context context) {
        super(context);
    }

    public DecisionCardDisagreeButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DecisionCardDisagreeButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void toggleText(boolean toggleAdmin) {
        if (toggleAdmin) {
            setText(R.string.disagree_admin);
        } else {
            setText(R.string.disagree_member);
        }
    }

    @Override
    public void setUpView() {
        if (!isClicked) {
            setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteBg));
        } else {
            setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
        }
        super.setUpView();
    }

    @Override
    public void setUpView(boolean toggleAdmin) {
        setUpView();
        toggleText(toggleAdmin);
        super.setUpView(toggleAdmin);
    }
}
