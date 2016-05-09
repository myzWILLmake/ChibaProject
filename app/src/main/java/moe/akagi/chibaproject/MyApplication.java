package moe.akagi.chibaproject;

import android.app.Application;
import android.content.SharedPreferences;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import moe.akagi.chibaproject.database.API;
import moe.akagi.chibaproject.datatype.User;
import moe.akagi.chibaproject.network.Utils;

/**
 * Created by yunze on 12/2/15.
 */
public class MyApplication extends Application {

    public static User user;

    @Override
    public void onCreate() {
        super.onCreate();
        Iconify.with(new FontAwesomeModule());

        API.init(getApplicationContext());
        SharedPreferences pref = getSharedPreferences("AppData", MODE_PRIVATE);
        if (!pref.getBoolean("init", false)) {
            API.initInsert();
            SharedPreferences.Editor editor = getSharedPreferences("AppData", MODE_PRIVATE).edit();
            editor.putBoolean("init", true);
            editor.apply();
        }
    }
}
