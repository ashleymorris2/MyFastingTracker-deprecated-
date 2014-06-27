package com.avaygo.myfastingtracker;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fFastingStarted.OnFragmentInteractionListener} interface
 * to handle interaction events.
 *
 */
public class fFastingStarted extends Fragment {

    private OnFragmentInteractionListener mListener;

    //UI Elements:
    Button BStopButton;

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

        BStopButton = (Button) getView().findViewById(R.id.stop_button);
        BStopButton.setOnClickListener(BStopButton_OnClickListener);
    }

    final View.OnClickListener BStopButton_OnClickListener = new View.OnClickListener() {
        public void onClick(View view) {

            //Shared preferences, stores the current state on the button press to save the activity's session.
            SharedPreferences preferences = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
