package moe.akagi.chibaproject.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by yunze on 3/2/16.
 */
public class Utils {
    private static Context context = null;
    private static String sCookie = null;
    private static final String urlPre = "http://133.130.111.101:9001";

    public static JSONObject getJsonObjectFromMap(Map params) throws JSONException {
        Iterator iter = params.entrySet().iterator();
        JSONObject holder = new JSONObject();
        while (iter.hasNext()) {
            Map.Entry pairs = (Map.Entry)iter.next();
            holder.put((String) pairs.getKey(), pairs.getValue());
        }
        return holder;
    }

    public static void configConnection(HttpURLConnection connection) throws ProtocolException {
        connection.setConnectTimeout(5000);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setChunkedStreamingMode(0);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setUseCaches(false);
    }

    public static String submitPostData(String pos, String data) throws IOException {

        URL url = new URL(urlPre + pos);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        configConnection(connection);
        if (sCookie != null && sCookie.length()>0) {
            connection.setRequestProperty("Cookie", sCookie);
        }
        connection.connect();

        // Send data
        DataOutputStream output = new DataOutputStream(connection.getOutputStream());
        output.writeBytes(data);
        output.flush();
        output.close();

        // check Cookie
        String cookie = connection.getHeaderField("set-cookie");
        if (cookie != null && !cookie.equals(sCookie)) {
            sCookie = cookie;
        }

        // Respond
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        connection.disconnect();
        String res = sb.toString();
        Log.v("res", res);
        return res;
    }

    public static void initCookie(Context context) {

        Utils.context = context;

    }
}
