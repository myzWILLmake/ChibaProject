package moe.akagi.chibaproject.datatype;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunze on 11/30/15.
 */
public class User extends Person {
    private String password;
    private List<String> friendIds;
    private List<String> partInEventIds;

    public User() {
        friendIds = new ArrayList<String>();
        partInEventIds = new ArrayList<String>();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
