package com.example.igor.depo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Igor on 22.02.2017.
 */

public class RoutesAdapter extends BaseAdapter {
    String[] route;
    private Context context;

    public RoutesAdapter(Context context,String t) {
        this.context = context;
        this.route = t.split(",");
    }

    @Override
    public int getCount() {
        return route.length;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listViewItem = inflater.inflate(R.layout.item_route, parent, false);
         TextView  textViewName = (TextView) listViewItem.findViewById(R.id.name);

        textViewName.setText(route[position]);


        return listViewItem;
    }
}
