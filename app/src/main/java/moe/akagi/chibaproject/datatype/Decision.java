package moe.akagi.chibaproject.datatype;

/**
 * Created by yunze on 12/16/15.
 */
public class Decision {
    public static final int TYPE_DATE = 0;
    public static final int TYPE_TIME = 1;
    public static final int TYPE_LOCA = 2;
    // public static final int TYPE_PERS = 3;

    private int id;
    private int eventId;
    private int sponsorId;
    private int type;
    private String content;
    private int agreePersonNum;
    private int rejectPersonNum;
}
