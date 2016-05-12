package moe.akagi.chibaproject.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import moe.akagi.chibaproject.datatype.Event;
import moe.akagi.chibaproject.datatype.Person;
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

    public static boolean logOut(String _id) {
        try {
            String res = Utils.submitPostData("/auth/logout", "");
            JSONObject jsonObj = new JSONObject(res);
            if (jsonObj.has("error")) {
               return false;
            } else {
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Object getFriendsByPersonId(String _id) {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("_id", _id);
            String res = Utils.submitPostData("/friend/id", jsonObj.toString());
            jsonObj = new JSONObject(res);
            if (jsonObj.has("error")) {
                int state = jsonObj.getInt("error");
                return Integer.valueOf(state);
            }
            List<String> list = new ArrayList<String>();
            JSONArray array = jsonObj.getJSONArray("friends");
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

    public static Object getUserById(String _id) {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("_id", _id);
            String res = Utils.submitPostData("/person/id", jsonObj.toString());
            jsonObj = new JSONObject(res);
            if (jsonObj.has("error")) {
                int state = jsonObj.getInt("error");
                return Integer.valueOf(state);
            }
            Person person = new Person();
            person.set_id(jsonObj.getString("_id"));
            person.setPhone(jsonObj.getString("phone"));
            person.setNickname(jsonObj.getString("nickname"));
            return person;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
            if (jsonObj.has("location")) {
                event.setLocation(jsonObj.getString("location"));
            } else {
                event.setLocation(null);
            }
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

    public static Object addEvent(Event evt) {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("manager", evt.getManager_id());
            jsonObj.put("title", evt.getTitle());
            jsonObj.put("time", evt.getTime());
            jsonObj.put("time_stat", evt.isTimeStat());
            jsonObj.put("location", evt.getLocation());
            jsonObj.put("state", evt.getState());
            String res = Utils.submitPostData("/event/new", jsonObj.toString());
            jsonObj = new JSONObject(res);
            if (jsonObj.has("error")) {
                int state = jsonObj.getInt("error");
                return Integer.valueOf(state);
            }
            String _id = jsonObj.getString("_id");
            return _id;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int addPartInPerson(String evt_id, List<String> people) {
        try {
            JSONObject jsonObj = new JSONObject();
            JSONArray peopleJson = new JSONArray(people);
            jsonObj.put("_id", evt_id);
            jsonObj.put("partin", peopleJson);
            String res = Utils.submitPostData("/event/new/partin", jsonObj.toString());
            jsonObj = new JSONObject(res);
            if (jsonObj.has("error")) {
                int state = jsonObj.getInt("error");
                return state;
            }
            int state = jsonObj.getInt("state");
            return state;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 111;
    }

    public static int addLaunchEvent(String usr_id, String evt_id) {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("usr_id", usr_id);
            jsonObj.put("evt_id", evt_id);
            String res = Utils.submitPostData("/person/launch", jsonObj.toString());
            jsonObj = new JSONObject(res);
            if (jsonObj.has("error")) {
                int state = jsonObj.getInt("error");
                return state;
            }
            int state = jsonObj.getInt("state");
            return state;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 111;
    }

}
