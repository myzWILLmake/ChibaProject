package moe.akagi.chibaproject.datatype;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunze on 11/30/15.
 */
public class Event {
    // event state
    public final static int READY  = 0; // 发起
    public final static int PLAY   = 1; // 进行中
    public final static int END    = 2; // 结束
    public final static int CANCEL = 3; // 取消

    private String id;
    private Person maneger;
    private Time time;
    private Location location;
    private List<String> memberIds;
    private int state;
    // private Decision decision;


    public Event() {
        memberIds = new ArrayList<String>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Person getManeger() {
        return maneger;
    }

    public void setManeger(Person maneger) {
        this.maneger = maneger;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
