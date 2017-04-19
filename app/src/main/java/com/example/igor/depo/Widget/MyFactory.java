package com.example.igor.depo.Widget;

/**
 * Created by Igor on 11.04.2017.
 */

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.example.igor.depo.ParseJSON;
import com.example.igor.depo.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.igor.depo.ParseJSON.result;

public class MyFactory implements RemoteViewsFactory {

    ArrayList<HashMap<String, String>> data;
    Context context;
    String str,type="";
    int widgetID;

    MyFactory(Context ctx, Intent intent) {
        this.type=intent.getStringExtra("type");
        Log.e("TypeFact",type);
        context = ctx;
        widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        this.str=intent.getStringExtra("data_for_factory");
    }

    @Override
    public void onCreate() {
        data = new ArrayList<HashMap<String, String>>();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rView = new RemoteViews(context.getPackageName(),
                R.layout.item_widget);
        rView.setTextViewText(R.id.f_name, data.get(position).get("first_name"));
        rView.setTextViewText(R.id.dashe, " - ");
        rView.setTextViewText(R.id.l_name,data.get(position).get("last_name"));
        rView.setTextViewText(R.id.number_name,data.get(position).get("number_name"));

        Log.e("REMOTEVIEWS",rView.toString());
        return rView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        data.clear();
        ParseJSON pj = new ParseJSON(str);
        pj.parseJSON();

        if(type.equals("Tram"))
        data= result.get(0);
        else
            data= result.get(1);

        Log.e("DAtaSetChangedFactory:",data.toString());
    }

    @Override
    public void onDestroy() {

    }

}