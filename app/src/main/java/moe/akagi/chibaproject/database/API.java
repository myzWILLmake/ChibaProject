package moe.akagi.chibaproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import moe.akagi.chibaproject.datatype.Event;
import moe.akagi.chibaproject.datatype.Person;
import moe.akagi.chibaproject.datatype.User;

/**
 * Created by yunze on 12/3/15.
 */
public class API {

    public static DatabaseOpenHelper dbHelper;

    public static void init(Context context) {
        dbHelper = new DatabaseOpenHelper(context, "db", null, 3);
    }

    public static void initInsert() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues va = new ContentValues();
        for (int i=0; i<Data.person.length; i++) {
            va.put("phone", Data.person[i][0]);
            va.put("password", Data.person[i][1]);
            va.put("nickname", Data.person[i][2]);
            db.insert("person", null, va);
            va.clear();
        }

        for (int i=0; i<Data.event.length; i++) {
            va.put("manager", Integer.parseInt(Data.event[i][0]));
            va.put("title", Data.event[i][1]);
            va.put("time", Long.parseLong(Data.event[i][2]));
            va.put("time_stat", Integer.parseInt(Data.event[i][3]));
            va.put("location", Data.event[i][4]);
            va.put("state", Integer.parseInt(Data.event[i][5]));
            db.insert("event", null, va);
            va.clear();
        }

        for (int i=0; i<Data.friend.length; i++) {
            va.put("usr0_id", Integer.parseInt(Data.friend[i][0]));
            va.put("usr1_id", Integer.parseInt(Data.friend[i][1]));
            db.insert("friend", null, va);
            va.clear();
        }
        
        for (int i=0; i<Data.part_in.length; i++) {
            va.put("event_id", Integer.parseInt(Data.part_in[i][0]));
            va.put("usr_id", Integer.parseInt(Data.part_in[i][1]));
            db.insert("part_in", null, va);
            va.clear();
        }

        for (int i=0; i<Data.launch.length; i++) {
            va.put("usr_id", Integer.parseInt(Data.launch[i][0]));
            va.put("event_id", Integer.parseInt(Data.launch[i][1]));
            db.insert("launch", null, va);
            va.clear();
        }
    }

    public static Person getPersonByPersonId(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select phone, nickname from person where id = ?", new String[]{Integer.toString(id)});
        Person person = new Person();
        if (cursor.moveToFirst()) {
            do {
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
                person.setId(id);
                person.setPhone(phone);
                person.setNickname(nickname);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return person;
    }

    public static User getUserByAuth(String phone, String passwd) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from person where phone = ?", new String[]{phone});
        User user = new User();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
                if (passwd.equals(password)) {
                    user.setId(id);
                    user.setPassword(password);
                    user.setPhone(phone);
                    user.setNickname(nickname);
                } else  {
                    user = null;
                }
            } while (cursor.moveToNext());
        } else {
            user = null;
        }
        cursor.close();
        return user;
    }

    public static List<String> getPartInEventsByPersonId(int usr_id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select event_id from part_in where usr_id = ?", new String[]{Integer.toString(usr_id)});
        List<String> partInEvents = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("event_id"));
                partInEvents.add(Integer.toString(id));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return partInEvents;
    }

    public static Event getEventById(String eventId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from event where id = ?", new String[]{eventId});
        Event event = new Event();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int manager = cursor.getInt(cursor.getColumnIndex("manager"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                long time = cursor.getLong(cursor.getColumnIndex("time"));
                int timeStat = cursor.getInt(cursor.getColumnIndex("time_stat"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                int state = cursor.getInt(cursor.getColumnIndex("state"));
                event.setId(id);
                event.setManegerId(manager);
                event.setTitle(title);
                event.setTime(time);
                if (timeStat == 1)
                    event.setTimeStat(true);
                else
                    event.setTimeStat(false);
                event.setLocation(location);
                event.setState(state);
                event.setMemberIds(null);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return event;
    }

    public static List<String> getFriendsByPersonId(int usr_id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select usr0_id, usr1_id from friend where usr0_id = ? or usr1_id = ?", new String[]{Integer.toString(usr_id), Integer.toString(usr_id)});
        List<String> friends = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                int usr0_id = cursor.getInt(cursor.getColumnIndex("usr0_id"));
                int usr1_id = cursor.getInt(cursor.getColumnIndex("usr1_id"));
                if (usr0_id == usr_id) {
                    friends.add(Integer.toString(usr1_id));
                } else {
                    friends.add(Integer.toString(usr0_id));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return friends;
    }

    public static int insertNewEvent(int managerId, String title, long time, int timeStat, String location) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues va = new ContentValues();
        va.put("manager", managerId);
        va.put("title", title);
        va.put("time", time);
        va.put("time_stat", timeStat);
        va.put("location", location);
        va.put("state", 0);
        int id = (int)db.insert("event", null, va);
        va.clear();
        return id;
    }

    public static void insertPartInPersons(int eventId, List<String> persons) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues va = new ContentValues();
        for (String person : persons) {
            va.put("event_id", eventId);
            va.put("usr_id", Integer.parseInt(person));
            db.insert("part_in", null, va);
            va.clear();
        }
    }

    public static void insertLaunchEvent(int managerId, int eventId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues va = new ContentValues();
        va.put("usr_id", managerId);
        va.put("event_id", eventId);
        db.insert("launch", null, va);
        va.clear();
    }
}
