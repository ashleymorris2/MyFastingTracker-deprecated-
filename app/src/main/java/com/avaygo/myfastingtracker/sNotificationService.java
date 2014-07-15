package com.avaygo.myfastingtracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

public class sNotificationService extends Service {
    public sNotificationService() {
    }

    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void onCreate() {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent myIntent = new Intent(this.getApplicationContext(), FastingTrackerActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, myIntent, 0);

        Notification mNotify = new Notification.Builder(this)
                .setContentTitle("Fast completed")
                .setContentText("It's time to get something to eat.")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)
                .setSound(sound)
                .build();

        mNotify.flags = Notification.FLAG_AUTO_CANCEL;

        mNM.notify(2, mNotify);

        stopSelf();

    }
}
