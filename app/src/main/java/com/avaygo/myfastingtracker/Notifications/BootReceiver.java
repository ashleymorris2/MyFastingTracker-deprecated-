package com.avaygo.myfastingtracker.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.avaygo.myfastingtracker.Adapters.cReminder;
import com.avaygo.myfastingtracker.Databases.AlarmsDataSource;

import java.util.Calendar;

/**
 * BootReceiver is called to reset the notifications when the phone has been reset.
 * It is registered in the android manifest to receive the ON_BOOT_COMPLETED intent
 *
* Ashley Morris
**/
public class BootReceiver extends BroadcastReceiver {

    private AlarmsDataSource mAlarmsDataSource;
    private Calendar alarmCalendar = Calendar.getInstance();


    public BootReceiver() {
    }

    public void onReceive(Context context, Intent intent) {

        AlarmSetup alarm = new AlarmSetup();//Used to set the notification reminder.
        RecurringAlarmSetup recurringAlarm = new RecurringAlarmSetup();

        mAlarmsDataSource  = new AlarmsDataSource(context.getApplicationContext());

        mAlarmsDataSource.open();


        //Open a shared preferences session to retrieve the alarm end time.
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("appData", 0); // 0 - for private mode
        SharedPreferences preferences2 = context.getApplicationContext().getSharedPreferences("userPref", 0); // 0 - for private mode

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
                cReminder mReminder =  mAlarmsDataSource.getAlarm(i);

                if(mReminder.isEnabled()){
                    recurringAlarm.createRecurringAlarm(context.getApplicationContext(), mReminder.getStartTime(),
                            mReminder.get_id());
                }
            }

            mAlarmsDataSource.close();
        }
    }
}
