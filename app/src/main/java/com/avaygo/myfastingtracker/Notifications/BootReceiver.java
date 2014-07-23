package com.avaygo.myfastingtracker.Notifications;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.avaygo.myfastingtracker.Activities.FastingLogActivity;

import java.util.Calendar;

/**
 * BootReceiver is called to reset the notifications when the phone has been reset.
 * It is registered in the android manifest to receive the BOOT_COMPLETED intent
 *
 *
* Ashley Morris
**/
public class BootReceiver extends BroadcastReceiver {

    private Calendar alarmCalendar = Calendar.getInstance();

    public BootReceiver() {
    }

    public void onReceive(Context context, Intent intent) {

        cNotificationSetup myNotification = new cNotificationSetup();//Used to set the notification reminder.

        //Open a shared preferences session to retrieve the alarm end time.
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("appData", 0); // 0 - for private mode
        long endMill = preferences.getLong("END_TIME", 0);// @Param endMill the end time in milliseconds

        //Checks that the shared preferences has actually been set.
       if(endMill > 0) {
           alarmCalendar.setTimeInMillis(endMill);
           myNotification.setReminderCalendar(alarmCalendar);
           myNotification.createAlarm(context.getApplicationContext());
       }
    }

}
