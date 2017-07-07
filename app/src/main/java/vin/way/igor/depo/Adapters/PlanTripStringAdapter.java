package vin.way.igor.depo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import vin.way.igor.depo.R;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Igor on 14.04.2017.
 */

public class PlanTripStringAdapter  extends BaseAdapter {
    ArrayList<String> arrayList;
    private Context context;

    public PlanTripStringAdapter(Context context, Set<String> tset) {

        this.context = context;
        this.arrayList=new ArrayList<>(tset);
    }
    @Override
    public int getCount() {
        return arrayList.size();
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View listViewItem = inflater.inflate(R.layout.item_plan_trip_string, parent, false);
        TextView str_name=(TextView)listViewItem.findViewById(R.id.str_name);
        str_name.setText(arrayList.get(position));

        return listViewItem;
    }
}
