package com.avaygo.myfastingtracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.passy.holocircularprogressbar.HoloCircularProgressBar;


public class TimerStartedFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    //UI Elements:
    private HoloCircularProgressBar holoCircularProgressBar;
    private Button BtnBreakFast;
    private TextView txtStartTime, txtEndTime, txtHourMins, txtSecs, txtFastDuration, txtPercentComplete;

    //Calendars and time formatting
    private Calendar startCalendar, endCalendar;
    SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm");
    SimpleDateFormat TimeDateFormat = new SimpleDateFormat("EE HH:mm");
    SimpleDateFormat TimeDateFormat2 = new SimpleDateFormat("HH:mm EE");

    //Fragment Class:
    FragmentTransaction fragmentChange;

    //CountDownTimer class, overwritten methods to save state.
    private static MyCounter counter;

    public TimerStartedFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_f_fasting_started, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Find View Elements
        txtStartTime = (TextView) getView().findViewById(R.id.start_time);
        txtEndTime = (TextView) getView().findViewById(R.id.end_time);
        txtHourMins = (TextView) getView().findViewById(R.id.txt_time_HoursMins);
        txtSecs = (TextView) getView().findViewById(R.id.txt_time_seconds);
        txtFastDuration = (TextView) getView().findViewById(R.id.txt_FastingDuration);
        txtPercentComplete = (TextView) getView().findViewById(R.id.txt_completed);
        BtnBreakFast = (Button) getView().findViewById(R.id.breakFast_button);
        BtnBreakFast.setOnClickListener(BtnBreakFast_OnClickListener);

        holoCircularProgressBar = (HoloCircularProgressBar) getView().findViewById(R.id.holoCircularProgressBar1);

        //Shared preferences to retrieve the session data.
        SharedPreferences preferences = getActivity().getSharedPreferences("appData", 0); // 0 - for private mode

        long startMill = preferences.getLong("START_TIME", 0);//@Param startMill start time in milliseconds
        long endMill = preferences.getLong("END_TIME", 0);// @Param endMill the end time in milliseconds
        int endHour = preferences.getInt("END_HOUR", 1);// @Param endHour how many hours into the future the timer ends
        long endHourInMills = preferences.getLong("END_MILLISEC", 0);// @Param endHourInMills the hours in milliseconds
        boolean timerStarted = preferences.getBoolean("TIMER_START", false);

        //Calculates the difference in the current time and the end time.
        if (timerStarted == true) {
             endHourInMills = endMill - System.currentTimeMillis();
        }

        counter = new MyCounter(endHourInMills, 1000);//

        //Starts and sets the clocks.
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(startMill);
        endCalendar.setTimeInMillis(endMill);

        txtStartTime.setText(TimeFormat.format(startCalendar.getTime()));

        //If end day is tomorrow then show tomorrows date for the user, if not then it isn't necessary.
        if (endCalendar.get(Calendar.DATE) != startCalendar.get(Calendar.DATE)) {
            txtEndTime.setText(TimeDateFormat.format(endCalendar.getTime()));
        } else {
            txtEndTime.setText(TimeFormat.format(endCalendar.getTime()));
        }
        if (endHour == 1) {
            txtFastDuration.setText(endHour + " Hour");
        } else {
            txtFastDuration.setText(endHour + " Hours");
        }
    }

    final View.OnClickListener BtnBreakFast_OnClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            //Gives the user an alert dialog if they are over 5 percent into their fast.
            if (counter.getPercentageComplete() < 5 || counter.getPercentageComplete() == 100) {
                changeFragment();
            }
            else{
                AlertDialog.Builder builder1 = new AlertDialog.Builder(TimerStartedFragment.this.getActivity());
                builder1.setMessage("Do you want to break your fast early?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                changeFragment();
                            }
                        });
                builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        }
    };

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
        fragmentChange.replace(R.id.container, new TimerSettingFragment());
        fragmentChange.commit();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private class MyCounter extends CountDownTimer {

        private int mEndMinutes, mElapsedTime, mPercentCompleted;
        private boolean dateChange = false;
        private  float percentAsFloat;

        /*Constructor for MyCounter class
        @param millisInFuture time in milliseconds in the future.
        @param countDownInterval interval in milliseconds to countdown.*/
        private MyCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

            SharedPreferences preferences = getActivity().getSharedPreferences("appData", 0); // 0 - for private mode
            SharedPreferences.Editor editor = preferences.edit();
            mEndMinutes = preferences.getInt("END_HOUR", 0) * 3600; // 3600 for seconds, 60 for minutes.
            editor.putBoolean("TIMER_START", true);
            editor.commit();

            super.start();
        }

        public int getPercentageComplete(){
            return  mPercentCompleted;
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

            txtHourMins.setText(hours + ":" + minutes);
            txtSecs.setText(":" + seconds);

            //Calculates the remaining time as a percentage for the progress bar.
            // Seconds for high accuracy and minutes for low accuracy.
            mElapsedTime = mEndMinutes - totalMinutes;
            mPercentCompleted = (mElapsedTime * 100) / mEndMinutes;

            //@percentageAsFloat is needed because the circular progress bar is set to a maximum of 1.
            percentAsFloat = mPercentCompleted / 100f;
            holoCircularProgressBar.setProgress(percentAsFloat);
            holoCircularProgressBar.setMarkerProgress(percentAsFloat);

            txtPercentComplete.setText(mPercentCompleted + "%");

            if (dateChange == false) {
                //Checks if today's date matches the end date and updates the texts appropriately.
                if (startCalendar.get(Calendar.DATE) != Calendar.getInstance().get(Calendar.DATE)) {
                    txtStartTime.setText(TimeDateFormat2.format(startCalendar.getTime()));
                    txtEndTime.setText(TimeFormat.format(endCalendar.getTime()));
                    dateChange = true;
                }
            }
        }

        public void onFinish() {
            //Set the percentage to 100
            mPercentCompleted = 100;
            percentAsFloat = 1;
            holoCircularProgressBar.setProgress(percentAsFloat);
            holoCircularProgressBar.setMarkerProgress(percentAsFloat);
            txtPercentComplete.setText(mPercentCompleted + "%");

            txtHourMins.setText("00:00");
            txtSecs.setText(":00");
        }
    }

}
