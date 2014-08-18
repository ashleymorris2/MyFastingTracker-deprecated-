package com.avaygo.myfastingtracker.Activities;

import android.app.Activity;
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

import com.avaygo.myfastingtracker.R;
import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;

import java.util.Calendar;

public class ReminderSettingActivity extends FragmentActivity implements RadialTimePickerDialog.OnTimeSetListener {

    private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";

    private TextView tReminderDay, tReminderTime;
    private String cardDay;
    private LinearLayout buttonTimeSet;

    private Calendar timeNow;

    private boolean mHasDialogFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_setting);

        Intent intent = getIntent();
        cardDay = intent.getStringExtra("day");

        if (savedInstanceState == null) {
            mHasDialogFrame = findViewById(R.id.frame) != null;
        }

        tReminderDay = (TextView) findViewById(R.id.text_reminder_day);
        tReminderDay.setText(cardDay);

        tReminderTime = (TextView) findViewById(R.id.text_reminder_time);

        buttonTimeSet = (LinearLayout) findViewById(R.id.time_set_button);
        buttonTimeSet.setOnClickListener(new View.OnClickListener() {
            View v;
            @Override
            public void onClick(View view) {

                timeNow = Calendar.getInstance();
                RadialTimePickerDialog timePickerDialog = RadialTimePickerDialog
                        .newInstance(ReminderSettingActivity.this, timeNow.get(Calendar.HOUR),timeNow.get(Calendar.MINUTE),
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
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {

        tReminderTime.setText("" + hourOfDay + ":" + minute);

    }

}
