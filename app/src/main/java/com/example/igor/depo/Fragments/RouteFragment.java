package com.example.igor.depo.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.igor.depo.Adapters.RoutesAdapter;
import com.example.igor.depo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor on 22.02.2017.
 */

public class RouteFragment extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.routes_layout, container, false);
        listView = (ListView) rootView.findViewById(R.id.ListView);
        Log.e("BundleTRamRoute",getArguments().getString("route"));
        listView.setAdapter(new RoutesAdapter(getActivity(),getArguments().getString("route")));


        return rootView;
    }
/*
    private void sendResponse()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,  "https://depocom.000webhostapp.com/favourite.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("POST:",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                        Log.e("***ERROR_POST***:",error.toString());

                    }
                }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("TYPE","tram");
                params.put("ROUT",rout);
                params.put("FIRST_NAME",f_name);
                params.put("LAST_NAME", l_name);
                params.put("NUMBER_NAME", number_name);
                Log.e("***RESPONSE***:",params.get("TYPE")+"  "+ l_name +" "+ params.get("NUMBER_NAME") +"  "+rout);
                return params;
            }

        };

       final  RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }*/


}

