package com.avaygo.myfastingtracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
 *
 */
public class DaysListAdapter extends ArrayAdapter<cReminder> {

    private  Context context;
    private List <cReminder> mReminderDaysCard;

    SimpleDateFormat hourMinuteFormat = new SimpleDateFormat("HH:mm");
    SimpleDateFormat hourMinuteDayFormat = new SimpleDateFormat("HH:mm EEEE");

    public DaysListAdapter(Context context, List <cReminder> reminderDaysCard) {
        super(context, R.layout.listview_reminder_day, reminderDaysCard);
        this.context = context;
        this.mReminderDaysCard = reminderDaysCard;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = convertView;

        //Make sure that there is a view to work with in case we have been given null.
        if (itemView == null){
            itemView = inflater.inflate(R.layout.listview_reminder_day, parent, false);
        }

        //Find the current cardView to work with.
        cReminder currentCard = mReminderDaysCard.get(position);

        TextView dayText = (TextView) itemView.findViewById(R.id.item_text_reminderday);
        TextView startText = (TextView) itemView.findViewById(R.id.item_text_start_datetime);
        TextView endText = (TextView) itemView.findViewById(R.id.item_text_end_datetime);
        TextView durationText = (TextView) itemView.findViewById(R.id.item_text_fasting_duration);

        //Fill the view
        dayText.setText(currentCard.getDayName());
        startText.setText(hourMinuteFormat.format(currentCard.getStartTime().getTime()));
        endText.setText(hourMinuteFormat.format(currentCard.getEndTime().getTime()));

        durationText.setText(Integer.toString(currentCard.getFastLength()));

        return itemView;
    }
}
