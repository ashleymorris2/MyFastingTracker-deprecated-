package com.avaygo.myfastingtracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.avaygo.myfastingtracker.Databases.AlarmsDataSource;
import com.avaygo.myfastingtracker.Notifications.RecurringAlarmSetup;
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
    private RecurringAlarmSetup mRecuringAlarmSetup = new RecurringAlarmSetup();

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
        final Switch switchReminderToggle = (Switch) itemView.findViewById(R.id.switch_reminder_toggle);

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

     /*   switchReminderToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            View v;
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //Save into the database whether the reminder has been enabled or not.
                mAlarmsDataSource.open();
                mAlarmsDataSource.setIsEnabled(currentCard.get_id(), isChecked);
                mAlarmsDataSource.close();

                //If is checked then a recurring alarm is set-up.
                if (isChecked) {
                    mRecuringAlarmSetup.createRecurringAlarm(getContext(), currentCard.getStartTime(),
                            currentCard.get_id());

                } else {
                    //TODO  Cancel a reoccurring alarm that takes the current cards start time.
                }

            }
        });*/

        switchReminderToggle.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = ((Switch)view).isChecked();

                //Save into the database whether the reminder has been enabled or not.
                mAlarmsDataSource.open();
                mAlarmsDataSource.setIsEnabled(currentCard.get_id(), isChecked);
                mAlarmsDataSource.close();

                if(isChecked){
                    mRecuringAlarmSetup.createRecurringAlarm(getContext(), currentCard.getStartTime(),
                            currentCard.get_id());

                }
                else {
                    mRecuringAlarmSetup.cancelRecurringAlarm(getContext(), currentCard.get_id());
                }
            }
        });


        mAlarmsDataSource.open();
        currentCard.setEnabled(mAlarmsDataSource.getIsEnabled(currentCard.get_id()));
        switchReminderToggle.setChecked(currentCard.isEnabled());
        mAlarmsDataSource.close();

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
