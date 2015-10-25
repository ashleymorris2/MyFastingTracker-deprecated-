package com.avaygo.myfastingtracker.fragments;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.avaygo.myfastingtracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatsScreenFragment extends Fragment {


    public StatsScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats_screen, container, false);
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Spinner spinnerPeriod = (Spinner) getView().findViewById(R.id.spinner_period);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.array_period,
                R.layout.my_spinner);
        spinnerPeriod.setAdapter(adapter);

    }
}
