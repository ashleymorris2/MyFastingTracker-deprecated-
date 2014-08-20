package com.avaygo.myfastingtracker.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.avaygo.myfastingtracker.R;
import com.devadvance.circularseekbar.CircularSeekBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DurationPickerDialogue extends Activity {

    private Calendar timeEnd;
    private CircularSeekBar circleSeekBar;
    private TextView textHour, textMinutes, textDay, textSeekValue;

    private SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");//The hour for the future clock.
    private SimpleDateFormat hourFormat = new SimpleDateFormat("HH");//The hour for the future clock.
    private SimpleDateFormat minuteFormat = new SimpleDateFormat(":mm");//The minutes for the future clock

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialogue_duration_picker);

        final Intent intent = getIntent();
        timeEnd = Calendar.getInstance();
        timeEnd.setTimeInMillis(intent.getLongExtra("TIME", 0));
        timeEnd.add(Calendar.HOUR_OF_DAY, 1);

        textHour = (TextView) findViewById(R.id.text_picker_hour);
        textHour.setText(hourFormat.format(timeEnd.getTimeInMillis()));

        textMinutes = (TextView) findViewById(R.id.text_picker_minutes);
        textMinutes.setText(minuteFormat.format(timeEnd.getTimeInMillis()));

        textDay = (TextView) findViewById(R.id.text_picker_day);
        textDay.setText(dayFormat.format(timeEnd.getTimeInMillis()));

        textSeekValue = (TextView) findViewById(R.id.text_seek_value);
        circleSeekBar = (CircularSeekBar) findViewById(R.id.circularSeekBar2);
        circleSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {

                int seekValue = progress + 1;

                if (seekValue == 1) {
                    textSeekValue.setText(seekValue + " Hour");
                } else {
                    textSeekValue.setText(seekValue + " Hours");
                }

                timeEnd = Calendar.getInstance();
                timeEnd.setTimeInMillis(intent.getLongExtra("TIME", 0));

                timeEnd.add(Calendar.HOUR_OF_DAY, seekValue);

                textHour.setText(hourFormat.format(timeEnd.getTime()));
                textDay.setText(dayFormat.format(timeEnd.getTime()));
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }


        });
    }
}
