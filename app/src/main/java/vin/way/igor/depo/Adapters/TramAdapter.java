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

public class TramAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> result;
    HashMap<String, String> map = new HashMap<String, String>();
    private Context context;

    public TramAdapter(Context context, ArrayList<HashMap<String, String>> array) {
        this.context = context;
        this.result = array;
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
        View listViewItem = inflater.inflate(vin.way.igor.depo.R.layout.item, parent, false);
        final TextView textViewFirstName, textViewLastName, textViewNumbers, textViewDashe;

        textViewFirstName = (TextView) listViewItem.findViewById(vin.way.igor.depo.R.id.f_name);
        textViewDashe = (TextView) listViewItem.findViewById(vin.way.igor.depo.R.id.dashe);
        textViewLastName = (TextView) listViewItem.findViewById(vin.way.igor.depo.R.id.l_name);
        textViewNumbers = (TextView) listViewItem.findViewById(vin.way.igor.depo.R.id.number_name);
        try {

            map = result.get(position);
            textViewFirstName.setText(map.get("first_name"));
            textViewDashe.setText(" - ");
            textViewLastName.setText(map.get("last_name"));
            textViewNumbers.setText(map.get("number_name"));
            Log.e("ErrorList:", map.get("first_name") + " " + map.get("last_name") + " " + map.get("number_name") + " ");


        } catch (Exception e) {
            Log.e("ErrorList:", e.getMessage());
            e.printStackTrace();
        }


        return listViewItem;
    }
}