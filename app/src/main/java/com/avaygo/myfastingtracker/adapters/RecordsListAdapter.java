package com.avaygo.myfastingtracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.avaygo.myfastingtracker.R;
import com.avaygo.myfastingtracker.classes.FastingRecord;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ash on 14/03/2015.
 */
public class RecordsListAdapter extends ArrayAdapter<FastingRecord> {

    private Context context;
    private List<FastingRecord> recordList;

    private SimpleDateFormat hourMinuteFormat = new SimpleDateFormat("HH:mm");//Current time

    public RecordsListAdapter(Context context, List<FastingRecord> recordList) {
        super(context, R.layout.listview_record_row, recordList);
        this.context = context;
        this.recordList = recordList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = convertView;

        //Make sure that there is a view to work with in case we have been given null.
        if (itemView == null){
            itemView = inflater.inflate(R.layout.listview_record_row, parent, false);
        }

        //Find the current record to work with
        final FastingRecord currentRecord = recordList.get(position);

        TextView textDuration = (TextView) itemView.findViewById(R.id.text_duration);
        textDuration.setText(currentRecord.getFastDuration() + " Hours");

        TextView textPercentage = (TextView) itemView.findViewById(R.id.text_percentage);
        textPercentage.setText(currentRecord.getPercentageComplete() + "%");


        TextView textStartTime = (TextView) itemView.findViewById(R.id.text_start_time);
        textStartTime.setText(hourMinuteFormat.format(currentRecord.getStartTimeStamp().getTime()));

        TextView textEndTime = (TextView) itemView.findViewById(R.id.text_end_time);
        textEndTime.setText(hourMinuteFormat.format(currentRecord.getLogTimeStamp().getTime()));


        return itemView;
    }

    public void refill(List<FastingRecord> recordList){

        this.recordList = new ArrayList<FastingRecord>();
        this.recordList.addAll(recordList);

        notifyDataSetChanged();
    }

}
