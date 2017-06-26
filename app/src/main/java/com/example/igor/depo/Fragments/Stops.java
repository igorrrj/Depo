package com.example.igor.depo.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.example.igor.depo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Igor on 26.02.2017.
 */

public class Stops extends Fragment {

    private ListView listView;
    TextView error_text;
    String JsonString;
    ArrayList<HashMap<String, String>> stops_array;
    ImageView loop;
    Toolbar mtoolbar;
    EditText msearch;
    ProgressDialog progressDialog;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (listView.getVisibility() == View.VISIBLE) {
                    ArrayList<HashMap<String, String>> templist = new ArrayList<HashMap<String, String>>();
                    for (int i = 0; i < stops_array.size(); i++) {
                        HashMap<String, String> hm = stops_array.get(i);
                        String[] stops;
                        stops = hm.get("route").trim().split(",");

                        for (String s : stops) {
                            if (s.toLowerCase().contains(newText.toLowerCase())) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("type", hm.get("type"));
                                map.put("number", hm.get("number"));
                                map.put("route", s);
                                map.put("first_name", hm.get("first_name"));
                                map.put("last_name", hm.get("last_name"));
                                map.put("begin_time", hm.get("begin_time"));
                                map.put("end_time", hm.get("end_time"));
                                map.put("from_depo", hm.get("from_depo"));
                                map.put("to_depo", hm.get("to_depo"));
                                map.put("time_interval", hm.get("time_interval"));

                                templist.add(map);
                            }
                        }

                    }
                    listView.setAdapter(new StopsAdapter(getActivity(), templist));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stops, container, false);

        openLoadingDialog();

        new StopsTask().execute();

        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setFastScrollEnabled(true);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        error_text=(TextView)rootView.findViewById(R.id.error_text);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView s_n = (TextView) view.findViewById(R.id.stop_name);
                String stop_name = s_n.getText().toString().trim();
                JSONArray jsarray = new JSONArray();
                for (int i = 0; i < stops_array.size(); i++) {
                    String[] stops;
                    stops = stops_array.get(i).get("route").trim().split(",");

                    for (int j = 0; j < stops.length; j++) {
                        stops[j] = stops[j].trim();
                    }

                    for (int j = 0; j < stops.length; j++) {
                        if (stop_name.equals(stops[j])) {
                            JSONObject jsonObj = new JSONObject();

                            try {

                                jsonObj.put("type", stops_array.get(i).get("type"));
                                jsonObj.put("number", stops_array.get(i).get("number"));
                                jsonObj.put("first_name", stops_array.get(i).get("first_name"));
                                jsonObj.put("last_name", stops_array.get(i).get("last_name"));
                                jsonObj.put("route", stops_array.get(i).get("route"));
                                jsonObj.put("begin_time", stops_array.get(i).get("begin_time"));
                                jsonObj.put("end_time", stops_array.get(i).get("end_time"));
                                jsonObj.put("from_depo", stops_array.get(i).get("from_depo"));
                                jsonObj.put("to_depo", stops_array.get(i).get("to_depo"));
                                jsonObj.put("time_interval", stops_array.get(i).get("time_interval"));

                                jsarray.put(jsonObj);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                }
                Fragment fragment = null;
                Class fragmentClass = TransportForStops.class;

                Bundle bundle = new Bundle();
                bundle.putString("jslist", jsarray.toString());
                bundle.putString("stop_name", stop_name);

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

    class StopsTask extends AsyncTask<String, Void, Void> {
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

    private void sendRequest() {
        StringRequest stringRequest = new StringRequest("https://depocom.000webhostapp.com/routes.php", new Response.Listener<String>() {
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
                        Log.e("topsError:", error.getMessage() + "");
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        error_text.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json) throws JSONException {

        //////////////// ROUTES //////////////
        stops_array = new ArrayList<>();
        JSONArray json_stops = new JSONArray(json);
        Log.e("JSonSTopsLength",json_stops.length()+"");
        for (int i = 0; i < json_stops.length(); i++) {
            JSONArray jsonArrayj = json_stops.getJSONArray(i);

            for (int j = 0; j < jsonArrayj.length(); ++j) {
                JSONObject dd = jsonArrayj.getJSONObject(j);

                HashMap<String, String> map = new HashMap<>();

                map.put("id", dd.getString("id"));
                map.put("type", dd.getString("type"));
                map.put("number", dd.getString("number"));
                map.put("route", dd.getString("route"));
                map.put("first_name", dd.getString("first_name"));
                map.put("last_name", dd.getString("last_name"));
                map.put("begin_time", dd.getString("begin_time"));
                map.put("end_time", dd.getString("end_time"));
                map.put("from_depo", dd.getString("from_depo"));
                map.put("to_depo", dd.getString("to_depo"));
                map.put("time_interval", dd.getString("time_interval"));
                stops_array.add(map);

            }
        }

        Log.e("StopsArraySize:", stops_array.size() + "");
        Log.e("StopsArray:", stops_array.toString());
        listView.setAdapter(new StopsAdapter(getActivity(), stops_array));
        listView.setFastScrollEnabled(true);
    }

    void openLoadingDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.dialog_message));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}