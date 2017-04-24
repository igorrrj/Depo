package com.example.igor.depo.Fragments;

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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.igor.depo.Adapters.CustomList;
import com.example.igor.depo.ParseJSON;
import com.example.igor.depo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Igor on 14.02.2017.
 */

public class CityBus extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ListView listView;
    CustomList cl;
     ArrayList<ArrayList<HashMap<String, String>>>two_d_array;

     ArrayList<HashMap<String, String>>time_d_array;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.city_bus,container, false);
        sendRequest();
        listView = (ListView) rootView.findViewById(R.id.listView);
        sharedPreferences= getActivity().getSharedPreferences("save_list_city_bus", Context.MODE_PRIVATE);
        try{
            ParseJSON pj = new ParseJSON(sharedPreferences.getString("city_bus",null));
            pj.parseJSON();
            listView.setAdapter( new CustomList(getActivity(), ParseJSON.result,1) );
        }catch (Exception e)
        {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name =((TextView)view.findViewById(R.id.f_name)).getText().toString().trim();
                String number_name =((TextView)view.findViewById(R.id.number_name)).getText().toString().trim();
                Log.e("name:",name);
                Log.e("NameFrom:",time_d_array.get(position).get("name").toString()+"");
                Log.e("number:",number_name);
                Log.e("NumberFrom:",time_d_array.get(position).get("number_name").toString()+"");

               for(int i=0;i<time_d_array.size();i++)
                {
                    HashMap<String, String> map_d;
                    map_d = time_d_array.get(i);
                   if( name.equals(map_d.get("name").trim())&&number_name.equals(map_d.get("number_name").trim()) )
                   {
                        Log.e("NumberFromL:",time_d_array.get(position).get("number_name")+"");
                        Fragment fragment = null;
                        Class fragmentClass=Time.class;

                        Bundle bundle=new Bundle();
                        bundle.putString("time",map_d.get("time"));

                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                            fragment.setArguments(bundle);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
                    }
                }

            }
        });




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
                        //Toast.makeText(getActivity(),error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("CityBusError:", error.getMessage()+"");

                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json) throws JSONException {
        ParseJSON pj = new ParseJSON(json);
        pj.parseJSON();

        Log.e("SIZE:", ParseJSON.result.size()+"");
        two_d_array=ParseJSON.result;

        Log.e("TWO_D_ARRAY_LENGTH", two_d_array.size()+"");

        cl = new CustomList(getActivity(),two_d_array,1);
        listView.setAdapter(cl);
        cl.notifyDataSetChanged();


        editor=sharedPreferences.edit();
        editor.putString("city_bus", json);
        editor.apply();

        ////////////////Time //////////////
        time_d_array=new ArrayList<>();
        JSONArray trams = null;
        trams = new JSONArray(json);

            JSONArray jsonArray1 = trams.getJSONArray(2);
            for(int j=0;j<jsonArray1.length();++j)
            {
                JSONObject dd=jsonArray1.getJSONObject(j);

                HashMap<String,String>map=new HashMap<>();

                map.put("id",dd.getString("id"));
                map.put("number_name",dd.getString("number_name"));
                map.put("name",dd.getString("name"));
                map.put("time",dd.getString("time"));
                time_d_array.add(map);

            }


        }
    }

