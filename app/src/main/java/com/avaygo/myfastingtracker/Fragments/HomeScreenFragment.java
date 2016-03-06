package com.avaygo.myfastingtracker.Fragments;


import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.avaygo.myfastingtracker.R;
import com.avaygo.myfastingtracker.classes.FileProvider;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class HomeScreenFragment extends Fragment {

    private Button exportButton;


    public HomeScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        exportButton = (Button) getActivity().findViewById(R.id.export_button);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exportUserContent();
            }
        });
    }

    private void exportUserContent(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/octet-stream");

        Uri uri = new FileProvider().getDatabaseURI(getActivity(), "fastingLog.db");
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(intent.createChooser(intent, "Backup using.."));
    }
}
