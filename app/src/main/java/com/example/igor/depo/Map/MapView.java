package com.example.igor.depo;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.igor.depo.Fragments.PublicTransportTabFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Igor on 18.04.2017.
 */

public class MapView extends Fragment implements OnMapReadyCallback {
    static GoogleMap mGoogleMap;
    com.google.android.gms.maps.MapView mMapView;
    ArrayList<LatLng>coordsArray;
    PolylineOptions polylineOptions;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2 ;
    SlidingUpPanelLayout slidingUpPanelLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.sliding_panel_for_map,container,false);
        sendRequest();

        mMapView = (com.google.android.gms.maps.MapView) v.findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this); //this is important
        slidingUpPanelLayout = (SlidingUpPanelLayout)v.findViewById(R.id.sliding_layout);

        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new PublicTransportTabFragment.MyAdapter(getChildFragmentManager()));

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
        return v;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e("onMapREady","YES");
        LatLng sydney = new LatLng(-33.867, 151.206);
        mGoogleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
       // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,13));
        mGoogleMap.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));




        mGoogleMap.addPolyline(polylineOptions);

        for(int i=0;i<coordsArray.size();i++)
        {
            CircleOptions circleOptions = new CircleOptions()
                    .center(coordsArray.get(i))
                    .radius(30)
                    .strokeColor(Color.rgb(52,70,93))
                    .fillColor(Color.rgb(52,70,93))
                    .strokeWidth(4);

            mGoogleMap.addCircle(circleOptions);
        }
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordsArray.get(0),13));
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    private void sendRequest(){
        StringRequest stringRequest=new StringRequest("https://depocom.000webhostapp.com/stops_coords.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("MapResponse", response);
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
                        //  Toast.makeText(getActivity(),error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("MapJSONError:", error.getMessage()+"");

                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json) throws JSONException {
        ParseJSON pj = new ParseJSON(json);
        pj.parseJSON();

        Log.e("SIZE", ParseJSON.result.size()+"");

        coordsArray=new ArrayList<>();
        JSONArray array = null;
        array =  new JSONArray(json);

        for(int j=0;j<array.length();++j)
        {
            JSONObject dd=array.getJSONObject(j);

            LatLng latLng=new LatLng(Double.parseDouble(dd.getString("latitude")),Double.parseDouble(dd.getString("longtitude")));

            coordsArray.add(latLng);

        }
        Log.e("Coords",coordsArray.toString());

        polylineOptions=new PolylineOptions().width(5).color(Color.RED).geodesic(true);
        if(!coordsArray.isEmpty())
        {
            Log.e("PolilineOptionsArray",coordsArray.toString());
            polylineOptions.addAll(coordsArray);
        }
    }
}
