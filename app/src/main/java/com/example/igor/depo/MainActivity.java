package com.example.igor.depo;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.igor.depo.Fragments.Liked;
import com.example.igor.depo.Fragments.PublicTransportTabFragment;
import com.example.igor.depo.Fragments.Stops;
import com.example.igor.depo.Fragments.Taxi;
import com.example.igor.depo.Preference.SettingsActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

interface mInterface {
    public void wifi();
}

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, mInterface {
    Toolbar toolbar;
    FrameLayout frameLayout;

    private static final String TAG = "MainActivity";
    private AdView mAdView;
    @Override
    protected void onResume() {
        ConnectivityManager connectivityManager = (ConnectivityManager) (MainActivity.this)
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = PublicTransportTabFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, new PublicTransportTabFragment()).commit();

            mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

        } else {
            // display error
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Ввімкніть мережеве з'єднання")
                    .setCancelable(true)
                    .setNegativeButton("Добре",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.hideOverflowMenu();
        // final mInterface callback = this;


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                frameLayout.setTranslationX(slideOffset * drawerView.getWidth());

                drawer.bringChildToFront(drawerView);
                drawer.requestLayout();
                //below line used to remove shadow of drawer
                drawer.setScrimColor(Color.TRANSPARENT);
            }//this method helps you to aside menu drawer
        };
        drawer.setDrawerListener(toggle);


        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        frameLayout = (FrameLayout) findViewById(R.id.content_main);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final AlertDialog builder=new AlertDialog.Builder(MainActivity.this)
                    .setMessage("Ви впевнені що хочете вийти?")
                    .setPositiveButton("Так", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton("Ні", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                          dialogInterface.cancel();
                        }
                    })
                    .setCancelable(true)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;

        if (id == R.id.nav_home) {
            // Handle the camera action
            fragmentClass = PublicTransportTabFragment.class;
            toolbar.setTitle("Транспорт");

        } else if (id == R.id.nav_taxi) {
            fragmentClass = Taxi.class;
            toolbar.setTitle("Таксі");
        } else if (id == R.id.nav_stops) {
            fragmentClass = Stops.class;
            toolbar.setTitle("Зупинки");
        } else if (id == R.id.nav_choosen) {
            fragmentClass = Liked.class;
            toolbar.setTitle("Обране");
        }


        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void wifi() {

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {


            }
        });
    }
}
