package com.example.igor.depo.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.igor.depo.Adapters.PopupTaxiAdapter;
import com.example.igor.depo.Adapters.TaxiAdapter;
import com.example.igor.depo.MySQLite;
import com.example.igor.depo.R;

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
    MySQLite mySQLite;
    SharedPreferences.Editor editor;
    private ListView listView;
    String JsonString;
    Animation fab_up,fab_down;
    static boolean isFab;
    ArrayList<HashMap<String, String>> taxi_array;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.taxi,container, false);

        sendRequest();
        listView = (ListView) rootView.findViewById(R.id.listView);
        sharedPreferences= getActivity().getSharedPreferences("save_taxi", Context.MODE_PRIVATE);
        try{
            ////////////////TAXI //////////////
           /* taxi_array=new ArrayList<>();
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

        //    listView.setAdapter(new TaxiAdapter(getActivity(),mySQLite.Get_Taxi()));
*/

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
                new AsyncTaxiDB(response).execute();
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
                       mySQLite=new MySQLite(getActivity());
                        mySQLite.Get_Taxi();
                        listView.setAdapter(new TaxiAdapter(getActivity(),mySQLite.arrayList));

                        Toast.makeText(getActivity(),error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Error:", error.getMessage()+"");

                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json) throws JSONException {
        AsyncTaxiDB asyncTaxiDB= (AsyncTaxiDB) new AsyncTaxiDB(json).execute();

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
        Log.e("TAXIsize:",taxi_array.size()+"");


        listView.setAdapter(new TaxiAdapter(getActivity(),taxi_array));

        /////////////////////


    }

    private class AsyncTaxiDB extends AsyncTask<Void,Void,Void> {
        String json;

        AsyncTaxiDB(String str) {
            this.json = str;
            Log.e("***Str***", this.json);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.e("***post***", "YES");

        }


        @Override
        protected void onPreExecute() {
            Log.e("***Pre***", "YES");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.e("***inbk***", "YES");
            mySQLite = new MySQLite(getActivity());

            try {

                JSONArray trams = null;
                trams = new JSONArray(json);

                JSONArray jsonArray1 = trams.getJSONArray(4);
                for (int j = 0; j < jsonArray1.length(); ++j) {
                    JSONObject dd = jsonArray1.getJSONObject(j);

                    HashMap<String, String> map = new HashMap<>();

                    map.put("id", dd.getString("id"));
                    map.put("name", dd.getString("name"));
                    map.put("number", dd.getString("number"));
                    if (mySQLite.arrayList.isEmpty())
                        mySQLite.Add_Taxi(dd.getString("name"), dd.getString("number"));
                    Log.e("***ASYNK***", dd.getString("name"));
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return null;
        }
    }

}
