package com.example.igor.depo.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.igor.depo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Igor on 26.02.2017.
 */

public class StopsAdapter extends BaseAdapter implements SectionIndexer{

    String[] result_string;
    String[] sections;
    HashMap<String, Integer> mapIndex;
    ///////////
    private Context context;

    public StopsAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {

        this.context = context;

        Set<String> stop_epta=new LinkedHashSet<>();



        for(int i=0;i<arraylist.size();i++)
        {
            String []stops;
            stops=arraylist.get(i).get("route").trim().split(",");

            for(String s:stops)
                stop_epta.add(s);
        }


       ArrayList<String> arrayList=new ArrayList<>(stop_epta);

        Collections.sort(arrayList);

        ////////////////*
        /*sections = new String[arraylist.size()];
        arraylist.toArray(sections);*/


        /////////////////

        stop_epta=new LinkedHashSet<>(arrayList);
        Log.e("SORTED:",arraylist+"\n");
        int x=0;
        result_string = new String[stop_epta.size()];

        for(String s:stop_epta)
        {
            result_string[x]=s;
            x++;
        }

        ////////////////////////////
        mapIndex = new HashMap<String, Integer>();

        for(int i=0;i<result_string.length;i++)
        {
            String ch = result_string[i].substring(0, 1);
            ch = ch.toUpperCase();

            if(isInteger(ch))
                mapIndex.put("#",i);

           else if (!mapIndex.containsKey(ch))
            {
                mapIndex.put(ch,i);
            }

        }

        Set<String> sectionLetters = mapIndex.keySet();

        // create a list from the set to sort
        ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);

        Collections.sort(sectionList);
        Log.e("sectionList", sectionList.toString());

        sections = new String[sectionList.size()];

        sectionList.toArray(sections);
        //////////////////////////////

    }
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getCount() {
        return result_string.length;
    }

    @Override
    public Object getItem(int position) {
        return result_string[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View listViewItem = inflater.inflate(R.layout.item_stops, parent, false);
        final TextView textViewName;
        textViewName = (TextView) listViewItem.findViewById(R.id.stop_name);

        try {
            textViewName.setText(result_string[position]);
            Log.e("STOPS:",textViewName.getText().toString().trim()+"");



        } catch (Exception e) {
            Log.e("ErrorLisTaxi:", e.getMessage());
            e.printStackTrace();
        }


        return listViewItem;
    }

    @Override
    public Object[] getSections() {
        return sections;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mapIndex.get(sections[sectionIndex]);
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }
}