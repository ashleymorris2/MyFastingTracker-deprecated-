package com.avaygo.myfastingtracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.avaygo.myfastingtracker.R;

import java.util.List;

/**
 * Created by Ash on 10/08/2014.
 */
public class DaysListAdapter extends ArrayAdapter<cReminderDaysCard> {

    private  Context context;
    private List <cReminderDaysCard> mReminderDaysCard;

    public DaysListAdapter(Context context, List <cReminderDaysCard> reminderDaysCard) {
        super(context, R.layout.listview_reminder_day, reminderDaysCard);
        this.context = context;
        this.mReminderDaysCard = reminderDaysCard;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = convertView;
        //Make sure that there is a view to work with incase we have been given null.
        if (itemView == null){
            itemView = inflater.inflate(R.layout.listview_reminder_day, parent, false);
        }

        //Find the current cardview to work with.
        cReminderDaysCard currentCard = mReminderDaysCard.get(position);

        //Fill the view.
        TextView textView = (TextView) itemView.findViewById(R.id.item_text_reminderday);
        textView.setText(currentCard.getmDayName());


        return itemView;
    }
}
