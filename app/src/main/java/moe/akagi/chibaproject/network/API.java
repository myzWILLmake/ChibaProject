package moe.akagi.chibaproject.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import moe.akagi.chibaproject.datatype.Event;
import moe.akagi.chibaproject.datatype.User;

/**
 * Created by yunze on 3/2/16.
 */
public class API {

    public static Object getUserByAuth(String phone, String password) {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("phone", phone);
            jsonObj.put("pass", password);
            String res;
            res = Utils.submitPostData("/auth", jsonObj.toString());
            jsonObj = new JSONObject(res);
            if (jsonObj.has("error")) {
                int state = jsonObj.getInt("error");
                return Integer.valueOf(state);
            }

            User user = new User();
            user.set_id(jsonObj.getString("_id"));
            user.setPassword(password);
            user.setPhone(jsonObj.getString("phone"));
            user.setNickname(jsonObj.getString("nickname"));
            return user;
        } catch (IOException e) {
            Log.v("API", "IOE");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.v("API", "Json");
            e.printStackTrace();
        }
        return null;
    }

    public static Object getPartInEventsByPersonId(String _id) {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("_id", _id);
            String res = Utils.submitPostData("/person/partin", jsonObj.toString());
            jsonObj = new JSONObject(res);
            if (jsonObj.has("error")) {
                int state = jsonObj.getInt("error");
                return Integer.valueOf(state);
            }

            List<String> list = new ArrayList<String>();
            JSONArray array = jsonObj.getJSONArray("part_in");
            for (int i=0; i<array.length(); i++) {
                String s = array.getString(i);
                list.add(s);
            }
            return list;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getEventById(String _id) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("_id", _id);
            String res = Utils.submitPostData("/event/id", jsonObj.toString());
            jsonObj = new JSONObject(res);
            if (jsonObj.has("error")) {
                int state = jsonObj.getInt("error");
                return Integer.valueOf(state);
            }

            Event event = new Event();
            event.set_id(jsonObj.getString("_id"));
            event.setManager_id(jsonObj.getString("manager"));
            event.setTitle(jsonObj.getString("title"));
            event.setTime(jsonObj.getLong("time"));
            event.setTimeStat(jsonObj.getBoolean("time_stat"));
            event.setLocation(jsonObj.getString("location"));
            event.setState(jsonObj.getInt("state"));
            event.setMemberIds(null);
            return event;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
