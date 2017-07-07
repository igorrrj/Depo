package vin.way.igor.depo.Fragments;

/**
 * Created by Igor on 19.10.2016.
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import vin.way.igor.depo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


interface  MyCallBack
{
    public  void callBack();
}

public class SelectedBusTabFragment extends Fragment implements MyCallBack {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    static boolean ans = false;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.favorites_menu, menu);
        final MenuItem mFav = menu.findItem(R.id.action_favorites);
        mFav.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ans = !ans;
                if (ans) {
                    mFav.setIcon(R.drawable.ic_favorite_black_24dp);
                } else mFav.setIcon(R.drawable.ic_favorite_border_black_24dp);
                return true;
            }
        });
    }

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2 ;
    public static String number_name = "",f_name="",l_name="",rout="";
    public static int index=0;
    ProgressDialog progressDialog;
    public static ArrayList<HashMap<String, String>> tram_routes_array;
    public static ArrayList<HashMap<String, String>> tram_time_array;
    boolean taskEnded;
    MyCallBack myCallBack;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            /**
             *Inflate tab_layout and setup Views.
             */
            View x =  inflater.inflate(R.layout.selected_bus_tab_layout,null);

            ((AppCompatActivity)getActivity()).getSupportActionBar().setElevation(0);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Міський Автобус");
            openLoadingDialog();
            taskEnded=false;
            tabLayout = (TabLayout) x.findViewById(R.id.tabs);
            viewPager = (ViewPager) x.findViewById(R.id.viewpager);

            new CityBusRouteTask().execute();

            myCallBack=this;

            Bundle bundle = this.getArguments();
            if (bundle != null) {
                number_name = bundle.getString("bus_number");
                f_name= bundle.getString("bus_f_name");
                l_name= bundle.getString("bus_l_name");
            }
            Log.e("BundleSelectedBus",number_name+"|"+f_name);

            ((TextView) x.findViewById(R.id.f_name)).setText(f_name);
            ((TextView) x.findViewById(R.id.dashe)).setText(" - ");
            ((TextView) x.findViewById(R.id.l_name)).setText(l_name);
            ((TextView) x.findViewById(R.id.number_name)).setText(number_name);


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

    @Override
    public void callBack() {
            viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        Log.e("CallBack","CALLLLLL");
    }


    public static class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
               /* case 0 : return new CityBusRoute();
                case 1 : return new CityBusTime();*/
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

            switch (position){
                case 0 :
                    return "Маршрут";
                case 1 :
                    return "Час";
            }
            return null;
        }
    }

    class CityBusRouteTask extends AsyncTask<String,Void,Void> {
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
        StringRequest stringRequest=new StringRequest("https://depocom.000webhostapp.com/bus_routes.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    showJSON(response);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        myCallBack.callBack();
                    }
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
                        Log.e("SelectedBusError:", error.getMessage()+"");
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json) throws JSONException {

        tram_routes_array=new ArrayList<>();
        tram_time_array=new ArrayList<>();

        JSONArray routes_json = new JSONArray(json);
        for(int j=0;j<routes_json.length();++j)
        {
            JSONObject dd=routes_json.getJSONObject(j);
           /* HashMap<String,String>map=new HashMap<>();
            HashMap<String,String>tmap=new HashMap<>();
            map.put("id",dd.getString("id"));
            map.put("number",dd.getString("number"));
            map.put("route",dd.getString("route"));
            tmap.put("begin_time",dd.getString("begin_time"));
            tmap.put("end_time",dd.getString("end_time"));
            tmap.put("from_depo",dd.getString("from_depo"));
            tmap.put("to_depo",dd.getString("to_depo"));
            tmap.put("time_interval",dd.getString("time_interval"));
            tram_routes_array.add(map);
            tram_time_array.add(tmap);
*/
            if(number_name.equals(dd.getString("number")))
            {
                index=j;
                SharedPreferences sp=getActivity().getSharedPreferences("bus_selected", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("route",dd.getString("route"));
                editor.putString("begin_time",dd.getString("begin_time"));
                editor.putString("end_time",dd.getString("end_time"));
                editor.putString("from_depo",dd.getString("from_depo"));
                editor.putString("to_depo",dd.getString("to_depo"));
                editor.putString("time_interval",dd.getString("time_interval"));
                editor.apply();
                Log.e("Time_Index_TO_Depo:", dd.getString("time_interval")+"");
            }
        }
        Log.e("ROUT_Index:", index+"");
        Log.e("Number_Name:", number_name+"");
        //Log.e("ROUT:",tram_routes_array+"");
    }

    void openLoadingDialog() {
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage( getString(R.string.dialog_message) );
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

}


