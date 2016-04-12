package moe.akagi.chibaproject.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import moe.akagi.chibaproject.datatype.User;
import moe.akagi.chibaproject.network.RetrieveData.DataManager;

/**
 * Created by yunze on 3/2/16.
 */
public class API {

    private static ExecutorService exec = Executors.newCachedThreadPool();

    public static void getUserByAuth(final DataManager dataManager, final String phone, final String password) {
        exec.execute(new Runnable() {
            @Override
            public void run() {
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
                        dataManager.dataReady(Integer.valueOf(state));
                        return;
                    }

                    User user = new User();
                    user.set_id(jsonObj.getString("_id"));
                    user.setPassword(password);
                    user.setPhone(jsonObj.getString("phone"));
                    user.setNickname(jsonObj.getString("nickname"));
                    dataManager.dataReady(user);
                    return;
                } catch (IOException e) {
                    Log.v("API", "IOE");
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.v("API", "Json");
                    e.printStackTrace();
                }
            }
        });
    }
}
