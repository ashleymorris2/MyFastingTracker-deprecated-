package com.avaygo.myfastingtracker.Adapters;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ash on 10/08/2014.
 * A class to hold Day objects for the list view.
 */
public class cReminder {

    private int _id;
    private String mDayName;
    private Calendar mStartTime;
    private Calendar mEndTime;
    private int mFastLength;
    private int mEnabled;

    public cReminder() {

    }


    public void set_id(int _id) {
        this._id = _id;
    }

    public void setDayName(String mDayName) {
        this.mDayName = mDayName;
    }

    public void setStartTime(Calendar mStartTime) {
        this.mStartTime = mStartTime;
    }

    public void setEndTime(Calendar mEndTime) {
        this.mEndTime = mEndTime;
    }

    public void setFastLength(int mFastLength) {
        this.mFastLength = mFastLength;
    }

    public void setEnabled(int mEnabled) {
        this.mEnabled = mEnabled;
    }


    public int get_id() {
        return _id;
    }

    public String getDayName() {
        return mDayName;
    }

    public Calendar getStartTime() {
        return mStartTime;
    }

    public Calendar getEndTime() {
        return mEndTime;
    }

    public int getFastLength() {
        return mFastLength;
    }

    public int isEnabled() {
        return mEnabled;
    }
}
