package com.avaygo.myfastingtracker;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Ash on 25/06/2014.
 */
public class cNotificationSetup {

    Calendar reminderCalendar;
    SimpleDateFormat reminderTimeFormat = new SimpleDateFormat("HH:mm:ss EE");

    public Calendar getReminderCalendar() {
        return reminderCalendar;
    }

    public void setReminderCalendar(Calendar reminderCalendar) {
        this.reminderCalendar = reminderCalendar;
    }

    public void createAlarm(Activity activity) {

        Intent myIntent = new Intent(activity, myNotification.class);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(activity, 0 ,myIntent, 0);

        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.set(Calendar.DATE,reminderCalendar.get(Calendar.DATE));
        alarmCalendar.set(Calendar.HOUR_OF_DAY, reminderCalendar.get(Calendar.HOUR_OF_DAY));
        alarmCalendar.set(Calendar.MINUTE,reminderCalendar.get(Calendar.MINUTE) );
        alarmCalendar.set(Calendar.SECOND, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(activity,"Fasting started, End at " + reminderTimeFormat.format(alarmCalendar.getTime()).toString(), Toast.LENGTH_LONG).show();
    }
}
