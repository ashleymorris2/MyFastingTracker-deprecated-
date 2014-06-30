package com.avaygo.myfastingtracker;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class fFastingSettings extends Fragment {

    //UI Elements:
    Button BStartToggle;
    TextView TStatusText, TCurrentTime, TEndTime, TSeekVal, TCurrentClock, TEndClock, TEndHour, TDayText;
    SeekBar timeSeek;
    //Threads and Runnables:
    Thread myThread = null;
    Runnable myRunnableThread = new CountDownRunner();
    //Calendars and time formatting:
    Calendar currentCalendar, futureCalendar;
    SimpleDateFormat currentTimeFormat = new SimpleDateFormat("HH:mm");//Current time
    SimpleDateFormat futureHourFormat = new SimpleDateFormat("HH:");//The hour for the future clock.
    SimpleDateFormat futureMinuteFormat = new SimpleDateFormat("mm");//The minutes for the future clock
    SimpleDateFormat dayFormat = new SimpleDateFormat(" EE");//
    //Fragment Class:
    FragmentTransaction fragmentChange;
    //Classes:
    cNotificationSetup myNotification = new cNotificationSetup();//Used to set the notification reminder.

    public fFastingSettings(){
        //empty constructor, ok then.
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fasting_length, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("fFastingSettings-onActivityCreated", "fasting should be 0");

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

        //Sets the future clock to be at least an hour ahead and updates the text.
        futureCalendar.add(Calendar.HOUR_OF_DAY, 1);
        TEndHour.setText(futureHourFormat.format(futureCalendar.getTime()));
    }

    //On click listener for BStartToggle
    final View.OnClickListener BStartToggle_OnClickListener = new View.OnClickListener() {
        public void onClick(View view) {

            //Passes the future time to the notification class so that it can set a notification at that time.
            myNotification.setReminderCalendar(futureCalendar);
            myNotification.createAlarm(getActivity());

            //Shared preferences, stores the current state on the button press to save the activity's session.
            SharedPreferences preferences = getActivity().getSharedPreferences("appData", 0); // 0 - for private mode
            SharedPreferences.Editor editor = preferences.edit();

            //IS_FASTING tag is used to describe the current state of the session.
            editor.putBoolean("IS_FASTING", true);

            editor.putLong("START_TIME", currentCalendar.getTimeInMillis());
            editor.putLong("END_TIME", futureCalendar.getTimeInMillis());

            // Commit the edits
            editor.commit();

            //Launches a new fragment and replaces the current one.
            fragmentChange = getActivity().getFragmentManager().beginTransaction();
            fragmentChange.replace(R.id.container, new fFastingStarted());
            fragmentChange.commit();
        }
    };

    //Seek bar listener for timeSeek
    final SeekBar.OnSeekBarChangeListener timeSeek_listener = new SeekBar.OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            int seekValue = progress + 1;
            TDayText.setText(" ");

            if (seekValue == 1) {
                TSeekVal.setText("Duration: " + seekValue + " hour");
            } else {
                TSeekVal.setText("Duration: " + seekValue + " hours");
            }

            futureCalendar = Calendar.getInstance();//Opens an instance of the future calendar.

            //Adds seekValue to the current hour of the day and updates the text.
            futureCalendar.add(Calendar.HOUR_OF_DAY, seekValue);
            TEndHour.setText(futureHourFormat.format(futureCalendar.getTime()));

            /*Checks if today matches future date, if not then it must be tomorrow, sets the text to tomorrows
            day.*/
            if (currentCalendar.get(Calendar.DAY_OF_WEEK) != futureCalendar.get(Calendar.DAY_OF_WEEK)) {
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
                    //Gets the hour before updating the time.
                    int previousHour = currentCalendar.get(Calendar.HOUR_OF_DAY);
                    currentCalendar = Calendar.getInstance();

                   /* This block of code checks if the hour has changed, if it has the futureTime gets incremented by 1.
                    to keep the two clocks in sync with each other.*/
                    if (previousHour != currentCalendar.get(Calendar.HOUR_OF_DAY)) {
                        futureCalendar.add(Calendar.HOUR_OF_DAY, 1);
                        TEndHour.setText(futureHourFormat.format(futureCalendar.getTime()));
                    }

                    TCurrentClock.setText(currentTimeFormat.format(currentCalendar.getTime()));
                    TEndClock.setText(futureMinuteFormat.format(currentCalendar.getTime()));
                } catch (Exception e) {
                    Log.e("UpDateTime", "An error has happened!");
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

