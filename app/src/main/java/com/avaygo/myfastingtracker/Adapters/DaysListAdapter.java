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
 * Override of an array adapter.
 *
 * The Constructor takes two parameters. The context of the calling application and a list of cReminder objects.
 *
 *
 */
public class DaysListAdapter extends ArrayAdapter<cReminder> {

    private  Context context;
    private List <cReminder> mReminderDaysCard;

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

        //Find the current cardview to work with.
        cReminder currentCard = mReminderDaysCard.get(position);

        //Fill the view.
        TextView textView = (TextView) itemView.findViewById(R.id.item_text_reminderday);
        textView.setText(currentCard.getDayName());


        return itemView;
    }
}
