package com.freelift.passenger.navigation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.freelift.ConnectionDetector;
import com.freelift.Constants;
import com.freelift.CustomAdapter;
import com.freelift.CustomRequest;
import com.freelift.JSONParser;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by ADMIN on 31-Jan-17.
 */

public class Home extends Fragment implements Constants, OnMapReadyCallback, LocationListener, AdapterView.OnItemClickListener {

    //    private ArrayList<Profile> searchList;
//
    private PopupWindow pwindo;
    //    private SearchListAdapter adapter;
    String strAdd = "";
    Boolean loadingMore = false;
    Boolean viewMore = false;
    public static GoogleMap mGoogleMap;

    public static LatLng latLng = null;
    public static MarkerOptions markerOptions = new MarkerOptions();
    public static HashMap<String, String> mMarkerPlaceLink = new HashMap<String, String>();
    String id;
    private static final long MIN_TIME_BW_UPDATES = 5;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 20;
    public static Double mLatitude = 0.0, mLongitude = 0.0;
    public static Location location = null;

    private static final String LOG_TAG = "FreeLift";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyBVcKaeFrAXk9W9OoFCAZXXZYspNdiHJwo";
    //                private static final String API_KEY ="AIzaSyCZMx4SBP7Nec9jM39uYWYVnEeAMQ1ly3Y";
    AutoCompleteTextView autoComp_source_add, autoComp_dest_add;
    GooglePlacesAutocompleteAdapter adapter1, adapter2;
    RelativeLayout rl_start;
    int arrayLength;
    String s_latitude, s_longitude, d_latitude, d_longitude;
    String Source_lat = "", Dest_lat = "";
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String Source_add, Dest_add;
    ListView taxi_type_lv;
    AlertDialog mDialog;
    //    int[] icon;
//    String[] names;
    CustomAdapter adapter;
    static String[] Ac_driver_Id;
    static String[] Ac_driver_latitude;
    static String[] Ac_driver_longitude;
    static String[] NonAc_driver_Id;
    static String[] NonAc_driver_latitude;
    static String[] NonAc_driver_longitude;
    static String[] driver_latitude;
    static String[] driver_longitude;
    String taxi_type = "";
    String Ac_driver_Ids = "", NonAc_driver_Ids = "", booking_message;
    double lat = 0.0, lng = 0.0;
    TextView txtstart_point, txtdest_po_point;
    Button rl_confirm, rl_cancel, rl_close;
    String Taxi_Type = "0";
    View view;
    TextView txt_message, txttitle, txtmessage;
    String input_field = "";
    Double s_lat = 0.0, s_lng = 0.0, d_lat = 0.0, d_lng = 0.0;
    String first_country = "", second_country = "";
    public static String Country_name = "", Country_Code = "";
    String booking_date = "";
    RelativeLayout rlreportemergency;
    String currentAddress = "";
//    boolean connectionweek = false;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.home, container, false);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        autoComp_source_add = (AutoCompleteTextView) view.findViewById(R.id.autoComplete_source_add);
        autoComp_dest_add = (AutoCompleteTextView) view.findViewById(R.id.autoComplete_dest_add);
        rl_start = (RelativeLayout) view.findViewById(R.id.rl_start);
        txt_message = (TextView) view.findViewById(R.id.txt_message);
        rlreportemergency = (RelativeLayout) view.findViewById(R.id.rlreportemergency);
//        autoComp_source_add.setText("");
        adapter1 = new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item);
        adapter2 = new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item);
        adapter1.clear();
        autoComp_source_add.setAdapter(adapter1);
        autoComp_dest_add.setAdapter(adapter2);
        autoComp_source_add.setOnItemClickListener(this);
//        autoComp_source_add.setThreshold(1);
        autoComp_dest_add.setOnItemClickListener(this);
        adapter1.setNotifyOnChange(true);
        Country_Code = getActivity().getResources().getConfiguration().locale.getCountry();
        Country_name = getActivity().getResources().getConfiguration().locale.getDisplayCountry();

        NumberKeyListener inputListener = new NumberKeyListener() {

            public int getInputType() {
                return InputType.TYPE_MASK_VARIATION;
            }

            @Override
            protected char[] getAcceptedChars() {
                return new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '#', ',', ' '};
            }
        };
        autoComp_source_add.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                input_field = "source";
                autoComp_source_add.setError(null);
                if (s.toString().replace("\n", "").length() == 0) {
                    autoComp_source_add.setHint("Source address");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                input_field = "source";
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input_field = "source";
            }
        });
        autoComp_dest_add.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                input_field = "dest";

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                input_field = "dest";
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input_field = "dest";
            }
        });
        rlreportemergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent emailIntent = new Intent(Intent.ACTION_SEND);
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, "ashok@gmail.com");
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Report Emergency.");
//                emailIntent.setType("text/plain");
//                emailIntent.putExtra(Intent.EXTRA_TEXT,"Your contact Mr. Ashok Kumar has reported an emergency while using the Freelift app. The current location is");
//                final PackageManager pm = getActivity().getPackageManager();
//                final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
//                ResolveInfo best = null;
//                for(final ResolveInfo info : matches)
//                    if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
//                        best = info;
//                if (best != null)
//                    emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
//                startActivity(emailIntent);
                emg_report();
            }
        });
        autoComp_source_add.setKeyListener(inputListener);
        autoComp_dest_add.setKeyListener(inputListener);
        rl_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Source_lat = "";
                Dest_lat = "";
                s_latitude = "";
                s_longitude = "";
                d_latitude = "";
                d_longitude = "";

                Source_add = autoComp_source_add.getText().toString();
                Dest_add = autoComp_dest_add.getText().toString();
                if (Source_add.equalsIgnoreCase("")) {
                    autoComp_source_add.requestFocus();
                    autoComp_source_add.setText("");
                    autoComp_source_add.setError("Please fill source address");
                } else {
                    if (Dest_add.equalsIgnoreCase("")) {
                        autoComp_dest_add.requestFocus();
                        autoComp_dest_add.setText("");
                        autoComp_dest_add.setError("Please fill destination address");
                    } else {
                        if (Source_add.trim().equalsIgnoreCase(Dest_add.trim())) {
                            autoComp_dest_add.requestFocus();
                            autoComp_dest_add.setText("");
                            autoComp_dest_add.setError("Please fill different destination from source");
                        } else {
                            if (ConnectionDetector.getInstance().isConnectingToInternet()) {


                                new ProgreshCheck().execute();
                            } else {
                                Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                }
            }
        });
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
            new Progress().execute();
//            if (location != null) {
//                mLatitude = location.getLatitude();
//                mLongitude = location.getLongitude();
//                latLng = new LatLng(mLatitude, mLongitude);
////                getCompleteAddressString(mLatitude,mLongitude);
//                markerOptions.title("Your current Location:");
//                currentAddress = getCompleteAddressString(mLatitude, mLongitude);
//                markerOptions.snippet(currentAddress);
//                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
//                markerOptions.position(latLng);
//                mGoogleMap.addMarker(markerOptions);
//                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
//                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11), 1500, null);
//                onLocationChanged(location);
//                autoComp_source_add.setText(strAdd);
////                autoComp_source_add.postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
//////                        autoComp_source_add.showDropDown();
////                    }
////                },500);
////                autoComp_source_add.setText(strAdd);
////                autoComp_source_add.setSelection(autoComp_source_add.getText().length());
//                postSourceData();
//            }
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

    public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder
                    .getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                android.location.Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                            "\n");
                }
                strAdd = strReturnedAddress.toString();
                Country_Code = addresses.get(0).getCountryCode();
//                Log.w("My Current loction address","" + strReturnedAddress.toString());
            } else {
//                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
//            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        strAdd = (String) adapterView.getItemAtPosition(position);
//        Toast.makeText(getActivity(), strAdd, Toast.LENGTH_SHORT).show();
        if (input_field.equalsIgnoreCase("source")) {
            postSourceData();
        }

    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:" + Country_Code);
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        autoComp_source_add.setText(strAdd);
    }

    public void postSourceData() {
        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_PASSENGER_SEARCH_TAXI, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray successArray = response.getJSONArray("results");

                            arrayLength = successArray.length();
                            Ac_driver_Ids = "";
                            NonAc_driver_Ids = "";
                            if (arrayLength > 0) {
                                Ac_driver_Id = new String[arrayLength];
                                Ac_driver_latitude = new String[arrayLength];
                                Ac_driver_longitude = new String[arrayLength];

                                NonAc_driver_Id = new String[arrayLength];
                                NonAc_driver_latitude = new String[arrayLength];
                                NonAc_driver_longitude = new String[arrayLength];

                                driver_latitude = new String[arrayLength];
                                driver_longitude = new String[arrayLength];
                                for (int i = 0; i < successArray.length(); i++) {

                                    JSONObject driverListObj = (JSONObject) successArray.get(i);
                                    if (driverListObj.has("taxi_type")) {
                                        taxi_type = driverListObj.getString("taxi_type");
                                    }
                                    if (taxi_type.equalsIgnoreCase("0")) {
                                        if (driverListObj.has("longitude")) {
                                            NonAc_driver_longitude[i] = driverListObj.getString("longitude");
                                            driver_longitude[i] = driverListObj.getString("longitude");
                                        }
                                        if (driverListObj.has("latitude")) {
                                            NonAc_driver_latitude[i] = driverListObj.getString("latitude");
                                            driver_latitude[i] = driverListObj.getString("latitude");
                                        }
                                        if (driverListObj.has("driver_id")) {
                                            NonAc_driver_Id[i] = driverListObj.getString("driver_id");
                                            NonAc_driver_Ids = NonAc_driver_Ids + "," + driverListObj.getString("driver_id");
                                        }
                                    } else if (taxi_type.equalsIgnoreCase("1")) {
                                        if (driverListObj.has("longitude")) {
                                            Ac_driver_longitude[i] = driverListObj.getString("longitude");
                                            driver_longitude[i] = driverListObj.getString("longitude");
                                        }
                                        if (driverListObj.has("latitude")) {
                                            Ac_driver_latitude[i] = driverListObj.getString("latitude");
                                            driver_latitude[i] = driverListObj.getString("latitude");
                                        }
                                        if (driverListObj.has("driver_id")) {
                                            Ac_driver_Id[i] = driverListObj.getString("driver_id");
                                            Ac_driver_Ids = Ac_driver_Ids + "," + driverListObj.getString("driver_id");
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {
                            loadingComplete();
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
                Map<String, String> params = new HashMap<String, String>();
                Geocoder geoCoder1 = new Geocoder(getActivity());
                List<Address> pickAddress;
                try {
                    String p = strAdd;
                    p = p.replace("\n", " ");
                    if (p.length() > 0) {
                        pickAddress = geoCoder1.getFromLocationName(p, 1);
                        if (pickAddress.size() > 0) {
                            s_latitude = String.valueOf(pickAddress.get(0).getLatitude());
                            s_longitude = String.valueOf(pickAddress.get(0).getLongitude());
                        }
                    }
                } catch (IOException e) {

                }
                params.put("source_lat", s_latitude);
                params.put("source_long", s_longitude);
                params.put("login_id", Long.toString(sharedpreferences.getLong("customer_driver_id", 0)));
                return params;
            }
        };

        ConnectionDetector.getInstance().addToRequestQueue(jsonReq);
    }

    public void loadingComplete() {
//        Toast.makeText(getActivity(), "" + arrayLength, Toast.LENGTH_LONG).show();
        if (arrayLength > 0) {
            mGoogleMap.clear();
            txt_message.setVisibility(View.GONE);
            for (int i = 0; i < arrayLength; i++) {
                if ((!driver_latitude[i].equalsIgnoreCase("")) && (!driver_longitude[i].equalsIgnoreCase(""))) {
                    lat = Double.parseDouble(driver_latitude[i]);
                    lng = Double.parseDouble(driver_longitude[i]);
                    LatLng latLng = new LatLng(lat, lng);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                    markerOptions.position(latLng);
                    Marker m = mGoogleMap.addMarker(markerOptions);
                    if (i == 0) {
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11), 1500, null);
                    }
                }
            }
        } else {
            txt_message.setVisibility(View.GONE);
        }
    }

    public void bookCab() {
        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_PASSENGER_BOOK_TAXI, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray successArray = response.getJSONArray("success");

                            arrayLength = successArray.length();

                            if (arrayLength > 0) {

                                for (int i = 0; i < successArray.length(); i++) {

                                    JSONObject registerObj = (JSONObject) successArray.get(i);
                                    if (registerObj.has("msg")) {
                                    }
                                }
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {
//                            loadingComplete();
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
                booking_date = df.format(Calendar.getInstance().getTime());

                Map<String, String> params = new HashMap<String, String>();
                Geocoder geoCoder1 = new Geocoder(getActivity());
                List<Address> pickAddress;
                try {
                    String p = autoComp_source_add.getText().toString();
                    p = p.replace("\n", " ");
                    if (p.length() > 0) {
                        pickAddress = geoCoder1.getFromLocationName(p, 1);
                        if (pickAddress.size() > 0) {
                            s_latitude = String.valueOf(pickAddress.get(0).getLatitude());
                            s_longitude = String.valueOf(pickAddress.get(0).getLongitude());
                            Source_lat = s_latitude;
                        }
                    }
                } catch (IOException e) {

                }
                Geocoder geoCoder2 = new Geocoder(getActivity());
                List<Address> dropAddress;
                try {
                    String p = autoComp_dest_add.getText().toString();
                    p = p.replace("\n", " ");
                    if (p.length() > 0) {
                        dropAddress = geoCoder2.getFromLocationName(p, 1);
                        if (dropAddress.size() > 0) {
                            d_latitude = String.valueOf(dropAddress.get(0).getLatitude());
                            d_longitude = String.valueOf(dropAddress.get(0).getLongitude());
                            Dest_lat = d_latitude;
                        } else {
                            s_latitude = "";
                            s_longitude = "";

                        }
                    }
                } catch (IOException e) {

                }
                params.put("source_address", autoComp_source_add.getText().toString().replace("\n", " "));
                params.put("destination_address", autoComp_dest_add.getText().toString().replace("\n", " "));
                params.put("src_lat", s_latitude);
                params.put("src_long", s_longitude);
                params.put("dest_lat", d_latitude);
                params.put("dest_long", d_longitude);
                params.put("login_id", Long.toString(sharedpreferences.getLong("customer_driver_id", 0)));
                params.put("taxi_type", Taxi_Type);
                params.put("booking_date", booking_date);

                return params;
            }
        };

        ConnectionDetector.getInstance().addToRequestQueue(jsonReq);
    }

    public static String getCountryName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getCountryName();
            }
            return null;
        } catch (IOException ignored) {
            //do something
        }
        return null;
    }


    public void emg_report() {
        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_EMERGENCY_REPORT, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray successArray = response.getJSONArray("success");

                            arrayLength = successArray.length();

                            if (arrayLength > 0) {

                                for (int i = 0; i < successArray.length(); i++) {

                                    JSONObject registerObj = (JSONObject) successArray.get(i);
                                    if (registerObj.has("msg")) {
                                    }
                                }
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {
                            Toast.makeText(getActivity(), "Email successfully sent", Toast.LENGTH_LONG).show();
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

                Map<String, String> params = new HashMap<String, String>();
                params.put("address", currentAddress.replace("\n", " "));
                params.put("login_id", Long.toString(sharedpreferences.getLong("customer_driver_id", 0)));

                return params;
            }
        };

        ConnectionDetector.getInstance().addToRequestQueue(jsonReq);
    }

    public class ProgreshCheck extends AsyncTask<String, String, String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(getActivity());
            pdia.setMessage("Loading...");
            pdia.show();
            pdia.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
//            Geocoder geoCoder1 = new Geocoder(getActivity());
//            List<Address> pickAddress;
//            try {
//                String p = Source_add;
//                p = p.replace("\n", " ");
//                if (p.length() > 0) {
//
////                    GeocodingLocation locationAddress = new GeocodingLocation();
////                    locationAddress.getAddressFromLocation(p,
////                           getActivity(), new GeocoderHandler());
//
//
//
//                    pickAddress = geoCoder1.getFromLocationName(p, 1);
//                    if (pickAddress.size() > 0) {
//                        s_latitude = String.valueOf(pickAddress.get(0).getLatitude());
//                        s_longitude = String.valueOf(pickAddress.get(0).getLongitude());
//                        Source_lat = s_latitude;
//                        connectionweek = false;
//                    } else {
//                        connectionweek = false;
//                    }
//                }
//            } catch (IOException e) {
//                connectionweek = true;
//
//            }
//            Geocoder geoCoder2 = new Geocoder(getActivity());
//            List<Address> dropAddress;
//            try {
//                String p = Dest_add;
//                p = p.replace("\n", " ");
//                if (p.length() > 0) {
//                    dropAddress = geoCoder2.getFromLocationName(p, 1);
//                    if (dropAddress.size() > 0) {
//                        d_latitude = String.valueOf(dropAddress.get(0).getLatitude());
//                        d_longitude = String.valueOf(dropAddress.get(0).getLongitude());
//                        Dest_lat = d_latitude;
//                        connectionweek = false;
//                    } else {
//                        connectionweek = false;
//                    }
//                }
//            } catch (IOException e) {
//
//                connectionweek = true;
//
//            }
            JSONParser jParser = new JSONParser();
            JSONObject[] json = new JSONObject[2];
            json[0] = jParser.getLocationInfo(URLEncoder.encode(Source_add));
            json[1] = jParser.getLocationInfo(URLEncoder.encode(Dest_add));
            getSourceLatLong(json[0]);
            getDestLatLong(json[1]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pdia.dismiss();
            pdia = null;

            if ((Source_lat.equalsIgnoreCase("")) || (Dest_lat.equalsIgnoreCase(""))) {
                if (Source_lat.equalsIgnoreCase("")) {
//                    if (connectionweek) {
//                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//                        LayoutInflater inflaterr = getActivity().getLayoutInflater();
//                        View convertView = (View) inflaterr.inflate(R.layout.rides_popup, null);
//                        rl_close = (Button) convertView.findViewById(R.id.btn_close);
//                        txtmessage = (TextView) convertView.findViewById(R.id.txtmessage);
//                        txttitle = (TextView) convertView.findViewById(R.id.txttitle);
//                        txttitle.setText("Warning!");
//                        txtmessage.setText("Check your internet connection / Weak connection.");
//                        rl_close.setText("OK");
//                        alertDialog.setView(convertView);
//                        rl_close.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                mDialog.cancel();
////                        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////                        startActivity(gpsOptionsIntent);
//                            }
//                        });
//                        mDialog = alertDialog.show();
//                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflaterr = getActivity().getLayoutInflater();
                        View convertView = (View) inflaterr.inflate(R.layout.rides_popup, null);
                        rl_close = (Button) convertView.findViewById(R.id.btn_close);
                        txtmessage = (TextView) convertView.findViewById(R.id.txtmessage);
                        txttitle = (TextView) convertView.findViewById(R.id.txttitle);
                        txttitle.setText("Message");
                        txtmessage.setText("Please fill correct source address.");
                        rl_close.setText("OK");
                        alertDialog.setView(convertView);
                        rl_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.cancel();
//                        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivity(gpsOptionsIntent);
                            }
                        });
                        mDialog = alertDialog.show();
//                    }
                } else if (Dest_lat.equalsIgnoreCase("")) {
//                    if (connectionweek) {
//                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//                        LayoutInflater inflaterr = getActivity().getLayoutInflater();
//                        View convertView = (View) inflaterr.inflate(R.layout.rides_popup, null);
//                        rl_close = (Button) convertView.findViewById(R.id.btn_close);
//                        txtmessage = (TextView) convertView.findViewById(R.id.txtmessage);
//                        txttitle = (TextView) convertView.findViewById(R.id.txttitle);
//                        txttitle.setText("Warning!");
//                        txtmessage.setText("Check your internet connection / Weak connection.");
//                        rl_close.setText("OK");
//                        alertDialog.setView(convertView);
//                        rl_close.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                mDialog.cancel();
////                        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////                        startActivity(gpsOptionsIntent);
//                            }
//                        });
//                        mDialog = alertDialog.show();
//                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflaterr = getActivity().getLayoutInflater();
                        View convertView = (View) inflaterr.inflate(R.layout.rides_popup, null);
                        rl_close = (Button) convertView.findViewById(R.id.btn_close);
                        txtmessage = (TextView) convertView.findViewById(R.id.txtmessage);
                        txttitle = (TextView) convertView.findViewById(R.id.txttitle);
                        txttitle.setText("Message");
                        txtmessage.setText("Please fill correct destination address.");
                        rl_close.setText("OK");
                        alertDialog.setView(convertView);
                        rl_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.cancel();
//                        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivity(gpsOptionsIntent);
                            }
                        });
                        mDialog = alertDialog.show();
                    }
//                }
            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.booking_confirmation, null);
                txtstart_point = (TextView) convertView.findViewById(R.id.txt_startpoint);
                txtdest_po_point = (TextView) convertView.findViewById(R.id.txt_destpoint);
                rl_confirm = (Button) convertView.findViewById(R.id.btn_confirm);
                rl_cancel = (Button) convertView.findViewById(R.id.btn_cancel);
                txtstart_point.setText("Start Point: " + Source_add.replace("\n", " "));
                txtdest_po_point.setText("Destination Point: " + Dest_add);
                alertDialog.setView(convertView);
                alertDialog.setCancelable(false);
                rl_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.cancel();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View convertView = (View) inflater.inflate(R.layout.rides_popup, null);
                        rl_close = (Button) convertView.findViewById(R.id.btn_close);
                        alertDialog.setView(convertView);
                        alertDialog.setCancelable(false);
                        rl_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.cancel();
                            }
                        });
                        mDialog = alertDialog.show();
                        bookCab();
                    }
                });
                rl_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.cancel();
                    }
                });
                mDialog = alertDialog.show();
            }
        }
    }

    public class Progress extends AsyncTask<String, String, String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(getActivity());
            pdia.setMessage("Loading...");
            pdia.show();
            pdia.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            if (location != null) {
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
                latLng = new LatLng(mLatitude, mLongitude);
//                getCompleteAddressString(mLatitude,mLongitude);
                markerOptions.title("Your current Location:");
                currentAddress = getCompleteAddressString(mLatitude, mLongitude);
                markerOptions.snippet(currentAddress);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                markerOptions.position(latLng);
//                mGoogleMap.addMarker(markerOptions);
//                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
//                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11), 1500, null);
//                onLocationChanged(location);

//                autoComp_source_add.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
////                        autoComp_source_add.showDropDown();
//                    }
//                },500);
//                autoComp_source_add.setText(strAdd);
//                autoComp_source_add.setSelection(autoComp_source_add.getText().length());
                postSourceData();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pdia.dismiss();
            pdia = null;
            mGoogleMap.addMarker(markerOptions);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11), 1500, null);
            onLocationChanged(location);
            autoComp_source_add.setText(strAdd);

        }
    }

    public boolean getSourceLatLong(JSONObject jsonObject) {

        try {

            double longitute = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");
            s_longitude = String.valueOf(longitute);
            double latitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");
            s_latitude = String.valueOf(latitude);
            Source_lat = s_latitude;
        } catch (JSONException e) {
            return false;

        }

        return true;
    }

    public boolean getDestLatLong(JSONObject jsonObject) {

        try {

            double longitute = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");
            d_longitude = String.valueOf(longitute);
            double latitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");
            d_latitude = String.valueOf(latitude);
            Dest_lat = d_latitude;
        } catch (JSONException e) {
            return false;

        }

        return true;
    }
}