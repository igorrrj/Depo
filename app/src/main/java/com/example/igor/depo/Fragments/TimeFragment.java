package com.example.igor.depo.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.igor.depo.Adapters.TimeAdapter;
import com.example.igor.depo.R;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Igor on 17.02.2017.
 */

public class TimeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.time,container, false);

        ((TextView)rootView.findViewById(R.id.begin_time)).setText(getArguments().getString("begin_time"));
        ((TextView)rootView.findViewById(R.id.end_time)).setText(getArguments().getString("end_time"));
        ((TextView)rootView.findViewById(R.id.from_depo)).setText(getArguments().getString("from_depo"));
        ((TextView)rootView.findViewById(R.id.to_depo)).setText(getArguments().getString("to_depo"));
        ((TextView)rootView.findViewById(R.id.interval)).setText(getArguments().getString("time_interval"));

        return rootView;
    }
}
