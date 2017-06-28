package com.example.igor.depo.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.igor.depo.Adapters.LikedAdapter;
import com.example.igor.depo.Adapters.StopsAdapter;
import com.example.igor.depo.activities.SelectedLikedTabActivity;
import com.example.igor.depo.data_base.LikedSQLite;
import com.example.igor.depo.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Igor on 27.03.2017.
 */

public class Liked extends Fragment {

    private ListView listView;
    LikedSQLite likedSQLite;
    TextView error;
    ArrayList<HashMap<String, String>> liked_array;
    LikedAdapter likedAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.liked, container, false);
        Spinner spinner = new Spinner(((AppCompatActivity) getActivity()).getSupportActionBar().getThemedContext());

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_spiner, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_list_item_array, R.layout.item_spiner_text);
        adapter.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0)
                {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.spiner_frame, new Liked_Transport_Fragment()).commit();
                }
                else if(i==1)
                {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.spiner_frame, new Liked_Stops_Fragment()).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
