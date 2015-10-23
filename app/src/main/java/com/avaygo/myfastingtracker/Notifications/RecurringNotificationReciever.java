package com.avaygo.myfastingtracker.notifications;

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
import android.support.v4.app.NotificationCompat;

import com.avaygo.myfastingtracker.R;
import com.avaygo.myfastingtracker.activities.MainActivity;
import com.avaygo.myfastingtracker.classes.Reminder;
import com.avaygo.myfastingtracker.databases.AlarmsDataSource;

import java.util.Calendar;

/**
 * Created by Ash on 10/09/2014.
 */
public class RecurringNotificationReciever extends BroadcastReceiver {

    private RecurringAlarmSetup recurringAlarm;
    private AlarmsDataSource alarmsDataSource;
    private Reminder reminder;
    private Calendar alarmTime, timeNow;
    private long timeLeft;

    public RecurringNotificationReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();

        int alarmId = extras.getInt("_id");
        boolean timerStarted;
        long timeLeftInMill, endMill;

        SharedPreferences preferences = context.getSharedPreferences("appData", 0);
        timerStarted = preferences.getBoolean("TIMER_START", false);
        timeLeftInMill = preferences.getLong("END_MILLISEC", 0);
        endMill = preferences.getLong("END_TIME", 0);


        alarmTime = Calendar.getInstance();
        timeNow = Calendar.getInstance();

        timeNow.setTimeInMillis(System.currentTimeMillis());

        recurringAlarm = new RecurringAlarmSetup();
        alarmsDataSource = new AlarmsDataSource(context);

        //Recalls the saved alarm from the database with the supplied ID, in order to set the alarms
        alarmsDataSource.open();

        reminder = new Reminder();
        reminder = alarmsDataSource.getAlarm(alarmId);

        //Action Pending Intents
        //For the buttons on the notification

        //Now Intent
        Intent nowReceive = new Intent(context, PostponeFastReciever.class);
        nowReceive.setAction("NOW_ACTION");
        nowReceive.putExtra("_id", alarmId);
        nowReceive.putExtra("END_TIME", endMill);
        PendingIntent pendingIntentNow = PendingIntent.getBroadcast(context, 1, nowReceive,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //Postpone 1 Intent
        Intent postponeOneReceive = new Intent(context, PostponeFastReciever.class);
        postponeOneReceive.setAction("POSTPONE_1_ACTION");
        postponeOneReceive.putExtra("_id", alarmId);
        PendingIntent pendingIntentPostponeOne = PendingIntent.getBroadcast(context, 1, postponeOneReceive,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //Postpone 2 Intent
        Intent postponeTwoReceive = new Intent(context, PostponeFastReciever.class);
        postponeTwoReceive.setAction("POSTPONE_2_ACTION");
        postponeTwoReceive.putExtra("_id", alarmId);
        PendingIntent pendingIntentPostponeTwo = PendingIntent.getBroadcast(context, 1, postponeTwoReceive,
                PendingIntent.FLAG_UPDATE_CURRENT);


        //Checks if the user is currently fasting, if so the remaining time is calculated.
        if (timerStarted) {
            timeLeftInMill = endMill - System.currentTimeMillis();

            //If the time left in milliseconds is less than 0 than time left will be set as 0
            //This is because time left can be a negative number, but we only want to work with zero
            //or greater.
            timeLeft = Math.max(0, timeLeftInMill);
        }

        //Only show a notification to the user if they are not currently fasting or the timer is
        // complete. Else skip and reschedule a new one
        if(!timerStarted) {

            //Change for user selected one.
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            //Sets a pending intent that is called when the notification is selected
            Intent myIntent = new Intent(context, MainActivity.class);

            //Fragment number is the screen for the timer setting screen, in this case it is 1.
            myIntent.putExtra("fromReminder", true);
            myIntent.putExtra("fragmentNumber", 1);
            myIntent.putExtra("duration", reminder.getFastLength());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(context)

                    //Lollipop specific notification code
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)

                    .setContentTitle("It's " + reminder.getDayName())
                    .setContentText("Time to start your " +
                            reminder.getFastLength() + " hour fast")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setVibrate(new long[]{1000, 500, 1000, 500, 1000})
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setSound(sound)
                    .addAction(R.drawable.ic_action_action_alarm, "NOW", pendingIntentNow)//Start from notification

                    //Change the +1 and +5 for the user selected values at a later time
                    .addAction(R.drawable.ic_action_action_alarm, "+ 5", pendingIntentPostponeOne)//Postpone depending on user setting
                    .addAction(R.drawable.ic_action_action_alarm, "+ 10", pendingIntentPostponeTwo)//Postpone 2
                    .build();

            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(1, notification);
        }

        /*Debugging code. To test if the timer is being stopped correctly. Remove once tested and use
          the one if statement above.*/
        else if(timeLeft == 0) {

            //Change for user selected one.
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            //Sets a pending intent that is called when the notification is selected
            Intent myIntent = new Intent(context, MainActivity.class);

            //Fragment number is the screen for the timer setting screen, in this case it is 1.
            myIntent.putExtra("fromReminder", true);
            myIntent.putExtra("fragmentNumber", 1);
            myIntent.putExtra("duration", reminder.getFastLength());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(context)

                    //Lollipop specific notification code
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                    .setContentTitle("It's " + reminder.getDayName())
                    .setContentText("Time to start your next fast for " +
                            reminder.getFastLength() + " hours.")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setVibrate(new long[]{1000, 500, 1000})
                    .setContentIntent(pendingIntent)
                    .setSound(sound)
                    .build();

            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(1, notification);
        }

        //Reschedule with a 7 day break
        alarmTime.set(Calendar.DAY_OF_YEAR, reminder.getStartTime().get(Calendar.DAY_OF_YEAR) + 7);
        alarmTime.set(Calendar.DAY_OF_WEEK, reminder.getStartTime().get(Calendar.DAY_OF_WEEK));
        alarmTime.set(Calendar.HOUR_OF_DAY, reminder.getStartTime().get(Calendar.HOUR_OF_DAY));
        alarmTime.set(Calendar.MINUTE, reminder.getStartTime().get(Calendar.MINUTE));
        alarmTime.set(Calendar.SECOND, 0);

        //Saves the new time to the database in case it needs to be recalled in the boot receiver
        alarmsDataSource.updateAlarm(alarmId, alarmTime, reminder.getFastLength(), reminder.getEndTime());
        recurringAlarm.createRecurringAlarm(context, alarmTime, alarmId);

        alarmsDataSource.close();
    }
}
