package com.avaygo.myfastingtracker.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.avaygo.myfastingtracker.classes.FastingRecord;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ash on 09/03/2015.
 */
public class LogDataSource {

    private SQLiteDatabase database;
    private LogDatabaseHelper dbHelper;

    //Constructor
    public LogDataSource(Context context) {
        dbHelper = new LogDatabaseHelper(context);
    }

    //Opens a writable database
    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * @param startDate From here the timestamp, start day, month and year can be saved.
     * @param endDate   From here the timestamp, end day, month and year can be saved.
     * @param duration  The duration that the fast was supposed to last for
     */
    public void createRecord(Calendar startDate, Calendar endDate, int duration,
                             int percentComplete, String userNote, int userRating) {

        Calendar fastEnd = Calendar.getInstance();
        fastEnd.setTimeInMillis(startDate.getTimeInMillis());
        fastEnd.add(Calendar.HOUR_OF_DAY, duration);

        //Get the additional parameters for the database
        int startDay = startDate.get(Calendar.DAY_OF_MONTH);
        int startMonth = startDate.get(Calendar.MONTH);
        int startYear = startDate.get(Calendar.YEAR);

        int endDay = endDate.get(Calendar.DAY_OF_MONTH);
        int endMonth = endDate.get(Calendar.MONTH);
        int endYear = endDate.get(Calendar.YEAR);

        ContentValues values = new ContentValues();
        values.put(LogDatabaseHelper.COLUMN_START_DAY, startDay);
        values.put(LogDatabaseHelper.COLUMN_START_MONTH, startMonth);
        values.put(LogDatabaseHelper.COLUMN_START_YEAR, startYear);

        values.put(LogDatabaseHelper.COLUMN_END_DAY, endDay);
        values.put(LogDatabaseHelper.COLUMN_END_MONTH, endMonth);
        values.put(LogDatabaseHelper.COLUMN_END_YEAR, endYear);

        values.put(LogDatabaseHelper.COLUMN_START_TIMESTAMP, startDate.getTimeInMillis());
        values.put(LogDatabaseHelper.COLUMN_END_TIMESTAMP, endDate.getTimeInMillis());
        values.put(LogDatabaseHelper.COLUMN_FAST_END, fastEnd.getTimeInMillis());

        values.put(LogDatabaseHelper.COLUMN_FAST_DURATION, duration);
        values.put(LogDatabaseHelper.COLUMN_PERCENT_COMPLETE, percentComplete);
        values.put(LogDatabaseHelper.COLUMN_USER_NOTE, userNote);
        values.put(LogDatabaseHelper.COLUMN_USER_RATING, userRating);

        database.insert(LogDatabaseHelper.TABLE_NAME, null, values);
    }

    /**
     * Gets all the records currently in the database by month and year.
     *
     * @param month The month that is to be searched for. Either end month or start month of the fast.
     * @param year The year that is to be searched for.
     * @return A list of all the records that match the searched parameters.
     */
    public List<FastingRecord> getRecordsForMonth(int month, int year){

        //Select by month and year
        //E.g. want all of February records but only from 2015.
        List<FastingRecord> recordsList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " +LogDatabaseHelper.TABLE_NAME+ " WHERE "
                +LogDatabaseHelper.COLUMN_START_MONTH+" = "+ month + " AND "
                +LogDatabaseHelper.COLUMN_START_YEAR+" = " +year+";";




        return null;
    }
}
