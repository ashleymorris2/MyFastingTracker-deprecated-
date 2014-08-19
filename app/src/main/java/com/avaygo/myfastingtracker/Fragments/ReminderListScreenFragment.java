package com.avaygo.myfastingtracker.Fragments;



import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.avaygo.myfastingtracker.Activities.ReminderSettingActivity;
import com.avaygo.myfastingtracker.Adapters.DaysListAdapter;
import com.avaygo.myfastingtracker.Adapters.cReminder;
import com.avaygo.myfastingtracker.Databases.AlarmsDataSource;
import com.avaygo.myfastingtracker.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ReminderListScreenFragment extends Fragment {

    private AlarmsDataSource mAlarmsDataSource;
    private String[] mDaysOfTheWeek;
    private ListView mListView;

    private Calendar now;

    //An array list to hold cReminder objects for the listview.
    private List <cReminder> mReminderCardsList;


    public ReminderListScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder_settings, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDaysOfTheWeek = getResources().getStringArray(R.array.array_weekdays);

        mAlarmsDataSource = new AlarmsDataSource(getActivity());
        mAlarmsDataSource.open();

        now = Calendar.getInstance();

        //Checks to see if the initial setup of the database has been performed
        if(mAlarmsDataSource.getAlarmsCount() != 7) {
            for (int i = 0; i < 7; i++) {
                mAlarmsDataSource.initialiseAlarms(mDaysOfTheWeek[i], now, 1, now);
            }
        }
        populateReminderCardsList();
        populateListView();

    }

    //Calls the constructor for cReminder and populates a list of objects.
    private void populateReminderCardsList() {

        mReminderCardsList = mAlarmsDataSource.getAllReminders();

    }

    private void populateListView() {

        DaysListAdapter adapter = new DaysListAdapter (getActivity(), mReminderCardsList);

        mListView  = (ListView) getActivity().findViewById(R.id.listview_days);

        mListView.addFooterView(new View(getActivity()), null, false);
        mListView.addHeaderView(new View(getActivity()), null, false);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            View v;
          @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View viewClicker, int position, long id) {

                Intent intent = new Intent(getActivity(), ReminderSettingActivity.class);
                cReminder currentCard = mReminderCardsList.get(position - 1);

                intent.putExtra("day", currentCard.getDayName());
                startActivity(intent);

            }
        });

    }
}







