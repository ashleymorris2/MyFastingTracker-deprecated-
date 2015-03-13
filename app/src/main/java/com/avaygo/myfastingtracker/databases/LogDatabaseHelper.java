package com.avaygo.myfastingtracker.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Ash on 21/02/2015.
 */
public class LogDatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "fastingLog.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "log";
    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_START_DAY = "start_day"; // The day that this fast started on
    public static final String COLUMN_START_MONTH = "start_month"; // The month in which this fast started
    public static final String COLUMN_START_YEAR = "start_year"; // The year in which this fast started

    public static final String COLUMN_END_DAY = "end_day"; // The day that the fast actually ended on
    public static final String COLUMN_END_MONTH = "end_month"; // The month in which the fast actually ended
    public static final String COLUMN_END_YEAR = "end_year"; // The year in which the fast actually ended

    public static final String COLUMN_START_TIMESTAMP = "start_timestamp"; //The timestamp of the actual start time
    public static final String COLUMN_LOG_TIMESTAMP = "log_timestamp"; //The timestamp of time that the log was made at.
    public static final String COLUMN_END_TIMESTAMP = "end_timestamp";  /*The timestamp of the time the fast was expected to end at
                                                                         Take the start time and add the duration to get the correct
                                                                         figure*/

    public static final String COLUMN_FAST_DURATION = "fast_duration"; // The duration in hours that the fast was supposed to last

    public static final String COLUMN_PERCENT_COMPLETE = "percent_complete"; //The percentage that was actually completed
    public static final String COLUMN_USER_NOTE = "user_note"; // A user specified note about how their fast went.
    public static final String COLUMN_USER_RATING = "user_rating";// A numerical rating from the user 1-5


    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME
            +" ("+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_START_DAY + " INTEGER, "
            + COLUMN_START_MONTH + " INTEGER, "
            + COLUMN_START_YEAR + " INTEGER, "
            + COLUMN_END_DAY + " INTEGER, "
            + COLUMN_END_MONTH + " INTEGER, "
            + COLUMN_END_YEAR + " INTEGER, "
            + COLUMN_START_TIMESTAMP + " INTEGER, "
            + COLUMN_END_TIMESTAMP + " INTEGER, "
            + COLUMN_LOG_TIMESTAMP + " INTEGER, "
            + COLUMN_FAST_DURATION + " INTEGER, "
            + COLUMN_PERCENT_COMPLETE + " INTEGER, "
            + COLUMN_USER_NOTE + " TEXT, "
            + COLUMN_USER_RATING + " INTEGER);";

    public LogDatabaseHelper(Context context) {
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //Change this before release.
        Log.w(AlarmsDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
