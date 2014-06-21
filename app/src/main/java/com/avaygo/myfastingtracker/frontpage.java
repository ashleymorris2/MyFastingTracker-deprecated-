package com.avaygo.myfastingtracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class frontpage extends Activity {

    Button BStartToggle;
    TextView TStatusText, TCurrentTime, TEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontpage);

        //Find View Elements
        TStatusText = (TextView) findViewById(R.id.status_text);
        TCurrentTime = (TextView) findViewById(R.id.current_time);
        TEndTime = (TextView) findViewById(R.id.end_time);

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
