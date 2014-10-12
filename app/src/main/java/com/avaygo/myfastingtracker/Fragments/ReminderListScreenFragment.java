package com.avaygo.myfastingtracker.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.avaygo.myfastingtracker.Activities.ReminderSettingActivity;
import com.avaygo.myfastingtracker.Adapters.DaysListAdapter;
import com.avaygo.myfastingtracker.Adapters.cReminder;
import com.avaygo.myfastingtracker.Databases.AlarmsDataSource;
import com.avaygo.myfastingtracker.Notifications.RecurringAlarmSetup;
import com.avaygo.myfastingtracker.R;

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
    private MenuItem mSwitchMenuItem;

    private RecurringAlarmSetup mRecurringAlarm;

    private boolean listEnabled;
    private TextView textRemindersDisabled;

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

        setHasOptionsMenu(true);

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

        textRemindersDisabled = (TextView) getActivity().findViewById(R.id.text_reminders_disabled);
        mListView  = (ListView) getActivity().findViewById(R.id.listview_days);
        populateReminderCardsList();
        populateListView();

        //Sets the initial viability of the fragments views
        SharedPreferences preferences = getActivity().getSharedPreferences("userPref", 0); // 0 - for private mode
        listEnabled = preferences.getBoolean("listEnabled", false);

        if(listEnabled == false){
            mListView.setVisibility(View.INVISIBLE);
            textRemindersDisabled.setVisibility(View.VISIBLE);
        }
        else {
            textRemindersDisabled.setVisibility(View.INVISIBLE);
            mListView.setVisibility(View.VISIBLE);
        }
    }

    //Calls the constructor for cReminder and populates a list of objects.
    private void populateReminderCardsList() {

        mReminderCardsList = mAlarmsDataSource.getAllAlarms();

    }

    private void populateListView() {

        DaysListAdapter adapter = new DaysListAdapter (getActivity(), mReminderCardsList);

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
                intent.putExtra("startTime", currentCard.getStartTime().getTimeInMillis());
                intent.putExtra("duration", currentCard.getFastLength());
                intent.putExtra("_id", currentCard.get_id());

                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Redraws the list view after receiving a result from the ReminderSettingActivity
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){

                mReminderCardsList = mAlarmsDataSource.getAllAlarms();

                DaysListAdapter adapter = new DaysListAdapter (getActivity(), mReminderCardsList);

                mListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                mListView.invalidate();
            }
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        SharedPreferences preferences = getActivity().getSharedPreferences("userPref", 0); // 0 - for private mode
        Switch switch1 = (Switch)menu.findItem(R.id.myswitch).getActionView().findViewById(R.id.switchForActionBar);

        //Makes the switch visible and set its checked status
        listEnabled = preferences.getBoolean("listEnabled", false);
        mSwitchMenuItem.setVisible(true);
        switch1.setChecked(listEnabled);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


        mSwitchMenuItem = menu.findItem(R.id.myswitch);
        mSwitchMenuItem.setVisible(true);

        //Gets the switch to work with from the action bar menu, we can now specify the onClickListener
        Switch switch1 = (Switch)menu.findItem(R.id.myswitch).getActionView().findViewById(R.id.switchForActionBar);

        switch1.setOnClickListener(new View.OnClickListener() {
            View v;

            @Override
            public void onClick(View view) {
                boolean isChecked = ((Switch)view).isChecked();

                SharedPreferences preferences = getActivity().getSharedPreferences("userPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = preferences.edit();

                editor.putBoolean("listEnabled", isChecked);
                editor.commit();

                mRecurringAlarm = new RecurringAlarmSetup();

                //ALARMS ON
                if(isChecked){
                    textRemindersDisabled.setVisibility(View.INVISIBLE);
                    mListView.setVisibility(View.VISIBLE);

                    //Cancel all the set alarms.
                    for(int i = 0; i <7; i++){
                        cReminder mReminder =  mReminderCardsList.get(i);

                        //If the alarm is enabled then reset  it...
                        if(mReminder.isEnabled()){
                            mRecurringAlarm.createRecurringAlarm(getActivity(), mReminder.getStartTime(),
                                    mReminder.get_id());
                        }
                    }
                }
                //ALARMS OFF
                else {
                    mListView.setVisibility(View.INVISIBLE);
                    textRemindersDisabled.setVisibility(View.VISIBLE);

                    cancelAlarms();
                }
            }
        });
    }

    //Put into a separate thread because it was slightly hanging the UI.
    public void cancelAlarms() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i <7; i++){
                    cReminder mReminder =  mReminderCardsList.get(i);

                    if(mReminder.isEnabled()){
                        mRecurringAlarm.cancelRecurringAlarm(getActivity(), mReminder.get_id());
                    }
                }
            }
        }){

        };
        thread.start();
    }

    }











