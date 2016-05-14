package moe.akagi.chibaproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import moe.akagi.chibaproject.datatype.Decision;
import moe.akagi.chibaproject.datatype.Event;
import moe.akagi.chibaproject.datatype.Person;
import moe.akagi.chibaproject.datatype.User;
import moe.akagi.chibaproject.datatype.Vote;

/**
 * Created by yunze on 12/3/15.
 */
public class API {

    public static DatabaseOpenHelper dbHelper;

    public static void init(Context context) {
        dbHelper = new DatabaseOpenHelper(context, "db", null, 4);
    }

    public static void initInsert() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues va = new ContentValues();
        for (int i = 0; i < Data.person.length; i++) {
            va.put("phone", Data.person[i][0]);
            va.put("password", Data.person[i][1]);
            va.put("nickname", Data.person[i][2]);
            db.insert("person", null, va);
            va.clear();
        }

        for (int i = 0; i < Data.event.length; i++) {
            va.put("manager", Integer.parseInt(Data.event[i][0]));
            va.put("title", Data.event[i][1]);
            va.put("time", Long.parseLong(Data.event[i][2]));
            va.put("time_stat", Integer.parseInt(Data.event[i][3]));
            va.put("location", Data.event[i][4]);
            va.put("state", Integer.parseInt(Data.event[i][5]));
            db.insert("event", null, va);
            va.clear();
        }

        for (int i = 0; i < Data.friend.length; i++) {
            va.put("usr0_id", Integer.parseInt(Data.friend[i][0]));
            va.put("usr1_id", Integer.parseInt(Data.friend[i][1]));
            db.insert("friend", null, va);
            va.clear();
        }

        for (int i = 0; i < Data.part_in.length; i++) {
            va.put("event_id", Integer.parseInt(Data.part_in[i][0]));
            va.put("usr_id", Integer.parseInt(Data.part_in[i][1]));
            db.insert("part_in", null, va);
            va.clear();
        }

        for (int i = 0; i < Data.launch.length; i++) {
            va.put("usr_id", Integer.parseInt(Data.launch[i][0]));
            va.put("event_id", Integer.parseInt(Data.launch[i][1]));
            db.insert("launch", null, va);
            va.clear();
        }

        for (int i = 0; i < Data.decision.length; i++) {
            va.put("event_id", Integer.parseInt(Data.decision[i][0]));
            va.put("usr_id", Integer.parseInt(Data.decision[i][1]));
            va.put("type", Integer.parseInt(Data.decision[i][2]));
            va.put("content", Data.decision[i][3]);
            db.insert("decision", null, va);
            va.clear();
        }

        for (int i = 0; i < Data.vote.length; i++) {
            va.put("decision_id", Integer.parseInt(Data.vote[i][0]));
            va.put("usr_id", Integer.parseInt(Data.vote[i][1]));
            va.put("type", Integer.parseInt(Data.vote[i][2]));
            db.insert("vote", null, va);
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

    public static void cleanDb() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.cleanUp(db);
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
                } else {
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

    public static Event getEventById(int eventId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from event where id = ?", new String[]{Integer.toString(eventId)});
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
                //event.setPlace(location);
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

    public static List<String> getPartInPeopleByEventId(int event_id) {
        List<String> partInPeoples = new ArrayList<>();
        //add part in members
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select usr_id from part_in where event_id = ?", new String[]{Integer.toString(event_id)});
        if (cursor.moveToFirst()) {
            do {
                int usr_id = cursor.getInt(cursor.getColumnIndex("usr_id"));
                partInPeoples.add(Integer.toString(usr_id));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return partInPeoples;
    }

    public static int insertEvent(Event event) {
        int managerId = event.getManegerId();
        String title = event.getTitle();
        long time = event.getTime();
        String location = event.getPlace();
        int timeStat;
        if (event.isTimeStat()) {
            timeStat = 1;
        } else {
            timeStat = 0;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues va = new ContentValues();
        va.put("manager", managerId);
        va.put("title", title);
        va.put("time", time);
        va.put("time_stat", timeStat);
        va.put("location", location);
        va.put("state", 0);
        int id = (int) db.insert("event", null, va);
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

    public static int insertDecision(Decision decision) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues va = new ContentValues();
        va.put("event_id", decision.getEventId());
        va.put("usr_id", decision.getSponsorId());
        va.put("type", decision.getType());
        va.put("content", decision.getContent());
        va.put("agree", decision.getAgreePersonNum());
        va.put("reject", decision.getRejectPersonNum());
        int decisionId = (int) db.insert("decision", null, va);
        va.clear();
        decision.setId(decisionId);
        return decisionId;
    }

    public static List<String> getDecisionsByEventId(int eventId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select id from decision where event_id = ?", new String[]{Integer.toString(eventId)});
        List<String> decisionIds = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                decisionIds.add(Integer.toString(id));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return decisionIds;
    }

    public static Decision getDecisionById(int decisionId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from decision where id = ?", new String[]{Integer.toString(decisionId)});
        Decision decision = new Decision();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int eventId = cursor.getInt(cursor.getColumnIndex("event_id"));
                int usrId = cursor.getInt(cursor.getColumnIndex("usr_id"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                int agreePersonNum = cursor.getInt(cursor.getColumnIndex("agree"));
                int rejectPersonNum = cursor.getInt(cursor.getColumnIndex("reject"));
                decision.setId(id);
                decision.setEventId(eventId);
                decision.setSponsorId(usrId);
                decision.setType(type);
                decision.setContent(content);
                decision.setAgreePersonNum(agreePersonNum);
                decision.setRejectPersonNum(rejectPersonNum);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return decision;
    }

    public static Vote getVoteByUsrIdDecisionId(int usrId, int decisionId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from vote where usr_id = ? and decision_id = ?", new String[]{Integer.toString(usrId), Integer.toString(decisionId)});
        int type = -1;
        if(cursor.getCount() == 0)
            return null;
        if (cursor.moveToFirst()) {
            do {
                type = cursor.getInt(cursor.getColumnIndex("type"));
            } while (cursor.moveToNext());
        }
        return new Vote(decisionId, usrId, type);
    }

    public static void insertVote(Vote vote) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues va = new ContentValues();
        va.put("decision_id", vote.getDecisionId());
        va.put("usr_id", vote.getUsrId());
        va.put("type", vote.getType());
        db.insert("vote", null, va);
    }

    public static void deleteVote(Vote vote) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("vote", "decision_id = ? and usr_id = ?", new String[]{Integer.toString(vote.getDecisionId()), Integer.toString(vote.getUsrId())});
    }

    public static void deleteDecisionById(int decisionId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("decision", "id = ?", new String[]{Integer.toString(decisionId)});
    }

    public static void updateEventByDecision(Decision decision) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        switch (decision.getType()) {
            case Decision.TYPE_TIME:
            case Decision.TYPE_DATE:
                values.put("time",Long.valueOf(decision.getContent()));
                values.put("time_stat",1);
                break;
            case Decision.TYPE_LOCA:
                values.put("location",decision.getContent());
                values.put("state",1);
                break;
        }
        db.update("event", values, "id = ?", new String[]{Integer.toString(decision.getEventId())});
    }
}
