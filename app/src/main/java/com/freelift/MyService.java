package com.freelift;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ADMIN on 03-Feb-17.
 */

public class MyService extends Service implements Constants, LocationListener {
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private boolean isRigistered = false;
    double lat, lng;
    protected LocationManager locationManager;
    Location location;
    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;

    public Location getLocation(String provider) {
        if (locationManager.isProviderEnabled(provider)) {
            locationManager.requestLocationUpdates(provider,
                    MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(provider);
                return location;
            }
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        startLocationService();
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location gpsLocation = getLocation(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled) {
            if (gpsLocation != null) {
                lat = gpsLocation.getLatitude();
                lng = gpsLocation.getLongitude();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please enable GPS", Toast.LENGTH_LONG).show();
        }

        Location nwLocation = getLocation(LocationManager.NETWORK_PROVIDER);
        if (isNetworkEnabled) {
            if (nwLocation != null) {
                lat = nwLocation.getLatitude();
                lng = nwLocation.getLongitude();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please enable network", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.

//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        blinkText();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    public void startLocationService() {
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (gps_enabled) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            if (locListener != null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
                Log.e("Location service", "started GPS");
                isRigistered = true;
            }
        }

        if (network_enabled) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            if (locListener != null) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
                Log.e("Location service", "started Network");
                isRigistered = true;
            }


        }
    }

    IBaseGpsListener locListener = new IBaseGpsListener() {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.e("Lat ", "" + location.getLatitude());
                Log.e("Long ", "" + location.getLongitude());
                lat = location.getLatitude();
                lng = location.getLongitude();
//                Toast.makeText(getApplicationContext(),"lat:"+lat+ "Long:" + lng,Toast.LENGTH_LONG).show();
                if (lat == 0.0) {
                    Toast.makeText(getApplicationContext(), "lat:" + lat + "Long:" + lng, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onGpsStatusChanged(int event) {

        }
    };


    public interface IBaseGpsListener extends LocationListener, GpsStatus.Listener {
        public void onLocationChanged(Location location);

        public void onProviderDisabled(String provider);

        public void onProviderEnabled(String provider);

        public void onStatusChanged(String provider, int status, Bundle extras);

        public void onGpsStatusChanged(int event);
    }

    private void blinkText() {
        // TODO Auto-generated method stub
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 10000;    //in ms
                try {
                    Thread.sleep(timeToBlink);
                } catch (Exception e) {

                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(getApplicationContext(), " Start", Toast.LENGTH_LONG).show();

//                        if ((sharedpreferences.getLong("customer_driver_id", 0)) != 0) {
//                            if ((sharedpreferences.getString("login_as", "")).equalsIgnoreCase("driver")) {
//                                if (Double.toString(lat).equalsIgnoreCase("")) {
//                                    Toast.makeText(getApplicationContext(), "Your location not found. Please restart your device.", Toast.LENGTH_LONG).show();
//                                } else {
//                                    if (!ConnectionDetector.getInstance().isConnectingToInternet()) {
//                                        Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();
//                                        blinkText();
//                                    } else {
//                                        updateLocation();
//                                        blinkText();
//                                    }
//                                }
//                            }
//                        }

                        if ((sharedpreferences.getLong("customer_driver_id", 0)) != 0) {
                            if ((sharedpreferences.getString("login_as", "")).equalsIgnoreCase("driver")) {
                                if (ConnectionDetector.getInstance().isConnectingToInternet()) {
                                    if ((Double.toString(lat).equalsIgnoreCase(""))||(Double.toString(lat).equalsIgnoreCase("0.0"))) {
                                        Toast.makeText(getApplicationContext(), "Your device not found current location.", Toast.LENGTH_LONG).show();
                                        startLocationService();
                                        blinkText();
                                    } else {
                                        updateLocation();
                                        blinkText();
                                    }
                                } else {
                                    blinkText();

                                }
                            }
                        }
                    }
                });
            }
        }).start();
    }

    public void updateLocation() {
        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_UPDATE_LOCATION, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                            Toast.makeText(getApplicationContext(), response.toString()+"\n lat:"+lat+" lng:"+lng, Toast.LENGTH_LONG).show();
                            JSONArray successArray = response.getJSONArray("success");


                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(getApplicationContext(), "When loading data error has occurred. Check your network connection.", Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("login_id", Long.toString(sharedpreferences.getLong("customer_driver_id", 0)));
                params.put("lat", Double.toString(lat));
                params.put("long", Double.toString(lng));
                if (sharedpreferences.getLong("customer_driver_id", 0) == 0) {
                    params.put("status", "false");
                } else {
                    params.put("status", "true");
                }
                return params;
            }
        };

        ConnectionDetector.getInstance().addToRequestQueue(jsonReq);
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}