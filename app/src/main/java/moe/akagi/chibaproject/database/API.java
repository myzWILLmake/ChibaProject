package moe.akagi.chibaproject.database;

import android.content.Context;

import moe.akagi.chibaproject.datatype.Event;

/**
 * Created by yunze on 12/3/15.
 */
public class API {

    public static DatabaseOpenHelper dbHelper;

    public static void init(Context context) {
        dbHelper = new DatabaseOpenHelper(context, "db", null, 1);
        dbHelper.getWritableDatabase();
    }

    public Event getEventById(String eventId) {
        return null;
    }
}
