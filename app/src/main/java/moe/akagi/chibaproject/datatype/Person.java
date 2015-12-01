package moe.akagi.chibaproject.datatype;

import java.util.List;

/**
 * Created by yunze on 12/1/15.
 */
public class Person {
    private String id;
    private String phone;
    private String nickname;
    private List<String> lanuchEventIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

}