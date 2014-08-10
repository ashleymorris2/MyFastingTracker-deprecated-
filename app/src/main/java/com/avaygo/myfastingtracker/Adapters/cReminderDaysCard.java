package com.avaygo.myfastingtracker.Adapters;

import java.util.Date;

/**
 * Created by Ash on 10/08/2014.
 * A class to hold Day objects for the list view.
 */
public class cReminderDaysCard {

    private String mDayName;
    private Date mStartTime;
    private Date mEndTime;
    private int mFastLength;


    public cReminderDaysCard(String mDayName) {
        this.mDayName = mDayName;
    }

    public String getmDayName() {
        return mDayName;
    }

    public Date getmStartTime() {
        return mStartTime;
    }

    public Date getmEndTime() {
        return mEndTime;
    }

    public int getmFastLength() {
        return mFastLength;
    }
}
