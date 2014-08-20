package com.avaygo.myfastingtracker.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.avaygo.myfastingtracker.Adapters.cReminder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ash on 18/08/2014.
 */
public class AlarmsDataSource {

    private SQLiteDatabase database;
    private ReminderAlarmsHelper dbHelper;

    public AlarmsDataSource(Context context){
        dbHelper = new ReminderAlarmsHelper(context);
    }

    public void open(){
        //Opens a writable database
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    /**
    Initialises the alarm list for first time usage.
        @param  day is the day of the alarm
        @param  start is the time the  alarm is set for
        @param duration is how long between the start and the end
        @param  end is the time the fast is due to end at
    */
    public void initialiseAlarms(String day, Calendar start, int duration, Calendar end){

        ContentValues values = new ContentValues();
        values.put(ReminderAlarmsHelper.COLUMN_DAY, day);
        values.put(ReminderAlarmsHelper.COLUMN_START, start.getTimeInMillis());
        values.put(ReminderAlarmsHelper.COLUMN_END, end.getTimeInMillis());
        values.put(ReminderAlarmsHelper.COLUMN_LENGTH, duration);
        values.put(ReminderAlarmsHelper.COLUMN_ENABLED, 0);

        database.insert(ReminderAlarmsHelper.TABLE_NAME, null, values);
    }

    //CRUD Operations:
    public cReminder getReminder(int id){

        return null;
    }

    //Returns all reminders from the database in an array list
    public List <cReminder> getAllReminders(){

        Calendar calendar = Calendar.getInstance();

        List <cReminder> reminderList = new ArrayList<cReminder>();
        String selectQuery = "SELECT * FROM " + ReminderAlarmsHelper.TABLE_NAME;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                cReminder reminder = new cReminder();

                reminder.set_id(cursor.getInt(0));
                reminder.setDayName(cursor.getString(1));

                calendar.setTimeInMillis(cursor.getLong(2));
                reminder.setStartTime(calendar);

                calendar.setTimeInMillis(cursor.getLong(3));
                reminder.setEndTime(calendar);

                reminder.setFastLength(cursor.getInt(4));
                reminder.setEnabled(cursor.getInt(5));

                reminderList.add(reminder);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return reminderList;
    }

    public int getAlarmsCount(){

        int count;

        String countQuery = "SELECT  * FROM " + ReminderAlarmsHelper.TABLE_NAME;
        Cursor cursor = database.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        return count;
    }



}
