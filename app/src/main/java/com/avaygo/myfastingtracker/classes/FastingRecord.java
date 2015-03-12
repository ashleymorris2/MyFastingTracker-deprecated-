package com.avaygo.myfastingtracker.classes;

import java.util.Calendar;

/**
 * Created by Ash on 11/03/2015.
 */
public class FastingRecord {

    private int id;
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
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStartDay() {
        return startDay;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndDay() {
        return endDay;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public int getFastDuration() {
        return fastDuration;
    }

    public void setFastDuration(int fastDuration) {
        this.fastDuration = fastDuration;
    }

    public Calendar getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(Calendar startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public Calendar getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(Calendar endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public Calendar getLogTimeStamp() {
        return logTimeStamp;
    }

    public void setLogTimeStamp(Calendar logTimeStamp) {
        this.logTimeStamp = logTimeStamp;
    }

    public int getPercentageComplete() {
        return percentageComplete;
    }

    public void setPercentageComplete(int percentageComplete) {
        this.percentageComplete = percentageComplete;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }
}
