package com.avaygo.myfastingtracker.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.avaygo.myfastingtracker.classes.Reminder;
import com.avaygo.myfastingtracker.databases.AlarmsDataSource;

import java.util.Calendar;

/**
 * BootReceiver is called to reset the notifications when the phone has been reset.
 * It is registered in the android manifest to receive the ON_BOOT_COMPLETED intent
 *
* Ashley Morris
**/
public class BootReceiver extends BroadcastReceiver {

    private AlarmsDataSource alarmsDataSource;
    private Calendar alarmCalendar = Calendar.getInstance();

    public BootReceiver() {
    }

    public void onReceive(Context context, Intent intent) {

        AlarmSetup alarm = new AlarmSetup();//Used to set the notification reminder.
        RecurringAlarmSetup recurringAlarm = new RecurringAlarmSetup();

        alarmsDataSource = new AlarmsDataSource(context.getApplicationContext());
        alarmsDataSource.open();

        //Open a shared preferences session to retrieve the alarm end time.
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("appData", 0);
        SharedPreferences preferences2 = context.getApplicationContext().getSharedPreferences("userPref", 0);

        long endMill = preferences.getLong("END_TIME", 0);// @Param endMill the end time in milliseconds
        Boolean listEnabled = preferences2.getBoolean("listEnabled", false);

       //Checks that the shared preferences has actually been set.
       if(endMill > 0) {
           alarmCalendar.setTimeInMillis(endMill);
           alarm.setAlarmTime(alarmCalendar);
           alarm.createAlarm(context.getApplicationContext());
       }

        if(listEnabled){
            for(int i = 1; i <=7; i++){
                Reminder mReminder =  alarmsDataSource.getAlarm(i);

                if(mReminder.isEnabled()){
                    recurringAlarm.createRecurringAlarm(context.getApplicationContext(), mReminder.getStartTime(),
                            mReminder.get_id());
                }
            }

            alarmsDataSource.close();
        }
    }
}
