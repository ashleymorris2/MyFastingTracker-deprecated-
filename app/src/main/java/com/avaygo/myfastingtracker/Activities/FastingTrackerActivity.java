package com.avaygo.myfastingtracker.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.avaygo.myfastingtracker.Fragments.HomeScreenFragment;
import com.avaygo.myfastingtracker.Fragments.ReminderListScreenFragment;
import com.avaygo.myfastingtracker.Fragments.TimerSettingScreenFragment;
import com.avaygo.myfastingtracker.Fragments.TimerStartedScreenFragment;
import com.avaygo.myfastingtracker.R;


public class FastingTrackerActivity extends Activity implements TimerStartedScreenFragment.OnFragmentInteractionListener {

    boolean fastingState;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private String[] menu;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fasting_tracker);

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.container);
        mDrawerListView = (ListView) findViewById(R.id.drawerList);
        menu = getResources().getStringArray(R.array.array_menu);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerListView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, menu));
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            View v;

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getApplication(), menu[position], Toast.LENGTH_SHORT).show();
                selectItem(position);
            }
        });

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                             //Host activity
                mDrawerLayout,                    //DrawerLayout object
                R.drawable.ic_drawer,              //nav drawer image
                R.string.navdrawer_open,  //Description for accessibility.
                R.string.navdrawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);

            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActionBar().setTitle(mTitle);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            //Shared preferences to load the activity's session.
            /*SharedPreferences preferences = getSharedPreferences("appData", 0); // 0 - for private mode
            fastingState = preferences.getBoolean("IS_FASTING", false);*/

            selectItem(1);//Change for 0 when home has been implemented.

        }
    }

    //Sets the ListView Item as checked.
    private void selectItem(int position) {

        mDrawerListView.setItemChecked(position, true);
        setTitle(menu[position]);

        Fragment fragment = null;
        Bundle args = new Bundle();

        SharedPreferences preferences = getSharedPreferences("appData", 0); // 0 - for private mode
        fastingState = preferences.getBoolean("IS_FASTING", false);

        switch (position){
            case 0:
                fragment = new HomeScreenFragment();
                break;
            case 1:
                if (fastingState == false) {
                    fragment = new TimerSettingScreenFragment();
                } else {
                    fragment = new TimerStartedScreenFragment();
                }
                break;
            case 2:
                fragment = new ReminderListScreenFragment();
                break;
            default:
                break;
        }
        if(fragment != null) {
            //Remove when all cases have been implemented.
            getFragmentManager().beginTransaction()
                    .replace(R.id.mainContent, fragment)
                    .commit();
        }
        mDrawerLayout.closeDrawer(mDrawerListView);
    }

    //Changes the title of the action bar to the supplied string.
    public void setTitle(String title){
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fasting_tracker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* Handle action bar item clicks here. The action bar will
        automatically handle clicks on the Home/Up button, so long
        as you specify a parent activity in AndroidManifest.xml.

        The action bar home/up action should open or close the drawer.
        ActionBarDrawerToggle will take care of this.
        Forwards it to the mDrawerToggle*/

        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void loadSavedSession(boolean state) {
   /*Loads the previous session for the user:
     If the user is not fasting then fFastingSettings() is the default.
     If the user is fasting then fFastingStarted() is loaded instead.*/

        boolean mFastingState = state;

        if (mFastingState == false){
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new TimerSettingScreenFragment())
                    .commit();
        }
        else{
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new TimerStartedScreenFragment())
                    .commit();
        }
    }
}

