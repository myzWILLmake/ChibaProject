package moe.akagi.chibaproject.button;

import android.content.Context;
import android.util.AttributeSet;

import com.joanzapata.iconify.widget.IconButton;

/**
 * Created by a15 on 12/18/15.
 */
public class DecisionCardButton extends IconButton{
    protected boolean isClicked;
    protected String text;

    public DecisionCardButton(Context context) {
        super(context);
        isClicked = false;
    }

    public DecisionCardButton(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        isClicked = false;
    }

    public DecisionCardButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        isClicked = false;
    }

    public boolean isClicked() { return isClicked; }

    public void setIsClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }

    public void toggleClicked() {
        isClicked = !isClicked;
    }

    public void setText(String text) {
        this.text = text;
    }

    protected void setUpView() {
    }

    protected void setUpView(boolean toggleAdmin) {
    }
}
