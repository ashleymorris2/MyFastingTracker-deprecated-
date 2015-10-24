package com.avaygo.myfastingtracker.classes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.avaygo.myfastingtracker.notifications.AlarmSetup;

import java.util.Calendar;

/**
 * Created by Ash on 23/10/2015.
 *
 * Manages the setting of notifications and the starting of a fasting period
 */
public class FastingManager implements IFastingManager {

    AlarmSetup alarm = new AlarmSetup();

    @Override
    public boolean startFast(Context context, long startTime, long endTime, int duration) {

        Calendar alarmTime = Calendar.getInstance();
        alarmTime.setTimeInMillis(endTime);

        alarm.setAlarmTime(alarmTime);
        alarm.createAlarm(context);

        //Shared preferences, stores the current state on the button press to save the activity's session.
        SharedPreferences preferences = context.getSharedPreferences("appData", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("IS_FASTING", true);
        editor.putLong("START_TIME", System.currentTimeMillis());
        editor.putLong("END_TIME", endTime);
        editor.putInt("END_HOUR", duration);

        return editor.commit();
    }
}
