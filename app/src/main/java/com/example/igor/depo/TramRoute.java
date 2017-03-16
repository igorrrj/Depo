package com.example.igor.depo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Igor on 22.02.2017.
 */

public class TramRoute extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.routes_layout,container, false);
        listView = (ListView) rootView.findViewById(R.id.ListView);
        Bundle bundle = this.getArguments();
        String str = "";
        if (bundle != null) {
            str  = bundle.getString("tram_route");
        }
        Log.e("Route Tram:",str);
        listView.setAdapter(new RoutesAdapter(getActivity(),str));

        return rootView;
    }

}