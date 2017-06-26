package com.example.igor.depo.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.igor.depo.Adapters.TramAdapter;
import com.example.igor.depo.R;
import com.example.igor.depo.activities.SelectedBusTabActivity;
import com.example.igor.depo.activities.SelectedTroleyBusTabActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Igor on 14.02.2017.
 */

public class TroleyBus extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ListView listView;
    ArrayList<HashMap<String, String>> troley_bus_array;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.transport,container, false);

        listView = (ListView) rootView.findViewById(R.id.listView);

        try {
            showJSON(getArguments().getString("json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView number=((TextView)view.findViewById(R.id.number_name));
                TextView f_name=((TextView)view.findViewById(R.id.f_name));
                TextView l_name=((TextView)view.findViewById(R.id.l_name));

                Intent intent=new Intent(getActivity(), SelectedTroleyBusTabActivity.class);
                intent.putExtra("troley_number",number.getText().toString().trim());
                intent.putExtra("troley_f_name",f_name.getText().toString().trim());
                intent.putExtra("troley_l_name",l_name.getText().toString().trim());
                startActivity(intent);

            }
        });


        return rootView;
    }


    private void showJSON(String json) throws JSONException {

        troley_bus_array = new ArrayList<>();
        JSONArray json_array = new JSONArray(json);

        for (int j = 0; j < json_array.getJSONArray(1).length(); ++j) {
            JSONObject dd = json_array.getJSONArray(1).getJSONObject(j);

            HashMap<String, String> map = new HashMap<>();
            map.put("id", dd.getString("id"));
            map.put("first_name", dd.getString("first_name"));
            map.put("last_name", dd.getString("last_name"));
            map.put("number_name", dd.getString("number_name"));
            troley_bus_array.add(map);
            Log.e("TroleyBusId",dd.getString("id")+" |Number"+ dd.getString("number_name"));

        }
        Log.e("TroleyROUT:", troley_bus_array + "");

        listView.setAdapter(new TramAdapter(getActivity(), troley_bus_array));

    }
}

