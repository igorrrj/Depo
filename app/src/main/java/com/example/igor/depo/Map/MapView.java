package com.example.igor.depo.Map;

import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.igor.depo.Fragments.PublicTransportTabFragment;
import com.example.igor.depo.ParseJSON;
import com.example.igor.depo.R;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Igor on 18.04.2017.
 */

public class MapView extends Fragment implements OnMapReadyCallback , LocationListener {
    static GoogleMap mGoogleMap;
    com.google.android.gms.maps.MapView mMapView;
    ArrayList<LatLng>coordsArray;
    PolylineOptions polylineOptions;
    public static TabLayout tabLayout;
    ArrayList<LatLng> MarkerPoints;
    public static ViewPager viewPager;
    public static int int_items = 2 ;
    static int isTouched=0;
    SlidingUpPanelLayout slidingUpPanelLayout;
    LocationRequest mLocationRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.sliding_panel_for_map,container,false);
      //  sendRequest();

        mMapView = (com.google.android.gms.maps.MapView) v.findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this); //this is important
        slidingUpPanelLayout = (SlidingUpPanelLayout)v.findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setAnchorPoint(0.4f);


        MarkerPoints = new ArrayList<>();




        tabLayout = (TabLayout) v.findViewById(R.id.tabs);

        viewPager = (ViewPager) v.findViewById(R.id.viewpager);

        slidingUpPanelLayout.setOnTouchListener(new View.OnTouchListener() {
               @Override
               public boolean onTouch(View v, MotionEvent event) {
                   if(event.getAction()==MotionEvent.ACTION_SCROLL)
                   {
                       tabLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.apptheme_color));
                       tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.White));
                       tabLayout.setTabTextColors(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.LightGrey)));
                       isTouched=0;
                       return false;
                   }
                        if (event.getAction()==MotionEvent.ACTION_OUTSIDE)
                        {
                            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                            tabLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.White));
                            tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.Purple));
                            tabLayout.setTabTextColors(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.Red)));

                        }

                   return false;
               }
           });


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

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                // Already two locations
                if (MarkerPoints.size() > 1) {
                    MarkerPoints.clear();
                    mGoogleMap.clear();
                }

                // Adding new item to the ArrayList
                MarkerPoints.add(point);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                //options.position(point);

                /**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED.
                 */
                if (MarkerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (MarkerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }


                // Add new marker to the Google Map Android API V2
                mGoogleMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if (MarkerPoints.size() >= 2) {
                    LatLng origin = MarkerPoints.get(0);
                    LatLng dest = MarkerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = getUrl(origin, dest);
                    Log.e("onMapClick", url.toString());
                    FetchUrl FetchUrl = new FetchUrl();

                    // Start downloading json data from Google Directions API
                    FetchUrl.execute(url);
                    //move map camera
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                }

            }
        });

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
            CircleOptions circleOptions = new CircleOptions()
                    .center(latLng)
                    .radius(30)
                    .strokeColor(Color.rgb(52,70,93))
                    .fillColor(Color.rgb(52,70,93))
                    .strokeWidth(4);

            mGoogleMap.addCircle(circleOptions);

        }
        Log.e("Coords",coordsArray.toString());

        polylineOptions=new PolylineOptions().width(5).color(Color.RED).geodesic(true);
        if(!coordsArray.isEmpty())
        {
            Log.e("PolilineOptionsArray",coordsArray.toString());
            polylineOptions.addAll(coordsArray);
            mGoogleMap.addPolyline(polylineOptions);

        }
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordsArray.get(0),13));

    }

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String waypoints="waypoints="+ "49.222666+,+28.442375"+ "|"+"49.246260+,+28.499677";

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor+"&"+waypoints+ "&";

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.e("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.e("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.e("Background Task data", data.toString());
            } catch (Exception e) {
                Log.e("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.e("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.e("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.e("ParserTask","Executing routes");
                Log.e("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.e("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.e("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mGoogleMap.addPolyline(lineOptions);
            }
            else {
                Log.e("onPostExecute","without Polylines drawn");
            }
        }
    }



}
