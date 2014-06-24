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

    //UI Elements:
    Button BStartToggle;
    TextView TStatusText, TCurrentTime, TEndTime, TSeekVal, TCurrentClock, TEndClock, TEndHour;
    TextView TDayText;
    SeekBar timeSeek;

    //Threads and Runnables.
    Thread myThread = null;
    Runnable myRunnableThread = new CountDownRunner();

    //Calendars and time formatting.
    Calendar currentCalendar, futureCalendar;
    SimpleDateFormat currentTimeFormat = new SimpleDateFormat("HH:mm:ss");//Current time
    SimpleDateFormat futureHourFormat = new SimpleDateFormat("HH:");//The hour for the future clock.
    SimpleDateFormat futureMinuteFormat = new SimpleDateFormat("mm:ss");//The minutes for the future clock

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontpage);//Load the layout


        myThread= new Thread(myRunnableThread);//New thread to run the timer separately so that the UI doesn't get held up.
        myThread.start();

        //Find View Elements
        TStatusText = (TextView) findViewById(R.id.status_text);
        TCurrentTime = (TextView) findViewById(R.id.current_time);
        TCurrentClock = (TextView) findViewById(R.id.clock_text);

        TEndClock = (TextView) findViewById(R.id.endclock_text);
        TEndHour = (TextView) findViewById(R.id.dynamicHour);
        TEndTime = (TextView) findViewById(R.id.end_time);
        TSeekVal = (TextView) findViewById(R.id.seekVal);
        TDayText = (TextView) findViewById(R.id.dayText);

        timeSeek = (SeekBar) findViewById(R.id.timeSeek);
        timeSeek.setOnSeekBarChangeListener(timeSeek_listener);

        BStartToggle = (Button) findViewById(R.id.start_toggle);
        BStartToggle.setOnClickListener(BStartToggle_OnClickListener);

        //Starts the clocks.
        futureCalendar = Calendar.getInstance();
        currentCalendar = Calendar.getInstance();

        futureCalendar.add(Calendar.HOUR_OF_DAY, 1); //Sets the future clock to be at least an hour ahead.
        TEndHour.setText(futureHourFormat.format(futureCalendar.getTime()));

    }

    //On click listener for BStartToggle
    final View.OnClickListener BStartToggle_OnClickListener = new View.OnClickListener() {
        public void onClick(View v){
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

            SimpleDateFormat dayFormat;

            if (seekValue == 1) {
                TSeekVal.setText("Duration: " + seekValue + " hour");
            }
            else{
                TSeekVal.setText("Duration: " + seekValue + " hours");
            }

            futureCalendar = Calendar.getInstance();

            dayFormat = new SimpleDateFormat(" EE");//For the day of the week.

            futureCalendar.add(Calendar.HOUR_OF_DAY, seekValue);//Adds seekValue to the current hour of the day.
            TEndHour.setText(futureHourFormat.format(futureCalendar.getTime()));
            TDayText.setText(dayFormat.format(futureCalendar.getTime()));

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
                    int previousHour =  currentCalendar.get(Calendar.HOUR_OF_DAY);

                    currentCalendar = Calendar.getInstance();

                    if (previousHour != currentCalendar.get(Calendar.HOUR_OF_DAY))
                    {
                        // This block of code checks if the hour has changed, if it has the futureTime gets incremented by 1.
                        futureCalendar.add(Calendar.HOUR_OF_DAY, 1);
                        TEndHour.setText(futureHourFormat.format(futureCalendar.getTime()));
                    }
                    TCurrentClock.setText(currentTimeFormat.format(currentCalendar.getTime()));
                    TEndClock.setText(futureMinuteFormat.format(currentCalendar.getTime()));
                }
                catch (Exception e) {


                }
            }
        });
    }

    class CountDownRunner implements Runnable{
        //This thread runs every 1 second in the background which updates the text view in the upDateTime() method.
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    upDateTime();
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                    Thread.currentThread().interrupt();
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
