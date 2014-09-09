package com.avaygo.myfastingtracker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.avaygo.myfastingtracker.Databases.AlarmsDataSource;
import com.avaygo.myfastingtracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ash on 10/08/2014.
 * Override of an array adapter.
 *
 * The Constructor takes two parameters. The context of the calling application and a list of cReminder objects.
 *
 */
public class DaysListAdapter extends ArrayAdapter<cReminder> {

    private  Context context;
    private List <cReminder> mReminderDaysCard;

    private AlarmsDataSource mAlarmsDataSource = new AlarmsDataSource(getContext());

    SimpleDateFormat hourMinuteFormat = new SimpleDateFormat("HH:mm");
    SimpleDateFormat hourMinuteDayFormat = new SimpleDateFormat("HH:mm, EEEE");

    public DaysListAdapter(Context context, List <cReminder> reminderDaysCard) {
        super(context, R.layout.listview_reminder_day, reminderDaysCard);
        this.context = context;
        this.mReminderDaysCard = reminderDaysCard;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = convertView;

        //Make sure that there is a view to work with in case we have been given null.
        if (itemView == null){
            itemView = inflater.inflate(R.layout.listview_reminder_day, parent, false);
        }

        //Find the current cardView to work with.
        final cReminder currentCard = mReminderDaysCard.get(position);

        TextView textDay = (TextView) itemView.findViewById(R.id.item_text_reminderday);
        TextView textStart = (TextView) itemView.findViewById(R.id.item_text_start_datetime);
        TextView textEnd = (TextView) itemView.findViewById(R.id.item_text_end_datetime);
        TextView textDuration = (TextView) itemView.findViewById(R.id.item_text_fasting_duration);
        Switch switchReminderToggle = (Switch) itemView.findViewById(R.id.switch_reminder_toggle);

        //Fill the view
        textDay.setText(currentCard.getDayName());
        textStart.setText(hourMinuteFormat.format(currentCard.getStartTime().getTime()));


        //If the start day doesn't match the end day then show some more info for the user.
        if(currentCard.getStartTime().get(Calendar.DAY_OF_WEEK) !=
                currentCard.getEndTime().get(Calendar.DAY_OF_WEEK)){
            textEnd.setText(hourMinuteDayFormat.format(currentCard.getEndTime().getTime()));
        }
        else{
            textEnd.setText(hourMinuteFormat.format(currentCard.getEndTime().getTime()));
        }

        switchReminderToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //Save into the database whether the reminder has been enabled or not.
                mAlarmsDataSource.open();
                mAlarmsDataSource.setIsEnabled(currentCard.get_id(), isChecked);
                mAlarmsDataSource.close();

            }

        });

        mAlarmsDataSource.open();
        currentCard.setEnabled(mAlarmsDataSource.getIsEnabled(currentCard.get_id()));
        switchReminderToggle.setChecked(currentCard.isEnabled());
        mAlarmsDataSource.close();

        textDuration.setText(Integer.toString(currentCard.getFastLength()));

        return itemView;
    }
}
