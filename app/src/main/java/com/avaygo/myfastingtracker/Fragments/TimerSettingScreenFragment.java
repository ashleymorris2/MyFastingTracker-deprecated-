package com.avaygo.myfastingtracker.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;

import com.avaygo.myfastingtracker.Notifications.AlarmSetup;
import com.avaygo.myfastingtracker.R;
import com.devadvance.circularseekbar.CircularSeekBar;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimerSettingScreenFragment extends Fragment {

    //UI Elements:
    private Button buttonStart;
    private TextView textSeekBarValue, textCurrentClock, textEndClock, textEndHour,
            textStartDay, textEndDay;

    private CircularSeekBar seekbarTime;

    //Threads and Runnables:
    private Thread myThread = null;
    private Runnable myRunnableThread = new CountDownRunner();

    //Calendars and time formatting:
    private Calendar currentCalendar, futureCalendar;
    private SimpleDateFormat currentTimeFormat = new SimpleDateFormat("HH:mm");//Current time
    private SimpleDateFormat DayFormat = new SimpleDateFormat("EEEE");//The day for the future clock.
    private SimpleDateFormat hourFormat = new SimpleDateFormat("HH:");//The hour for the future clock.
    private SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");//The minutes for the future clock

    //Fragment Class:
    private FragmentTransaction fragmentChange;

    //Custom Classes:
    AlarmSetup myNotification = new AlarmSetup();//Used to set the notification reminder.

    public TimerSettingScreenFragment(){
        //empty constructor, ok then.
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fasting_length, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myThread = new Thread(myRunnableThread);//New thread to run the timer separately so that the UI doesn't get held up.
        myThread.start();

        //===================================================================
        //Find View Elements
        //===================================================================
        textCurrentClock = (TextView) getView().findViewById(R.id.clock_text);
        textEndClock = (TextView) getView().findViewById(R.id.endclock_text);
        textEndHour = (TextView) getView().findViewById(R.id.dynamicHour);

        textEndDay = (TextView) getView().findViewById(R.id.TXT_FUTURE);
        textStartDay = (TextView) getView().findViewById(R.id.TXT_TODAY);
        textSeekBarValue = (TextView) getView().findViewById(R.id.seekVal);

        //====================================================================

        seekbarTime = (CircularSeekBar) getView().findViewById(R.id.circularSeekBar2);

        seekbarTime.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }

            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                int seekValue = progress + 1;

                if (seekValue < 10) {
                    textSeekBarValue.setText("0" + seekValue + ":00");
                } else {
                    textSeekBarValue.setText(seekValue + ":00");
                }

                futureCalendar = Calendar.getInstance();//Opens an instance of the future calendar.

                //Adds seekValue to the current hour of the day and updates the text.
                futureCalendar.add(Calendar.HOUR_OF_DAY, seekValue);

                textEndHour.setText(hourFormat.format(futureCalendar.getTime()));
                textEndDay.setText(DayFormat.format(futureCalendar.getTime()));
            }

            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                int seekValue = seekBar.getProgress();

                seekValue += 1;

                //Once the user has stopped using the circle seekbar then their selection is saved into the shared preferences.
                SharedPreferences preferences = getActivity().getSharedPreferences("appData", 0); // 0 - for private mode
                SharedPreferences.Editor editor = preferences.edit();

                long endMillies = seekValue * 3600000;//3600000 is 1 hour in milliseconds.

                editor.putInt("END_HOUR", seekValue);//@END_HOUR how many hours into the future the timer will run until
                editor.putLong("END_MILLISEC", endMillies);//@END_MILLISEC the number of hours broken down into milliseconds.
                editor.commit();
            }
        });

        //START BUTTON
        buttonStart = (Button) getView().findViewById(R.id.start_toggle);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            private View v;

            public void onClick(View v) {

                futureCalendar.set(Calendar.MINUTE, currentCalendar.get(Calendar.MINUTE));
                futureCalendar.set(Calendar.SECOND, currentCalendar.get(Calendar.SECOND));

                //Passes the future time to the notification class so that it can set a notification at that time.
                myNotification.setAlarmTime(futureCalendar);
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
                fragmentChange.replace(R.id.mainContent, new TimerStartedScreenFragment());
                fragmentChange.commit();
            }
        });

        futureCalendar = Calendar.getInstance();
        currentCalendar = Calendar.getInstance();

        //Sets the future clock to be at least an hour ahead and updates the text.
        futureCalendar.add(Calendar.HOUR_OF_DAY, 1);
        textEndHour.setText(hourFormat.format(futureCalendar.getTime()));

        //Starts the clocks
        textCurrentClock.setText(currentTimeFormat.format(currentCalendar.getTime()));
        textEndClock.setText(minuteFormat.format(currentCalendar.getTime()));

        textStartDay.setText(DayFormat.format(currentCalendar.getTime()));
        textEndDay.setText(DayFormat.format(futureCalendar.getTime()));

        SharedPreferences preferences = getActivity().getSharedPreferences("appData", 0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();

        //Default end of one hour is set.
        editor.putInt("END_HOUR", 1);
        editor.putLong("END_MILLISEC", 3600000);
        editor.commit();
    }

//    Updates the time in the UI thread, changes the textview to match.
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
                        textEndHour.setText(hourFormat.format(futureCalendar.getTime()));

                        textStartDay.setText(DayFormat.format(currentCalendar.getTime()));
                        textEndDay.setText(DayFormat.format(futureCalendar.getTime()));
                    }

                    textCurrentClock.setText(currentTimeFormat.format(currentCalendar.getTime()));
                    textEndClock.setText(minuteFormat.format(currentCalendar.getTime()));
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

    public void onDestroyView() {
        super.onDestroyView();
        myThread.interrupt();
    }
}

