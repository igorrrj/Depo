package com.example.igor.depo.activities;

/**
 * Created by Igor on 19.10.2016.
 */


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.igor.depo.Fragments.RouteFragment;
import com.example.igor.depo.Fragments.TimeFragment;
import com.example.igor.depo.R;
import com.example.igor.depo.data_base.LikedSQLite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.igor.depo.Fragments.Liked_Transport_Fragment.DELETE;
import static com.example.igor.depo.Fragments.Liked_Transport_Fragment.NOT_DELETE;

public class SelectedLikedTabActivity extends AppCompatActivity {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2 ;
    public  static String type="",number_name = "",f_name="",l_name="",rout="",begin_time="",end_time="",from_depo="",to_depo="",time_interval="";

    ProgressDialog progressDialog;
    LikedSQLite likedSQLite;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_tab_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            number_name = bundle.getString("number");
            f_name= bundle.getString("f_name");
            l_name= bundle.getString("l_name");
            type=bundle.getString("type");
            rout=bundle.getString("rout");
            begin_time=bundle.getString("begin_time");
            end_time=bundle.getString("end_time");
            from_depo=bundle.getString("from_depo");
            to_depo=bundle.getString("to_depo");
            time_interval=bundle.getString("time_interval");
        }

        Log.e("Selected",number_name+"|"+f_name+"|Route"+rout);

        ((TextView)findViewById(R.id.f_name)).setText(f_name);
        ((TextView)findViewById(R.id.dashe)).setText(" - ");
        ((TextView)findViewById(R.id.l_name)).setText(l_name);
        ((TextView)findViewById(R.id.number_name)).setText(number_name);

        Log.e("Type",type);
        String title = "";
        switch (type)
        {
            case "tram":title="Трамвай";break;
            case "troley":title="Тролейбус";break;
            case "bus": title="Автобус";break;
        }
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        getSupportActionBar().setElevation(0);

        getSupportActionBar().setTitle(title);

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

        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));


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
                case 0 :
                {
                    Bundle bundle=new Bundle();
                    bundle.putString("route",rout);
                    Log.e("PUtedString ROute",rout);
                    Fragment fragment=new RouteFragment();
                    fragment.setArguments(bundle);
                    return fragment;
                }
                case 1 : {
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
        Log.e("ANS",ans+"");
        LikedSQLite likedSQLite= new LikedSQLite(this);

      if(!ans){
            likedSQLite.Delete_Item(number_name,type);
            getIntent().putExtra("result",DELETE);
      }
     else{
          getIntent().putExtra("result",NOT_DELETE);
      }
        Log.e("RESULT_OK",getIntent().getExtras().getInt("result")+"");
        setResult(RESULT_OK, getIntent());
        finish();

        //super.onBackPressed();

    }

    boolean ans = false;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.favorites_menu, menu);
        final MenuItem mFav = menu.findItem(R.id.action_favorites);

        LikedSQLite likedSQLite= new LikedSQLite(this);
        if (likedSQLite.isLiked(number_name,type)) ans=true;
        if (ans) mFav.setIcon(R.drawable.ic_favorite_black_24dp);
        Log.e("SElectedLikeTram",likedSQLite.isLiked(number_name,type)+"");
        mFav.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ans = !ans;
                if (ans) {
                    mFav.setIcon(R.drawable.ic_favorite_black_24dp);
                } else
                {
                    mFav.setIcon(R.drawable.ic_favorite_border_black_24dp);
                }
                return true;
            }
        });
        return true;
    }


    void openLoadingDialog() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage( getString(R.string.dialog_message) );
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

}


