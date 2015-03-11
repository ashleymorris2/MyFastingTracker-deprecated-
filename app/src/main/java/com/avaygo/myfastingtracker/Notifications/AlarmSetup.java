package com.avaygo.myfastingtracker.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Ash on 25/06/2014.
 *
 * Class to set up alarms to fire off the notifications for the fasting application
 *
 */
public class AlarmSetup {

   private Calendar mAlarmTime;
   private SimpleDateFormat reminderTimeFormat = new SimpleDateFormat("HH:mm EEEE");

   public AlarmSetup(){
        
    }

    public void setAlarmTime(Calendar reminderCalendar) {
        this.mAlarmTime = reminderCalendar;
    }

    /**
     * Creates an intent that will be called at a future time. Our intent here is the notification service.
     * AlarmManager is used to set the time for when the intent is called.
     * Pending intent is an intent that will be called at a later time.
     *
     * @param context the context of the calling activity
     */
    public void createAlarm(Context context) {

        Calendar alarmCalendar = Calendar.getInstance();

        Intent myIntent = new Intent(context, TimerNotificationService.class);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(context, 1 ,myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmCalendar.set(Calendar.DAY_OF_YEAR, mAlarmTime.get(Calendar.DAY_OF_YEAR));
        alarmCalendar.set(Calendar.HOUR_OF_DAY, mAlarmTime.get(Calendar.HOUR_OF_DAY));
        alarmCalendar.set(Calendar.MINUTE, mAlarmTime.get(Calendar.MINUTE));
        alarmCalendar.set(Calendar.SECOND, mAlarmTime.get(Calendar.SECOND));

        if(Build.VERSION.SDK_INT >= 19) {
           alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);

            //Allow a 1 second maximum variation, testing purposes
           // alarmManager.setWindow(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), 1000, pendingIntent);
        }
       else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
        }
    }

    public void cancelAlarm(Context context){
        try {
            //Re-calls and then cancels the future intent.
            Intent myIntent = new Intent(context, TimerNotificationService.class);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getService(context,1, myIntent,0);

            alarmManager.cancel(pendingIntent);
            Toast.makeText(context, "Fast Cancelled", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){

        }
    }
}
