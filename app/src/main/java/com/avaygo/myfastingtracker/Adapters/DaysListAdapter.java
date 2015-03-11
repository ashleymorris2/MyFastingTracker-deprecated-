package com.avaygo.myfastingtracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.avaygo.myfastingtracker.R;
import com.avaygo.myfastingtracker.databases.AlarmsDataSource;
import com.avaygo.myfastingtracker.notifications.RecurringAlarmSetup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ash on 10/08/2014.
 * Override of an array adapter.
 *
 * The Constructor takes two parameters. The context of the calling application and a list of Reminder objects.
 *
 */
public class DaysListAdapter extends ArrayAdapter<Reminder> {

    private  Context context;
    private List <Reminder> reminderDaysList;
    private RecurringAlarmSetup recurringAlarmSetup = new RecurringAlarmSetup();
    private AlarmsDataSource alarmsDataSource = new AlarmsDataSource(getContext());

    SimpleDateFormat hourMinuteFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    SimpleDateFormat hourMinuteDayFormat = new SimpleDateFormat("HH:mm, EEEE", Locale.getDefault());

    public DaysListAdapter(Context context, List <Reminder> reminderDaysList) {
        super(context, R.layout.listview_reminder_day, reminderDaysList);
        this.context = context;
        this.reminderDaysList = reminderDaysList;
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
        final Reminder currentCard = reminderDaysList.get(position);

        final Switch switchReminderToggle = (Switch) itemView.findViewById(R.id.switch_reminder_toggle);
        TextView textDay = (TextView) itemView.findViewById(R.id.item_text_reminderday);
        TextView textStart = (TextView) itemView.findViewById(R.id.item_text_start_datetime);
        TextView textEnd = (TextView) itemView.findViewById(R.id.item_text_end_datetime);
        TextView textDuration = (TextView) itemView.findViewById(R.id.item_text_fasting_duration);


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

        switchReminderToggle.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = ((Switch)view).isChecked();

                //Save into the database whether the reminder has been enabled or not.
                alarmsDataSource.open();
                alarmsDataSource.setIsEnabled(currentCard.get_id(), isChecked);
                alarmsDataSource.close();

                if(isChecked){
                    recurringAlarmSetup.createRecurringAlarm(getContext(), currentCard.getStartTime(),
                            currentCard.get_id());

                }
                else {
                    recurringAlarmSetup.cancelRecurringAlarm(getContext(), currentCard.get_id());
                }
            }
        });


        alarmsDataSource.open();
        currentCard.setEnabled(alarmsDataSource.getIsEnabled(currentCard.get_id()));
        switchReminderToggle.setChecked(currentCard.isEnabled());
        alarmsDataSource.close();

        textDuration.setText(Integer.toString(currentCard.getFastLength()));

        TextView textHours = (TextView) itemView.findViewById(R.id.text_hours);
        if(currentCard.getFastLength() == 1){
            textHours.setText("Hour");
        }
        else {
            textHours.setText("Hours");
        }

        return itemView;
    }
}
