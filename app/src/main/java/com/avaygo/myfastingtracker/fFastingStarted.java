package com.avaygo.myfastingtracker;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;



public class fFastingStarted extends Fragment {

    private OnFragmentInteractionListener mListener;

    //UI Elements:
    Button BtnBreakFast;
    TextView txtStartTime, txtEndTime;

    //Calendars and time formatting
    Calendar startCalendar, endCalendar;
    SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm");


    //Fragment Class:
    FragmentTransaction fragmentChange;

    public fFastingStarted() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_f_fasting_started, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Find View Elements
        txtStartTime = (TextView) getView().findViewById(R.id.start_time);
        txtEndTime = (TextView) getView().findViewById(R.id.end_time);

        BtnBreakFast = (Button) getView().findViewById(R.id.breakFast_button);
        BtnBreakFast.setOnClickListener(BtnBreakFast_OnClickListener);

        SharedPreferences preferences = getActivity().getSharedPreferences("appData", 0); // 0 - for private mode

        long startMill = preferences.getLong("START_TIME", 0);
        long endMill = preferences.getLong("END_TIME", 0);

        //Starts and sets the clocks.
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(startMill);
        endCalendar.setTimeInMillis(endMill);


        txtStartTime.setText(TimeFormat.format(startCalendar.getTime()));
        txtEndTime.setText(TimeFormat.format(endCalendar.getTime()));



    }



    final View.OnClickListener BtnBreakFast_OnClickListener = new View.OnClickListener() {
        public void onClick(View view) {

            //Shared preferences, stores the current state on the button press to save the activity's session.
            SharedPreferences preferences = getActivity().getSharedPreferences("appData", 0); // 0 - for private mode
            SharedPreferences.Editor editor = preferences.edit();

            //IS_FASTING tag is used to describe the current state of the session.
            editor.putBoolean("IS_FASTING", false);

            // Commit the edits
            editor.commit();

            //Launches a new fragment and replaces the current one.
            fragmentChange = getActivity().getFragmentManager().beginTransaction();
            fragmentChange.replace(R.id.container, new fFastingSettings());
            fragmentChange.commit();
        }
    };

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
