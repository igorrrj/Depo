package com.example.igor.depo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
 * Created by Igor on 12.02.2017.
 */

public class Tram extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ListView listView;
    CustomList cl;
    String JsonString;
    ArrayList<HashMap<String, String>> tram_routes_array;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tram,container, false);

        sendRequest();
        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView number=(TextView)view.findViewById(R.id.number_name);
                String numb=number.getText().toString().trim();
                Log.e("Tram Number:",numb);

                for(int i=0;i<tram_routes_array.size();i++)
                {
                    HashMap<String, String> map_d;
                    map_d = tram_routes_array.get(i);
                    Log.e("Number:",map_d.get("number").toString().trim());

                    if(numb.equals(map_d.get("number").toString().trim()))
                    {
                        Fragment fragment = null;
                        Class fragmentClass=TramRoute.class;

                        Bundle bundle=new Bundle();
                        bundle.putString("tram_route",map_d.get("route"));


                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                            fragment.setArguments(bundle);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
                        break;
                    }
                }

            }
        });
       sharedPreferences= getActivity().getSharedPreferences("save_tram", Context.MODE_PRIVATE);
        try{
            ParseJSON pj = new ParseJSON(sharedPreferences.getString("edit",null));
            pj.parseJSON();
            listView.setAdapter( new CustomList(getActivity(), ParseJSON.result,0) );

            ////////////////TRAM ROUTES //////////////
            tram_routes_array=new ArrayList<>();
            JSONArray trams = null;
            trams = new JSONArray(sharedPreferences.getString("edit",null));

            JSONArray jsonArray1 = trams.getJSONArray(3);
            for(int j=0;j<jsonArray1.length();++j)
            {
                JSONObject dd=jsonArray1.getJSONObject(j);

                HashMap<String,String>map=new HashMap<>();

                map.put("id",dd.getString("id"));
                map.put("type",dd.getString("type"));
                map.put("number",dd.getString("number"));
                map.put("route",dd.getString("route"));
                tram_routes_array.add(map);

            }
            Log.e("ROUT:",tram_routes_array+"");
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
        ParseJSON pj = new ParseJSON(json);
        pj.parseJSON();

        Log.e("SIZE", ParseJSON.result.size()+"");

        cl = new CustomList(getActivity(), ParseJSON.result,0);
       listView.setAdapter(cl);

        /////////////////////

        editor=sharedPreferences.edit();
        editor.putString("edit", json);
        editor.apply();


        ////////////////TRAM ROUTES //////////////
        tram_routes_array=new ArrayList<>();
        JSONArray trams = null;
        trams = new JSONArray(json);

        JSONArray jsonArray1 = trams.getJSONArray(3);
        for(int j=0;j<jsonArray1.length();++j)
        {
            JSONObject dd=jsonArray1.getJSONObject(j);

            HashMap<String,String>map=new HashMap<>();

            map.put("id",dd.getString("id"));
            map.put("type",dd.getString("type"));
            map.put("number",dd.getString("number"));
            map.put("route",dd.getString("route"));
            tram_routes_array.add(map);

        }
        Log.e("ROUT:",tram_routes_array+"");

    }
}
