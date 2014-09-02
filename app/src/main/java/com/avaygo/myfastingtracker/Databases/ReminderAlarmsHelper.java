package com.avaygo.myfastingtracker.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by Ash on 16/08/2014.
 *
 * Constructs a database to our described schema
 *
 */
public class ReminderAlarmsHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reminderAlarms.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME  = "alarms";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_START = "start_time";
    public static final String COLUMN_END = "end_time";
    public static final String COLUMN_LENGTH = "length";
    public static final String COLUMN_ENABLED = "enabled";


    //SQL statement to create the database.
    private static final String DATABASE_CREATE = "CREATE TABLE " +
            TABLE_NAME +" (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +COLUMN_DAY+
            " STRING," +COLUMN_START+ " INTEGER," +COLUMN_END+ " INTEGER," +
            COLUMN_LENGTH +" INTEGER," + COLUMN_ENABLED + " INTEGER);";

    public ReminderAlarmsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(DATABASE_CREATE);
        } catch (android.database.SQLException e) {
            e.printStackTrace();
            Log.e("Database creation error", e.toString());
        }

    };

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
       //Change this before release.
        Log.w(ReminderAlarmsHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
