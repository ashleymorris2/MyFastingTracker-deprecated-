package com.avaygo.myfastingtracker.activities;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;


import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.avaygo.myfastingtracker.R;

import com.avaygo.myfastingtracker.Fragments.HomeScreenFragment;
import com.avaygo.myfastingtracker.Fragments.LogScreenFragment;
import com.avaygo.myfastingtracker.Fragments.ReminderListScreenFragment;
import com.avaygo.myfastingtracker.Fragments.StatsScreenFragment;
import com.avaygo.myfastingtracker.Fragments.TimerSettingScreenFragment;
import com.avaygo.myfastingtracker.Fragments.TimerStartedScreenFragment;


public class MainActivity extends AppCompatActivity{

    boolean fastingState;

    private DrawerLayout mDrawerLayout;

    private int screenNumber;

    private boolean  fromReminder;

    static final String STATE_SCREEN = "userScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fasting_tracker);

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create Navigation drawer and inflate layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);

                        selectItem(menuItem.getItemId());

                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        Intent intent = getIntent();
        fromReminder = intent.getBooleanExtra("fromReminder", false);

        if (savedInstanceState == null) {
            if(fromReminder){
                int savedFragment = intent.getIntExtra("fragmentNumber", 1);
                selectItem(savedFragment);
            }
            else {
             screenNumber =  selectItem(R.id.timer);//Change for home when it has been implemented.
            }
        }
        else{
            //Restore the users activity
            screenNumber = savedInstanceState.getInt(STATE_SCREEN);
            selectItem(screenNumber);
        }

    }

    //Sets the ListView Item as checked.
    private int selectItem(final int itemId) {

        Bundle args = new Bundle();

        SharedPreferences preferences = getSharedPreferences("appData", 0); // 0 - for private mode
        fastingState = preferences.getBoolean("IS_FASTING", false);

        new Handler().postDelayed(new Runnable() {
            public void run() {

                Fragment fragment = null;

                switch (itemId) {

                    case R.id.home:
                        fragment = new HomeScreenFragment();
                        break;

                    case R.id.timer:
                        if (!fastingState) {
                            fragment = new TimerSettingScreenFragment();
                            if (fromReminder = true) {
                                fragment.setArguments(getIntent().getExtras());
                            }
                        }
                        else {
                            fragment = new TimerStartedScreenFragment();
                        }
                        break;

                    case R.id.reminder:
                        fragment = new ReminderListScreenFragment();
                        break;

                    case R.id.log:
                        fragment = new LogScreenFragment();
                        break;

                    case R.id.stats:
                        fragment = new StatsScreenFragment();
                        break;

                    default:
                        break;
                }
                if (fragment != null) {
                    //Remove when all cases have been implemented.
                    //Position will be used as a tag.
                    getFragmentManager().beginTransaction()
                            .replace(R.id.mainContent, fragment)
                            .commit();
                }
            }
        }, 280);

        return itemId;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fasting_tracker, menu);

        //Make the switch invisible.
        MenuItem mSwitchMenuItem = menu.findItem(R.id.myswitch);
        mSwitchMenuItem.setVisible(false);


        return super.onCreateOptionsMenu(menu);
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
        } else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        //Save the users current activity state.
        savedInstanceState.putInt(STATE_SCREEN, screenNumber);

        super.onSaveInstanceState(savedInstanceState);
    }

}

