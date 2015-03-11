package com.avaygo.myfastingtracker.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avaygo.myfastingtracker.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class LogScreenFragment extends Fragment {

    private CaldroidFragment caldroidFragment;
    private FragmentActivity myContext;

    private TextView textDateTitle;
    private Date previousDate;

    SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d");


    public LogScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myContext = (FragmentActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        caldroidFragment = new CaldroidCustomFragment();
        Bundle args = new Bundle();
        Calendar calendar = Calendar.getInstance();
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        args.putInt(CaldroidFragment.MONTH, calendar.get(Calendar.MONTH) +1);
        args.putInt(CaldroidFragment.YEAR, calendar.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, false);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

        caldroidFragment.setArguments(args);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        previousDate = calendar.getTime();
        caldroidFragment.setSelectedDates(previousDate, previousDate);


        android.support.v4.app.FragmentTransaction transaction = myContext.getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.calendar1, caldroidFragment).commit();



        CaldroidListener caldroidListener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {

                //Format the selected date to the text view
                textDateTitle.setText(dateFormat.format(date));

                //Clears the previously selected date from the fragment.
                //Updates the graphic for the currently selected date and clears the old one.
                if(previousDate.getTime() != date.getTime()) {

                    caldroidFragment.clearSelectedDates();
                    caldroidFragment.setSelectedDates(date,date);

                    previousDate = date;
                    caldroidFragment.refreshView();
                }

            }

            @Override
            public void onChangeMonth(int month, int year) {
                super.onChangeMonth(month, year);
            }
        };

        caldroidFragment.setCaldroidListener(caldroidListener);

        textDateTitle = (TextView) getActivity().findViewById(R.id.text_date_title);
        textDateTitle.setText(dateFormat.format(System.currentTimeMillis()));
    }


}
