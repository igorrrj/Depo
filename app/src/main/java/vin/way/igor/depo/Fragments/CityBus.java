package vin.way.igor.depo.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import vin.way.igor.depo.Adapters.TramAdapter;
import vin.way.igor.depo.activities.SelectedBusTabActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Igor on 14.02.2017.
 */

public class CityBus extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ListView listView;
    ArrayList<HashMap<String, String>> bus_array;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(vin.way.igor.depo.R.layout.city_bus,container, false);
        listView = (ListView) rootView.findViewById(vin.way.igor.depo.R.id.listView);

        try {
            showJSON(getArguments().getString("json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView number=((TextView)view.findViewById(vin.way.igor.depo.R.id.number_name));
                TextView f_name=((TextView)view.findViewById(vin.way.igor.depo.R.id.f_name));
                TextView l_name=((TextView)view.findViewById(vin.way.igor.depo.R.id.l_name));

                Intent intent=new Intent(getActivity(), SelectedBusTabActivity.class);
                intent.putExtra("tram_number",number.getText().toString().trim());
                intent.putExtra("tram_f_name",f_name.getText().toString().trim());
                intent.putExtra("tram_l_name",l_name.getText().toString().trim());
                startActivity(intent);

            }
        });


        return rootView;
    }

    private void showJSON(String json) throws JSONException {

        bus_array = new ArrayList<>();
        JSONArray json_array = new JSONArray(json);

        for (int j = 0; j < json_array.getJSONArray(2).length(); ++j) {
            JSONObject dd = json_array.getJSONArray(2).getJSONObject(j);

            HashMap<String, String> map = new HashMap<>();
            map.put("id", dd.getString("id"));
            map.put("first_name", dd.getString("first_name"));
            map.put("last_name", dd.getString("last_name"));
            map.put("number_name", dd.getString("number_name"));
            bus_array.add(map);
            Log.e("BusId",dd.getString("id")+" |Number"+ dd.getString("number_name"));

        }
        Log.e("ROUT:", bus_array + "");

        for (int i = 1; i < bus_array.size(); i++) {
            for (int j = i; j > 0 && Integer.parseInt(bus_array.get(j - 1).get("number_name")) > Integer.parseInt(bus_array.get(j).get("number_name")); j--) {
                    Collections.swap(bus_array, j - 1, j);
            }
        }
        listView.setAdapter(new TramAdapter(getActivity(), bus_array));

    }
}

