package com.example.igor.depo.Fragments;

/**
 * Created by Igor on 19.10.2016.
 */


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.igor.depo.R;
import com.example.igor.depo.activities.SelectedTroleyBusTabActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PublicTransportTabFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3;
    ProgressDialog progressDialog;
    TextView error_text;
    LinearLayout linearLayout;
    static String json;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */

        new TransportTask().execute();
        openLoadingDialog();

        View x = inflater.inflate(R.layout.public_transport_tab_layout, null);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);

        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        error_text = (TextView) x.findViewById(R.id.error_text);
        linearLayout = (LinearLayout) x.findViewById(R.id.tab_liner);

        /**
         *Set an Apater for the View Pager
         */

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;

    }

    public static class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    Bundle bundle = new Bundle();
                    bundle.putString("json", json);
                    Log.e("JsonTram", json);
                    Fragment fragment = new Tram();
                    fragment.setArguments(bundle);
                    return fragment;
                }
                case 1: {
                    Bundle bundle = new Bundle();
                    bundle.putString("json", json);
                    Log.e("JsonTroley", json);
                    Fragment fragment = new TroleyBus();
                    fragment.setArguments(bundle);
                    return fragment;
                }
                case 2: {
                    Bundle bundle = new Bundle();
                    bundle.putString("json", json);
                    Log.e("JsonBus", json);
                    Fragment fragment = new CityBus();
                    fragment.setArguments(bundle);
                    return fragment;
                }


            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Трамвай";
                case 1:
                    return "Тролейбус";
                case 2:
                    return "Автобус";
            }
            return null;
        }
    }


    class TransportTask extends AsyncTask<String, Void, Void> {
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
        StringRequest stringRequest = new StringRequest("https://depocom.000webhostapp.com/transport.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                json = response;
                viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                error_text.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Log.e("TransportTabError:", error.getMessage() + "");
                        error_text.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.GONE);
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    void openLoadingDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.dialog_message));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

}


