package com.example.igor.depo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Igor on 24.02.2017.
 */

public class TaxiAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> result;
    HashMap<String, String> map = new HashMap<String, String>();
    private Context context;

    public TaxiAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        this.result = arraylist;
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View listViewItem = inflater.inflate(R.layout.item_taxi, parent, false);
        final TextView textViewName, textViewNumber;
        textViewName = (TextView) listViewItem.findViewById(R.id.taxi_name);
        textViewNumber = (TextView) listViewItem.findViewById(R.id.taxi_number);
        final String[]numbs;
        try {

            map = result.get(position);
            textViewName.setText(map.get("name"));
            textViewNumber.setText(map.get("number"));
            numbs=textViewNumber.getText().toString().trim().split(",");
            Log.e("NUMBERS_SPLIT:",numbs.length+" | "+ textViewName.getText().toString().trim()+"");



        } catch (Exception e) {
            Log.e("ErrorLisTaxi:", e.getMessage());
            e.printStackTrace();
        }


        return listViewItem;
    }
}