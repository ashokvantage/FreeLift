package com.freelift.passenger.navigation;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.freelift.Constants;
import com.freelift.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by ADMIN on 31-Jan-17.
 */

public class AAA extends Fragment implements Constants, OnMapReadyCallback, LocationListener {

    public static GoogleMap mGoogleMap;

    public static LatLng latLng = null;
    private static final long MIN_TIME_BW_UPDATES = 5;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 20;
    public static Double mLatitude = 0.0, mLongitude = 0.0;
    public static Location location = null;

    AlertDialog mDialog;
    TextView txtstart_point, txtdest_po_point;
    Button rl_confirm, rl_cancel, rl_close;
    String Taxi_Type = "0";
    View view;
    TextView txt_message, txttitle, txtmessage;
    String input_field = "";
    Double s_lat = 0.0, s_lng = 0.0, d_lat = 0.0, d_lng = 0.0;
    String first_country = "", second_country = "";
    public static String Country_name = "",Country_Code="";
    String booking_date="";

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.home, container, false);

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), requestCode);
            dialog.show();
        } else { // Google Play Services are available
            // Getting reference to the SupportMapFragment
//            SupportMapFragment fragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mapp);
//
//            mGoogleMap = fragment.getMap();
            FragmentManager fm = getChildFragmentManager();
            SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.mapp);
            if (fragment == null) {
                fragment = SupportMapFragment.newInstance();
                fm.beginTransaction().replace(R.id.mapp, fragment).commit();
            } else {
                mGoogleMap = fragment.getMap();
            }
//            mGoogleMap = mapView.getMap();
//            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
//            mGoogleMap.setMyLocationEnabled(true);

            MarkerOptions markerOptions = new MarkerOptions();
            try {
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                // getting GPS status
                boolean isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                boolean isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflaterr = getActivity().getLayoutInflater();
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
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                mLatitude = location.getLatitude();
                                mLongitude = location.getLongitude();
                            }
                        }
                    }
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    mLatitude = location.getLatitude();
                                    mLongitude = location.getLongitude();
                                } else {
                                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
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
            if (location != null) {
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
                latLng = new LatLng(mLatitude, mLongitude);
//                getCompleteAddressString(mLatitude,mLongitude);
                markerOptions.title("Your current Location:");
                markerOptions.snippet("Gfh");
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                markerOptions.position(latLng);
                mGoogleMap.addMarker(markerOptions);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11), 1500, null);
                onLocationChanged(location);
            }
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        mGoogleMap = googleMap;

        LatLng TutorialsPoint = new LatLng(mLatitude, mLongitude);
        mGoogleMap.addMarker(new MarkerOptions().position(TutorialsPoint).title("Chandigarh"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(TutorialsPoint));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(TutorialsPoint)
                .zoom(11.0f)
                .bearing(90)
                .tilt(30)
                .build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
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
}