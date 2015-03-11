package com.avaygo.myfastingtracker.fragment;


import com.avaygo.myfastingtracker.R;
import com.avaygo.myfastingtracker.adapters.CaldroidCustomAdapter;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

/**
 * Created by Ash on 03/03/2015.
 */
public class CaldroidCustomFragment extends CaldroidFragment {

    @Override
    protected int getGridViewRes() {
        return R.layout.custom_grid_fragment;
    }

    //Returns the custom adapter that we have created to the new fragment
    //The calendar can now be customised.
    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        return new CaldroidCustomAdapter(getActivity(), month, year,
                getCaldroidData(), extraData);
    }


}
