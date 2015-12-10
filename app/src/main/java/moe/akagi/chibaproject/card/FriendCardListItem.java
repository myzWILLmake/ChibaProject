package moe.akagi.chibaproject.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import it.gmariotti.cardslib.library.internal.Card;
import moe.akagi.chibaproject.R;

/**
 * Created by yunze on 12/10/15.
 */
public class FriendCardListItem extends Card{
    private Context mContext;

    protected CircleImageView personImage;
    protected TextView nicknameTextView;
    protected int imageResId;
    protected String nickname;

    public FriendCardListItem(Context context) {
        this(context, R.layout.friend_list_item_content);
        mContext = context;
        imageResId = 0;
        nickname = "Nickname";
    }

    public FriendCardListItem(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        personImage = (CircleImageView) parent.findViewById(R.id.friend_activity_card_item_image);
        nicknameTextView = (TextView) parent.findViewById(R.id.friend_activity_card_item_nickname);

        if (personImage != null) {
            if (imageResId != 0) {
                personImage.setImageResource(imageResId);
            }
        }

        if (nicknameTextView != null) {
            nicknameTextView.setText(nickname);
        }
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
