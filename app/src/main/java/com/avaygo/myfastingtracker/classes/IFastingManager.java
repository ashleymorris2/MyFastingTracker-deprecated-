package com.avaygo.myfastingtracker.classes;

import android.content.Context;

/**
 * Created by Ash on 23/10/2015.
 */
public interface IFastingManager {

    boolean startFast(Context context, long startTime, long endTime, int duration);
}
