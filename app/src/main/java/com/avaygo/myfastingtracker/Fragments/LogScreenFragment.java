package com.avaygo.myfastingtracker.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ScrollView;
import android.widget.TextView;

import com.avaygo.myfastingtracker.R;
import com.avaygo.myfastingtracker.activities.FastDetailsActivity;
import com.avaygo.myfastingtracker.adapters.RecordsListAdapter;
import com.avaygo.myfastingtracker.classes.FastingRecord;
import com.avaygo.myfastingtracker.classes.NonScrollListView;
import com.avaygo.myfastingtracker.databases.LogDataSource;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogScreenFragment extends Fragment {

    private CaldroidFragment caldroidFragment;
    private FragmentActivity myContext;

    private TextView textDateTitle;
    private Date previousDate;

    private TextView textStatus;
    private NonScrollListView nonScrollListView;

    private RecordsListAdapter adapter;
    private GetRecords getRecords;
    private GetRecordsForDay getRecordsForDay;
    private ScrollView scrollView;

    private int selectedDay, month, year;

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
        final Calendar calendar = Calendar.getInstance();
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        args.putInt(CaldroidFragment.MONTH, calendar.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, calendar.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, false);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

        caldroidFragment.setArguments(args);

        ///Only interested in the date and not the time so set every other variable in the
        //calendar to 0.
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        previousDate = calendar.getTime();
        caldroidFragment.setSelectedDates(previousDate, previousDate);

        textStatus = (TextView) getActivity().findViewById(R.id.text_nothing_logged);
        nonScrollListView = (NonScrollListView) getActivity().findViewById(R.id.noscroll_listview);

        scrollView = (ScrollView) getActivity().findViewById(R.id.ScrollView);

        //Need to wait until the scroll view is on the screen before scrolling back to the top.
        //Once the view is ready can return the focus to the top.
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //The view is ready so able to scroll back up to the top.
                scrollView.fullScroll(View.FOCUS_UP);
            }
        });

        new GetRecords(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)).execute();
        new GetRecordsForDay(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.YEAR)).execute();

        android.support.v4.app.FragmentTransaction transaction = myContext.getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.calendar1, caldroidFragment).commit();

        final CaldroidListener caldroidListener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(date.getTime());

                selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);

                new GetRecordsForDay(selectedDay, month, year).execute();

                //Format the selected date to the text view
                textDateTitle.setText(dateFormat.format(date));

                //Clears the previously selected date from the fragment.
                //Updates the graphic for the currently selected date and clears the old one.
                if (previousDate.getTime() != date.getTime()) {

                    caldroidFragment.clearSelectedDates();
                    caldroidFragment.setSelectedDates(date, date);

                    previousDate = date;
                    caldroidFragment.refreshView();
                }
            }

            @Override
            public void onChangeMonth(int month, int year) {
                super.onChangeMonth(month, year);

                //Because Caldroid is retarded and adds on a month....
                month = month - 1;

                new GetRecords(month, year).execute();
            }
        };

        caldroidFragment.setCaldroidListener(caldroidListener);
        textDateTitle = (TextView) getActivity().findViewById(R.id.text_date_title);
        textDateTitle.setText(dateFormat.format(System.currentTimeMillis()));
    }

    private class GetRecords extends AsyncTask<Void, Void, ArrayList<FastingRecord>> {

        int month;
        int year;
        private LogDataSource logDataSource;

        private GetRecords(int month, int year) {
            this.month = month;
            this.year = year;
            logDataSource = new LogDataSource(getActivity());
            logDataSource.open();
        }

        @Override
        protected ArrayList<FastingRecord> doInBackground(Void... voids) {
            return logDataSource.getRecordsForMonth(month, year);
        }

        @Override
        protected void onPostExecute(ArrayList<FastingRecord> fastingRecords) {
            super.onPostExecute(fastingRecords);

            int day;
            int month;
            int year;

            Calendar calendar = Calendar.getInstance();

            if (fastingRecords != null) {
                //Goes through each element in the fasting records array and assigns
                // the day, month and year variables to the ones that have been retrieved
                for (int i = 0; i < fastingRecords.size(); i++) {
                    day = fastingRecords.get(i).getEndDay();
                    month = fastingRecords.get(i).getEndMonth();
                    year = fastingRecords.get(i).getEndYear();

                    //Sets the calendar to the retrieved date.
                    //Assigns current date the calendar date to pass to the caldroid fragment.
                    calendar.set(year, month, day, 0, 0);
                    Date currentDate = calendar.getTime();

                    if (caldroidFragment != null) {
                        if (fastingRecords.get(i).getPercentageComplete() < 100) {
                            caldroidFragment.setTextColorForDate(R.color.Red_Accent, currentDate);
                        } else {
                            caldroidFragment.setTextColorForDate(R.color.Blue_Accent, currentDate);
                        }
                    }
                }
            }

            caldroidFragment.refreshView();
            logDataSource.close();
        }
    }

    private class GetRecordsForDay extends AsyncTask<Void, Void, ArrayList<FastingRecord>> {

        int day;
        int month;
        int year;

        private LogDataSource logDataSource;

        private GetRecordsForDay(int day, int month, int year) {
            this.day = day;
            this.month = month;
            this.year = year;
            logDataSource = new LogDataSource(getActivity());
            logDataSource.open();
        }

        @Override
        protected ArrayList<FastingRecord> doInBackground(Void... voids) {
            return logDataSource.getRecordsForDay(day, month, year);
        }

        @Override
        protected void onPostExecute(ArrayList<FastingRecord> fastingRecords) {
            super.onPostExecute(fastingRecords);

            if (fastingRecords.size() > 0) {
                textStatus.setVisibility(View.GONE);
                nonScrollListView.setVisibility(View.VISIBLE);
                populateListView(fastingRecords);
            } else {
                nonScrollListView.setVisibility(View.GONE);
                textStatus.setVisibility(View.VISIBLE);
            }

            logDataSource.close();
        }
    }

    private void populateListView(final List<FastingRecord> recordList) {

        if(adapter == null) {
            adapter = new RecordsListAdapter(getActivity(), recordList);
            nonScrollListView.setAdapter(adapter);
        }
        else{
            adapter.refill(recordList);
        }

        //Handle the clicks on individual elements in the list
        nonScrollListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FastingRecord currentItem = recordList.get(position);

                Intent intent = new Intent(getActivity(), FastDetailsActivity.class);

                //Add the extras that will be used on the next screen.
                intent.putExtra("ROW_ID", currentItem.getId());

                intent.putExtra("START_TIMESTAMP", currentItem.getStartTimeStamp().getTimeInMillis());
                intent.putExtra("END_TIMESTAMP", currentItem.getEndTimeStamp().getTimeInMillis());
                intent.putExtra("LOG_TIMESTAMP", currentItem.getLogTimeStamp().getTimeInMillis());

                intent.putExtra("FAST_DURATION", currentItem.getFastDuration());
                intent.putExtra("PERCENT_COMPLETE", currentItem.getPercentageComplete());
                intent.putExtra("USER_NOTE", currentItem.getUserNote());

                intent.putExtra("DAY", selectedDay);
                intent.putExtra("MONTH", month);
                intent.putExtra("YEAR", year);

                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                Calendar calendar = Calendar.getInstance();

                int day, month, year;

                day = data.getIntExtra("DAY", calendar.get(Calendar.DAY_OF_MONTH));
                month = data.getIntExtra("MONTH", calendar.get(Calendar.MONTH));
                year = data.getIntExtra("YEAR", calendar.get(Calendar.YEAR));

                calendar.set(year, month, day, 0, 0);
                Date selectedDate = calendar.getTime();

                caldroidFragment.setTextColorForDate(R.color.caldroid_lighter_gray, selectedDate);
                caldroidFragment.refreshView();

                new GetRecordsForDay(day, month, year).execute();
            }
        }
    }
}
