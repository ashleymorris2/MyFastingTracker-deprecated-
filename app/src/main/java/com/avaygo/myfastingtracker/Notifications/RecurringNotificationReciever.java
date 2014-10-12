package com.avaygo.myfastingtracker.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import com.avaygo.myfastingtracker.Activities.FastingTrackerActivity;
import com.avaygo.myfastingtracker.Adapters.cReminder;
import com.avaygo.myfastingtracker.Databases.AlarmsDataSource;
import com.avaygo.myfastingtracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Ash on 10/09/2014.
 */
public class RecurringNotificationReciever extends BroadcastReceiver {

    private RecurringAlarmSetup  mRecurringAlarm;
    private AlarmsDataSource mAlarmsDataSource;
    private cReminder mReminder;
    private Calendar mAlarmTime, mTimeNow;

    SimpleDateFormat hourMinuteFormat = new SimpleDateFormat("HH:mm");


    public RecurringNotificationReciever() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();

        int _id = extras.getInt("_id");

        mAlarmTime = Calendar.getInstance();
        mTimeNow = Calendar.getInstance();
        mTimeNow.setTimeInMillis(System.currentTimeMillis());

        mRecurringAlarm = new RecurringAlarmSetup();
        mAlarmsDataSource = new AlarmsDataSource(context);
        mReminder = new cReminder();

        //Recalls the alarm from the database in order to set the alarms
        mAlarmsDataSource.open();
        mReminder = mAlarmsDataSource.getAlarm(_id);


        SharedPreferences preferences = context.getSharedPreferences("appData", 0);
        boolean timerStarted = preferences.getBoolean("TIMER_START", false);


        //Only show a notification to the user if they are not currently fasting
        // else skip and reschedule a new one
        if(!timerStarted) {

            //Change for user selected one.
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationManager mNM = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            //Sets a pending intent that is called when the notification is selected
            Intent myIntent = new Intent(context, FastingTrackerActivity.class);

            //Fragment number is the screen for the timer setting screen, in this case it is 1.
            myIntent.putExtra("fromReminder", true);
            myIntent.putExtra("fragmentNumber", 1);
            myIntent.putExtra("duration", mReminder.getFastLength());

            PendingIntent pIntent = PendingIntent.getActivity(context, 0,
                    myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification mNotify = new Notification.Builder(context)
                    .setContentTitle("It's " + mReminder.getDayName())
                    .setContentText("Time to start your " +
                            mReminder.getFastLength() + " hour fast")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setVibrate(new long[]{1000, 500, 1000})
                    .setContentIntent(pIntent)
                    .setSound(sound)
                    .build();

            mNotify.flags = Notification.FLAG_AUTO_CANCEL;
            mNM.notify(1, mNotify);
        }


        //Reschedule with a 7 day break
        mAlarmTime.set(Calendar.DAY_OF_YEAR, mReminder.getStartTime().get(Calendar.DAY_OF_YEAR) + 7);
        mAlarmTime.set(Calendar.DAY_OF_WEEK, mReminder.getStartTime().get(Calendar.DAY_OF_WEEK));
        mAlarmTime.set(Calendar.HOUR_OF_DAY, mReminder.getStartTime().get(Calendar.HOUR_OF_DAY));
        mAlarmTime.set(Calendar.MINUTE, mReminder.getStartTime().get(Calendar.MINUTE));
        mAlarmTime.set(Calendar.SECOND, 0);

        //Saves the new time to the database in case it needs to be recalled in the boot receiver
        mAlarmsDataSource.updateAlarm(_id, mAlarmTime, mReminder.getFastLength(),mReminder.getEndTime());

        mRecurringAlarm.createRecurringAlarm(context, mAlarmTime, _id );

        mAlarmsDataSource.close();
    }

}
