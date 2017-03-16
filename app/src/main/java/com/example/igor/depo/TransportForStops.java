package com.example.igor.depo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Igor on 01.03.2017.
 */

public class TransportForStops  extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ListView listView;
    ArrayList<HashMap<String, String>> stops_array;
    String str;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.transport_for_stops,container, false);
        Bundle bundle = this.getArguments();
        if(bundle!=null)
        {
            str= bundle.getString("jslist");
            Log.e("STR:::::::::",str);
        }
        try {

            stops_array=new ArrayList<>();

            JSONArray array=new JSONArray(str);
            for(int j=0;j<array.length();++j)
            {
                JSONObject dd=array.getJSONObject(j);

                    HashMap<String,String>map=new HashMap<>();

                    //map.put("id",dd.getString("id"));
                    map.put("type",dd.getString("type"));
                    map.put("number",dd.getString("number"));
                   // map.put("route",dd.getString("route"));
                    map.put("first_name",dd.getString("first_name"));
                    map.put("last_name",dd.getString("last_name"));
                    stops_array.add(map);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("LLLLL::",stops_array+"");

        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(new TransportForStopsAdapter(getContext(),stops_array));

        return rootView;
    }

}