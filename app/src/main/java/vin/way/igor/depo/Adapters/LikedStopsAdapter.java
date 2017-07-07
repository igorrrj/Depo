package vin.way.igor.depo.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import vin.way.igor.depo.R;

import java.util.ArrayList;
import java.util.HashMap;

public class LikedStopsAdapter extends BaseAdapter{
    ArrayList<HashMap<String, String>> result;
    HashMap<String, String> map = new HashMap<String, String>();
    private Context context;

    public LikedStopsAdapter(Context context, ArrayList< ArrayList<HashMap<String, String>>> arraylist, int i) {
        this.context = context;
       this.result=arraylist.get(i);
    }
    public LikedStopsAdapter(Context context, ArrayList<HashMap<String, String>>array) {
        this.context = context;
        this.result=array;
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
        View listViewItem = inflater.inflate(R.layout.item_stops, parent, false);
        final TextView textViewName;

        textViewName = (TextView) listViewItem.findViewById(R.id.stop_name);

        try{

          map = result.get(position);
          textViewName.setText(map.get("name"));

            Log.e("ErrorList:",map.get("name") + " ");


      }catch (Exception e) {
          Log.e("ErrorLikedStopListAdapter:", e.getMessage());
          e.printStackTrace();
      }


        return listViewItem;
    }
}