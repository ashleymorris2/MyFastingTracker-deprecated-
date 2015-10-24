package com.avaygo.myfastingtracker.notifications;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.avaygo.myfastingtracker.classes.FastingManager;

import java.util.Calendar;

public class PostponeFastReciever extends BroadcastReceiver {

    AlarmManager alarmManager;
    FastingManager fastingManager = new FastingManager();

    public PostponeFastReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(1);

        switch (action) {
            case "NOW_ACTION":
                fastingManager.startFast(context, System.currentTimeMillis(),
                        intent.getLongExtra("END_TIME", 0), intent.getIntExtra("END_HOUR", 1));
                break;

            case "POSTPONE_1_ACTION":
                postponeFast(context, 5, intent.getIntExtra("_id", -1));
                break;

            case "POSTPONE_2_ACTION":
                postponeFast(context, 10, intent.getIntExtra("_id", -1));
                break;
        }
    }

    //Sets a notification (alarm) to ring in (currant time + duration = minutes)
    //The id relates to the unique identifier of the fast that is being postponed.
    private void postponeFast(Context context, int duration, int id) {

        PendingIntent pendingIntent;
        Intent intent = new Intent(context, RecurringNotificationReceiver.class);
        intent.putExtra("_id", id);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + duration);

        pendingIntent = PendingIntent.getBroadcast(context, 40, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //Sets the alarm to be exact if available, if not it will use the method from the older API
        if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}
