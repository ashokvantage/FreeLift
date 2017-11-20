package com.freelift;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;

public class RegisterActivity extends ActivityBase implements AdapterView.OnItemClickListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    //    SwitchCompat register_type;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    RelativeLayout rl_register;
    EditText edtfname, edtlname, edtphonenumber, edtemailid, edtpass, edtemg_email1, edt_driving_licence, edt_vehicle_no;
    CheckBox checkbox;
    TextView edt_c_code;
    String Fname, Lname, PhoneNumber, EmailId, Pass, Complete_address, Emg_email1, Register_Type = "driver", DeviceId, Driving_Licence, Vehicle_no, Taxt_Type = "0";
    //    String emailPattern = "[a-zA-Z0-9.]+@[a-z]+\\.+[a-z]+";
    private static final String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    boolean tc_accept = false;
    int arrayLength;
    ListView country_lv;
    String Message = "Internet connection problem.Please try again";
    String responce_key;
    String username, useremail;
    RadioGroup radiogroup_registerType;//radiogroup
//    RadioButton rbac, rbnonac;
    AutoCompleteTextView autoComplete_address;
    GooglePlacesAutocompleteAdapter adapter1;
    private static final String LOG_TAG = "FreeLift";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    //------------ make your specific key ------------
//    private static final String API_KEY = "AIzaSyBVcKaeFrAXk9W9OoFCAZXXZYspNdiHJwo";
    private static final String API_KEY = "AIzaSyCZMx4SBP7Nec9jM39uYWYVnEeAMQ1ly3Y";
    CheckBox register_type;
    AlertDialog mDialog;
    Button rl_close;
    TextView txt_title, txt_message;
    private SignInButton btnSignIn;
    GoogleApiClient mGoogleApiClient;
    final int RC_SIGN_IN = 9001;
    String googleplushId = "", googleplushFName = "", googleplushLName = "", googleplushEmail = "";
    RelativeLayout rlgplush;
    /// facebook
    String refreshedToken = "";

    private static final long MIN_TIME_BW_UPDATES = 5;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    public static Double mLatitude = 0.0, mLongitude = 0.0;
    public static Location location = null;
    TextView txttitle, txtmessages;
    String url = "";
    //=================facebook
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private ProgressDialog progressDialog;
    RelativeLayout rlfb;
    String facebookId = "", facebookFName = "", facebookLName = "", facebookEmail = "", facebookGender = "";

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask, selectedPhoto;
    Country_CustomAdapter adapter;
    ImageView img_licence, img_vehicle_no;
    String selectphoto = "";
    private Bitmap bitmap_licence, bitmap_vehicle;
    ArrayList<Holder> list = new ArrayList<Holder>();
    String register_date="";
    Long customer_driver_login_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.register);
        getKeyHash();
//        if (android.os.Build.VERSION.SDK_INT >= 21) {
//            Window window = this.getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
//        }
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        register_type = (SwitchCompat) findViewById(R.id.registration_type);
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        ConnectionDetector.getInstance().setGcmToken(refreshedToken);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
//                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .build();

        btnSignIn = (SignInButton) findViewById(R.id.sign_in_button);
        btnSignIn.setScopes(gso.getScopeArray());

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btnSignIn.setOnClickListener(this);
//        loginButton = (LoginButton) findViewById(R.id.login_button);
//
//        loginButton.setReadPermissions("user_friends, email");
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("gcm_regId", FirebaseInstanceId.getInstance().getToken());
        editor.commit();
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        ConnectionDetector.getInstance().setGcmToken(refreshedToken);

        register_type = (CheckBox) findViewById(R.id.registration_type);
        rl_register = (RelativeLayout) findViewById(R.id.rl_register);
        edtfname = (EditText) findViewById(R.id.edtfname);
        edtlname = (EditText) findViewById(R.id.edtlname);
        edtphonenumber = (EditText) findViewById(R.id.edtphone);
        edtemailid = (EditText) findViewById(R.id.edtemail);
        edtpass = (EditText) findViewById(R.id.edtpass);
        autoComplete_address = (AutoCompleteTextView) findViewById(R.id.autoComplete_address);
        edtemg_email1 = (EditText) findViewById(R.id.edt_emg_email1);
        edt_driving_licence = (EditText) findViewById(R.id.edt_driving_licence);
        edt_vehicle_no = (EditText) findViewById(R.id.edt_vehicle_no);
        edt_c_code=(TextView) findViewById(R.id.edt_c_code);
//        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
//        rbac = (RadioButton) findViewById(R.id.rb_ac);
//        rbnonac = (RadioButton) findViewById(R.id.rbnonac);
        rlgplush = (RelativeLayout) findViewById(R.id.rlgplush);
        rlgplush.setOnClickListener(this);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        adapter1 = new GooglePlacesAutocompleteAdapter(RegisterActivity.this, R.layout.list_item);
        autoComplete_address.setAdapter(adapter1);
        autoComplete_address.setOnItemClickListener(this);
        radiogroup_registerType=(RadioGroup)findViewById(R.id.radiogroup_registertype);
        img_licence = (ImageView) findViewById(R.id.img_licence);
        img_vehicle_no = (ImageView) findViewById(R.id.img_vehicle_no);

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                tc_accept = b;
            }
        });
//        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == R.id.rb_ac) {
//                    Taxt_Type = "1";
//                } else if (checkedId == R.id.rbnonac) {
//                    Taxt_Type = "0";
//                }
//            }
//        });
        edt_c_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.taxi_type_list, null);
                alertDialog.setView(convertView);
//                            alertDialog.setTitle("List");
                country_lv = (ListView) convertView.findViewById(R.id.taxilv);
//                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,names);
                adapter = new Country_CustomAdapter(RegisterActivity.this,list);
                country_lv.setAdapter(adapter);
                mDialog = alertDialog.show();
                country_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView c = (TextView) view.findViewById(R.id.txtcode);
                        String code = c.getText().toString();
                        edt_c_code.setText(code);
                        mDialog.dismiss();
                    }
                });
            }
        });
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        img_vehicle_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectphoto = "vehicle";
                selectImage();
            }
        });
        img_licence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectphoto = "licence";
                selectImage();
            }
        });
        rl_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_all_fields();
            }
        });
//        register_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                edtfname.setText("");
////                edtlname.setText("");
////                edtphonenumber.setText("");
////                edtemailid.setText("");
////                edtpass.setText("");
////                edtemg_email1.setText("");
////                edt_driving_licence.setText("");
////                edt_vehicle_no.setText("");
//                if (isChecked) {
//                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                    editor.putString("registration_type", "driver");
//                    editor.commit();
//                    Register_Type = "driver";
//                    edtemg_email1.setVisibility(View.GONE);
//                    edt_driving_licence.setVisibility(View.VISIBLE);
//                    edt_vehicle_no.setVisibility(View.VISIBLE);
//                    radiogroup.setVisibility(View.VISIBLE);
//                } else {
//                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                    editor.putString("registration_type", "customer");
//                    editor.commit();
//                    Register_Type = "customer";
//                    edtemg_email1.setVisibility(View.VISIBLE);
//                    edt_driving_licence.setVisibility(View.GONE);
//                    edt_vehicle_no.setVisibility(View.GONE);
//                    radiogroup.setVisibility(View.GONE);
//                }
//            }
//        });
        radiogroup_registerType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio_customer) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("registration_type", "customer");
                    editor.commit();
                    Register_Type = "customer";
                    edtemg_email1.setVisibility(View.VISIBLE);
                    edt_driving_licence.setVisibility(View.GONE);
                    edt_vehicle_no.setVisibility(View.GONE);
//                    radiogroup.setVisibility(View.GONE);
                } else if(checkedId == R.id.radio_driver) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("registration_type", "driver");
                    editor.commit();
                    Register_Type = "driver";
                    edtemg_email1.setVisibility(View.GONE);
                    edt_driving_licence.setVisibility(View.VISIBLE);
                    edt_vehicle_no.setVisibility(View.VISIBLE);
//                    radiogroup.setVisibility(View.VISIBLE);
                }

            }
        });

        NumberKeyListener inputListener = new NumberKeyListener() {

            public int getInputType() {
                return InputType.TYPE_MASK_VARIATION;
            }

            @Override
            protected char[] getAcceptedChars() {
                return new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','.', '#',',',' '};
            }
        };
        edtfname.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtfname.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        edtlname.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtlname.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        edtphonenumber.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtphonenumber.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        edtemailid.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtemailid.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        edtpass.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtpass.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        autoComplete_address.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                autoComplete_address.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        edtemg_email1.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtemg_email1.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        edt_driving_licence.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edt_driving_licence.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        edt_vehicle_no.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edt_vehicle_no.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        autoComplete_address.setKeyListener(inputListener);
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
                LayoutInflater inflaterr = RegisterActivity.this.getLayoutInflater();
                View convertView = (View) inflaterr.inflate(R.layout.rides_popup, null);
                rl_close = (Button) convertView.findViewById(R.id.btn_close);
                txtmessages = (TextView) convertView.findViewById(R.id.txtmessage);
                txttitle = (TextView) convertView.findViewById(R.id.txttitle);
                txttitle.setText("Message");
                txtmessages.setText("Please enable the GPS.");
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
                                AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
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
        try {
            JSONObject jobject=new JSONObject(SplashPage.country_code);
            JSONArray jsonArray = jobject.getJSONArray("countries");
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObj = (JSONObject) jsonArray.get(i);

                Holder holder = new Holder();
                holder.setCountry_code(jsonObj.getString("calling-code"));
                holder.setCountry_name(jsonObj.getString("name"));
                list.add(holder);
            }
        }
        catch (JSONException e) {

            e.printStackTrace();

        }

    }

    private void check_all_fields() {
        url = "";
        Fname = edtfname.getText().toString();
        Lname = edtlname.getText().toString();
        PhoneNumber = edtphonenumber.getText().toString();
        EmailId = edtemailid.getText().toString();
        Pass = edtpass.getText().toString();
        Complete_address = autoComplete_address.getText().toString();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        register_date= df.format(Calendar.getInstance().getTime());
        if (Register_Type.equalsIgnoreCase("customer")) {
            Emg_email1 = edtemg_email1.getText().toString();
        } else {
            Driving_Licence = edt_driving_licence.getText().toString();
            Vehicle_no = edt_vehicle_no.getText().toString();
        }
        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

            Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

        } else {
            if (Fname.equalsIgnoreCase("")) {
                edtfname.requestFocus();
                edtfname.setText("");
                edtfname.setError("Please fill first name");
            } else {
                if (Lname.equalsIgnoreCase("")) {
                    edtlname.requestFocus();
                    edtlname.setText("");
                    edtlname.setError("Please fill last name");
                } else {
                    if (PhoneNumber.equalsIgnoreCase("")) {
                        edtphonenumber.requestFocus();
                        edtphonenumber.setText("");
                        edtphonenumber.setError("Please fill phone number");
                    } else {
//                        if (PhoneNumber.length() < 10) {
//                            edtphonenumber.requestFocus();
//                            edtphonenumber.setText("");
//                            edtphonenumber.setError("fill correct number");
//                        } else {
                            PhoneNumber =edt_c_code.getText().toString()+" "+PhoneNumber;
                            if (EmailId.equalsIgnoreCase("")) {
                                edtemailid.requestFocus();
                                edtemailid.setText("");
                                edtemailid.setError("Please fill email");
                            } else {
                                if (!EmailId.matches(emailPattern)) {
                                    edtemailid.requestFocus();
                                    edtemailid.setText("");
                                    edtemailid.setError("Invalid email id.");
                                } else {
                                    if (Pass.equalsIgnoreCase("")) {
                                        edtpass.requestFocus();
                                        edtpass.setText("");
                                        edtpass.setError("Please fill password");
                                    } else {
                                        if (Pass.length() < 6) {
                                            edtpass.requestFocus();
                                            edtpass.setText("");
                                            edtpass.setError("minimum 6 characters");
                                        } else {
                                            if (Complete_address.equalsIgnoreCase("")) {
                                                autoComplete_address.requestFocus();
                                                autoComplete_address.setText("");
                                                autoComplete_address.setError("Please fill complete address");
                                            } else {
                                                if (Register_Type.equalsIgnoreCase("customer")) {
                                                    if (Emg_email1.equalsIgnoreCase("")) {
                                                        edtemg_email1.requestFocus();
                                                        edtemg_email1.setText("");
                                                        edtemg_email1.setError("Please fill Emg. email to send email");
                                                    } else {
                                                        if (!Emg_email1.matches(emailPattern)) {
                                                            edtemg_email1.requestFocus();
                                                            edtemg_email1.setText("");
                                                            edtemg_email1.setError("Invalid email id.");
                                                        } else {
                                                            if (EmailId.equalsIgnoreCase(Emg_email1)) {
                                                                edtemg_email1.requestFocus();
                                                                edtemg_email1.setText("");
                                                                edtemg_email1.setError("fill different email id.");
                                                            } else {
                                                                if (tc_accept) {
                                                                    url = "&first_name=" + URLEncoder.encode(Fname) + "&last_name=" + URLEncoder.encode(Lname) + "&mobile=" + URLEncoder.encode(PhoneNumber) + "&email=" + URLEncoder.encode(EmailId) + "&password=" + URLEncoder.encode(Pass) + "&address=" + URLEncoder.encode(Complete_address) + "&register_as=" + URLEncoder.encode(Register_Type) + "&emergency_email_1=" + URLEncoder.encode(Emg_email1) + "&gcm_id=" + URLEncoder.encode(ConnectionDetector.getInstance().getGcmToken()) + "&device_id=" + URLEncoder.encode(SplashPage.Androi_Id) + "&latitude=" + URLEncoder.encode(Double.toString(mLatitude)) + "&longitude=" + URLEncoder.encode(Double.toString(mLongitude))+"&register_date="+URLEncoder.encode(register_date);
//                                                                    register();
                                                                    username = Fname + " " + Lname;
                                                                    useremail = EmailId;
                                                                    new RegisterProgresh().execute();
                                                                } else {
                                                                    Toast.makeText(this, "Plase accept terms & conditions", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    if (Driving_Licence.equalsIgnoreCase("")) {
                                                        edt_driving_licence.requestFocus();
                                                        edt_driving_licence.setText("");
                                                        edt_driving_licence.setError("fill driving licence");
                                                    } else {
                                                        if (Vehicle_no.equalsIgnoreCase("")) {
                                                            edt_vehicle_no.requestFocus();
                                                            edt_vehicle_no.setText("");
                                                            edt_vehicle_no.setError("fill vehicle no.");
                                                        } else {
                                                            if (tc_accept) {
//                                                                url = "&first_name=" +Fname + "&last_name=" +Lname + "&mobile=" +PhoneNumber + "&email=" + EmailId + "&password=" + Pass + "&address=" + Complete_address + "&register_as=" + Register_Type + "&driving_licence=" + Driving_Licence + "&gcm_id=" + ConnectionDetector.getInstance().getGcmToken() + "&device_id=" + SplashPage.Androi_Id + "&latitude=" + Double.toString(mLatitude) + "&longitude=" + Double.toString(mLongitude) + "&vehicle_no=" +Vehicle_no + "&taxi_type=" +Taxt_Type+"&register_date="+register_date;
//
                                                                url = "&first_name=" + URLEncoder.encode(Fname) + "&last_name=" + URLEncoder.encode(Lname) + "&mobile=" + URLEncoder.encode(PhoneNumber) + "&email=" + URLEncoder.encode(EmailId) + "&password=" + URLEncoder.encode(Pass) + "&address=" + URLEncoder.encode(Complete_address) + "&register_as=" + URLEncoder.encode(Register_Type) + "&driving_licence=" + URLEncoder.encode(Driving_Licence) + "&gcm_id=" + URLEncoder.encode(ConnectionDetector.getInstance().getGcmToken()) + "&device_id=" + URLEncoder.encode(SplashPage.Androi_Id) + "&latitude=" + URLEncoder.encode(Double.toString(mLatitude)) + "&longitude=" + URLEncoder.encode(Double.toString(mLongitude)) + "&vehicle_no=" + URLEncoder.encode(Vehicle_no) + "&taxi_type=" + URLEncoder.encode(Taxt_Type)+"&register_date="+URLEncoder.encode(register_date);
////                                                                register();
                                                                username = Fname + " " + Lname;
                                                                useremail = EmailId;
                                                                new RegisterProgresh().execute();
                                                            } else {
                                                                Toast.makeText(this, "Plase accept terms & conditions", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
//                        }
                    }
                }
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public void startService() {
        startService(new Intent(getBaseContext(), MyService.class));
    }

    public void stopService() {
        stopService(new Intent(getBaseContext(), MyService.class));
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

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Complete_address = (String) adapterView.getItemAtPosition(position);
//        Toast.makeText(getActivity(), strAdd, Toast.LENGTH_SHORT).show();
    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
//            sb.append("&components=country:in");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlgplush:
                signIn();
                break;
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
//                        tv_username.setText("");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);

                Uri selectedImage = data.getData();
                try {
                    //Getting the Bitmap from Gallery
//                    bitmap_licence = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    if (selectphoto.equalsIgnoreCase("licence")) {
                        bitmap_licence = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } else if (selectphoto.equalsIgnoreCase("vehicle")) {
                        bitmap_vehicle = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    }


                    // /Setting the Bitmap to ImageView
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                // String selectedPhoto contains the path of selected Image
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                selectedPhoto = cursor.getString(columnIndex);
                cursor.close();

                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    UploadPhoto uploadProfilePhoto = new UploadPhoto();
                    uploadProfilePhoto.execute();
                }
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    UploadPhoto uploadProfilePhoto = new UploadPhoto();
                    uploadProfilePhoto.execute();
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
//                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            googleplushFName = acct.getGivenName();
            googleplushLName = acct.getFamilyName();
            googleplushEmail = acct.getEmail();
            googleplushId = acct.getId();
        } else {
//            signOut();
        }
        if (!googleplushEmail.equals("")) {
            signOut();
            edtfname.setText(googleplushFName);
            edtlname.setText(googleplushLName);
            edtemailid.setText(googleplushEmail);
//                edtpass.setEnabled(false);

        } else {
//            signOut();
            revokeAccess();
        }
//        /
//        /
//        /

    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        signOut();
        LoginManager.getInstance().logOut();
    }

    private void getKeyHash() {
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.freelift", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("Keyhash:",
//                        Base64.encodeToString(md.digest(), Base64.DEFAULT));//XRt1Im/ckYIrqWSc2Ev4kap5I14=
//
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//        } catch (NoSuchAlgorithmException e) {
//        }
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.freelift", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
//                Toast.makeText(RegisterActivity.this, something, Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    public class RegisterProgresh extends AsyncTask<String, String, String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(RegisterActivity.this);
            pdia.setMessage("Loading...");
            pdia.show();
            pdia.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber      checkuserlogin=1,user_login=emailid,user_pass=password
            JSONObject json = jParser.getJSONFromUrl(METHOD_REGISTER + url);
            Log.e("Profile", "Malformed JSON: \"" + json.toString() + "\"");
            try {
                Iterator<String> iter = json.keys();
                while (iter.hasNext()) {
                    responce_key = iter.next();
                }
                if (responce_key.equalsIgnoreCase("error")) {
                    JSONArray successArray = json.getJSONArray("error");

                    arrayLength = successArray.length();

                    if (arrayLength > 0) {

                        for (int i = 0; i < successArray.length(); i++) {

                            JSONObject registerObj = (JSONObject) successArray.get(i);
                            Message = registerObj.getString("email");
                            customer_driver_login_id = Long.parseLong("0");
                        }
                    }
                } else if (responce_key.equalsIgnoreCase("success")) {
                    JSONArray successArray = json.getJSONArray("success");

                    arrayLength = successArray.length();

                    if (arrayLength > 0) {

                        for (int i = 0; i < successArray.length(); i++) {

                            JSONObject registerObj = (JSONObject) successArray.get(i);
                            customer_driver_login_id = Long.parseLong(registerObj.getString("customer_id"));
                            Message = registerObj.getString("msg");
                        }
                    }
                }
            } catch (JSONException e) {

                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pdia.dismiss();
            pdia = null;
            if (customer_driver_login_id != 0) {

                if (Register_Type.equalsIgnoreCase("driver")) {
                    startService();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.rides_popup, null);
                    rl_close = (Button) convertView.findViewById(R.id.btn_close);
                    txt_title = (TextView) convertView.findViewById(R.id.txttitle);
                    txt_message = (TextView) convertView.findViewById(R.id.txtmessage);
                    txt_title.setText("FreeLift");
                    txt_message.setText("Thank you for registering with us.");
                    alertDialog.setView(convertView);
                    rl_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.cancel();
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putLong("customer_driver_id",customer_driver_login_id);
                            editor.putString("login_as", Register_Type);
                            editor.putString("username", username);
                            editor.putString("useremail", useremail);
                            editor.putString("userpic", "");
                            editor.commit();
                            Intent in = new Intent(RegisterActivity.this, DriverNavigation.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(in);
                            RegisterActivity.this.finish();
                        }
                    });
                    mDialog = alertDialog.show();
                } else {
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
//                    LayoutInflater inflater = getLayoutInflater();
//                    View convertView = (View) inflater.inflate(R.layout.rides_popup, null);
//                    rl_close = (Button) convertView.findViewById(R.id.btn_close);
//                    txt_title = (TextView) convertView.findViewById(R.id.txttitle);
//                    txt_message = (TextView) convertView.findViewById(R.id.txtmessage);
//                    txt_title.setText("FreeLift");
//                    txt_message.setText("Thank you for registering with us. A verification email has been send to your email.");
//                    alertDialog.setView(convertView);
//                    rl_close.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mDialog.cancel();
//                            Intent in = new Intent(RegisterActivity.this, LoginActivity.class);
//                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(in);
//                            RegisterActivity.this.finish();
//                        }
//                    });
//                    mDialog = alertDialog.show();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.rides_popup, null);
                    rl_close = (Button) convertView.findViewById(R.id.btn_close);
                    txt_title = (TextView) convertView.findViewById(R.id.txttitle);
                    txt_message = (TextView) convertView.findViewById(R.id.txtmessage);
                    txt_title.setText("FreeLift");
                    txt_message.setText("Thank you for registering with us.");
                    alertDialog.setView(convertView);
                    rl_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.cancel();
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putLong("customer_driver_id",customer_driver_login_id);
                            editor.putString("login_as", Register_Type);
                            editor.putString("username", username);
                            editor.putString("useremail", useremail);
                            editor.putString("userpic", "");
                            editor.commit();
                            Intent in = new Intent(RegisterActivity.this, PassengerNavigation.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(in);
                            RegisterActivity.this.finish();
                        }
                    });
                    mDialog = alertDialog.show();
                    stopService();
                }
//
//                Toast.makeText(RegisterActivity.this, Message, Toast.LENGTH_LONG).show();
            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.rides_popup, null);
                rl_close = (Button) convertView.findViewById(R.id.btn_close);
                txt_title = (TextView) convertView.findViewById(R.id.txttitle);
                txt_message = (TextView) convertView.findViewById(R.id.txtmessage);
                txt_title.setText("Message");
                txt_message.setText(Message);
                alertDialog.setView(convertView);
                rl_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.cancel();
                    }
                });
                mDialog = alertDialog.show();
            }
        }

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

    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            Log.e("response: ", response + "");
                            try {
                                if(object.has("id"))
                                {
                                    facebookId = object.getString("id").toString();
                                }
                                if(object.has("email"))
                                {
                                    facebookEmail = object.getString("email").toString();
                                }
                                if(object.has("first_name"))
                                {
                                    facebookFName = object.getString("first_name").toString();
                                }
                                if(object.has("last_name"))
                                {
                                    facebookLName = object.getString("last_name").toString();
                                }
                                if(object.has("gender"))
                                {
                                    facebookGender = object.getString("gender").toString();
                                }




                                LoginManager.getInstance().logOut();
                                if (!facebookEmail.equals("")) {
                                    edtfname.setText(facebookFName);
                                    edtlname.setText(facebookLName);
                                    edtemailid.setText(facebookEmail);
                                    LoginManager.getInstance().logOut();
                                }
                                else if(!facebookFName.equals(""))
                                {
                                    edtfname.setText(facebookFName);
                                    edtlname.setText(facebookLName);
                                    edtemailid.setText("");
                                    LoginManager.getInstance().logOut();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,first_name,last_name,email,gender");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            progressDialog.dismiss();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();


        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);

//        loginButton.setReadPermissions("public_profile", "email", "user_friends");
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        rlfb = (RelativeLayout) findViewById(R.id.rlfb);
        rlfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                loginButton.performClick();

                loginButton.setPressed(true);

                loginButton.invalidate();

                loginButton.registerCallback(callbackManager, mCallBack);

                loginButton.setPressed(false);

                loginButton.invalidate();

            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(RegisterActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Drawable d = new BitmapDrawable(getResources(), bm);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
        if (selectphoto.equalsIgnoreCase("licence")) {
            img_licence.setBackground(d);
//            img_licence.setLayoutParams(layoutParams);
        } else if (selectphoto.equalsIgnoreCase("vehicle")) {
            img_vehicle_no.setBackground(d);
//            img_vehicle_no.setLayoutParams(layoutParams);
        }

    }

    class UploadPhoto extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
//            showpDialog();
        }

        @Override
        protected String doInBackground(Void... params) {

            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(METHOD_PROFILE_UPLOADPHOTO);

            File sourceFile = new File(selectedPhoto);

            try {

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.setMode(HttpMultipartMode.STRICT);
                FileBody fileBody = new FileBody(sourceFile);
                builder.addPart("uploadPhoto", fileBody);
                builder.addPart("login_id", new StringBody(Long.toString(customer_driver_login_id), ContentType.TEXT_PLAIN));
                builder.addPart("register_as", new StringBody("customer", ContentType.TEXT_PLAIN));
                HttpEntity entity = builder.build();

                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200) {

                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                    JSONObject jobject = new JSONObject(responseString);

                    JSONArray successArray = jobject.getJSONArray("success");

                    arrayLength = successArray.length();

                    if (arrayLength > 0) {

                        for (int i = 0; i < successArray.length(); i++) {

                            JSONObject registerObj = (JSONObject) successArray.get(i);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("userpic", registerObj.getString("image"));
                            editor.commit();
                        }
                    }

                } else {

                    responseString = "Error occurred! Http Status Code: " + statusCode;
                }

            } catch (ClientProtocolException e) {

                responseString = e.toString();

            } catch (IOException e) {

                responseString = e.toString();
            } catch (JSONException e) {

                e.printStackTrace();

            } catch (Exception e) {
                responseString = "Error occurred! Http Status Code:";
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {

//            hidepDialog();
            super.onPreExecute();
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        selectedPhoto = destination.toString();
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Drawable d = new BitmapDrawable(getResources(), thumbnail);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
        if (selectphoto.equalsIgnoreCase("licence")) {
            img_licence.setBackground(d);
//            img_licence.setLayoutParams(layoutParams);
        } else if (selectphoto.equalsIgnoreCase("vehicle")) {
            img_vehicle_no.setBackground(d);
//            img_vehicle_no.setLayoutParams(layoutParams);
        }
    }

}
