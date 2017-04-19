package com.example.igor.depo.Widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.example.igor.depo.R;

/**
 * Created by Igor on 11.04.2017.
 */

public class ConfigActivity extends Activity {
    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // и проверяем его корректность
        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        // формируем intent ответа
        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        // отрицательный ответ
        setResult(RESULT_CANCELED, resultValue);

        setContentView(R.layout.config);
    }

    public void onClick(View view) {
        int type = ((RadioGroup) findViewById(R.id.typeGroup))
                .getCheckedRadioButtonId();
        String transport_type="Tram";
        switch (type)
        {
            case R.id.radio_tram:
                transport_type="Tram";
                break;
            case R.id.radio_bus:
                transport_type="CityBus";
                break;
        }
        SharedPreferences sp = getSharedPreferences("widgetTypeSharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Log.e("TypeConf",transport_type);
        editor.putString("typeOftransport", transport_type);
        editor.commit();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        MyProvider.updateWidget(this, appWidgetManager, sp, widgetID);
        // положительный ответ
        setResult(RESULT_OK, resultValue);

        finish();
    }
}
