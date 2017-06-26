package com.example.igor.depo.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.igor.depo.Adapters.PopupTaxiAdapter;
import com.example.igor.depo.Adapters.TaxiAdapter;
import com.example.igor.depo.data_base.MySQLite;
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
    TextView error_text;
    String JsonString;
    Animation fab_up,fab_down;
    static boolean isFab;
    ArrayList<HashMap<String, String>> taxi_array;
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.taxi,container, false);
        openLoadingDialog();

        new TaxiTask().execute();
        listView = (ListView) rootView.findViewById(R.id.listView);
        error_text=(TextView)rootView.findViewById(R.id.error_text);
        try{

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String[]numbs;
                    TextView phone=(TextView)view.findViewById(R.id.taxi_number);
                    TextView taxiName=(TextView)view.findViewById(R.id.taxi_name);
                    numbs=phone.getText().toString().trim().split(",");
                        openDialogForCall(taxiName.getText().toString().trim(),numbs);
                }
            });
            Log.e("Taxi:",taxi_array+"");
        }catch (Exception e)
        {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        return rootView;
    }

    class TaxiTask extends AsyncTask<String,Void,Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(String... strings) {
            sendRequest();
            return null;
        }
    }
    private void sendRequest(){
        StringRequest stringRequest=new StringRequest("https://depocom.000webhostapp.com/taxi.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    showJSON(response);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    error_text.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        error_text.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                        Log.e("TaxiError:", error.getMessage()+"");

                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json) throws JSONException {

        ////////////////TAXI //////////////
        taxi_array=new ArrayList<>();
        JSONArray json_taxi = new JSONArray(json);

        for(int j=0;j<json_taxi.length();++j)
        {
            JSONObject dd=json_taxi.getJSONObject(j);

            HashMap<String,String>map=new HashMap<>();
            map.put("id",dd.getString("id"));
            map.put("name",dd.getString("name"));
            map.put("number",dd.getString("number"));
            taxi_array.add(map);
        }
        Log.e("TAXIsize:",taxi_array.size()+"");

        listView.setAdapter(new TaxiAdapter(getActivity(),taxi_array));

    }

    void openDialogForCall(String firmName,String[] numbers)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.pop_up_for_taxi, null);
        dialogBuilder.setView(dialogView);

        TextView name=(TextView)dialogView.findViewById(R.id.taxi_name);
        name.setText(firmName);

        ListView listView=(ListView)dialogView.findViewById(R.id.listView);
        listView.setAdapter(new PopupTaxiAdapter(dialogView.getContext(),numbers));
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

    void openLoadingDialog() {
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage( getString(R.string.dialog_message) );
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

}
