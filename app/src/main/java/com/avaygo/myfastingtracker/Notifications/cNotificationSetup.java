package com.avaygo.myfastingtracker.Notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Ash on 25/06/2014.
 *
 * Class to set up notifications for the fasting application
 *
 *
 */
public class cNotificationSetup {

    Calendar reminderCalendar;
    SimpleDateFormat reminderTimeFormat = new SimpleDateFormat("HH:mm EEEE");

    public cNotificationSetup(){

    }

    public void setReminderCalendar(Calendar reminderCalendar) {
        this.reminderCalendar = reminderCalendar;
    }

    public Calendar getReminderCalendar() {
        return reminderCalendar;
    }

    public void createAlarm(Context context) {
        //Creates an intent that will be called at a future time. Our intent here is the notification service.
        //AlarmManager is used to set the time for when the intent is called.
        //Pending intent is an intent that will be called at a later time.

        Intent myIntent = new Intent(context, TimerNotificationService.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0 ,myIntent, 0);

        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.set(Calendar.DATE,reminderCalendar.get(Calendar.DATE));
        alarmCalendar.set(Calendar.HOUR_OF_DAY, reminderCalendar.get(Calendar.HOUR_OF_DAY));
        alarmCalendar.set(Calendar.MINUTE,reminderCalendar.get(Calendar.MINUTE) );
        alarmCalendar.set(Calendar.SECOND,reminderCalendar.get(Calendar.SECOND));

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(context,"Ending fast at: " + reminderTimeFormat.format(alarmCalendar.getTime()).toString(), Toast.LENGTH_SHORT).show();
    }
}
