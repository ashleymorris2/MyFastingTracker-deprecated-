package com.avaygo.myfastingtracker.classes;

import java.util.Calendar;

/**
 * Created by Ash on 11/03/2015.
 */
public class FastingRecord {

    private long id;
    private int startDay;
    private int startMonth;
    private int startYear;
    private int endDay;
    private int endMonth;
    private int endYear;

    private int fastDuration;

    private Calendar startTimeStamp;
    private Calendar endTimeStamp;
    private Calendar logTimeStamp;

    private int percentageComplete;
    private String userNote;
    private int userRating;


    public FastingRecord() {
        startTimeStamp = Calendar.getInstance();
        endTimeStamp = Calendar.getInstance();
        logTimeStamp = Calendar.getInstance();
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public void setFastDuration(int fastDuration) {
        this.fastDuration = fastDuration;
    }


    public void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp.setTimeInMillis(startTimeStamp);
    }

    public void setEndTimeStamp(long endTimeStamp) {
        this.endTimeStamp.setTimeInMillis(endTimeStamp);
    }

    public void setLogTimeStamp(long logTimeStamp) {
        this.logTimeStamp.setTimeInMillis(logTimeStamp);
    }


    public void setPercentageComplete(int percentageComplete) {
        this.percentageComplete = percentageComplete;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }


    public long getId() {
        return id;
    }

    public int getStartDay() {
        return startDay;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndDay() {
        return endDay;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public int getEndYear() {
        return endYear;
    }

    public int getFastDuration() {
        return fastDuration;
    }

    public Calendar getStartTimeStamp() {
        return startTimeStamp;
    }

    public Calendar getEndTimeStamp() {
        return endTimeStamp;
    }

    public Calendar getLogTimeStamp() {
        return logTimeStamp;
    }

    public int getPercentageComplete() {
        return percentageComplete;
    }

    public String getUserNote() {
        return userNote;
    }

    public int getUserRating() {
        return userRating;
    }
}
