package com.example.igor.depo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Igor on 24.02.2017.
 */

public class Taxi extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ListView listView;
    String JsonString;
    ArrayList<HashMap<String, String>> taxi_array;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.taxi,container, false);

        sendRequest();
        listView = (ListView) rootView.findViewById(R.id.listView);
        sharedPreferences= getActivity().getSharedPreferences("save_taxi", Context.MODE_PRIVATE);
        try{
            ////////////////TAXI //////////////
            taxi_array=new ArrayList<>();
            JSONArray trams = null;
            trams = new JSONArray(sharedPreferences.getString("taxi",null));

            JSONArray jsonArray1 = trams.getJSONArray(4);
            for(int j=0;j<jsonArray1.length();++j)
            {
                JSONObject dd=jsonArray1.getJSONObject(j);

                HashMap<String,String>map=new HashMap<>();

                map.put("id",dd.getString("id"));
                map.put("name",dd.getString("name"));
                map.put("number",dd.getString("number"));
                taxi_array.add(map);

            }
            Log.e("TAXI:",taxi_array+"");

            listView.setAdapter(new TaxiAdapter(getActivity(),taxi_array));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String[]numbs;
                    TextView phone=(TextView)view.findViewById(R.id.taxi_number);
                    TextView taxiName=(TextView)view.findViewById(R.id.taxi_name);

                    numbs=phone.getText().toString().trim().split(",");

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialogView = inflater.inflate(R.layout.pop_up_for_taxi, null);
                    dialogBuilder.setView(dialogView);

                    TextView name=(TextView)dialogView.findViewById(R.id.taxi_name);
                    name.setText(taxiName.getText().toString().trim());

                    ListView listView=(ListView)dialogView.findViewById(R.id.listView);
                    listView.setAdapter(new PopupTaxiAdapter(dialogView.getContext(),numbs));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView call_numb=(TextView)view.findViewById(R.id.taxi_number);
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + call_numb.getText().toString().trim()));
                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            getActivity().startActivity(callIntent);
                        }
                    });
                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                }

            });
            Log.e("Taxi:",taxi_array+"");
        }catch (Exception e)
        {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }



        // listView.setAdapter( new CustomList(getActivity(), ParseJSON.result) );
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


        ////////////////TAXI //////////////
        taxi_array=new ArrayList<>();
        JSONArray trams = null;
        trams = new JSONArray(json);

        JSONArray jsonArray1 = trams.getJSONArray(4);
        for(int j=0;j<jsonArray1.length();++j)
        {
            JSONObject dd=jsonArray1.getJSONObject(j);

            HashMap<String,String>map=new HashMap<>();

            map.put("id",dd.getString("id"));
            map.put("name",dd.getString("name"));
            map.put("number",dd.getString("number"));
            taxi_array.add(map);

        }
        Log.e("TAXI:",taxi_array+"");

        listView.setAdapter(new TaxiAdapter(getActivity(),taxi_array));

        /////////////////////

        editor=sharedPreferences.edit();
        editor.putString("taxi", json);
        editor.apply();


    }
}
