package com.avaygo.myfastingtracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avaygo.myfastingtracker.Days;
import com.avaygo.myfastingtracker.R;
import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReminderSettingActivity extends FragmentActivity implements RadialTimePickerDialog.OnTimeSetListener {

    private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";

    private TextView textReminderDay, textReminderTime;
    private String cardDay, cardStart, cardDuration;
    private LinearLayout buttonSetTime, buttonSetDuration;
    private Calendar timeReminder;
    private boolean mHasDialogFrame;

    SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_setting);

        Intent intent = getIntent();
        timeReminder = Calendar.getInstance();

        //Get the bundled intent extras to update the UI on this screen
        cardDay = intent.getStringExtra("day");
        timeReminder.setTimeInMillis(intent.getLongExtra("startTime", 0));
        cardStart = TimeFormat.format(timeReminder.getTimeInMillis());

        //Sets the date to be correct
        setCalendarDay();


        if (savedInstanceState == null) {
            mHasDialogFrame = findViewById(R.id.frame) != null;
        }

        //Find and set the layout views
        textReminderDay = (TextView) findViewById(R.id.text_reminder_day);
        textReminderDay.setText(cardDay);
        textReminderTime = (TextView) findViewById(R.id.text_reminder_time);
        textReminderTime.setText(cardStart);

        //Find and set the buttons and onClickListeners
        buttonSetTime = (LinearLayout) findViewById(R.id.button_time_set);
        buttonSetTime.setOnClickListener(new View.OnClickListener() {
            View v;
            @Override
            //On button click a RadialTimePicker opens with the previous set time.
            public void onClick(View view) {
                RadialTimePickerDialog timePickerDialog = RadialTimePickerDialog
                        .newInstance(ReminderSettingActivity.this, timeReminder.get(Calendar.HOUR_OF_DAY), timeReminder.get(Calendar.MINUTE),
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

        buttonSetDuration = (LinearLayout) findViewById(R.id.button_duration_set);
        buttonSetDuration.setOnClickListener(new View.OnClickListener() {
            View v;
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DurationPickerDialogue.class);
                intent.putExtra("TIME", timeReminder.getTimeInMillis());
                startActivity(intent);
            }
        });
    }

    private void setCalendarDay() {

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

        timeReminder.set(Calendar.DAY_OF_WEEK, dayInWeek);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reminder_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    /*This method is called once the time has been set by the radialTimePicker. Updates the string in
    the UI*/
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {

        String hours, minutes;

        timeReminder.set(Calendar.HOUR_OF_DAY, hourOfDay);
        timeReminder.set(Calendar.MINUTE, minute);


        textReminderTime.setText("" + hourOfDay + ":" + minute);
    }

}
