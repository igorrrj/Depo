package vin.way.igor.depo.activities;

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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import vin.way.igor.depo.Fragments.RouteFragment;
import vin.way.igor.depo.Fragments.TimeFragment;
import vin.way.igor.depo.data_base.LikedSQLite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelectedTroleyBusTabActivity extends AppCompatActivity {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2 ;
    public static String number_name = "",f_name="",l_name="",rout="",begin_time="",end_time="",from_depo="",to_depo="",time_interval="";
    public  int index=0;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(vin.way.igor.depo.R.layout.selected_bus_tab_layout);

        Toolbar toolbar = (Toolbar) findViewById(vin.way.igor.depo.R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Тролейбус");
        openLoadingDialog();

        tabLayout = (TabLayout)findViewById(vin.way.igor.depo.R.id.tabs);
        viewPager = (ViewPager)findViewById(vin.way.igor.depo.R.id.viewpager);

        new BusRouteTask().execute();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            number_name = bundle.getString("troley_number");
            f_name= bundle.getString("troley_f_name");
            l_name= bundle.getString("troley_l_name");
        }
        Log.e("TroleyBundleSelected",number_name+"|"+f_name);

        ((TextView)findViewById(vin.way.igor.depo.R.id.f_name)).setText(f_name);
        ((TextView)findViewById(vin.way.igor.depo.R.id.dashe)).setText(" - ");
        ((TextView)findViewById(vin.way.igor.depo.R.id.l_name)).setText(l_name);
        ((TextView)findViewById(vin.way.igor.depo.R.id.number_name)).setText(number_name);


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
                case 0 : {
                    Bundle bundle=new Bundle();
                    bundle.putString("route",rout);
                    Fragment fragment=new RouteFragment();
                    fragment.setArguments(bundle);
                    return fragment;
                }
                case 1 :{
                    Bundle bundle=new Bundle();
                    bundle.putString("begin_time",begin_time);
                    bundle.putString("end_time",end_time);
                    bundle.putString("from_depo",from_depo);
                    bundle.putString("to_depo",to_depo);
                    bundle.putString("time_interval",time_interval);
                    Fragment fragment=new TimeFragment();
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

            switch (position){
                case 0 :
                    return "Маршрут";
                case 1 :
                    return "Час";
            }
            return null;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LikedSQLite likedSQLite= new LikedSQLite(this);

        if(ans && !rout.equals(""))
        {
            likedSQLite.Add_Item(f_name,l_name,"troley",number_name,rout,
                    begin_time,end_time,from_depo,
                    to_depo,time_interval);
        }
        else {
            likedSQLite.Delete_Item(number_name,"troley");
        }
    }


    boolean ans = false;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(vin.way.igor.depo.R.menu.favorites_menu, menu);
        final MenuItem mFav = menu.findItem(vin.way.igor.depo.R.id.action_favorites);

        LikedSQLite likedSQLite= new LikedSQLite(this);
        if (likedSQLite.isLiked(number_name,"troley")) ans=true;
        if (ans) mFav.setIcon(vin.way.igor.depo.R.drawable.ic_favorite_black_24dp);
        Log.e("SElectedLikeTroley",likedSQLite.isLiked(number_name,"troley")+"");

        mFav.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ans = !ans;
                if (ans) {
                    mFav.setIcon(vin.way.igor.depo.R.drawable.ic_favorite_black_24dp);
                } else mFav.setIcon(vin.way.igor.depo.R.drawable.ic_favorite_border_black_24dp);
                return true;
            }
        });
        return true;
    }



    class BusRouteTask extends AsyncTask<String,Void,Void> {
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
        StringRequest stringRequest=new StringRequest("https://depocom.000webhostapp.com/troley_bus_routes.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    showJSON(response);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
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
                        Log.e("TroleyError:", error.getMessage()+"");
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json) throws JSONException {
        JSONArray routes_json = new JSONArray(json);
        for(int j=0;j<routes_json.length();++j)
        {
            JSONObject dd=routes_json.getJSONObject(j);

            if(number_name.equals(dd.getString("number")))
            {
                rout=dd.getString("route");
                begin_time=dd.getString("begin_time");
                end_time=dd.getString("end_time");
                from_depo=dd.getString("from_depo");
                to_depo=dd.getString("to_depo");
                time_interval=dd.getString("time_interval");
            }
        }
    }

    void openLoadingDialog() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage( getString(vin.way.igor.depo.R.string.dialog_message) );
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


}


