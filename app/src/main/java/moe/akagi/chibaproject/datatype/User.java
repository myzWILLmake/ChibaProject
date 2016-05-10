package moe.akagi.chibaproject.datatype;

import java.util.List;
import java.util.Map;

/**
 * Created by yunze on 11/30/15.
 */
public class User extends Person {
    private String password;
    private List<String> friendIds;
    private Map<String, Person> friendsMap;
    private List<String> partInEventIds;

    public User() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getFriendIds() {
        return friendIds;
    }

    public void setFriendIds(List<String> friendIds) {
        this.friendIds = friendIds;
    }

    public Map<String, Person> getFriendsMap() {
        return friendsMap;
    }

    public void setFriendsMap(Map<String, Person> friendsMap) {
        this.friendsMap = friendsMap;
    }

    public List<String> getPartInEventIds() {
        return partInEventIds;
    }

    public void setPartInEventIds(List<String> partInEventIds) {
        this.partInEventIds = partInEventIds;
    }
}
