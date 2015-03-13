package com.avaygo.myfastingtracker.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avaygo.myfastingtracker.R;
import com.avaygo.myfastingtracker.classes.FastingRecord;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.HashMap;
import java.util.List;

import hirondelle.date4j.DateTime;

public class CaldroidCustomAdapter extends CaldroidGridAdapter {


    private List<FastingRecord> fastingRecords;


    public CaldroidCustomAdapter(Context context, int month, int year,
                                       HashMap<String, Object> caldroidData,
                                       HashMap<String, Object> extraData) {

        super(context, month, year, caldroidData, extraData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;

        // For reuse
        if (convertView == null) {
            cellView = inflater.inflate(R.layout.custom_cell, null);
        }

        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        TextView cellText1 = (TextView) cellView.findViewById(R.id.cell_text1);

        // Get dateTime of this cell
        DateTime dateTime = this.datetimeList.get(position);
        Resources resources = context.getResources();



        cellText1.setText("" + dateTime.getDay());
        if (dateTime.equals(getToday())) {
            cellText1.setPaintFlags(cellText1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }

        cellView.setVisibility(View.VISIBLE);

        // Set color of the dates in previous / next month
        if (dateTime.getMonth() != month) {

            cellText1.setText("");
            cellView.setVisibility(View.INVISIBLE);
        }

        boolean shouldResetDiabledView = false;
        boolean shouldResetSelectedView = false;

        // Customize for disabled dates and date outside min/max dates
        if ((minDateTime != null && dateTime.lt(minDateTime))
                || (maxDateTime != null && dateTime.gt(maxDateTime))
                || (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

            cellText1.setTextColor(CaldroidFragment.disabledTextColor);
            if (CaldroidFragment.disabledBackgroundDrawable == -1) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
            } else {
                cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
            }

            //Border for today's date
            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);
            }

        } else {
            shouldResetDiabledView = true;
        }

        // Customize for selected dates
        if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
            if (CaldroidFragment.selectedBackgroundDrawable != -1) {
                cellView.setBackgroundResource(CaldroidFragment.selectedBackgroundDrawable);
            } else {
                cellView.setBackgroundResource(R.drawable.date_selector);
            }

            //cellText1.setTextColor(CaldroidFragment.selectedTextColor);

        } else {
            shouldResetSelectedView = true;
        }

        if (shouldResetDiabledView && shouldResetSelectedView) {
            // Customize for today
            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(R.drawable.cell_bg);

            } else {
                cellView.setBackgroundResource(R.drawable.cell_bg);
            }
        }

        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);

        // Set custom color if required
        setCustomResources(dateTime, cellView, cellText1);

        return cellView;
    }

    public void setFastingRecords(List<FastingRecord> fastingRecords) {
        this.fastingRecords = fastingRecords;
    }
}