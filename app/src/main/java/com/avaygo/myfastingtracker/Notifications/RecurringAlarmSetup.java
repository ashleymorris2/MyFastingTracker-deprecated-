package com.avaygo.myfastingtracker.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
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
    private Calendar alarmTime, timeNow;

    SimpleDateFormat testFormat = new SimpleDateFormat("HH:mm:ss, EEE, d/M/y, D ");


    public RecurringAlarmSetup() {

        alarmTime = Calendar.getInstance();
        timeNow = Calendar.getInstance();

    }

    public void createRecurringAlarm(Context context, Calendar calendar, int id){

        Calendar alarmCalendar = Calendar.getInstance();
        alarmTime.setTimeInMillis(calendar.getTimeInMillis());

        timeNow.setTimeInMillis(System.currentTimeMillis());

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, RecurringNotificationReciever.class);

        //So that the RecurringNotificationReceiver class can access the right element in the database
        intent.putExtra("_id", id);

        alarmIntent = PendingIntent.getBroadcast(context, id , intent , PendingIntent.FLAG_UPDATE_CURRENT);

        alarmCalendar.set(Calendar.DAY_OF_YEAR, alarmTime.get(Calendar.DAY_OF_YEAR));
        alarmCalendar.set(Calendar.DAY_OF_WEEK, alarmTime.get(Calendar.DAY_OF_WEEK));
        alarmCalendar.set(Calendar.HOUR_OF_DAY, alarmTime.get(Calendar.HOUR_OF_DAY));
        alarmCalendar.set(Calendar.MINUTE, alarmTime.get(Calendar.MINUTE));
        alarmCalendar.set(Calendar.SECOND, 0);

        //If the alarm is in the past then add a specific amount of time to set it in the future.
        //In this case it is 7 days, to set it for next week
        if (alarmCalendar.getTimeInMillis() <= timeNow.getTimeInMillis()){
            alarmCalendar.add(Calendar.DAY_OF_YEAR, 7);
        }

        Toast.makeText(context.getApplicationContext(), testFormat.format(alarmCalendar.getTime()),
                Toast.LENGTH_LONG).show();

        //Sets the alarm to be exact if available, if not it will use the method from the older API
        if(Build.VERSION.SDK_INT >= 19) {
           alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), alarmIntent);
           //alarmManager.setWindow(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), 1000, alarmIntent);
        }
        else {
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
            Log.e("cancelAlarm error:", e.toString());
        }
    }

    }
