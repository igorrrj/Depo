package com.example.igor.depo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.igor.depo.Adapters.CustomList;
import com.example.igor.depo.Adapters.StopsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Igor on 26.02.2017.
 */

public class Stops extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ListView listView;
    CustomList cl;
    String JsonString;
    ArrayList<HashMap<String, String>> stops_array;
    ImageView loop;
    Toolbar mtoolbar;
    EditText msearch;
    ScrollView scrollView;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<HashMap<String, String>> templist=new ArrayList<HashMap<String, String>>();
                for(int i=0;i<stops_array.size();i++)
                {
                    HashMap<String,String> hm=stops_array.get(i);
                    String []stops;
                    stops=hm.get("route").trim().split(",");

                    for(String s:stops)
                    {
                        if(s.toLowerCase().contains(newText.toLowerCase()))
                        {
                            HashMap<String,String>map=new HashMap<>();
                            map.put("type",hm.get("type"));
                            map.put("number",hm.get("number"));
                            map.put("route",s);
                            map.put("first_name",hm.get("first_name"));
                            map.put("last_name",hm.get("last_name"));

                            templist.add(map);
                        }
                    }

                }
                listView.setAdapter(new StopsAdapter(getActivity(),templist));
                return true;
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {


    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stops,container, false);
        //loop=(ImageView)rootView.findViewById(R.id.loop);
       // msearch=(EditText)rootView.findViewById(R.id.editText) ;
        sendRequest();

        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setFastScrollEnabled(true);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        sharedPreferences= getActivity().getSharedPreferences("save_stops", Context.MODE_PRIVATE);
        try{


            //////////////// ROUTES //////////////
            stops_array=new ArrayList<>();
            JSONArray trams = null;
            trams = new JSONArray(sharedPreferences.getString("edit_stops",null));

            JSONArray jsonArray1 = trams.getJSONArray(3);
            for(int j=0;j<jsonArray1.length();++j)
            {
                JSONObject dd=jsonArray1.getJSONObject(j);

                HashMap<String,String>map=new HashMap<>();

                map.put("id",dd.getString("id"));
                map.put("type",dd.getString("type"));
                map.put("number",dd.getString("number"));
                map.put("route",dd.getString("route"));
                map.put("first_name",dd.getString("first_name"));
                map.put("last_name",dd.getString("last_name"));

                stops_array.add(map);

            }
            listView.setAdapter(new StopsAdapter(getActivity(),stops_array));
            listView.setFastScrollEnabled(true);

            Log.e("ROUT:",stops_array+"");
        }catch (Exception e)
        {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              TextView s_n=(TextView)view.findViewById(R.id.stop_name);
              String stop_name=s_n.getText().toString().trim();
                JSONArray jsarray=new JSONArray();
                for(int i=0;i<stops_array.size();i++)
                {
                    String []stops;
                    stops=stops_array.get(i).get("route").trim().split(",");

                    for(int j=0;j<stops.length;j++)
                    {
                        if(stop_name.equals(stops[j].trim()))
                        {
                            JSONObject jsonObj= new JSONObject();

                            try {

                                jsonObj.put("type",stops_array.get(i).get("type"));
                                jsonObj.put("number",stops_array.get(i).get("number"));
                                jsonObj.put("first_name",stops_array.get(i).get("first_name"));
                                jsonObj.put("last_name",stops_array.get(i).get("last_name"));
                                jsarray.put(jsonObj);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }




                }
                Fragment fragment = null;
                Class fragmentClass=TransportForStops.class;

                Bundle bundle=new Bundle();
                bundle.putString("jslist",  jsarray.toString());
                bundle.putString("stop_name", stop_name);

                Log.e("NMB_FOR_STS:::",jsarray.toString());

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    fragment.setArguments(bundle);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();


            }
        });


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


        /////////////////////

        editor=sharedPreferences.edit();
        editor.putString("edit_stops", json);
        editor.apply();


        //////////////// ROUTES //////////////
        stops_array=new ArrayList<>();
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
            map.put("first_name",dd.getString("first_name"));
            map.put("last_name",dd.getString("last_name"));
            stops_array.add(map);

        }
        Log.e("ROUT:",stops_array+"");
        listView.setAdapter(new StopsAdapter(getActivity(),stops_array));
        listView.setFastScrollEnabled(true);

    }
}