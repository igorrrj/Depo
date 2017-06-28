package com.example.igor.depo.Fragments;

import android.app.Activity;
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

import com.example.igor.depo.Adapters.LikedAdapter;
import com.example.igor.depo.R;
import com.example.igor.depo.activities.SelectedLikedTabActivity;
import com.example.igor.depo.data_base.LikedSQLite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Igor on 27.06.2017.
 */

public class Liked_Transport_Fragment extends Fragment {
    private ListView listView;
    LikedSQLite likedSQLite;
    TextView error;
    ArrayList<HashMap<String, String>> liked_array;
    LikedAdapter likedAdapter;
    public static int DELETE=1;
    public static int NOT_DELETE=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_liked_transport, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        error = (TextView) rootView.findViewById(R.id.error_text);

        likedSQLite = new LikedSQLite(getActivity());

        if (likedSQLite.Get_Liked_Array().isEmpty()) {
            error.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            error.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            liked_array = likedSQLite.Get_Liked_Array();
            liked_array = sortLikedArrayList(liked_array);
            likedAdapter = new LikedAdapter(getActivity(), liked_array);

            Log.e("LikedArraySorted", liked_array.toString());
            Log.e("LikedArraySize", liked_array.size() + "");
            if (liked_array.get(0).get("type").equals("tram")) {
                likedAdapter.addSectionHeader("Трамвай", 0);
            }
            if (liked_array.get(0).get("type").equals("troley")) {
                likedAdapter.addSectionHeader("Тролейбус", 0);
            }
            if (liked_array.get(0).get("type").equals("bus")) {
                likedAdapter.addSectionHeader("Автобус", 0);
            }

            for (int i = 0; i < liked_array.size(); i++) {
                if ((i - 1 >= 0 && liked_array.get(i - 1).get("type").equals("troley") && liked_array.get(i).get("type").equals("bus"))
                        || ((i-1>=0 && liked_array.get(i-1).get("type").equals("tram") && liked_array.get(i).get("type").equals("bus"))
                        || (liked_array.get(i).get("type").equals("bus") && liked_array.size() == 1)))
                {
                    likedAdapter.addSectionHeader("Автобус", i);
                    Log.e("LikedArraySectionBus", i + 1 + "");
                }
                if ((i - 1 >= 0 && liked_array.get(i - 1).get("type").equals("tram") && liked_array.get(i).get("type").equals("troley"))
                        || (liked_array.get(i).get("type").equals("troley") && liked_array.size() == 1)) {
                    likedAdapter.addSectionHeader("Тролейбус", i);
                    Log.e("LikedArraySectionBus", i + 1 + "");
                }
            }
            listView.setAdapter(likedAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView number = ((TextView) view.findViewById(R.id.number_name));
                    TextView f_name = ((TextView) view.findViewById(R.id.f_name));
                    TextView l_name = ((TextView) view.findViewById(R.id.l_name));
                    String number_name = "", first_name = "", last_name = "", rout = "", begin_time = "", end_time = "", from_depo = "", to_depo = "", time_interval = "", type = "";
                    number_name = number.getText().toString().trim();
                    first_name = f_name.getText().toString().trim();
                    last_name = l_name.getText().toString().trim();
                    for (int j = 0; j < liked_array.size(); j++) {
                        if (liked_array.get(j).get("number").equals(number_name) && liked_array.get(j).get("first_name").equals(first_name)
                                && liked_array.get(j).get("last_name").equals(last_name)) {
                            rout = liked_array.get(j).get("route");
                            type = liked_array.get(j).get("type");
                            begin_time = liked_array.get(j).get("begin_time");
                            end_time = liked_array.get(j).get("end_time");
                            from_depo = liked_array.get(j).get("from_depo");
                            to_depo = liked_array.get(j).get("to_depo");
                            time_interval = liked_array.get(j).get("time_interval");

                        }
                    }

                    Intent intent = new Intent(getActivity(), SelectedLikedTabActivity.class);
                    intent.putExtra("number", number_name);
                    intent.putExtra("f_name", first_name);
                    intent.putExtra("l_name", last_name);
                    intent.putExtra("type", type);
                    intent.putExtra("rout", rout);
                    intent.putExtra("begin_time", begin_time);
                    intent.putExtra("end_time", end_time);
                    intent.putExtra("from_depo", from_depo);
                    intent.putExtra("to_depo", to_depo);
                    intent.putExtra("time_interval", time_interval);
                    Log.e("LikedTransfere route= ", rout);
                    startActivityForResult(intent, 1);
                }
            });
        }

        return rootView;
    }

    ArrayList<HashMap<String, String>> sortLikedArrayList(ArrayList<HashMap<String, String>> liked_array) {
        for (int i = 1; i < liked_array.size(); i++) {
            for (int j = i; j > 0; j--) {
                if ((liked_array.get(j - 1).get("type").equals("troley") && liked_array.get(j).get("type").equals("tram")) ||
                        (liked_array.get(j - 1).get("type").equals("bus") && liked_array.get(j).get("type").equals("tram")) ||
                        (liked_array.get(j - 1).get("type").equals("bus") && liked_array.get(j).get("type").equals("troley"))) {
                    Collections.swap(liked_array, j - 1, j);
                } else if (liked_array.get(j - 1).get("type").equals(liked_array.get(j).get("type"))
                        && StringToInt(liked_array.get(j - 1).get("number")) > StringToInt(liked_array.get(j).get("number"))) {
                    Collections.swap(liked_array, j - 1, j);
                }
            }
        }
        return liked_array;
    }

    Integer StringToInt(String str) {
        int index = str.length();
        for (int i = str.length() - 1; i >= 0; --i) {
            if (str.charAt(i) >= '0' && str.charAt(i) <= '9') break;
            index--;
        }
        Log.e("PArsed int", str.substring(0, index));
        return Integer.parseInt(str.substring(0, index));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        {
            if(data!=null && data.getIntExtra("result",0)==DELETE) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.spiner_frame, new Liked_Transport_Fragment()).commit();
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

}
