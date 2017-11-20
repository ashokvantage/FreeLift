package com.freelift.driver.navigation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.freelift.ActivityBase;
import com.freelift.ConnectionDetector;
import com.freelift.Constants;
import com.freelift.CustomRequest;
import com.freelift.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverMyRidesDetails extends ActivityBase implements Constants, LocationListener {
    String Source_lat, Source_lng, Dest_lat, Dest_lng, Source_Add, Dest_Add, Driver_name, Driver_mobile, Booking_id, StatUs;
    public static Double mLatitude = 0.0, mLongitude = 0.0;
    public static LatLng latLng = null;
    public static GoogleMap mGoogleMap;
    Double flat, flng, slat, slng, nav_flat, nav_flng, nav_slat, nav_slng;
    TextView distance, txtname, txtnumber;
    Polyline line, nav_line;
    ImageView imgcall;
    Button btn_start, btn_end;
    int arrayLength;
    RelativeLayout rlbutton;
    Toolbar toolbar;

    TextView txtmessage, txttitle;
    AlertDialog mDialog;
    String Comment = "";
    Handler handler;
    Runnable myRunnable;
    private static final long MIN_TIME_BW_UPDATES = 1;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 5;
    public static Location clocation = null;
    Button rl_close;
    Marker marker;
    LatLng city1, city2, currentcity;
    TextView txtdistance;
    String resultkm;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {

            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DriverMyRidesDetails.this);
                LayoutInflater inflaterr = getLayoutInflater();
                View convertView = (View) inflaterr.inflate(R.layout.rides_popup, null);
                rl_close = (Button) convertView.findViewById(R.id.btn_close);
                txtmessage = (TextView) convertView.findViewById(R.id.txtmessage);
                txttitle = (TextView) convertView.findViewById(R.id.txttitle);
                txttitle.setText("Message");
                txtmessage.setText("Please enable the GPS.");
                rl_close.setText("OK");
                alertDialog.setView(convertView);
                rl_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.cancel();
                        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(gpsOptionsIntent);
                    }
                });
                mDialog = alertDialog.show();
            } else {
                //this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network Enabled");
                    if (locationManager != null) {
                        clocation = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (clocation != null) {
                            mLatitude = clocation.getLatitude();
                            mLongitude = clocation.getLongitude();
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (clocation == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS", "GPS Enabled");
                        if (locationManager != null) {
                            clocation = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (clocation != null) {
                                mLatitude = clocation.getLatitude();
                                mLongitude = clocation.getLongitude();
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(DriverMyRidesDetails.this).create();
                                //				alertDialog.setTitle("Your phone does not support GPS or NETWORK access, you can still use the application but some features may not work properly.");
                                alertDialog.setMessage("Your phone does not find current location.");//but some features may not work properly
                                //alertDialog.setIcon(R.drawable.ic_launcher);
                                alertDialog.setButton("QUIT", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
//                                            MapActivity.this.finish();
                                    }
                                });
                                alertDialog.show();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        distance = (TextView) findViewById(R.id.distancevalue);
        txtname = (TextView) findViewById(R.id.txtpassengername);
        txtnumber = (TextView) findViewById(R.id.txtpassengernumber);
        imgcall = (ImageView) findViewById(R.id.imgcall);
        btn_end = (Button) findViewById(R.id.btn_end);
        btn_start = (Button) findViewById(R.id.btn_start);
        rlbutton = (RelativeLayout) findViewById(R.id.rlbutton);
        txtdistance = (TextView) findViewById(R.id.txtdistance);
//        Source_lat = getArguments().getString("source_lat");
//        Source_lng = getArguments().getString("source_lng");
//        Dest_lat = getArguments().getString("dest_lat");
//        Dest_lng = getArguments().getString("dest_lng");
//        Driver_name = getArguments().getString("driver_name");
//        Driver_mobile = getArguments().getString("driver_mob");
//        Booking_id = getArguments().getString("booking_id");
//        Status = getArguments().getString("status");

        Intent in = getIntent();
        Source_lat = in.getStringExtra("source_lat");
        Source_lng = in.getStringExtra("source_lng");
        Dest_lat = in.getStringExtra("dest_lat");
        Dest_lng = in.getStringExtra("dest_lng");
        Source_Add = in.getStringExtra("source_add");
        Dest_Add = in.getStringExtra("dest_add");
        Driver_name = in.getStringExtra("driver_name");
        Driver_mobile = in.getStringExtra("driver_mob");
        Booking_id = in.getStringExtra("booking_id");
        StatUs = in.getStringExtra("status");

        txtname.setText("Passenger: " + Driver_name);
        txtnumber.setText("Mobile: " + Driver_mobile);
        flat = Double.parseDouble(Source_lat);
        flng = Double.parseDouble(Source_lng);
        slat = Double.parseDouble(Dest_lat);
        slng = Double.parseDouble(Dest_lng);
        if (StatUs != null) {
            if (StatUs.equalsIgnoreCase("1")) {
                btn_start.setVisibility(View.GONE);
                btn_end.setVisibility(View.GONE);
                rlbutton.setVisibility(View.GONE);
            } else if (StatUs.equalsIgnoreCase("3")) {
                btn_start.setVisibility(View.VISIBLE);
                btn_end.setVisibility(View.VISIBLE);
                btn_start.setEnabled(false);
                btn_end.setEnabled(true);
            } else if (StatUs.equalsIgnoreCase("4")) {
                btn_start.setVisibility(View.GONE);
                btn_end.setVisibility(View.GONE);
                rlbutton.setVisibility(View.GONE);
            } else {
                btn_start.setVisibility(View.VISIBLE);
                btn_end.setVisibility(View.VISIBLE);
                btn_end.setEnabled(false);
            }
        }
        final FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        } else {
            mGoogleMap = fragment.getMap();
        }
        city1 = new LatLng(slat, slng);
        city2 = new LatLng(flat, flng);
        currentcity = new LatLng(mLatitude, mLongitude);
        mGoogleMap.addMarker(new MarkerOptions().position(city2).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).title(Source_Add));
        mGoogleMap.addMarker(new MarkerOptions().position(city1).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).title(Dest_Add));
        marker = mGoogleMap.addMarker(new MarkerOptions().position(currentcity).icon(BitmapDescriptorFactory.fromResource(R.drawable.redmarker)).title("Your current position"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(city2, 12));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

            Toast.makeText(DriverMyRidesDetails.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();

        } else {
            new connectAsyncTaskdirection().execute();
        }


        imgcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String number = "12345678";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + Driver_mobile));
                startActivity(intent);
            }
        });
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatUs = "3";
                start_end();
                btn_start.setEnabled(false);
                btn_end.setEnabled(true);

                marker.setPosition(new LatLng(clocation.getLatitude(), clocation.getLongitude()));
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(DriverMyRidesDetails.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    nav_flat = clocation.getLatitude();
                    nav_flng = clocation.getLongitude();
                    nav_slat = Double.parseDouble(Dest_lat);
                    nav_slng = Double.parseDouble(Dest_lng);
                    new connectAsyncTasknavigation().execute();
                }

            }
        });
        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatUs = "4";
                start_end();
                btn_start.setVisibility(View.GONE);
                btn_end.setVisibility(View.GONE);

                marker.remove();
                nav_line.remove();
            }
        });
        handler = new Handler();
        myRunnable = new Runnable() {
            @Override
            public void run() {
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

//                    Toast.makeText(DriverMyRidesDetails.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
//                    marker.remove();
//                    city1 = new LatLng(slat, slng);
//                    city2 = new LatLng(flat, flng);
//                    currentcity = new LatLng(mLatitude, mLongitude);
//                    marker = mGoogleMap.addMarker(new MarkerOptions().position(city2).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).title(Source_Add));
//                    marker = mGoogleMap.addMarker(new MarkerOptions().position(city1).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).title(Dest_Add));
//                    marker = mGoogleMap.addMarker(new MarkerOptions().position(currentcity).icon(BitmapDescriptorFactory.fromResource(R.drawable.taximarker)).title("Your current position"));
                    if (StatUs != null) {
                        if (StatUs.equalsIgnoreCase("2")) {
                            marker.setPosition(new LatLng(clocation.getLatitude(), clocation.getLongitude()));
                            if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

//                            Toast.makeText(DriverMyRidesDetails.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                            } else {
                                nav_flat = clocation.getLatitude();
                                nav_flng = clocation.getLongitude();
                                nav_slat = Double.parseDouble(Source_lat);
                                nav_slng = Double.parseDouble(Source_lng);
                                new connectAsyncTasknavigation().execute();
                            }
                        } else if (StatUs.equalsIgnoreCase("3")) {
                            marker.setPosition(new LatLng(clocation.getLatitude(), clocation.getLongitude()));
                            if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

//                            Toast.makeText(DriverMyRidesDetails.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                            } else {
                                nav_flat = clocation.getLatitude();
                                nav_flng = clocation.getLongitude();
                                nav_slat = Double.parseDouble(Dest_lat);
                                nav_slng = Double.parseDouble(Dest_lng);
                                new connectAsyncTasknavigation().execute();
                            }
                        } else {
                            marker.remove();
                        }
                    }

                }
                handler.postDelayed(this, 10 * 100);
            }
        };
        handler.post(myRunnable);
    }

    class connectAsyncTaskdirection extends AsyncTask<Void, Void, String> {
        private ProgressDialog pdia;
        String url;
        String responseString = null;
        String strDistance;
        double dist;
        float distkm;
        int integer;
        float distanceInMeters;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pdia = new ProgressDialog(DriverMyRidesDetails.this);//,R.style.MyTheme);
            //pdia.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pdia.setMessage("Fetching route," + "\n" + " Please wait...");
            pdia.setCancelable(false);
            pdia.setIndeterminate(true);
            pdia.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            final LatLng origin = new LatLng(flat, flng);
            final LatLng dest = new LatLng(slat, slng);
            // Getting URL to the Google Directions API
            String strurl = getDirectionsUrl(origin, dest);

            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL objurl = new URL(strurl);
                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) objurl.openConnection();
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
                br.close();

            } catch (Exception e) {
                Log.d("Exception url", e.toString());
            } finally {
                try {
                    iStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                urlConnection.disconnect();
            }

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(data);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (routes.size() > 0) {
                HashMap<String, String> point = routes.get(0).get(0);

                String DistanceKm = point.get("distance");

                resultkm = DistanceKm.replace("km", "Kilometers");
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pdia.dismiss();
            if (result != null) {
                drawPath(result);
                if (resultkm != null) {
                    if (resultkm.equalsIgnoreCase("")) {
                        distance.setText(" " + resultkm);
                        distance.setTextColor(Color.RED);
                        distance.setVisibility(View.GONE);
                    } else {
                        distance.setText(" " + resultkm);
                        distance.setTextColor(Color.RED);
                        txtdistance.setText(resultkm.replace("Kilometers", "\nkm"));
                    }
                }
            }
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    public void drawPath(String result) {
        if (line != null) {
            Log.i("removing line", "removed");
            mGoogleMap.clear();
        }
        try {
            // Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            PolylineOptions options = new PolylineOptions().width(8).color(Color.parseColor("#4883da")).geodesic(true);
            for (int z = 0; z < list.size(); z++) {
                LatLng point = list.get(z);
                options.add(point);
            }
            line = mGoogleMap.addPolyline(options);

        } catch (JSONException e) {

        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    class connectAsyncTasknavigation extends AsyncTask<Void, Void, String> {
        private ProgressDialog pdia;
        String url;
        String responseString = null;
        String strDistance;
        double dist;
        float distkm;
        int integer;
        String resultkm = "";
        float distanceInMeters;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//            pdia = new ProgressDialog(PassengerMyRidesDetails.this);//,R.style.MyTheme);
//            //pdia.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//            pdia.setMessage("Fetching route," + "\n" + " Please wait...");
//            pdia.setCancelable(false);
//            pdia.setIndeterminate(true);
//            pdia.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            final LatLng origin = new LatLng(nav_flat, nav_flng);
            final LatLng dest = new LatLng(nav_slat, nav_slng);
            // Getting URL to the Google Directions API
            String strurl = getDirectionsUrl(origin, dest);

            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL objurl = new URL(strurl);
                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) objurl.openConnection();
                // Connecting to url
                urlConnection.connect();

                // Reading data from url
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb = new StringBuffer();

                String nav_line = "";
                while ((nav_line = br.readLine()) != null) {
                    sb.append(nav_line);
                }

                data = sb.toString();
                br.close();

            } catch (Exception e) {
                Log.d("Exception url", e.toString());
            } finally {
                try {
                    iStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                urlConnection.disconnect();
            }

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(data);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            if (routes.size() > 0) {
//                HashMap<String, String> point = routes.get(0).get(0);
//
//                String DistanceKm = point.get("distance");
//
//                resultkm = DistanceKm.replace("km", "Kilometers");
//            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pdia.dismiss();
            if (result != null) {
                drawNavigationPath(result);
            }
//            if (resultkm.equalsIgnoreCase("")) {
//                distance.setText(" " + resultkm);
//                distance.setTextColor(Color.RED);
//                distance.setVisibility(View.GONE);
//            } else {
//                distance.setText(" " + resultkm);
//                distance.setTextColor(Color.RED);
//                txtdistance.setText(resultkm.replace("Kilometers", "\nkm"));
//            }


        }
    }

    public void drawNavigationPath(String result) {
        try {
            // Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            PolylineOptions options = new PolylineOptions().width(5).color(Color.parseColor("#FF00FF")).geodesic(true);
            for (int z = 0; z < list.size(); z++) {
                LatLng point = list.get(z);
                options.add(point);
            }
            if (nav_line != null) {
                Log.i("removing line", "removed");
//            mGoogleMap.clear();
                nav_line.remove();
            }
            nav_line = mGoogleMap.addPolyline(options);

        } catch (JSONException e) {

        }
    }

    public void start_end() {
        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_DRIVER_START_END, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Profile", "Malformed JSON: \"" + response.toString() + "\"");
                        try {
                            JSONArray successArray = response.getJSONArray("success");

                            arrayLength = successArray.length();
                            if (arrayLength > 0) {

                                for (int i = 0; i < successArray.length(); i++) {

                                    JSONObject myridesObj = (JSONObject) successArray.get(i);

                                }
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(getActivity(), "When loading data error has occurred. Check your network connection.", Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = "";
                Map<String, String> params = new HashMap<String, String>();
                params.put("login_id", Long.toString(sharedpreferences.getLong("customer_driver_id", 0)));
                params.put("booking_id", Booking_id);
                params.put("status", StatUs);
                params.put("calc_dist", resultkm);
                if (StatUs != null) {
                    if (StatUs.equalsIgnoreCase("3")) {
                        date = df.format(Calendar.getInstance().getTime());
                        params.put("start_time", date);
                    } else if (StatUs.equalsIgnoreCase("4")) {
                        date = df.format(Calendar.getInstance().getTime());
                        params.put("end_time", date);
                    }
                }
                return params;
            }
        };

        ConnectionDetector.getInstance().addToRequestQueue(jsonReq);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case android.R.id.home: {
                mGoogleMap.clear();
                handler.removeCallbacks(myRunnable);
                finish();
                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
////        mGoogleMap = googleMap;
//
//        LatLng TutorialsPoint = new LatLng(mLatitude, mLongitude);
//        mGoogleMap.addMarker(new MarkerOptions().position(TutorialsPoint).title("Chandigarh"));
//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(TutorialsPoint));
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(TutorialsPoint)
//                .zoom(11.0f)
//                .bearing(90)
//                .tilt(30)
//                .build();
//        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//    }

    @Override
    public void onLocationChanged(Location location) {
        clocation = location;
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
//        Toast.makeText(DriverMyRidesDetails.this, "check position=" + mLatitude, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onResume() {
        super.onResume();
//        autoComp_source_add.setText(strAdd);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        marker.remove();
        mGoogleMap.clear();
        handler.removeCallbacks(myRunnable);
    }
}
