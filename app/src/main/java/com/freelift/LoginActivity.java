package com.freelift;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Iterator;

public class LoginActivity extends ActivityBase implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    CheckBox login_type;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    EditText edt_email, edt_pass;
    String Email, Pass, DeviceId, GcmId, Login_Type = "driver";
    Button btnsubmit;
    //    String emailPattern = "[a-zA-Z0-9.]+@[a-z]+\\.+[a-z]+";
    private static final String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    //    String Message;
    int arrayLength;
    TextView txtmessage;
    String username, useremail, user_profileImage = "";
    String responce_key, Message = "";
    private SignInButton btnSignIn;
    GoogleApiClient mGoogleApiClient;
    final int RC_SIGN_IN = 9001;
    String googleplushEmail = "";
    RelativeLayout rlgplush;
    String refreshedToken = "";

    private static final long MIN_TIME_BW_UPDATES = 5;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 20;
    public static Double mLatitude = 0.0, mLongitude = 0.0;
    public static Location location = null;
    AlertDialog mDialog;
    Button rl_close, rl_submit;
    EditText edt_forget_email;
    TextView txttitle, txtmessages;
    //=================facebook
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private ProgressDialog progressDialog;
    RelativeLayout rlfb;
    String facebookEmail = "";
    String url = "", forgot_url = "";
String Forgot_Email="";
    boolean Social = false;
    TextView txtforgotpass;
    String forgot_message = "";
RadioButton radio_customer,radio_driver;
    RadioGroup radiogroup;
    Long customer_driver_login_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        ConnectionDetector.getInstance().setGcmToken(refreshedToken);


        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("gcm_regId", FirebaseInstanceId.getInstance().getToken());
        editor.commit();
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


        edt_email = (EditText) findViewById(R.id.edtemail);
        edt_pass = (EditText) findViewById(R.id.edtpass);
        btnsubmit = (Button) findViewById(R.id.btnsubmit);
        txtmessage = (TextView) findViewById(R.id.txtmessage);
        btnsubmit.setOnClickListener(this);
        login_type = (CheckBox) findViewById(R.id.login_type);
        rlgplush = (RelativeLayout) findViewById(R.id.rlgplush);
        txtforgotpass = (TextView) findViewById(R.id.txtforgetpass);
        radio_customer=(RadioButton)findViewById(R.id.radio_customer);
        radio_driver=(RadioButton)findViewById(R.id.radio_driver);
        radiogroup=(RadioGroup)findViewById(R.id.radiogroup);
        rlgplush.setOnClickListener(this);
        txtforgotpass.setOnClickListener(this);
//        login_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                Toast.makeText(CategoryandSetting.this, "" + isChecked, Toast.LENGTH_LONG).show();
////                edt_email.setText("");
////                edt_pass.setText("");
//                if (isChecked) {
//                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                    editor.putString("login_type", "driver");
//                    editor.commit();
//                    Login_Type = "driver";
//                } else {
//                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                    editor.putString("login_type", "customer");
//                    editor.commit();
//                    Login_Type = "customer";
//                }
//            }
//        });
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio_customer) {
                    Login_Type = "customer";
//                    Toast.makeText(getApplicationContext(), "customer",
//                            Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.radio_driver) {
                    Login_Type = "driver";
//                    Toast.makeText(getApplicationContext(), "driver",
//                            Toast.LENGTH_SHORT).show();
                }

            }
        });
        edt_email.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edt_email.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        edt_pass.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edt_pass.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                LayoutInflater inflaterr = LoginActivity.this.getLayoutInflater();
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
                                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
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
            case R.id.txtforgetpass:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.forgotpassword, null);
                rl_close = (Button) convertView.findViewById(R.id.btn_cancel);
                rl_submit = (Button) convertView.findViewById(R.id.btn_confirm);
                edt_forget_email = (EditText) convertView.findViewById(R.id.edt_forgot_email);
                alertDialog.setView(convertView);
                rl_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.cancel();
                    }
                });
                rl_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Forgot_Email=edt_forget_email.getText().toString();
                        if (Forgot_Email.equalsIgnoreCase("")) {
                            edt_forget_email.requestFocus();
                            edt_forget_email.setText("");
                            edt_forget_email.setError("Please fill email");
                        } else {
                            if (!Forgot_Email.matches(emailPattern)) {
                                edt_forget_email.requestFocus();
                                edt_forget_email.setText("");
                                edt_forget_email.setError("Invalid email id.");
                            }
                            else
                            {
                                mDialog.cancel();
                                forgot_url = "&email="+Forgot_Email+"&reset_as=" + URLEncoder.encode(Login_Type);
                                new ForgotPassword().execute();
                            }
                        }
                    }
                });
                edt_forget_email.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        // put the code of save Database here
                        edt_forget_email.setError(null);
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });
                mDialog = alertDialog.show();
                break;
            case R.id.btnsubmit:
                txtmessage.setVisibility(View.GONE);
                Email = edt_email.getText().toString();
                Pass = edt_pass.getText().toString();

                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    if (Email.equalsIgnoreCase("")) {
                        edt_email.requestFocus();
                        edt_email.setText("");
                        edt_email.setError("Please fill email");
                    } else {
                        if (!Email.matches(emailPattern)) {
                            edt_email.requestFocus();
                            edt_email.setText("");
                            edt_email.setError("Invalid email id.");
                        } else {
                            if (Pass.equalsIgnoreCase("")) {
                                edt_pass.requestFocus();
                                edt_pass.setText("");
                                edt_pass.setError("Please fill password");
                            } else {
                                if (Pass.length() < 6) {
                                    edt_pass.requestFocus();
                                    edt_pass.setText("");
                                    edt_pass.setError("minimum 6 characters");
                                } else {
                                    Social = false;
                                    url = "&email=" + URLEncoder.encode(Email) + "&password=" + URLEncoder.encode(Pass) + "&login_as=" + URLEncoder.encode(Login_Type) + "&gcm_id=" + URLEncoder.encode(ConnectionDetector.getInstance().getGcmToken()) + "&device_id=" + URLEncoder.encode(SplashPage.Androi_Id) + "&latitude=" + URLEncoder.encode(Double.toString(mLatitude)) + "&longitude=" + URLEncoder.encode(Double.toString(mLongitude)) + "&social=0";
//                                    signin();
                                    new LoginProgresh().execute();
                                }
                            }
                        }
                    }
                }
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
            googleplushEmail = acct.getEmail();
        } else {
//            signOut();
        }
        if (!googleplushEmail.equals("")) {
//            edt_email.setText(googleplushEmail);
            signOut();
            Social = true;
            url = "&email=" + URLEncoder.encode(googleplushEmail) + "&login_as=" + URLEncoder.encode(Login_Type) + "&gcm_id=" + URLEncoder.encode(ConnectionDetector.getInstance().getGcmToken()) + "&device_id=" + URLEncoder.encode(SplashPage.Androi_Id) + "&latitude=" + URLEncoder.encode(Double.toString(mLatitude)) + "&longitude=" + URLEncoder.encode(Double.toString(mLongitude)) + "&social=1";
            new LoginProgresh().execute();
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

    public void startService() {
        startService(new Intent(getBaseContext(), MyService.class));
    }

    public void stopService() {
        stopService(new Intent(getBaseContext(), MyService.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        signOut();
        LoginManager.getInstance().logOut();
    }

    public class LoginProgresh extends AsyncTask<String, String, String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(LoginActivity.this);
            pdia.setMessage("Loading...");
            pdia.show();
            pdia.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber      checkuserlogin=1,user_login=emailid,user_pass=password
            JSONObject json = jParser.getJSONFromUrl(METHOD_LOGIN + url);
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
                            if (registerObj.has("email")) {
                                Message = registerObj.getString("email");
                            }
                            if (registerObj.has("password")) {
                                Message = registerObj.getString("password");
                            }
                            customer_driver_login_id = Long.parseLong("0");
                        }
                    }
                } else if (responce_key.equalsIgnoreCase("success")) {
                    JSONArray successArray = json.getJSONArray("success");

                    arrayLength = successArray.length();

                    if (arrayLength > 0) {

                        for (int i = 0; i < successArray.length(); i++) {

                            JSONObject registerObj = (JSONObject) successArray.get(i);
                            if (registerObj.has("customer_id")) {
                                customer_driver_login_id = Long.parseLong(registerObj.getString("customer_id"));
                            }
                            if (registerObj.has("name")) {
                                username = registerObj.getString("name");
                            }
                            if (registerObj.has("email")) {
                                useremail = registerObj.getString("email");
                            }
                            if (registerObj.has("image")) {
                                user_profileImage = registerObj.getString("image");
                            }
                            if (registerObj.has("login")) {
                                if (registerObj.getString("login").equalsIgnoreCase("true")) {

                                } else {
                                    if (Login_Type.equalsIgnoreCase("driver")) {
                                        Message = "Invalid login details.";
                                        customer_driver_login_id = Long.parseLong("0");
                                    } else {
                                        Message = "Invalid login details or Account inactive.";
                                        customer_driver_login_id = Long.parseLong("0");
                                    }

                                }
                            }


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
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putLong("customer_driver_id", customer_driver_login_id);
                editor.putString("login_as", Login_Type);
                editor.putString("username", username);
                editor.putString("useremail", useremail);
                editor.putString("userpic", user_profileImage);
                editor.commit();
                if (Login_Type.equalsIgnoreCase("driver")) {
                    Intent in = new Intent(LoginActivity.this, DriverNavigation.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);
                    startService();
                    LoginActivity.this.finish();
                } else {
                    Intent in = new Intent(LoginActivity.this, PassengerNavigation.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);
                    stopService();
                    LoginActivity.this.finish();
                }
            } else {
                if (Social) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.invalid_credential, null);
                    rl_close = (Button) convertView.findViewById(R.id.btn_close);
                    alertDialog.setView(convertView);
                    rl_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.cancel();
                        }
                    });
                    mDialog = alertDialog.show();
                } else {
                    txtmessage.setText(Message);
                    txtmessage.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    public class ForgotPassword extends AsyncTask<String, String, String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(LoginActivity.this);
            pdia.setMessage("Loading...");
            pdia.show();
            pdia.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber      checkuserlogin=1,user_login=emailid,user_pass=password
            JSONObject json = jParser.getJSONFromUrl(METHOD_FORGOT+forgot_url);
            try {
                JSONArray successArray = json.getJSONArray("success");

                arrayLength = successArray.length();

                if (arrayLength > 0) {

                    for (int i = 0; i < successArray.length(); i++) {

                        JSONObject registerObj = (JSONObject) successArray.get(i);

                        if (registerObj.has("msg")) {
                            forgot_message = registerObj.getString("msg");
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
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.invalid_credential, null);
            rl_close = (Button) convertView.findViewById(R.id.btn_close);
            txtmessage = (TextView) convertView.findViewById(R.id.txtmessage);
            txtmessage.setText(forgot_message);
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
                                if (object.has("email")) {
                                    facebookEmail = object.getString("email").toString();
                                }
                                if (!facebookEmail.equals("")) {
//                                    edt_email.setText(facebookEmail);
                                    LoginManager.getInstance().logOut();
                                    Social = true;
                                    url = "&email=" + URLEncoder.encode(facebookEmail) + "&login_as=" + URLEncoder.encode(Login_Type) + "&gcm_id=" + URLEncoder.encode(ConnectionDetector.getInstance().getGcmToken()) + "&device_id=" + URLEncoder.encode(SplashPage.Androi_Id) + "&latitude=" + URLEncoder.encode(Double.toString(mLatitude)) + "&longitude=" + URLEncoder.encode(Double.toString(mLongitude)) + "&social=1";

                                    new LoginProgresh().execute();
                                } else {
                                    LoginManager.getInstance().logOut();
                                    Toast.makeText(LoginActivity.this, "Email id not found,Try other facebook account", Toast.LENGTH_LONG).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,first_name,last_name,email,gender,location");
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

        loginButton.setReadPermissions("public_profile", "email", "user_friends");

        rlfb = (RelativeLayout) findViewById(R.id.rlfb);
        rlfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(LoginActivity.this);
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
}
