package com.avaygo.myfastingtracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class frontpage extends Activity {

    Button BStartToggle;
    TextView TStatusText, TCurrentTime, TEndTime, TSeekVal, TCurrentClock;
    SeekBar timeSeek;

    Thread myThread = null;
    Runnable myRunnableThread = new CountDownRunner();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontpage);
        //Load the layout

        myThread= new Thread(myRunnableThread);
        myThread.start();

        //Find View Elements
        TStatusText = (TextView) findViewById(R.id.status_text);
        TCurrentTime = (TextView) findViewById(R.id.current_time);
        TCurrentClock = (TextView) findViewById(R.id.clock_text);

        TEndTime = (TextView) findViewById(R.id.end_time);
        TSeekVal = (TextView) findViewById(R.id.seekVal);

        timeSeek = (SeekBar) findViewById(R.id.timeSeek);
        timeSeek.setOnSeekBarChangeListener(timeSeek_listener);

        BStartToggle = (Button) findViewById(R.id.start_toggle);
        BStartToggle.setOnClickListener(BStartToggle_OnClickListener);

    }

    //On click listener for BStartToggle
    final View.OnClickListener BStartToggle_OnClickListener = new View.OnClickListener(){
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

            if (seekValue == 1) {
                TSeekVal.setText("Duration: " + seekValue + " hour");
            }
            else{
                TSeekVal.setText("Duration: " + seekValue + " hours");
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
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    Calendar myCalendar = Calendar.getInstance();
                    SimpleDateFormat timeFormat;
                    timeFormat = new SimpleDateFormat("HH:mm:ss");
                    TCurrentClock.setText(timeFormat.format(myCalendar.getTime()));
                }
                catch (Exception e) {


                }
            }
        });
    }


    class CountDownRunner implements Runnable{
        //This thread runs every 1 second in the background which updates the text view in the doWork() method.
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    upDateTime();
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        myThread.interrupt();
        super.onDestroy();
    }

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
