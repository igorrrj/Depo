package com.example.igor.depo.Widget;

/**
 * Created by Igor on 11.04.2017.
 */

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.igor.depo.R;

public class MyProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        SharedPreferences sp = context.getSharedPreferences(
                "widgetTypeSharedPref", Context.MODE_PRIVATE);
        for (int i : appWidgetIds) {
            updateWidget(context, appWidgetManager,sp, i);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        // Удаляем Preferences
        SharedPreferences.Editor editor = context.getSharedPreferences(
                "widgetTypeSharedPref", Context.MODE_PRIVATE).edit();
        for (int widgetID : appWidgetIds) {
            editor.remove("typeOftransport");
        }
        editor.commit();
    }

   static void updateWidget(Context context, AppWidgetManager appWidgetManager, SharedPreferences sp,
                      int appWidgetId) {
        RemoteViews rv = new RemoteViews(context.getPackageName(),
                R.layout.widget);

        setList(rv, context,sp, appWidgetId);

        setListClick(rv, context, appWidgetId);

        appWidgetManager.updateAppWidget(appWidgetId, rv);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.listView);

    }


    static void setList(RemoteViews rv, Context context,  SharedPreferences sp,int appWidgetId) {

        Intent adapter = new Intent(context, MyService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        adapter.putExtra("type",sp.getString("typeOftransport",null));
        Log.e("TypeProv",sp.getString("typeOftransport",null));
        SharedPreferences sharedPreferences=context.getSharedPreferences("save_list_city_bus", Context.MODE_PRIVATE);
        String str=sharedPreferences.getString("city_bus",null);
        Log.e("ProviderShred",str);
        adapter.putExtra("data_for_factory",sharedPreferences.getString("city_bus",null));



        rv.setRemoteAdapter(R.id.listView, adapter);
    }

    static void setListClick(RemoteViews rv, Context context, int appWidgetId) {

    }

}