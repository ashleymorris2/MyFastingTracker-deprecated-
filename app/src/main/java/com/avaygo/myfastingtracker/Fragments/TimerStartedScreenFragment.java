package com.avaygo.myfastingtracker.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.avaygo.myfastingtracker.Notifications.AlarmSetup;
import com.avaygo.myfastingtracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.passy.holocircularprogressbar.HoloCircularProgressBar;


public class TimerStartedScreenFragment extends Fragment {
    //UI Elements:
    private HoloCircularProgressBar holoCircularProgressBar;
    private Button buttonBreakFast;
    private TextView textStartTime, textEndTime, textHourAndMinutes, textSeconds, txtFastDuration, textPercentComplete,
            textStartDay, textEndDay;

    //Calendars and time formatting:
    private Calendar startCalendar, endCalendar;

    private SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat DayFormat = new SimpleDateFormat("EEEE");

    private AlarmSetup myNotification = new AlarmSetup();//Used to set the notification reminder.

    private final String FAST_PROGRESS = "fastProgress";

    //Fragment Class:
    private FragmentTransaction fragmentChange;

    //CountDownTimer class, overwritten methods to save state.
    private static MyCounter counter;

    public TimerStartedScreenFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fasting_started, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Find View Elements
        textStartTime = (TextView) getView().findViewById(R.id.start_time);
        textEndTime = (TextView) getView().findViewById(R.id.end_time);
        textHourAndMinutes = (TextView) getView().findViewById(R.id.txt_time_HoursMins);
        textSeconds = (TextView) getView().findViewById(R.id.txt_time_seconds);
        txtFastDuration = (TextView) getView().findViewById(R.id.txt_FastingDuration);
        textPercentComplete = (TextView) getView().findViewById(R.id.txt_completed);

        textStartDay = (TextView) getView().findViewById(R.id.txt_start_day);
        textEndDay = (TextView) getView().findViewById(R.id.txt_end_day);

        buttonBreakFast = (Button) getView().findViewById(R.id.breakFast_button);
        buttonBreakFast.setOnClickListener(new View.OnClickListener() {

            private View v;

            public void onClick(View view) {
                //Gives the user an alert dialog if they are over 1 percent into their fast.
                if (counter.getPercentageComplete() < 1 || counter.getPercentageComplete() == 100) {
                    myNotification.cancelAlarm(getActivity());
                    changeFragment();
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(TimerStartedScreenFragment.this.getActivity());
                    builder1.setMessage("Do you want to break your fast early?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    myNotification.cancelAlarm(getActivity());
                                    changeFragment();
                                }
                            }
                    );
                    builder1.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            }
                    );
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }
        });

        holoCircularProgressBar = (HoloCircularProgressBar) getView().findViewById(R.id.holoCircularProgressBar1);

        //Shared preferences to retrieve the session data.
        SharedPreferences preferences = getActivity().getSharedPreferences("appData", 0); // 0 - for private mode

        long startMillies = preferences.getLong("START_TIME", 0);//@Param startMillies start time in milliseconds
        long endMillies = preferences.getLong("END_TIME", 0);// @Param endMillies the end time in milliseconds
        int endHour = preferences.getInt("END_HOUR", 1);// @Param endHour how many hours into the future the timer ends
        long timeLeftInMillies = preferences.getLong("END_MILLISEC", 0);// @Param timeLeftInMillies the hours left in milliseconds
        boolean timerStarted = preferences.getBoolean("TIMER_START", false);

        //Calculates the difference in the current time and the end time.
        if (timerStarted == true) {
             timeLeftInMillies = endMillies - System.currentTimeMillis();
        }

        counter = new MyCounter(timeLeftInMillies, 1000);//

        //Starts and sets the clocks.
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(startMillies);
        endCalendar.setTimeInMillis(endMillies);

        textStartTime.setText(TimeFormat.format(startCalendar.getTime()));
        textStartDay.setText("Today");

        //If end day is tomorrow then show tomorrows date for the user, if not then it isn't necessary.
        if (endCalendar.get(Calendar.DATE) != startCalendar.get(Calendar.DATE)) {
            textEndTime.setText(TimeFormat.format(endCalendar.getTime()));
            textEndDay.setText("Tomorrow");//If two aren't equal then end must be tomorrow.
        }
        else {
            textEndTime.setText(TimeFormat.format(endCalendar.getTime()));
            textEndDay.setText("Today");
        }

        if(savedInstanceState != null){

            float progress = savedInstanceState.getFloat(FAST_PROGRESS);
            holoCircularProgressBar.setProgress(progress);

        }

        if (endHour == 1) {
            txtFastDuration.setText(endHour + " Hour");
        } else {
            txtFastDuration.setText(endHour + " Hours");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putFloat(FAST_PROGRESS, holoCircularProgressBar.getProgress());
        super.onSaveInstanceState(savedInstanceState);
    }

    //Clears the shared preferences and changes the fragment.
    private void changeFragment() {

        //Shared preferences, stores the current state on the button press to save the activity's session.
        SharedPreferences preferences = getActivity().getSharedPreferences("appData", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();

        // Commit the edits
        editor.clear();
        editor.commit();

        //Launches a new fragment and replaces the current one.
        fragmentChange = getActivity().getFragmentManager().beginTransaction();
        fragmentChange.replace(R.id.mainContent, new TimerSettingScreenFragment());
        fragmentChange.commit();
    }

    private class MyCounter extends CountDownTimer {

        private int mEndMinutes, mElapsedTime, mPercentCompleted;
        private boolean dateChange = false;
        private  float percentAsFloat;

        /**Constructor for MyCounter class
        @param millisInFuture time in milliseconds in the future.
        @param countDownInterval interval in milliseconds to countdown.**/
        private MyCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

            SharedPreferences preferences = getActivity().getSharedPreferences("appData", 0); // 0 - for private mode
            SharedPreferences.Editor editor = preferences.edit();

            mEndMinutes = preferences.getInt("END_HOUR", 0) * 3600; // 3600 for seconds, 60 for minutes.
            editor.putBoolean("TIMER_START", true);
            editor.commit();

            super.start();
        }

        public void onTick(long millisUntilFinished) {

            String hours, minutes, seconds;

            int iSeconds = (int) (millisUntilFinished / 1000) % 60;
            int iMinutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
            int iHours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
            int totalMinutes = (int) (millisUntilFinished / 1000);

            //Formatting code.
            if (iHours < 10) {
                hours = ("0" + Integer.toString(iHours));
            } else {
                hours = Integer.toString(iHours);
            }
            if (iMinutes < 10) {
                minutes = ("0" + Integer.toString(iMinutes));
            } else {
                minutes = Integer.toString(iMinutes);
            }
            if (iSeconds < 10) {
                seconds = ("0" + Integer.toString(iSeconds));
            } else {
                seconds = Integer.toString(iSeconds);
            }

            textHourAndMinutes.setText(hours + ":" + minutes);
            textSeconds.setText(":" + seconds);

            //Calculates the remaining time as a percentage for the progress bar.
            // Seconds for high accuracy and minutes for low accuracy.
            mElapsedTime = mEndMinutes - totalMinutes;
            mPercentCompleted = (mElapsedTime * 100) / mEndMinutes;

            //@percentageAsFloat is needed because the circular progress bar is set to a maximum of 1.
            percentAsFloat = mPercentCompleted / 100f;
            holoCircularProgressBar.setProgress(percentAsFloat);


            textPercentComplete.setText(mPercentCompleted + "%");

            if (dateChange == false) {
                //Checks if today's date matches the end date and updates the texts appropriately.
                if (startCalendar.get(Calendar.DATE) != Calendar.getInstance().get(Calendar.DATE)) {

                    textStartDay.setText("Yesterday");
                    textEndDay.setText("Today");
                    dateChange = true;

                }
            }
        }

        public void onFinish() {

            //Set the percentage to 100
            mPercentCompleted = 100;
            percentAsFloat = 1;
            holoCircularProgressBar.setProgress(percentAsFloat);
            textPercentComplete.setText(mPercentCompleted + "%");

            textHourAndMinutes.setText("00:00");
            textSeconds.setText(":00");
        }

        public int getPercentageComplete(){
            return  mPercentCompleted;
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
    }




}
