package com.example.igor.depo.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.igor.depo.Adapters.LikedAdapter;
import com.example.igor.depo.Adapters.TransportForStopsAdapter;
import com.example.igor.depo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Igor on 01.03.2017.
 */

public class TransportForStops  extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ListView listView;
    ArrayList<HashMap<String, String>> stops_array;
    String str,stop_name;
    TextView stop_nameTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.transport_for_stops,container, false);
        Bundle bundle = this.getArguments();
        if(bundle!=null)
        {
            str= bundle.getString("jslist");
            Log.e("STR:::::::::",str);
            stop_name= bundle.getString("stop_name");

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
                    map.put("route",dd.getString("route"));
                    map.put("first_name",dd.getString("first_name"));
                    map.put("last_name",dd.getString("last_name"));
                    map.put("begin_time",dd.getString("begin_time"));
                    map.put("end_time",dd.getString("last_name"));
                    map.put("from_depo",dd.getString("from_depo"));
                    map.put("to_depo",dd.getString("to_depo"));
                    map.put("time_interval",dd.getString("time_interval"));

                stops_array.add(map);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("LLLLL::",stops_array+"");
        stops_array = sortLikedArrayList(stops_array);
        LikedAdapter likedAdapter = new LikedAdapter(getActivity(), stops_array);

        Log.e("LikedArraySorted", stops_array.toString());
        Log.e("LikedArraySize", stops_array.size()+"");
        if(stops_array.get(0).get("type").equals("tram"))
        {
            likedAdapter.addSectionHeader("Трамвай",0);
        }
        if(stops_array.get(0).get("type").equals("troley"))
        {
            likedAdapter.addSectionHeader("Тролейбус",0);
        }
        if(stops_array.get(0).get("type").equals("bus"))
        {
            likedAdapter.addSectionHeader("Автобус",0);
        }
        for (int i = 0; i < stops_array.size(); i++) {
            if((i-1>=0 && stops_array.get(i-1).get("type").equals("troley") && stops_array.get(i).get("type").equals("bus"))
                    || (stops_array.get(i).get("type").equals("bus")  && stops_array.size()==1 ))
            {
                likedAdapter.addSectionHeader("Автобус",i);
                Log.e("LikedArraySectionBus",i+1+"");
            }
            if((i-1>=0 && stops_array.get(i-1).get("type").equals("tram") && stops_array.get(i).get("type").equals("troley"))
                    || (stops_array.get(i).get("type").equals("troley")  && stops_array.size()==1 ))
            {
                likedAdapter.addSectionHeader("Тролейбус",i);
                Log.e("LikedArraySectionBus",i+1+"");
            }
        }
        listView = (ListView) rootView.findViewById(R.id.listView);

        listView.setAdapter(likedAdapter);

        stop_nameTextView=(TextView)rootView.findViewById(R.id.stop_name);
        stop_nameTextView.setText(stop_name);

        return rootView;
    }

    ArrayList<HashMap<String, String>> sortLikedArrayList(ArrayList<HashMap<String, String>> liked_array) {
        for (int i = 1; i < liked_array.size(); i++) {
            for (int j = i; j > 0; j--) {
                if ((liked_array.get(j - 1).get("type").equals("troley") && liked_array.get(j).get("type").equals("tram"))||
                        (liked_array.get(j - 1).get("type").equals("bus") && liked_array.get(j).get("type").equals("tram"))||
                        (liked_array.get(j - 1).get("type").equals("bus") && liked_array.get(j).get("type").equals("troley")))
                {
                    Collections.swap(liked_array, j - 1, j);
                } else if (liked_array.get(j - 1).get("type").equals(liked_array.get(j).get("type"))
                        && StringToInt(liked_array.get(j - 1).get("number")) > StringToInt(liked_array.get(j).get("number"))) {
                    Collections.swap(liked_array, j - 1, j);
                }
            }
        }
        return liked_array;
    }
    Integer StringToInt(String str)
    {
        int index=str.length();
        for(int i=str.length()-1;i>=0;--i)
        {
            if(str.charAt(i)>='0' && str.charAt(i)<='9')break;
            index--;
        }
        Log.e("PArsed int",str.substring(0,index));
        return Integer.parseInt(str.substring(0,index));
    }

}