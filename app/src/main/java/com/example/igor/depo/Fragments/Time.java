package com.example.igor.depo.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.igor.depo.Adapters.TimeAdapter;
import com.example.igor.depo.R;

/**
 * Created by Igor on 17.02.2017.
 */

public class Time extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.city_bus_time,container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        Bundle bundle = this.getArguments();
        String str = "";
        if (bundle != null) {
             str  = bundle.getString("time");
        }
        listView.setAdapter(new TimeAdapter(getActivity(),str));

        return rootView;
    }

}
