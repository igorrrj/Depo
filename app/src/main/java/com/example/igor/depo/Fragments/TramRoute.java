package com.example.igor.depo.Fragments;

import android.content.SharedPreferences;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor on 22.02.2017.
 */

public class TramRoute extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ListView listView;
    TextView textViewFirstName, textViewLastName, textViewNumbers, textViewDashe;
    static boolean ans = false;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.favorites_menu, menu);
        final MenuItem mFav = menu.findItem(R.id.action_favorites);
        mFav.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ans = true;

                if (ans) {

                    mFav.setIcon(R.drawable.ic_favorite_black_24dp);
                } else mFav.setIcon(R.drawable.ic_favorite_border_black_24dp);
                return true;
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    String rout = "", f_name = "", l_name = "", number_name = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.routes_layout, container, false);
        listView = (ListView) rootView.findViewById(R.id.ListView);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            rout = bundle.getString("tram_route");
            f_name = bundle.getString("tram_f_name");
            l_name = bundle.getString("tram_l_name");
            number_name = bundle.getString("tram_number");

        }
        Log.e("Route Tram:", rout);
        listView.setAdapter(new RoutesAdapter(getActivity(), rout));


        sendResponse();


        textViewFirstName = (TextView) rootView.findViewById(R.id.f_name);
        textViewDashe = (TextView) rootView.findViewById(R.id.dashe);
        textViewLastName = (TextView) rootView.findViewById(R.id.l_name);
        textViewNumbers = (TextView) rootView.findViewById(R.id.number_name);

        textViewFirstName.setText(f_name);
        textViewDashe.setText(" - ");
        textViewLastName.setText(l_name);
        textViewNumbers.setText(number_name);


        return rootView;
    }


    private void sendResponse()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,  "https://depocom.000webhostapp.com/favourite.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                     //   Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();
                        Log.d("POST:",response);

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

    }
}

