package com.avaygo.myfastingtracker.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
* BootReceiver is called to reset the notifications when the phone has been reset.
 * It is registered in the android manifest to recieve the BOOT_COMPLETED intent
 *
 *
* Ashley Morris
**/
public class BootReceiver extends BroadcastReceiver {

    public BootReceiver() {
    }

    public void onReceive(Context context, Intent intent) {



    }

}
