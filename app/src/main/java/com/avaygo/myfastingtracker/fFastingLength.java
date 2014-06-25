package com.avaygo.myfastingtracker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class fFastingLength extends Fragment {

    //UI Elements:
    Button BStartToggle;
    TextView TStatusText, TCurrentTime, TEndTime, TSeekVal, TCurrentClock, TEndClock, TEndHour;
    TextView TDayText;
    SeekBar timeSeek;

    //Threads and Runnables.
    Thread myThread = null;
    Runnable myRunnableThread = new CountDownRunner();

    //Calendars and time formatting.
    Calendar currentCalendar, futureCalendar;
    SimpleDateFormat currentTimeFormat = new SimpleDateFormat("HH:mm");//Current time
    SimpleDateFormat futureHourFormat = new SimpleDateFormat("HH:");//The hour for the future clock.
    SimpleDateFormat futureMinuteFormat = new SimpleDateFormat("mm");//The minutes for the future clock
    SimpleDateFormat dayFormat = new SimpleDateFormat(" EE");//For the day of the week.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fasting_length, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        myThread = new Thread(myRunnableThread);//New thread to run the timer separately so that the UI doesn't get held up.
        myThread.start();

        //Find View Elements
        TStatusText = (TextView) getView().findViewById(R.id.status_text);
        TCurrentTime = (TextView) getView().findViewById(R.id.current_time);
        TCurrentClock = (TextView) getView().findViewById(R.id.clock_text);

        TEndClock = (TextView) getView().findViewById(R.id.endclock_text);
        TEndHour = (TextView) getView().findViewById(R.id.dynamicHour);
        TEndTime = (TextView) getView().findViewById(R.id.end_time);
        TSeekVal = (TextView) getView().findViewById(R.id.seekVal);
        TDayText = (TextView) getView().findViewById(R.id.dayText);

        timeSeek = (SeekBar) getView().findViewById(R.id.timeSeek);
        timeSeek.setOnSeekBarChangeListener(timeSeek_listener);

        BStartToggle = (Button) getView().findViewById(R.id.start_toggle);
        BStartToggle.setOnClickListener(BStartToggle_OnClickListener);

        //Starts the clocks.
        futureCalendar = Calendar.getInstance();
        currentCalendar = Calendar.getInstance();

        futureCalendar.add(Calendar.HOUR_OF_DAY, 1); //Sets the future clock to be at least an hour ahead.
        TEndHour.setText(futureHourFormat.format(futureCalendar.getTime()));

    }

    //On click listener for BStartToggle
    final View.OnClickListener BStartToggle_OnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //place holder
            TStatusText.setText("Started");
            BStartToggle.setText("Breakfast");
        }
    };

    //Seek bar listener for timeSeek
    final SeekBar.OnSeekBarChangeListener timeSeek_listener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            int seekValue = progress + 1;
            TDayText.setText(" ");

            if (seekValue == 1) {
                TSeekVal.setText("Duration: " + seekValue + " hour");
            } else {
                TSeekVal.setText("Duration: " + seekValue + " hours");
            }

            futureCalendar = Calendar.getInstance();
            futureCalendar.add(Calendar.HOUR_OF_DAY, seekValue);//Adds seekValue to the current hour of the day.
            TEndHour.setText(futureHourFormat.format(futureCalendar.getTime()));

            if (currentCalendar.get(Calendar.DAY_OF_WEEK) != futureCalendar.get(Calendar.DAY_OF_WEEK)) {
                //Checks if today matches future date, if not then it must be tomorrow.
                TDayText.setText(dayFormat.format(futureCalendar.getTime()));
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    public void upDateTime() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                try {
                    int previousHour = currentCalendar.get(Calendar.HOUR_OF_DAY);

                    currentCalendar = Calendar.getInstance();

                    if (previousHour != currentCalendar.get(Calendar.HOUR_OF_DAY)) {
                        // This block of code checks if the hour has changed, if it has the futureTime gets incremented by 1.
                        futureCalendar.add(Calendar.HOUR_OF_DAY, 1);
                        TEndHour.setText(futureHourFormat.format(futureCalendar.getTime()));
                    }
                    TCurrentClock.setText(currentTimeFormat.format(currentCalendar.getTime()));
                    TEndClock.setText(futureMinuteFormat.format(currentCalendar.getTime()));
                } catch (Exception e) {


                }
            }
        });
    }

    class CountDownRunner implements Runnable {
        //This thread runs every 1 second in the background which updates the text view in the upDateTime() method.
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    upDateTime();
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        myThread.interrupt();
        super.onDestroyView();
    }


}

