package com.example.igor.depo.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.igor.depo.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Igor on 28.03.2017.
 */

public class LikedAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> result;
    HashMap<String, String> map = new HashMap<String, String>();
    private Context context;

    public LikedAdapter(Context context,ArrayList<HashMap<String, String>>arraylist) {
        this.context = context;


        this.result=arraylist;
        for(int i=0;i<result.size();i++)
        {
            HashMap<String,String>hm=result.get(i);
            for(int j=i+1;j<result.size();j++)
            {
                String numb=result.get(j).get("number");
                if(hm.get("number").equals(numb))
                {
                    result.remove(i);
                }
            }
        }


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
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listViewItem = inflater.inflate(R.layout.item_liked, parent, false);
        final TextView textViewFirstName,textViewLastName,textViewNumbers,textViewDashe;

        textViewFirstName = (TextView) listViewItem.findViewById(R.id.f_name);
        textViewDashe=(TextView)listViewItem.findViewById(R.id.dashe);
        textViewLastName = (TextView) listViewItem.findViewById(R.id.l_name);
        textViewNumbers = (TextView) listViewItem.findViewById(R.id.number_name);
        try{

            map = result.get(position);
            textViewFirstName.setText(map.get("first_name"));
            textViewDashe.setText(" - ");
            textViewLastName.setText(map.get("last_name"));
            textViewNumbers.setText(map.get("number"));

            Log.e("LikedList:",map.get("first_name") + " " + map.get("last_name")+ " "+map.get("number")+" ");


        }catch (Exception e) {
            Log.e("ErrorList:", e.getMessage());
            e.printStackTrace();
        }


        return listViewItem;
    }
}