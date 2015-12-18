package moe.akagi.chibaproject.button;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import moe.akagi.chibaproject.R;

/**
 * Created by a15 on 12/18/15.
 */
public class DecisionCardAgreeButton extends DecisionCardButton{
    public DecisionCardAgreeButton(Context context) {
        super(context);
        setText(R.string.agree_member);
    }

    public DecisionCardAgreeButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DecisionCardAgreeButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void toggleText(boolean toggleAdmin) {
        if (toggleAdmin) {
            this.setText(R.string.agree_admin);
        } else {
            this.setText(R.string.agree_member);
        }
    }

    @Override
    public void toggleView() {
        if (isClicked) {
            setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteBg));
        } else {
            setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }
        super.toggleView();
    }
}
