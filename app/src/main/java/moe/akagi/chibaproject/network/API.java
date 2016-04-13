package moe.akagi.chibaproject.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
            Log.v("res", res);
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
}
