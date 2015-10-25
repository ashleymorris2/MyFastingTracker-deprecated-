package com.avaygo.myfastingtracker.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avaygo.myfastingtracker.R;
import com.avaygo.myfastingtracker.databases.LogDataSource;
import com.avaygo.myfastingtracker.notifications.AlarmSetup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.passy.holocircularprogressbar.HoloCircularProgressBar;

public class TimerStartedScreenFragment extends Fragment {

    private static MyCounter counter;
    private static final String FAST_PROGRESS = "fastProgress";

    //UI Elements:
    private HoloCircularProgressBar holoCircularProgressBar;
    private TextView textHourAndMinutes;
    private TextView textSeconds;
    private TextView textPercentComplete;
    private TextView textStartDay;
    private TextView textEndDay;
    private int duration;

    //Calendars and time formatting:
    private Calendar startCalendar, endCalendar;
    private SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private SimpleDateFormat DayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
    private AlarmSetup myNotification = new AlarmSetup();//Used to set the notification reminder.

    private LogDataSource logDataSource;

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

        TextView textFastDuration = (TextView) getView().findViewById(R.id.txt_FastingDuration);
        TextView textEndTime = (TextView) getView().findViewById(R.id.end_time);

        textHourAndMinutes = (TextView) getView().findViewById(R.id.txt_time_HoursMins);
        textSeconds = (TextView) getView().findViewById(R.id.txt_time_seconds);

        textPercentComplete = (TextView) getView().findViewById(R.id.txt_completed);
        textStartDay = (TextView) getView().findViewById(R.id.txt_start_day);
        textEndDay = (TextView) getView().findViewById(R.id.txt_end_day);

        logDataSource = new LogDataSource(getActivity());
        logDataSource.open();

        SharedPreferences preferences = getActivity().getSharedPreferences("appData", 0);
        final SharedPreferences.Editor editor = preferences.edit();

        Button buttonBreakFast = (Button) getView().findViewById(R.id.breakFast_button);
        buttonBreakFast.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                //Continues without an alert dialog if they are less than 1% or 100% into their fast.
                if (counter.getPercentageComplete() < 1 || counter.getPercentageComplete() == 100) {

                    myNotification.cancelAlarm(getActivity());

                    //Only a completed fast is saved to the database
                    if (counter.percentCompleted == 100) {

                        long newRowId = saveRecordToDatabase();
                        editor.clear();

                        //Clear this fast from shared preferences so that when the user reopens the
                        //app the session will be preserved
                        if (editor.commit()) {
                            displayAlert(newRowId);
                        }
                    } else {
                        changeFragment();
                    }
                    //Shows an alert if the user is over 1% or less than 100%
                } else {
                    AlertDialog.Builder dialogBuilder =
                            new AlertDialog.Builder(TimerStartedScreenFragment.this.getActivity());
                    dialogBuilder.setMessage("Do you want to break your fast early?");
                    dialogBuilder.setCancelable(true);

                    dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int id) {

                                    //cancels the notification
                                    myNotification.cancelAlarm(getActivity());

                                    //Works out how long the duration has been
                                    Calendar endCalendar = Calendar.getInstance();
                                    long difference = endCalendar.getTimeInMillis()
                                            - startCalendar.getTimeInMillis();

                                    long differenceHours = difference / (60 * 60 * 1000);

                                    //The fast has to be longer than an hour to be worth saving.
                                    if (differenceHours >= 1) {

                                        long newRowId = saveRecordToDatabase();
                                        editor.clear();

                                        if (editor.commit()) {
                                            displayAlert(newRowId);
                                        }
                                    } else {
                                        changeFragment();
                                    }
                                }
                            });

                    dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                }
            }
        });

        holoCircularProgressBar = (HoloCircularProgressBar)
                getView().findViewById(R.id.holoCircularProgressBar1);

        long startMill = preferences.getLong("START_TIME", 0);//The start time in milliseconds
        long endMill = preferences.getLong("END_TIME", 0);//The end time in milliseconds
        duration = preferences.getInt("END_HOUR", 1);//How many hours into the future the timer ends
        long timeLeftInMill = preferences.getLong("END_MILLISEC", 0);//The time left in milliseconds
        boolean timerStarted = preferences.getBoolean("TIMER_START", false);

        //Calculates the difference between the current time and the end time
        if (timerStarted) {
            timeLeftInMill = endMill - System.currentTimeMillis();
        }

        counter = new MyCounter(timeLeftInMill, 1000);//

        //Starts and sets the clocks.
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(startMill);
        endCalendar.setTimeInMillis(endMill);

        TextView textStartTime = (TextView) getView().findViewById(R.id.start_time);
        textStartTime.setText(TimeFormat.format(startCalendar.getTime()));

        textStartDay.setText("Today");

        //If end day is tomorrow then show tomorrows date for the user, if not then it isn't necessary.
        if (endCalendar.get(Calendar.DATE) != startCalendar.get(Calendar.DATE)) {

            textEndTime.setText(TimeFormat.format(endCalendar.getTime()));
            textEndDay.setText("Tomorrow");//If two aren't equal then end must be tomorrow.

        } else {

            textEndTime.setText(TimeFormat.format(endCalendar.getTime()));
            textEndDay.setText("Today");

        }

        //Recover the instance from the saved state, only if there is something to recover.
        if (savedInstanceState != null) {

            float progress = savedInstanceState.getFloat(FAST_PROGRESS);
            holoCircularProgressBar.setProgress(progress);

        }

        if (duration == 1) {
            textFastDuration.setText(duration + " Hour");
        } else {
            textFastDuration.setText(duration + " Hours");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putFloat(FAST_PROGRESS, holoCircularProgressBar.getProgress());
        super.onSaveInstanceState(savedInstanceState);
    }

    private long saveRecordToDatabase() {

        long newRowID;

        //Only save to the database if the fast is longer than an hour
        if (counter.getPercentageComplete() == 100) {

            //If the fast is at 100% only save the start time plus the duration.
            //This is so that the saved time is never beyond 100% of the duration.
            endCalendar.setTimeInMillis(startCalendar.getTimeInMillis());
            endCalendar.add(Calendar.HOUR_OF_DAY, duration);

            newRowID = logDataSource.createRecord(startCalendar, endCalendar, duration,
                    counter.getPercentageComplete(), "", 5);
        } else {
            //Else save the current time.
            endCalendar.setTimeInMillis(System.currentTimeMillis());
            newRowID = logDataSource.createRecord(startCalendar, endCalendar, duration,
                    counter.getPercentageComplete(), "", 5);
        }

        logDataSource.close();

        return newRowID;
    }

    private void displayAlert(final long rowId) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View alertView = layoutInflater.inflate(R.layout.dialog_fast_message, null);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());

        //Sets the layout to the alertview
        alertBuilder.setView(alertView);

        final EditText userNote = (EditText) alertView.findViewById(R.id.editTextDialogUserInput);

        long timeCompleted = System.currentTimeMillis() - startCalendar.getTimeInMillis();
        TextView textDuration = (TextView) alertView.findViewById(R.id.textDuration);

        if (counter.getPercentageComplete() == 100) {
            textDuration.setText(longToHours(timeCompleted));
        } else {
            textDuration.setText(longToHours(timeCompleted) + " " + longToMins(timeCompleted));
        }

        TextView textPercentage = (TextView) alertView.findViewById(R.id.textPercentage);
        textPercentage.setText(String.valueOf(counter.getPercentageComplete()) + "%");

        alertBuilder.setCancelable(false)
                .setTitle("Summary")
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Save the message to the database
                        logDataSource.open();
                        logDataSource.editNote(userNote.getText().toString(), rowId);
                        logDataSource.close();

                        dialogInterface.cancel();
                        changeFragment();
                    }
                });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    //Clears the shared preferences and changes the fragment.
    private synchronized void changeFragment() {

        //Shared preferences, stores the current state on the button press
        // to save the activity's session.
        SharedPreferences preferences = getActivity().getSharedPreferences("appData", 0);
        SharedPreferences.Editor editor = preferences.edit();

        //Commit the edits
        editor.clear();

        if (editor.commit()) {
            //Launches a new fragment and replaces the current one.
            //Toast.makeText(getActivity(), "Fast cancelled", Toast.LENGTH_SHORT).show();

            FragmentTransaction fragmentChange =
                    getActivity().getFragmentManager().beginTransaction();

            fragmentChange.replace(R.id.mainContent, new TimerSettingScreenFragment());
            fragmentChange.commit();
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    public void onDetach() {
        super.onDetach();
    }

    private class MyCounter extends CountDownTimer {

        private int endSeconds, elapsedTime, percentCompleted;
        private boolean dateChange = false;
        private float percentAsFloat;

        /**
         * Constructor for MyCounter class
         *
         * @param millisInFuture    time in milliseconds in the future.
         * @param countDownInterval interval in milliseconds to countdown.*
         */
        private MyCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

            // 0 = private mode - other apps have no access
            SharedPreferences preferences = getActivity().getSharedPreferences("appData", 0);
            SharedPreferences.Editor editor = preferences.edit();

            //3600 for seconds, 60 for minutes.
            endSeconds = preferences.getInt("END_HOUR", 0) * 3600;
            editor.putBoolean("TIMER_START", true);
            editor.apply();

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
            elapsedTime = endSeconds - totalMinutes;
            percentCompleted = (elapsedTime * 100) / endSeconds;

            //percentageAsFloat is needed because the circular progress bar is set to a maximum of 1.
            percentAsFloat = percentCompleted / 100f;
            holoCircularProgressBar.setProgress(percentAsFloat);

            textPercentComplete.setText(percentCompleted + "%");

            if (!dateChange) {
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
            percentCompleted = 100;
            percentAsFloat = 1;
            holoCircularProgressBar.setProgress(percentAsFloat);
            textPercentComplete.setText(percentCompleted + "%");

            textHourAndMinutes.setText("00:00");
            textSeconds.setText(":00");
        }

        public int getElapsedTime() {
            return elapsedTime;
        }

        public int getPercentageComplete() {
            return percentCompleted;
        }
    }

    public String longToHours(long millisToCalculate) {

        int iHours = (int) ((millisToCalculate / (1000 * 60 * 60)) % 24);
        int totalMinutes = (int) (millisToCalculate / 1000);

        String hours;
        String length;

        hours = Integer.toString(iHours);

        length = hours + "h";

        //Fasts less than an hour don't get recorded
        return length;
    }

    public String longToMins(long millisToCalculate) {

        int iMinutes = (int) ((millisToCalculate / (1000 * 60)) % 60);
        int totalMinutes = (int) (millisToCalculate / 1000);

        String minutes;
        String length;

        //Formatting code, add leading zero if less than 10 minutes
        if (iMinutes < 10) {
            minutes = ("0" + Integer.toString(iMinutes));
        } else {
            minutes = Integer.toString(iMinutes);
        }


        length = minutes + "m";


        return length;
    }
}
