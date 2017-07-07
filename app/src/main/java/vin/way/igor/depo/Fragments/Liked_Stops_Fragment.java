package vin.way.igor.depo.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import vin.way.igor.depo.Adapters.LikedStopsAdapter;
import vin.way.igor.depo.activities.SelectedLikedStopActivity;
import vin.way.igor.depo.data_base.StopsLikedSQLite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Igor on 27.06.2017.
 */

public class Liked_Stops_Fragment extends Fragment {
    private ListView listView;
    StopsLikedSQLite likedSQLite;
    TextView error;
    ArrayList<HashMap<String, String>> liked_array;
    public static int DELETE=1;
    public static int NOT_DELETE=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(vin.way.igor.depo.R.layout.fragment_liked_stops, container, false);
        listView = (ListView) rootView.findViewById(vin.way.igor.depo.R.id.listView);
        error = (TextView) rootView.findViewById(vin.way.igor.depo.R.id.error_text);

        likedSQLite = new StopsLikedSQLite(getActivity());

        if (likedSQLite.Get_Liked_Stops_Array().isEmpty()) {
            error.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            error.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            liked_array = likedSQLite.Get_Liked_Stops_Array();
            liked_array = sortLikedArrayList(liked_array);

            Log.e("LikedArraySorted", liked_array.toString());
            Log.e("LikedArraySize", liked_array.size() + "");

            listView.setAdapter(new LikedStopsAdapter(getActivity(),liked_array));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView nameText = ((TextView) view.findViewById(vin.way.igor.depo.R.id.stop_name));
                    String name = "";

                    name = nameText.getText().toString().trim();

                    Intent intent = new Intent(getActivity(), SelectedLikedStopActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("list", liked_array.get(i).get("stops_list"));
                    Log.e("LikedStopsList**",liked_array.get(i).get("stops_list"));
                    startActivityForResult(intent, 1);
                }
            });

        }
        return rootView;
    }

    ArrayList<HashMap<String, String>> sortLikedArrayList(ArrayList<HashMap<String, String>> liked_array) {
        for (int i = 1; i < liked_array.size(); i++) {
            for (int j = i; j > 0 && (liked_array.get(j - 1).get("name").compareTo(liked_array.get(j).get("name"))>0); j--) {

                    Collections.swap(liked_array, j - 1, j);
            }
        }
        return liked_array;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data!=null && data.getIntExtra("result",0)==DELETE) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(vin.way.igor.depo.R.id.spiner_frame, new Liked_Stops_Fragment()).commit();
            Log.e("Deleted ", "OK");
            super.onActivityResult(requestCode, resultCode, data);
        }
        if(data!=null && data.getExtras().getInt("result")==NOT_DELETE)
        {
            Log.e("Not deleted","OK");
        }
        Log.e("Result ", data + "");
        super.onActivityResult(requestCode, resultCode, data);
    }
}
