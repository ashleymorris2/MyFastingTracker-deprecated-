package com.avaygo.myfastingtracker.Fragments;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.avaygo.myfastingtracker.Adapters.DaysListAdapter;
import com.avaygo.myfastingtracker.Adapters.cReminderDaysCard;
import com.avaygo.myfastingtracker.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ReminderSettingFragment extends Fragment {

    private String[] mDaysOfTheWeek;

    //An array list to hold ReminderSettingFragment objects for the listview.
    List <cReminderDaysCard> mReminderCardsList = new ArrayList<cReminderDaysCard>();
    private ListView mListView;

    public ReminderSettingFragment() {
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
        populateReminderCardsList();
        populateListView();

    }

    private void populateReminderCardsList() {
        for(int i = 0; i < 7; i++){

            mReminderCardsList.add(new cReminderDaysCard(mDaysOfTheWeek[i]));

        }
    }

    private void populateListView() {

        DaysListAdapter adapter = new DaysListAdapter (getActivity(), mReminderCardsList);

        mListView  = (ListView) getActivity().findViewById(R.id.listview_days);

        mListView.addFooterView(new View(getActivity()), null, false);
        mListView.addHeaderView(new View(getActivity()), null, false);
        mListView.setAdapter(adapter);

    }


}







