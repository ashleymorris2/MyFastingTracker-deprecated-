package com.avaygo.myfastingtracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.avaygo.myfastingtracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FastDetailsActivity extends Activity {

    SimpleDateFormat titleFormat = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault());
    SimpleDateFormat dayDateMonthFormat = new SimpleDateFormat("EEEE, d MMMM", Locale.getDefault());
    SimpleDateFormat timeFormat =  new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_details);

        final Intent intent = getIntent();

        Calendar recordDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();

        recordDate.setTimeInMillis(intent.getLongExtra("LOG_TIMESTAMP", 0));
        startDate.setTimeInMillis(intent.getLongExtra("START_TIMESTAMP", 0));

        TextView textEndTitle = (TextView) findViewById(R.id.text_end_title);
        textEndTitle.setText(titleFormat.format(recordDate.getTime()));

        TextView textblock1 = (TextView) findViewById(R.id.text_block_1);
        textblock1.setText("This fast started at " + timeFormat.format(startDate.getTime()) +" on "+
                dayDateMonthFormat.format(startDate.getTime()) +" and ended at "+ timeFormat.format(recordDate.getTime())
                        +" on "+ dayDateMonthFormat.format(recordDate.getTime())+".");

        TextView textNote = (TextView) findViewById(R.id.text_note);
        textNote.setText(intent.getStringExtra("USER_NOTE"));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fast_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
