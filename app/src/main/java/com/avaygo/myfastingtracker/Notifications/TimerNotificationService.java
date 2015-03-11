package com.avaygo.myfastingtracker.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.avaygo.myfastingtracker.R;
import com.avaygo.myfastingtracker.Activities.MainActivity;

public class TimerNotificationService extends Service {

    public TimerNotificationService() {

    }

    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void onCreate() {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        //Sets a pending intent that is called when the notification is selected
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(this)

                //Lollipop specific notification code
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)

                .setContentTitle("Fast completed")
                .setContentText("It's time to get something to eat.")
                .setSmallIcon(R.drawable.ic_launcher)
                .setVibrate(new long[] { 1000, 500, 1000 })
                .setContentIntent(pIntent)
                .setSound(sound)
                .build();

        notification.flags = Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(2, notification);

        stopSelf();
    }
}
