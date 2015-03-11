package com.avaygo.myfastingtracker.Activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avaygo.myfastingtracker.Days;
import com.avaygo.myfastingtracker.R;
import com.avaygo.myfastingtracker.databases.AlarmsDataSource;
import com.avaygo.myfastingtracker.notifications.RecurringAlarmSetup;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReminderSettingActivity extends FragmentActivity implements RadialTimePickerDialog.OnTimeSetListener {

    private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";

    private TextView textReminderDay, textReminderStart, textDuration, textReminderEnd;
    private String cardDay, cardStart;
    private LinearLayout buttonSetTime, buttonSetDuration;
    private Calendar timeFastStart, timeFastEnd;
    private AlarmsDataSource mAlarmsDataSource;
    private RecurringAlarmSetup mRecurringAlarm;

    private boolean mHasDialogFrame;

    private int duration, _id;

    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    SimpleDateFormat timeDayFormat = new SimpleDateFormat("HH:mm, EEEE");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate a "Done/Discard" custom action bar view.
        LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        final View customActionBarView = inflater.inflate(R.layout.actionbar_done_discard, null);


        // Show the custom action bar view and hide the normal Home icon and title.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,ActionBar.DISPLAY_SHOW_CUSTOM
                        | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE
        );
        actionBar.setCustomView(customActionBarView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
        );

        setContentView(R.layout.activity_reminder_setting);

        Intent intent = getIntent();
        timeFastStart = Calendar.getInstance();
        timeFastEnd = Calendar.getInstance();

        //Get the bundled intent extras to update the UI on this screen
        cardDay = intent.getStringExtra("day");
        duration = intent.getIntExtra("duration", 1);
        _id = intent.getIntExtra("_id", 0);

        timeFastStart.setTimeInMillis(intent.getLongExtra("startTime", 0));
        timeFastEnd.setTimeInMillis(intent.getLongExtra("startTime", 0));

        //Sets the day to be correct
        setCalendarDay();

        timeFastEnd.add(Calendar.HOUR_OF_DAY, duration);

        cardStart = timeFormat.format(timeFastStart.getTimeInMillis());

        textDuration = (TextView) findViewById(R.id.text_duration);
        if (duration == 1) {
            textDuration.setText(Integer.toString(duration) + " Hour");
        } else {
            textDuration.setText(Integer.toString(duration) + " Hours");
        }

        //==============================================================

        if (savedInstanceState == null) {
            mHasDialogFrame = findViewById(R.id.frame) != null;
        }

        //Find and set the layout views
        textReminderDay = (TextView) findViewById(R.id.text_reminder_day);
        textReminderDay.setText(cardDay);

        textReminderStart = (TextView) findViewById(R.id.text_reminder_time);
        textReminderStart.setText(cardStart);

        textReminderEnd = (TextView) findViewById(R.id.text_reminder_end);
        textReminderEnd.setText(timeDayFormat.format(timeFastEnd.getTime()));

        //===========================================\\
        //Find and set the buttons and onClickListeners
        //===========================================//

        //DONE BUTTON
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener
                (new View.OnClickListener() {

            View v;

            @Override
            public void onClick(View view) {

                mAlarmsDataSource = new AlarmsDataSource(view.getContext());
                mRecurringAlarm = new RecurringAlarmSetup();

                mAlarmsDataSource.open();

                int result = mAlarmsDataSource.updateAlarm(_id, timeFastStart, duration,
                        timeFastEnd);

                if (result == 1) {

                    //Creates or updates a recurring alarm if alarms have been enabled for this reminder
                    //if not only the reminder is changed and the alarm isn't altered.
                    if (mAlarmsDataSource.getIsEnabled(_id) == 1) {

                        mRecurringAlarm.createRecurringAlarm(getApplicationContext(),
                                timeFastStart, _id);
                    }

                    Toast.makeText(view.getContext(), "Reminder has been updated",
                            Toast.LENGTH_SHORT).show();

                }
                mAlarmsDataSource.close();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        );
        //DISCARD BUTTON
        customActionBarView.findViewById(R.id.actionbar_discard).setOnClickListener
                (new View.OnClickListener() {
            View v;
            @Override
            public void onClick(View v) {

                finish();

            }
        }
        );

        //SET TIME BUTTON
        buttonSetTime = (LinearLayout) findViewById(R.id.button_time_set);
        buttonSetTime.setOnClickListener(new View.OnClickListener() {
            View v;

            @Override
            //On button click a RadialTimePicker opens with the previous set time.
            public void onClick(View view) {
                RadialTimePickerDialog timePickerDialog = RadialTimePickerDialog
                        .newInstance(ReminderSettingActivity.this,
                                timeFastStart.get(Calendar.HOUR_OF_DAY),
                                timeFastStart.get(Calendar.MINUTE),
                                DateFormat.is24HourFormat(ReminderSettingActivity.this));

                if (mHasDialogFrame) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                    ft.add(R.id.frame, timePickerDialog, FRAG_TAG_TIME_PICKER)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                } else {
                    timePickerDialog.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
                }
            }
        });

        //DURATION BUTTON
        buttonSetDuration = (LinearLayout) findViewById(R.id.button_duration_set);
        buttonSetDuration.setOnClickListener(new View.OnClickListener() {
            View v;

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DurationPickerDialogue.class);
                intent.putExtra("TIME", timeFastStart.getTimeInMillis());
                intent.putExtra("DURATION", duration);

                //Starts an activity expecting a result to return. 1 is the request code.
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Long time;

        //If the request code is the same and the result is returned ok:
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                //Uses the returned bundled extras to update the UI on this screen and set the timers
                duration = data.getIntExtra("DURATION", 1);
                time = data.getLongExtra("TIME", 0);

                timeFastEnd.setTimeInMillis(time);

                if (timeFastEnd.get(Calendar.DAY_OF_WEEK) != timeFastStart.get(Calendar.DAY_OF_WEEK)) {
                    textReminderEnd.setText(timeDayFormat.format(timeFastEnd.getTime()));
                } else {
                    textReminderEnd.setText(timeFormat.format(timeFastEnd.getTime()));
                }

                //==================================================================================

                textDuration = (TextView) findViewById(R.id.text_duration);
                if (duration == 1) {
                    textDuration.setText(Integer.toString(duration) + " Hour");
                } else {
                    textDuration.setText(Integer.toString(duration) + " Hours");
                }
            }
        }
    }

    private void setCalendarDay() {
        //Enumerates the cards day to a numerical value that can be used to set the day on the calendars.

        int dayInWeek = 0;
        Days currentDay = Days.valueOf(cardDay.toUpperCase());

        switch (currentDay) {
            case MONDAY:
                dayInWeek = 2;
                break;
            case TUESDAY:
                dayInWeek = 3;
                break;
            case WEDNESDAY:
                dayInWeek = 4;
                break;
            case THURSDAY:
                dayInWeek = 5;
                break;
            case FRIDAY:
                dayInWeek = 6;
                break;
            case SATURDAY:
                dayInWeek = 7;
                break;
            case SUNDAY:
                dayInWeek = 1;
                break;
        }

        timeFastStart.set(Calendar.DAY_OF_WEEK, dayInWeek);
        timeFastEnd.set(Calendar.DAY_OF_WEEK, dayInWeek);
    }

    @Override
    public void onTimeSet(RadialTimePickerDialog radialTimePickerDialog, int hourOfDay, int minute) {
       /*This method is called once the time has been set by the radialTimePicker. Updates the string in
        the UI and sets the calendar */

        timeFastStart.set(Calendar.HOUR_OF_DAY, hourOfDay);
        timeFastStart.set(Calendar.MINUTE, minute);
        textReminderStart.setText(timeFormat.format(timeFastStart.getTime()));

        //Sets the future clock to be ahead of the current clock.
        timeFastEnd.setTimeInMillis(timeFastStart.getTimeInMillis());
        timeFastEnd.add(Calendar.HOUR_OF_DAY, duration);

        if (timeFastEnd.get(Calendar.DAY_OF_WEEK) != timeFastStart.get(Calendar.DAY_OF_WEEK)) {
            textReminderEnd.setText(timeDayFormat.format(timeFastEnd.getTime()));
        } else {
            textReminderEnd.setText(timeFormat.format(timeFastEnd.getTime()));
        }
    }
}