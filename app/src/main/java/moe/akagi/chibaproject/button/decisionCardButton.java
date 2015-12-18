package moe.akagi.chibaproject.button;

import android.content.Context;
import android.util.AttributeSet;

import com.joanzapata.iconify.widget.IconButton;

/**
 * Created by a15 on 12/18/15.
 */
public class DecisionCardButton extends IconButton{
    protected boolean isClicked;

    public DecisionCardButton(Context context) {
        super(context);
        isClicked = false;
    }

    public DecisionCardButton(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
    }

    public DecisionCardButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isClicked() { return isClicked; }

    protected void toggleView() {
        isClicked = !isClicked;
    }
}
