package com.example.igor.depo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.igor.depo.R;

/**
 * Created by Igor on 25.02.2017.
 */

public class PopupTaxiAdapter extends BaseAdapter {
    String[]numbers;
    private Context context;

    public PopupTaxiAdapter(Context context, String[]numbs) {
        this.context = context;
        this.numbers = numbs;
    }

    @Override
    public int getCount() {
        return numbers.length;
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
        final View listViewItem = inflater.inflate(R.layout.item_pop_up_taxi_numbers, parent, false);
        final TextView textViewNumber;
        textViewNumber = (TextView) listViewItem.findViewById(R.id.taxi_number);
        textViewNumber.setText(numbers[position]);


        return listViewItem;
    }
}