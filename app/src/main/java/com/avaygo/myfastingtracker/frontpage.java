package com.avaygo.myfastingtracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;



public class frontpage extends Activity {

    Button BStartToggle;
    TextView TStatusText, TCurrentTime, TEndTime, TSeekVal;
    SeekBar timeSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontpage);
        //Load the layout

        //Find View Elements
        TStatusText = (TextView) findViewById(R.id.status_text);
        TCurrentTime = (TextView) findViewById(R.id.current_time);
        TEndTime = (TextView) findViewById(R.id.end_time);
        TSeekVal = (TextView) findViewById(R.id.seekVal);

        timeSeek = (SeekBar) findViewById(R.id.timeSeek);

        timeSeek.setOnSeekBarChangeListener(timeSeek_listener);

        BStartToggle = (Button) findViewById(R.id.start_toggle);
        BStartToggle.setOnClickListener(BStartToggle_OnClickListener);

    }


    //On click listener for BStartToggle
    final View.OnClickListener BStartToggle_OnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //place holder
            TStatusText.setText("Fasting Started");
        }
    };

    final SeekBar.OnSeekBarChangeListener timeSeek_listener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            TSeekVal.setText("Hours: " +progress);

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.frontpage, menu);
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
}
