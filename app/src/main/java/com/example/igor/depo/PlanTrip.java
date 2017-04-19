package com.example.igor.depo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.igor.depo.Adapters.CustomList;
import com.example.igor.depo.Adapters.PlanTripStringAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.igor.depo.R.style.Date_Picker;


/**
 * Created by Igor on 12.04.2017.
 */

public class PlanTrip extends Fragment {
   TextView curDateTime;
    ImageView inverseImageView;
    EditText first_stopEditText,last_stopEditText;
    ArrayList<HashMap<String, String>> stops_array;
    Set<String> SettArray;
    Button planButton;
    Calendar calendar= Calendar.getInstance();
    ListView firstList,lastList,planList;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.plan_trip,container, false);
        curDateTime=(TextView)rootView.findViewById(R.id.textDateTime);
        setInitialDateTime();
        Button dateButton=(Button)rootView.findViewById(R.id.date);
        Button timeButton=(Button)rootView.findViewById(R.id.time);
        first_stopEditText=(EditText)rootView.findViewById(R.id.first_stop);
        last_stopEditText=(EditText)rootView.findViewById(R.id.last_stop);
        planList=(ListView)rootView.findViewById(R.id.listViewPlan);
        planButton=(Button)rootView.findViewById(R.id.planButton);

        planButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String firstStr=first_stopEditText.getText().toString().trim();
                String lastStr=last_stopEditText.getText().toString().trim();

                ArrayList<HashMap<String,String>>planArray=new ArrayList<HashMap<String, String>>();
                for(int i=0;i<stops_array.size();i++)
                {
                    boolean firstBool=false,lastBool=false;
                    String []stops;
                    stops=stops_array.get(i).get("route").trim().split(",");
                    for(String str:stops)
                    {
                        if(str.toLowerCase().contains(firstStr.toLowerCase()))
                        {
                            firstBool=true;
                        }
                        if(str.toLowerCase().contains(lastStr.toLowerCase()))
                        {
                            lastBool=true;
                        }
                    }

                    if(firstBool && lastBool)
                    {
                        HashMap<String,String>hm=new HashMap<String, String>();
                        hm.put("first_name",stops_array.get(i).get("first_name"));
                        hm.put("last_name",stops_array.get(i).get("last_name"));
                        hm.put("number_name",stops_array.get(i).get("number"));
                        planArray.add(hm);
                    }
                }
                planList.setAdapter(new CustomList(getActivity(),planArray));

                AlarmManager alarmManager=(AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);

                Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
                notificationIntent.addCategory("android.intent.category.DEFAULT");
                PendingIntent broadcast = PendingIntent.getBroadcast(getActivity(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, 5);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
            }
        });

        inverseImageView=(ImageView)rootView.findViewById(R.id.inverseImage);
        inverseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String swp;
                swp=first_stopEditText.getText().toString().trim();
                first_stopEditText.setText(last_stopEditText.getText().toString().trim());
                last_stopEditText.setText(swp);
            }
        });
        firstList=(ListView)rootView.findViewById(R.id.listViewFirst);

        first_stopEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String firstStr=first_stopEditText.getText().toString().trim();
                SettArray=new LinkedHashSet<String>();
                for(int i=0;i<stops_array.size();i++)
                {
                    String []stops;
                    stops=stops_array.get(i).get("route").trim().split(",");
                    for(String str:stops)
                    {
                        if(str.toLowerCase().contains(firstStr.toLowerCase()))
                        {
                            SettArray.add(str);
                        }
                    }

                }
                if(firstStr.isEmpty())firstList.setVisibility(View.GONE);
                else{
                    firstList.setVisibility(View.VISIBLE);
                }
                firstList.setAdapter(new PlanTripStringAdapter(getActivity(),SettArray));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        firstList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView t=(TextView)view.findViewById(R.id.str_name);
                first_stopEditText.setText(t.getText().toString());

                firstList.setVisibility(View.GONE);
            }
        });

        lastList=(ListView)rootView.findViewById(R.id.listViewLast);

        last_stopEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String lastStr=last_stopEditText.getText().toString().trim();
                SettArray=new LinkedHashSet<String>();
                for(int i=0;i<stops_array.size();i++)
                {
                    String []stops;
                    stops=stops_array.get(i).get("route").trim().split(",");
                    for(String str:stops)
                    {
                        if(str.toLowerCase().contains(lastStr.toLowerCase()))
                        {
                            SettArray.add(str);
                        }
                    }

                }
                if(lastStr.isEmpty())lastList.setVisibility(View.GONE);
                else{
                    lastList.setVisibility(View.VISIBLE);
                }
                lastList.setAdapter(new PlanTripStringAdapter(getActivity(),SettArray));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        lastList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView t=(TextView)view.findViewById(R.id.str_name);
                last_stopEditText.setText(t.getText().toString());

                lastList.setVisibility(View.GONE);
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new DatePickerDialog(getActivity(), Date_Picker,dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_YEAR)).show();
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(),Date_Picker,timePickerDialog,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),true).show();

            }
        });
        sendRequest();

        return rootView;
    }

    private void sendRequest(){
        StringRequest stringRequest=new StringRequest("https://depocom.000webhostapp.com/index.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response", response);
                try {
                    showJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Toast:", error.getMessage()+"");

                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json) throws JSONException {

        //////////////// ROUTES //////////////
        stops_array=new ArrayList<>();
        JSONArray trams = null;
        trams = new JSONArray(json);

        JSONArray jsonArray1 = trams.getJSONArray(3);
        for(int j=0;j<jsonArray1.length();++j)
        {
            JSONObject dd=jsonArray1.getJSONObject(j);

            HashMap<String,String> map=new HashMap<>();

            map.put("id",dd.getString("id"));
            map.put("type",dd.getString("type"));
            map.put("number",dd.getString("number"));
            map.put("route",dd.getString("route"));
            map.put("first_name",dd.getString("first_name"));
            map.put("last_name",dd.getString("last_name"));
            stops_array.add(map);

        }
        Log.e("Plan:",stops_array+"");


    }

    private void setInitialDateTime() {
        curDateTime.setText(DateUtils.formatDateTime(getActivity(),
                calendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_TIME));
    }

    DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            setInitialDateTime();
        }
    };
    TimePickerDialog.OnTimeSetListener timePickerDialog= new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
            calendar.set(Calendar.MINUTE,minute);
            setInitialDateTime();
        }
    };

}
