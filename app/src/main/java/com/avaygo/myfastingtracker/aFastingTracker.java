package com.avaygo.myfastingtracker;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.cengalabs.flatui.FlatUI;


public class aFastingTracker extends Activity implements fFastingStarted.OnFragmentInteractionListener {

    boolean fastingState;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fasting_tracker);

        FlatUI.initDefaultValues(this);
        FlatUI.setDefaultTheme(FlatUI.SEA);



        if (savedInstanceState == null) {
            //Shared preferences to load the activity's session.
            SharedPreferences preferences = getSharedPreferences("appData", 0); // 0 - for private mode
            fastingState = preferences.getBoolean("IS_FASTING", false);

            loadSavedSession(fastingState);
        }
    }

    private void loadSavedSession(boolean state) {
   /*Loads the previous session for the user:
     If the user is not fasting then fFastingSettings() is the default.
     If the user is fasting then fFastingStarted() is loaded instead.*/

        boolean mFastingState = state;

        if (mFastingState == false){
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new fFastingSettings())
                    .commit();
        }
        else{
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new fFastingStarted())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fasting_tracker, menu);
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
    public void onFragmentInteraction(Uri uri) {

    }

}

