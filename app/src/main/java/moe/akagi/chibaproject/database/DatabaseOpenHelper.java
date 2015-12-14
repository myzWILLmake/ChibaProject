package moe.akagi.chibaproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by yunze on 12/3/15.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    Context mContext;

    public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    private static final String CREATE_PERSON = "CREATE TABLE person (" +
            "id         integer PRIMARY KEY AUTOINCREMENT," +
            "phone      text    NOT NULL UNIQUE," +
            "password   text    NOT NULL," +
            "nickname   text    NOT NULL)";

    private static final String CREATE_EVENT = "CREATE TABLE event (" +
            "id         integer PRIMARY KEY AUTOINCREMENT," +
            "manager    integer NOT NULL," +
            "title      text    NOT NULL," +
            "time       integer," + // 如果年数是1970 则日期待定
            "time_stat  integer," + // 确定时间是不是待定 0 待定 1 确定
            "location   text," +
            "state      integer NOT NULL)";

    private static final String CREATE_FRIEND = "CREATE TABLE friend (" +
            "id         integer PRIMARY KEY AUTOINCREMENT," +
            "usr0_id    integer NOT NULL," +
            "usr1_id    integer NOT NULL)";

    private static final String CREATE_PARTIN = "CREATE TABLE part_in (" +
            "id         integer PRIMARY KEY AUTOINCREMENT," +
            "event_id   integer NOT NULL," +
            "usr_id    integer NOU NULL)";

    private static final String CREATE_LAUNCH = "CREATE TABLE launch (" +
            "id         integer PRIMARY KEY AUTOINCREMENT," +
            "usr_id     integer NOT NULL," +
            "event_id   integer NOT NULL)";

    private static final String CREATE_DECISION =  "CREATE TABLE decision (" +
            "id         integer PRIMARY KEY AUTOINCREMENT," +
            "event_id   integer NOT NULL," +
            "usr_id     integer NOT NULL," +
            "type       integer NOT NULL," + // 0 time 1 location 2 add_person
            "content    text NOT NULL," +
            "agree      integer NOT NULL," +
            "reject     integer NOT NULL)";

    private static final String CREATE_VOTE = "CREATE TABLE vote (" +
            "id         integer PRIMARY KEY AUTOINCREMENT," +
            "decision_id integer NOT NULL," +
            "usr_id     integer NOT NULL," +
            "type       integer NOT NULL)"; // 1 同意 0 否决

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PERSON);
        db.execSQL(CREATE_EVENT);
        db.execSQL(CREATE_FRIEND);
        db.execSQL(CREATE_PARTIN);
        db.execSQL(CREATE_LAUNCH);
        db.execSQL(CREATE_DECISION);
        db.execSQL(CREATE_VOTE);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS person");
        db.execSQL("DROP TABLE IF EXISTS event");
        db.execSQL("DROP TABLE IF EXISTS friend");
        db.execSQL("DROP TABLE IF EXISTS part_in");
        db.execSQL("DROP TABLE IF EXISTS launch");
        db.execSQL("DROP TABLE IF EXISTS decision");
        db.execSQL("DROP TABLE IF EXISTS vote");
        onCreate(db);
    }
}
