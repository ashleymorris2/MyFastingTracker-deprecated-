package com.avaygo.myfastingtracker;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class fFastingStarted extends Fragment {

    private OnFragmentInteractionListener mListener;

    //UI Elements:
    Button BtnBreakFast;
    TextView txtStartTime, txtEndTime, txtHourMins, txtSecs, txtFastDuration;
    //Calendars and time formatting
    Calendar startCalendar, endCalendar;
    SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm");
    //Threads and Runnables:
    Thread myThread = null;
    Runnable myRunnableThread = new CountDownSaver();
    //Fragment Class:
    FragmentTransaction fragmentChange;
    //CountDownTimer class, overwritten methods to save state.
    MyCounter counter;

    public fFastingStarted() {
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

        BtnBreakFast = (Button) getView().findViewById(R.id.breakFast_button);
        BtnBreakFast.setOnClickListener(BtnBreakFast_OnClickListener);

        //Shared preferences to retrieve the session data.
        SharedPreferences preferences = getActivity().getSharedPreferences("appData", 0); // 0 - for private mode

        long startMill = preferences.getLong("START_TIME", 0);//@Param startMill start time in milliseconds
        long endMill = preferences.getLong("END_TIME", 0);// @Param endMill the end time in milliseconds
        int endHour = preferences.getInt("END_HOUR", 1);// @Param endHour how many hours into the future the timer ends
        long endMillies = preferences.getLong("END_MILLISEC", 0);// @Param endMillies the hours in milliseconds
        long saveTime = preferences.getLong("SAVE_TIME", 0);

        boolean timerStarted = preferences.getBoolean("TIMER_START", false);
        //Starts and sets the clocks.
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(startMill);
        endCalendar.setTimeInMillis(endMill);

        txtStartTime.setText(TimeFormat.format(startCalendar.getTime()));
        txtEndTime.setText(TimeFormat.format(endCalendar.getTime()));

        if (endHour == 1) {
            txtFastDuration.setText(endHour + " Hour");
        }else {txtFastDuration.setText(endHour + " Hours");}

        if (timerStarted == true){
          long difference =  endMill - saveTime ;
            endMillies = endMillies - difference;
        }

        counter = new MyCounter(endMillies, 1000);//
        myThread = new Thread(myRunnableThread);//New thread to save the state so that the UI doesn't get held up.
        myThread.start();
    }

    final View.OnClickListener BtnBreakFast_OnClickListener = new View.OnClickListener() {
        public void onClick(View view) {

            //Shared preferences, stores the current state on the button press to save the activity's session.
            SharedPreferences preferences = getActivity().getSharedPreferences("appData", 0); // 0 - for private mode
            SharedPreferences.Editor editor = preferences.edit();


            // Commit the edits
            editor.clear();
            editor.commit();

            //Launches a new fragment and replaces the current one.
            fragmentChange = getActivity().getFragmentManager().beginTransaction();
            fragmentChange.replace(R.id.container, new fFastingSettings());
            fragmentChange.commit();
        }
    };

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private class MyCounter extends CountDownTimer {

        private long mTimeLeft;

        /*Constructor for MyCounter class
        @param millisInFuture time in milliseconds in the future.
        @param countDownInterval interval in milliseconds to countdown.*/
        private MyCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

            super.start();

            mTimeLeft = millisInFuture;

            SharedPreferences preferences = getActivity().getSharedPreferences("appData", 0); // 0 - for private mode
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("TIMER_START", true);
            editor.commit();
        }

        public void onTick(long millisUntilFinished) {

            int seconds = (int) (millisUntilFinished / 1000) % 60;
            int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
            int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);

            txtHourMins.setText(Integer.toString(hours) + ":" + Integer.toString(minutes));
            txtSecs.setText(Integer.toString(seconds));

            this.mTimeLeft = millisUntilFinished;
        }

       /* This method is run in a separate thread, saves the current millisecond time so that it can be
        retrieved later.*/
        public void saveTimer() {
            SharedPreferences preferences = getActivity().getSharedPreferences("appData", 0); // 0 - for private mode
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong("END_MILLISEC", this.mTimeLeft);
            editor.putLong("SAVE_TIME", System.currentTimeMillis());
            editor.commit();
        }

        public void onFinish() {
            txtHourMins.setText("Finished!");
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    private class CountDownSaver implements Runnable {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    counter.saveTimer();
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
