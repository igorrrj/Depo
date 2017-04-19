package com.example.igor.depo.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.igor.depo.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Igor on 18.02.2017.
 */

public class TimeAdapter extends BaseAdapter {
    private Context context;
    String[] time;
    int gradient_position;
    public TimeAdapter(Context context, String t) {
        this.context = context;
        this.time = t.split(",");

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm");
// you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));

        String localTime = date.format(currentLocalTime);
        String []split_time,split_devise_time;
        split_devise_time=localTime.split(":");

        for(int i=0;i<time.length;i++)
        {
            String toSplit;
            if(time[i].trim().contains("("))
            {
                toSplit=time[i].trim().substring(0,time[i].length()-time[i].lastIndexOf("(")-1);
            }
            else toSplit=time[i].trim();
            split_time=toSplit.split("\\.");
            Log.e("DEVISE: ",toSplit+"");

            //Log.e("SRING:",time_textView.getText().toString().trim()+"");
            if( Integer.parseInt(split_time[0])== Integer.parseInt(split_devise_time[0]) &&
                Integer.parseInt(split_time[1])>= Integer.parseInt(split_devise_time[1]) )
            {
                gradient_position=i;
                Log.e("AAAA: ", gradient_position +" | "+time[i] );
                break;
            }
            else if( Integer.parseInt(split_time[0])> Integer.parseInt(split_devise_time[0]) )
            {
                gradient_position=i;
                Log.e("AAAA: ", gradient_position +" | "+time[i] );
                break;
            }

        }
    }

    @Override
    public int getCount() {
        return time.length;
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
        View listViewItem = inflater.inflate(R.layout.item_city_bus_time, parent, false);
        TextView time_textView=(TextView)listViewItem.findViewById(R.id.time_textView);
        time_textView.setText(time[position]);

        if(position==gradient_position)
        {
            listViewItem.setBackgroundColor(Color.CYAN);
        }


        return listViewItem;

    }
}
