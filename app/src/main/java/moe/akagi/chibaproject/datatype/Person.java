package moe.akagi.chibaproject.datatype;

import java.util.List;

/**
 * Created by yunze on 12/1/15.
 */
public class Person {
    private  String _id;
    private int id;
    private String phone;
    private String nickname;
    private List<String> lanuchEventIds;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<String> getLanuchEventIds() {
        return lanuchEventIds;
    }

    public void setLanuchEventIds(List<String> lanuchEventIds) {
        this.lanuchEventIds = lanuchEventIds;
    }
}