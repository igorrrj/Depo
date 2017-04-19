package com.example.igor.depo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.igor.depo.Adapters.CustomList;
import com.example.igor.depo.Adapters.LikedAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Igor on 27.03.2017.
 */

public class Liked extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ListView listView;
    CustomList cl;
    String JsonString;
    ArrayList<HashMap<String, String>> liked_array=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.liked,container, false);
        sendRequest();

        listView = (ListView) rootView.findViewById(R.id.listView);
        return rootView;
    }

    private void sendRequest(){
        StringRequest stringRequest=new StringRequest("https://depocom.000webhostapp.com/choosen.php", new Response.Listener<String>() {
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


        /////////////////////
/*
        editor=sharedPreferences.edit();
        editor.putString("liked", json);
        editor.apply();

*/
        ////////////////TRAM ROUTES //////////////
        JSONArray array = null;
           array =  new JSONArray(json);

        for(int j=0;j<array.length();++j)
        {
            JSONObject dd=array.getJSONObject(j);

            HashMap<String,String>map=new HashMap<>();

            map.put("id",dd.getString("id"));
            map.put("type",dd.getString("type"));
            map.put("number",dd.getString("number"));
            map.put("route",dd.getString("route"));
            map.put("first_name",dd.getString("first_name"));
            map.put("last_name",dd.getString("last_name"));
            liked_array.add(map);

        }
        Log.e("ROUT:",liked_array+"");
        listView.setAdapter( new LikedAdapter(getActivity(),liked_array) );

    }
}
