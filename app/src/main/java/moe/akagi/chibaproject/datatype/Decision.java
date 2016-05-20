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
    private String sponsorPhone;
    private String sponsorNickName;
    private int type;
    private String content;
    private int agreePersonNum;
    private int rejectPersonNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(int sponsorId) {
        this.sponsorId = sponsorId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAgreePersonNum() {
        return agreePersonNum;
    }

    public void setAgreePersonNum(int agreePersonNum) {
        this.agreePersonNum = agreePersonNum;
    }

    public int getRejectPersonNum() {
        return rejectPersonNum;
    }

    public void setRejectPersonNum(int rejectPersonNum) {
        this.rejectPersonNum = rejectPersonNum;
    }

    public String getSponsorPhone() {
        return sponsorPhone;
    }

    public void setSponsorPhone(String sponsorPhone) {
        this.sponsorPhone = sponsorPhone;
    }

    public void setSponsorNickName(String sponsorNickName) {
        this.sponsorNickName = sponsorNickName;
    }

    public String getSponsorNickName() {
        return sponsorNickName;
    }
}
