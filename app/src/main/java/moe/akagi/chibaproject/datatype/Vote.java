package moe.akagi.chibaproject.datatype;

/**
 * Created by a15 on 12/19/15.
 */
public class Vote {
    private int decisionId;
    private int usrId;
    private int type;

    public static final int TYPE_AGREE = 1;
    public static final int TYPE_REJECT = 0;


    public Vote(int decisionId, int usrId, int type) {
        this.decisionId = decisionId;
        this.usrId = usrId;
        this.type = type;
    }

    public int getDecisionId() {
        return decisionId;
    }

    public int getUsrId() {
        return usrId;
    }

    public int getType() {
        return type;
    }
}
