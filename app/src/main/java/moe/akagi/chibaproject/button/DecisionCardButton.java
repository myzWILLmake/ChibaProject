package moe.akagi.chibaproject.button;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.util.AttributeSet;

import com.joanzapata.iconify.widget.IconButton;


/**
 * Created by a15 on 12/18/15.
 */
public class DecisionCardButton extends IconButton {
    protected ObservableBoolean clicked = new ObservableBoolean(false);

    public DecisionCardButton(Context context) {
        super(context);
    }

    public DecisionCardButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DecisionCardButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableBoolean isClicked() {
        return clicked;
    }

    public void setClicked(ObservableBoolean clicked) {
        this.clicked.set(clicked.get());
    }

    public void toggleClicked() {
        clicked.set(!clicked.get());
    }

    public Boolean mIsClicked(){
        return isClicked().get();
    }

    public void mSetClicked(Boolean bClicked){
        setClicked(new ObservableBoolean(bClicked));
    }
}
