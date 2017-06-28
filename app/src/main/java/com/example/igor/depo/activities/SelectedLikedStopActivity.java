package com.example.igor.depo.activities;

/**
 * Created by Igor on 19.10.2016.
 */


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.igor.depo.Adapters.LikedAdapter;
import com.example.igor.depo.Fragments.RouteFragment;
import com.example.igor.depo.Fragments.TimeFragment;
import com.example.igor.depo.R;
import com.example.igor.depo.data_base.LikedSQLite;
import com.example.igor.depo.data_base.StopsLikedSQLite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static com.example.igor.depo.Fragments.Liked_Stops_Fragment.DELETE;
import static com.example.igor.depo.Fragments.Liked_Transport_Fragment.NOT_DELETE;

public class SelectedLikedStopActivity extends AppCompatActivity {


    public static String json = "", name = "";
    private ListView listView;
    ArrayList<HashMap<String, String>> stops_array;
    LikedSQLite likedSQLite;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport_for_stops);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Зупинка");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            json = bundle.getString("list");
            name = bundle.getString("name");
        }
        ((TextView) findViewById(R.id.stop_name)).setText(name);
        Log.e("Selected", json);


        try {

            stops_array = new ArrayList<>();

            JSONArray array = new JSONArray(json);
            for (int j = 0; j < array.length(); ++j) {
                JSONObject dd = array.getJSONObject(j);

                HashMap<String, String> map = new HashMap<>();

                //map.put("id",dd.getString("id"));
                map.put("type", dd.getString("type"));
                map.put("number", dd.getString("number"));
                map.put("route", dd.getString("route"));
                map.put("first_name", dd.getString("first_name"));
                map.put("last_name", dd.getString("last_name"));
                map.put("begin_time", dd.getString("begin_time"));
                map.put("end_time", dd.getString("last_name"));
                map.put("from_depo", dd.getString("from_depo"));
                map.put("to_depo", dd.getString("to_depo"));
                map.put("time_interval", dd.getString("time_interval"));

                stops_array.add(map);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        stops_array = sortLikedArrayList(stops_array);
        LikedAdapter likedAdapter = new LikedAdapter(this, stops_array);

        Log.e("LikedArraySorted", stops_array.toString());
        Log.e("LikedArraySize", stops_array.size() + "");
        if (stops_array.get(0).get("type").equals("tram")) {
            likedAdapter.addSectionHeader("Трамвай", 0);
        }
        if (stops_array.get(0).get("type").equals("troley")) {
            likedAdapter.addSectionHeader("Тролейбус", 0);
        }
        if (stops_array.get(0).get("type").equals("bus")) {
            likedAdapter.addSectionHeader("Автобус", 0);
        }
        for (int i = 0; i < stops_array.size(); i++) {
            if ((i - 1 >= 0 && stops_array.get(i - 1).get("type").equals("troley") && stops_array.get(i).get("type").equals("bus"))
                    || ((i - 1 >= 0 && stops_array.get(i - 1).get("type").equals("tram") && stops_array.get(i).get("type").equals("bus"))
                    || (stops_array.get(i).get("type").equals("bus") && stops_array.size() == 1))) {
                likedAdapter.addSectionHeader("Автобус", i);
                Log.e("LikedArraySectionBus", i + 1 + "");
            }
            if ((i - 1 >= 0 && stops_array.get(i - 1).get("type").equals("tram") && stops_array.get(i).get("type").equals("troley"))
                    || (stops_array.get(i).get("type").equals("troley") && stops_array.size() == 1)) {
                likedAdapter.addSectionHeader("Тролейбус", i);
                Log.e("LikedArraySectionBus", i + 1 + "");
            }
        }

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(likedAdapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Log.e("ANS", ans + "");
        StopsLikedSQLite likedSQLite = new StopsLikedSQLite(this);
        if (!ans) {
            likedSQLite.Delete_Item(name);
            getIntent().putExtra("result",DELETE);
        }
        else
        {
            getIntent().putExtra("result",NOT_DELETE);
        }
       // Log.e("RESULT_OK",getIntent().getExtras().getInt("result")+"");
        setResult(RESULT_OK, getIntent());
        finish();

    }

    boolean ans = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.favorites_menu, menu);
        final MenuItem mFav = menu.findItem(R.id.action_favorites);

        StopsLikedSQLite likedSQLite = new StopsLikedSQLite(this);
        if (likedSQLite.isLiked(name)) ans = true;
        if (ans) mFav.setIcon(R.drawable.ic_favorite_black_24dp);
        Log.e("SElectedLikeTram", likedSQLite.isLiked(name) + "");
        mFav.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ans = !ans;
                if (ans) {
                    mFav.setIcon(R.drawable.ic_favorite_black_24dp);
                } else {
                    mFav.setIcon(R.drawable.ic_favorite_border_black_24dp);
                }
                return true;
            }
        });
        return true;
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
}


