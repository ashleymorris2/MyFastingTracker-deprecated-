package com.avaygo.myfastingtracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.avaygo.myfastingtracker.R;
import com.devadvance.circularseekbar.CircularSeekBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * DurationPickerDialogue is a dialogue screen that allows a used to select the duration of the fast
 * in the reminder settings.
 */
public class DurationPickerDialogue extends Activity {

    private Calendar timeEnd;
    private CircularSeekBar circleSeekBar;
    private TextView textHour, textMinutes, textDay, textSeekValue;
    private Button buttonDone;

    private int duration;

    private SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");//The day for the future clock.
    private SimpleDateFormat hourFormat = new SimpleDateFormat("HH");//The hour for the future clock.
    private SimpleDateFormat minuteFormat = new SimpleDateFormat(":mm");//The minutes for the future clock

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Sets the layout to be similar to a dialog box
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialogue_duration_picker);

        final Intent intent = getIntent();
        duration = intent.getIntExtra("DURATION", 1);

        timeEnd = Calendar.getInstance();
        timeEnd.setTimeInMillis(intent.getLongExtra("TIME", 0));

        //Adds the duration to the calendars hour.
        timeEnd.add(Calendar.HOUR_OF_DAY, duration);

        textHour = (TextView) findViewById(R.id.text_picker_hour);
        textHour.setText(hourFormat.format(timeEnd.getTimeInMillis()));

        textMinutes = (TextView) findViewById(R.id.text_picker_minutes);
        textMinutes.setText(minuteFormat.format(timeEnd.getTimeInMillis()));

        textDay = (TextView) findViewById(R.id.text_picker_day);
        textDay.setText(dayFormat.format(timeEnd.getTimeInMillis()));

        textSeekValue = (TextView) findViewById(R.id.text_seek_value);
        if (duration == 1) {
            textSeekValue.setText(duration + " Hour");
        } else {
            textSeekValue.setText(duration + " Hours");
        }

        circleSeekBar = (CircularSeekBar) findViewById(R.id.circularSeekBar2);
        circleSeekBar.setProgress(duration - 1);
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

                duration = seekValue;
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

        });

        buttonDone = (Button) findViewById(R.id.button_done);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            View v;
            @Override
            public void onClick(View view) {
                //On click the activity is finished and the result is returned to the calling activity
                Intent intent = new Intent();
                intent.putExtra("DURATION", duration);
                intent.putExtra("TIME", timeEnd.getTimeInMillis());

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
