package com.avaygo.myfastingtracker.Notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;



/**
 * Created by Ash on 10/09/2014.
 */
public class RecurringAlarmSetup {

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private Calendar mTime, mTimeNow;

    //SimpleDateFormat testFormat = new SimpleDateFormat("HH:mm:ss, EEE, d/M/y, D ");


    public RecurringAlarmSetup() {

        mTime = Calendar.getInstance();

    }

    public void createRecurringAlarm(Context context, Calendar calendar, int id){

        Calendar alarmCalendar = Calendar.getInstance();
        mTime.setTimeInMillis(calendar.getTimeInMillis());

        mTimeNow = Calendar.getInstance();
        mTimeNow.setTimeInMillis(System.currentTimeMillis());

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, RecurringNotificationReciever.class);

        //So that the RecurringNotificationReceiver class can access the right element in the database
        intent.putExtra("_id", id);

        alarmIntent = PendingIntent.getBroadcast(context, id , intent , PendingIntent.FLAG_UPDATE_CURRENT);

        alarmCalendar.set(Calendar.DAY_OF_YEAR, mTime.get(Calendar.DAY_OF_YEAR));
        alarmCalendar.set(Calendar.DAY_OF_WEEK, mTime.get(Calendar.DAY_OF_WEEK));
        alarmCalendar.set(Calendar.HOUR_OF_DAY, mTime.get(Calendar.HOUR_OF_DAY));
        alarmCalendar.set(Calendar.MINUTE, mTime.get(Calendar.MINUTE));
        alarmCalendar.set(Calendar.SECOND, 0);

        //If the alarm is in the past then add a specific amount of time to set it in the future.
        while (alarmCalendar.getTimeInMillis() <= mTimeNow.getTimeInMillis()){
            alarmCalendar.add(Calendar.DAY_OF_YEAR, 7);
        }

        try {
            //Set the alarm to call on a weekly basis.
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), alarmIntent);
        }
        catch (NoSuchMethodError e){
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), alarmIntent);
        }
    }

    public void cancelRecurringAlarm(Context context, int id){

        try {
            //Re-calls and then cancels the future intent.
            Intent intent = new Intent(context, RecurringNotificationReciever.class);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.cancel(alarmIntent);

        }
        catch (Exception e){
            Log.e("cancelAlarm method error:", e.toString());
        }
    }

    }
