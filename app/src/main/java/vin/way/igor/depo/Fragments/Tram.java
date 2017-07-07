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
import vin.way.igor.depo.activities.SelectedTramTabActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Igor on 12.02.2017.
 */

public class Tram extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(vin.way.igor.depo.R.layout.tram, container, false);

        listView = (ListView) rootView.findViewById(vin.way.igor.depo.R.id.listView);

        try {
            showJSON(getArguments().getString("json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView number = ((TextView) view.findViewById(vin.way.igor.depo.R.id.number_name));
                TextView f_name = ((TextView) view.findViewById(vin.way.igor.depo.R.id.f_name));
                TextView l_name = ((TextView) view.findViewById(vin.way.igor.depo.R.id.l_name));
                 /*
                        Bundle bundle=new Bundle();
                        bundle.putString("tram_number",number.getText().toString().trim());
                        bundle.putString("tram_f_name",f_name.getText().toString().trim());
                        bundle.putString("tram_l_name",l_name.getText().toString().trim());

                Fragment fragment = null;
                Class fragmentClass=SelectedTramTabFragment.class;
                try {
                        fragment = (Fragment) fragmentClass.newInstance();
                        fragment.setArguments(bundle);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
                        */
                Intent intent = new Intent(getActivity(), SelectedTramTabActivity.class);
                intent.putExtra("tram_number", number.getText().toString().trim());
                intent.putExtra("tram_f_name", f_name.getText().toString().trim());
                intent.putExtra("tram_l_name", l_name.getText().toString().trim());
                startActivity(intent);

            }
        });
        return rootView;
    }


    private void showJSON(String json) throws JSONException {

        ArrayList<HashMap<String, String>> tram_array = new ArrayList<>();
        JSONArray json_trams = new JSONArray(json);


        for (int j = 0; j < json_trams.getJSONArray(0).length(); ++j) {
            JSONObject dd =  json_trams.getJSONArray(0).getJSONObject(j);

            HashMap<String, String> map = new HashMap<>();
            map.put("id", dd.getString("id"));
            map.put("first_name", dd.getString("first_name"));
            map.put("last_name", dd.getString("last_name"));
            map.put("number_name", dd.getString("number_name"));
            tram_array.add(map);

        }
        Log.e("ROUT:", tram_array + "");

        listView.setAdapter(new TramAdapter(getActivity(), tram_array));

    }

}
