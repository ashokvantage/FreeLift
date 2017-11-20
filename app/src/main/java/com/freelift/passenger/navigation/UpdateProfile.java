package com.freelift.passenger.navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.freelift.ConnectionDetector;
import com.freelift.Constants;
import com.freelift.Country_CustomAdapter;
import com.freelift.CustomRequest;
import com.freelift.Holder;
import com.freelift.JSONParser;
import com.freelift.R;
import com.freelift.SplashPage;
import com.freelift.Utility;
import com.pkmmte.view.CircularImageView;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ADMIN on 31-Jan-17.
 */

public class UpdateProfile extends Fragment implements Constants, AdapterView.OnItemClickListener {
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    EditText edtphonenumber, edtemg_email1, edt_driving_licence, edt_vehicle_no,edtpass;
    String Fname, Lname, PhoneNumber, EmailId, Complete_address, Emg_email1, LoginId, Profile_pic_url,Pass="";
    //    String emailPattern = "[a-zA-Z0-9.]+@[a-z]+\\.+[a-z]+";
    private static final String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private ProgressDialog pDialog;
    RelativeLayout rl_save;
    CircularImageView img_pic;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask, selectedPhoto;
    FileBody fileBody;
    int arrayLength;
    RadioGroup radiogroup;
    RadioButton rbac, rbnonac;
    AutoCompleteTextView autoComplete_address;
    GooglePlacesAutocompleteAdapter adapter1;
    private static final String LOG_TAG = "FreeLift";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    //------------ make your specific key ------------
//    private static final String API_KEY = "AIzaSyBVcKaeFrAXk9W9OoFCAZXXZYspNdiHJwo";
    private static final String API_KEY = "AIzaSyCZMx4SBP7Nec9jM39uYWYVnEeAMQ1ly3Y";
    TextView edtfname, edtlname, edtemailid;
    LinearLayout layout_profile;
    String url = "";
    ArrayList<Holder> list = new ArrayList<Holder>();
    TextView edt_c_code;
    ListView country_lv;
    Country_CustomAdapter adapter;
    AlertDialog mDialog;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.updateprofile, container, false);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        edtfname = (TextView) view.findViewById(R.id.edtfname);
        edtlname = (TextView) view.findViewById(R.id.edtlname);
        edtphonenumber = (EditText) view.findViewById(R.id.edtphone);
        edtemailid = (TextView) view.findViewById(R.id.edtemail);
        autoComplete_address = (AutoCompleteTextView) view.findViewById(R.id.autoComplete_address);
        edtemg_email1 = (EditText) view.findViewById(R.id.edt_emg_email1);
        edt_driving_licence = (EditText) view.findViewById(R.id.edt_driving_licence);
        edt_vehicle_no = (EditText) view.findViewById(R.id.edt_vehicle_no);
        radiogroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        rbac = (RadioButton) view.findViewById(R.id.rb_ac);
        rbnonac = (RadioButton) view.findViewById(R.id.rbnonac);
        rl_save = (RelativeLayout) view.findViewById(R.id.rl_save);
        edt_c_code = (TextView) view.findViewById(R.id.edt_c_code);
        img_pic = (CircularImageView) view.findViewById(R.id.img_pic);
        layout_profile = (LinearLayout) view.findViewById(R.id.layout_profile);
        edtpass=(EditText)view.findViewById(R.id.edtpass);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        adapter1 = new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item);
        autoComplete_address.setAdapter(adapter1);
        autoComplete_address.setOnItemClickListener(this);


        edtemg_email1.setVisibility(View.VISIBLE);
        edt_driving_licence.setVisibility(View.GONE);
        edt_vehicle_no.setVisibility(View.GONE);
        radiogroup.setVisibility(View.GONE);


        img_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        rl_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(), "Update Successfully", Toast.LENGTH_LONG).show();
//                Home home = new Home();
//                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.frame, home);
//                ft.commit();
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    check_all_fields();
                }

            }
        });
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
        edtphonenumber.setFilters(new InputFilter[]{ignoreFirstWhiteSpace()});
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
        autoComplete_address.setKeyListener(inputListener);
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
        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

            Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

        } else {
//            getallProfileData();
            url = "&login_id=" + URLEncoder.encode(Long.toString(sharedpreferences.getLong("customer_driver_id", 0))) + "&login_as=customer";
            new GetallProfileData().execute();

        }
        try {
            JSONObject jobject = new JSONObject(SplashPage.country_code);
            JSONArray jsonArray = jobject.getJSONArray("countries");
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObj = (JSONObject) jsonArray.get(i);

                Holder holder = new Holder();
                holder.setCountry_code(jsonObj.getString("calling-code"));
                holder.setCountry_name(jsonObj.getString("name"));
                list.add(holder);
            }
        } catch (JSONException e) {

            e.printStackTrace();

        }
        edt_c_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.taxi_type_list, null);
                alertDialog.setView(convertView);
//                            alertDialog.setTitle("List");
                country_lv = (ListView) convertView.findViewById(R.id.taxilv);
//                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,names);
                adapter = new Country_CustomAdapter(getActivity(), list);
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
        return view;
    }

    // ignore enter First space on edittext
    public InputFilter ignoreFirstWhiteSpace() {
        return new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        if (dstart == 0)
                            return "";
                    }
                }
                return null;
            }
        };
    }

    private void check_all_fields() {
        Fname = edtfname.getText().toString();
        Lname = edtlname.getText().toString();
        PhoneNumber = edtphonenumber.getText().toString();
        EmailId = edtemailid.getText().toString();
        Complete_address = autoComplete_address.getText().toString();
        Emg_email1 = edtemg_email1.getText().toString();
        Pass=edtpass.getText().toString();

        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

            Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

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
                                if (Complete_address.equalsIgnoreCase("")) {
                                    autoComplete_address.requestFocus();
                                    autoComplete_address.setText("");
                                    autoComplete_address.setError("Please fill complete address");
                                } else {
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
                                                update();
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

    public void update() {

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_PROFILE_UPDATE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), "Your profile updated successfully.", Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("username", Fname + " " + Lname);
                        editor.putString("useremail", EmailId);
                        editor.commit();

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "When loading data error has occurred. Check your network connection.", Toast.LENGTH_LONG).show();

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", Fname);
                params.put("last_name", Lname);
                params.put("mobile", edt_c_code.getText().toString() + " " + PhoneNumber);
                params.put("email", EmailId);
                params.put("address", Complete_address);
                params.put("emergency_tel", "");
                params.put("emergency_email_1", Emg_email1);
                params.put("emergency_email_2", "");
                params.put("emergency_email_3", "");
                params.put("login_id", Long.toString(sharedpreferences.getLong("customer_driver_id", 0)));
                params.put("register_as", "customer");
                params.put("update_pass",Pass);

                return params;
            }
        };

        ConnectionDetector.getInstance().addToRequestQueue(jsonReq);
    }

    protected void showpDialog() {

        if (!pDialog.isShowing())
            pDialog.show();
    }

    protected void hidepDialog() {

        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                // String selectedPhoto contains the path of selected Image
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                selectedPhoto = cursor.getString(columnIndex);
                cursor.close();

                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    UploadPhoto uploadProfilePhoto = new UploadPhoto();
                    uploadProfilePhoto.execute();
                }
            } else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

            } else {
                UploadPhoto uploadProfilePhoto = new UploadPhoto();
                uploadProfilePhoto.execute();
            }
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

        img_pic.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        img_pic.setImageBitmap(bm);
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
                builder.addPart("login_id", new StringBody(Long.toString(sharedpreferences.getLong("customer_driver_id", 0)), ContentType.TEXT_PLAIN));
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

    public class GetallProfileData extends AsyncTask<String, String, String> {
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
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber      checkuserlogin=1,user_login=emailid,user_pass=password
            JSONObject json = jParser.getJSONFromUrl(METHOD_PROFILE_DETAILS_GET + url);
            try {
                JSONArray profileArray = json.getJSONArray("success");

                arrayLength = profileArray.length();

                if (arrayLength > 0) {

                    for (int i = 0; i < profileArray.length(); i++) {

                        JSONObject jsonObj = (JSONObject) profileArray.get(i);

                        if (jsonObj.has("firstname")) {
                            Fname = jsonObj.getString("firstname");
                        }
                        if (jsonObj.has("lastname")) {
                            Lname = jsonObj.getString("lastname");
                        }
                        if (jsonObj.has("email")) {
                            EmailId = jsonObj.getString("email");
                        }
                        if (jsonObj.has("address")) {
                            Complete_address = jsonObj.getString("address");
                        }
                        if (jsonObj.has("mobile")) {
                            PhoneNumber = jsonObj.getString("mobile");
                        }
                        if (jsonObj.has("emergency_email_1")) {
                            Emg_email1 = jsonObj.getString("emergency_email_1");
                        }
                        if (jsonObj.has("mobile")) {
                            PhoneNumber = jsonObj.getString("mobile");
                        }
                        if (jsonObj.has("image")) {
                            Profile_pic_url = jsonObj.getString("image");
//                                            img_pic.setText(jsonObj.getString("msg"));
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
            edtfname.setText(Fname);
            edtlname.setText(Lname);
//            edtphonenumber.setText(PhoneNumber.substring(PhoneNumber.length()-10,PhoneNumber.length()));
//            edt_c_code.setText(PhoneNumber.substring(0,PhoneNumber.length()-10));
            if (PhoneNumber.contains(" ")) {
                edt_c_code.setText(PhoneNumber.substring(0, PhoneNumber.indexOf(" ")));
                edtphonenumber.setText(PhoneNumber.substring(PhoneNumber.indexOf(" ") + 1, PhoneNumber.length()));
            } else {
                edtphonenumber.setText(PhoneNumber.substring(PhoneNumber.length() - 10, PhoneNumber.length()));
                edt_c_code.setText(PhoneNumber.substring(0, PhoneNumber.length() - 10));
            }

            edtemailid.setText(EmailId);
            edtemg_email1.setText(Emg_email1);
            autoComplete_address.setText(Complete_address);
            if (!Profile_pic_url.equalsIgnoreCase(""))
                showPhoto(Profile_pic_url);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("username", Fname + " " + Lname);
            editor.putString("useremail", EmailId);
            editor.putString("userpic", Profile_pic_url);
            editor.commit();
            layout_profile.setVisibility(View.VISIBLE);
        }
    }

    public void getallProfileData() {
        showpDialog();
        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_PROFILE_DETAILS_GET, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray profileArray = response.getJSONArray("success");

                            arrayLength = profileArray.length();

                            if (arrayLength > 0) {

                                for (int i = 0; i < profileArray.length(); i++) {

                                    JSONObject jsonObj = (JSONObject) profileArray.get(i);

                                    if (jsonObj.has("firstname")) {
                                        Fname = jsonObj.getString("firstname");
                                    }
                                    if (jsonObj.has("lastname")) {
                                        Lname = jsonObj.getString("lastname");
                                    }
                                    if (jsonObj.has("email")) {
                                        EmailId = jsonObj.getString("email");
                                    }
                                    if (jsonObj.has("address")) {
                                        Complete_address = jsonObj.getString("address");
                                    }
                                    if (jsonObj.has("mobile")) {
                                        PhoneNumber = jsonObj.getString("mobile");
                                    }
                                    if (jsonObj.has("emergency_email_1")) {
                                        Emg_email1 = jsonObj.getString("emergency_email_1");
                                    }
                                    if (jsonObj.has("mobile")) {
                                        PhoneNumber = jsonObj.getString("mobile");
                                    }
                                    if (jsonObj.has("image")) {
                                        Profile_pic_url = jsonObj.getString("image");
//                                            img_pic.setText(jsonObj.getString("msg"));
                                    }

                                }
                            }


                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {
                            hidepDialog();
                            loadingComplete();

//                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("login_id", Long.toString(sharedpreferences.getLong("customer_driver_id", 0)));
                params.put("login_as", "customer");
                return params;
            }
        };

        ConnectionDetector.getInstance().addToRequestQueue(jsonReq);
    }

    public void loadingComplete() {
        edtfname.setText(Fname);
        edtlname.setText(Lname);
        edtphonenumber.setText(PhoneNumber);
        edtemailid.setText(EmailId);
        edtemg_email1.setText(Emg_email1);
        autoComplete_address.setText(Complete_address);
        if (!Profile_pic_url.equalsIgnoreCase(""))
            showPhoto(Profile_pic_url);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("username", Fname + " " + Lname);
        editor.putString("useremail", EmailId);
        editor.putString("userpic", Profile_pic_url);
        editor.commit();
        layout_profile.setVisibility(View.VISIBLE);
    }

    public void showPhoto(String photoUrl) {

        if (photoUrl.length() > 0) {

            ImageLoader imageLoader = ConnectionDetector.getInstance().getImageLoader();

            imageLoader.get(photoUrl, ImageLoader.getImageListener(img_pic, R.drawable.profilepic, R.drawable.profilepic));
        }
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

}
