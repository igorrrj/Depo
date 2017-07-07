package vin.way.igor.depo.Adapters;

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
 * Created by Igor on 28.03.2017.
 */

public class LikedAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> result=new ArrayList<>();
    ArrayList<HashMap<String, Object>> sections_list=new ArrayList<>();
    HashMap<String, String> map = new HashMap<String, String>();
    private Context context;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    public LikedAdapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
        this.context = context;
        this.result=arrayList;
        sections_list=new ArrayList<>();
    }

    public void addSectionHeader(String sectionName, int position) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name",sectionName);
        map.put("position",position);
        sections_list.add(map);
        notifyDataSetChanged();
        Log.e("AddedSection",sectionName);
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return result.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(vin.way.igor.depo.R.layout.item_liked, parent, false);
        TextView textViewFirstName,textViewLastName,textViewNumbers,textViewDashe,textViewHeader;

        textViewFirstName = (TextView) convertView.findViewById(vin.way.igor.depo.R.id.f_name);
        textViewDashe=(TextView)convertView.findViewById(vin.way.igor.depo.R.id.dashe);
        textViewLastName = (TextView) convertView.findViewById(vin.way.igor.depo.R.id.l_name);
        textViewNumbers = (TextView) convertView.findViewById(vin.way.igor.depo.R.id.number_name);
        textViewHeader = (TextView) convertView.findViewById(vin.way.igor.depo.R.id.section_header_text);
        try{
            map = result.get(position);
            textViewFirstName.setText(map.get("first_name"));
            textViewDashe.setText(" - ");
            textViewLastName.setText(map.get("last_name"));
            textViewNumbers.setText(map.get("number"));
            Log.e("LikedList:",map.get("first_name") + " " + map.get("last_name")+ " "+map.get("number")+" "+map.get("type"));

            for(int i=0;i<sections_list.size();i++)
            {
                if(sections_list.get(i).get("position").equals(position))
                {
                    textViewHeader.setVisibility(View.VISIBLE);
                    textViewHeader.setText(sections_list.get(i).get("name")+"");
                    Log.e("InSection",sections_list.get(i).get("name")+"");
                    break;
                }
            }
            Log.e("Section",sections_list+"");
        }catch (Exception e) {
            Log.e("ErrorList:", e.getMessage());
            e.printStackTrace();
        }

        return convertView;
    }
}