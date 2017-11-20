package com.freelift.driver.navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.freelift.ConnectionDetector;
import com.freelift.Constants;
import com.freelift.CustomRequest;
import com.freelift.Holder;
import com.freelift.JSONParser;
import com.freelift.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ADMIN on 31-Jan-17.
 */

public class MyRides extends Fragment implements Constants, SwipeRefreshLayout.OnRefreshListener {
    private ProgressDialog pDialog;
    int arrayLength;
    ListView mListView;
    TextView mMessage, txtmessage;
    SwipeRefreshLayout mItemsContainer;
    private MyRidesListAdapter itemsAdapter;

    private Boolean restore = false;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    ArrayList<Holder> list = new ArrayList<Holder>();
    String BookingId = "";
    AlertDialog mDialog;
    EditText edt_cancel_trip;
    Button btn_cancel, btn_confirm;
    String Comment = "", message = "";
    Button btn_close;
    TextView txt_title, txt_message;
    Handler handler;
    Runnable myRunnable;
    Spinner staticSpinner;
    String cancel_reason = "";
    String url = "";

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.driver_myrides, container, false);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

//        handler = new Handler();
//        myRunnable = new Runnable() {
//            @Override
//            public void run() {
//                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {
//
//                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    url = "&login_id=" + URLEncoder.encode(Long.toString(SplashPage.customer_driver_login_id));
//                    new DriverRides().execute();
//                }
//                handler.postDelayed(this, 20 * 1000);
//            }
//        };
//        handler.post(myRunnable);

        mItemsContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.container_items);
        mItemsContainer.setOnRefreshListener(this);
        mMessage = (TextView) rootView.findViewById(R.id.message);
        mListView = (ListView) rootView.findViewById(R.id.listView);
        return rootView;
    }

    public class DriverRides extends AsyncTask<String, String, String> {
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
            JSONObject json = jParser.getJSONFromUrl(METHOD_DRIVER_RIDES + url);
            try {
                JSONArray successArray = json.getJSONArray("success");

                arrayLength = successArray.length();
                list.clear();
                if (arrayLength > 0) {

                    for (int i = 0; i < successArray.length(); i++) {

                        JSONObject myridesObj = (JSONObject) successArray.get(i);

                        Holder holder = new Holder();
                        holder.setSrc_address(myridesObj.getString("source_address"));
                        holder.setDest_address(myridesObj.getString("destination_address"));
                        holder.setSrc_lat(myridesObj.getString("source_latitude"));
                        holder.setSrc_long(myridesObj.getString("source_longitude"));
                        holder.setDest_lat(myridesObj.getString("destination_latitude"));
                        holder.setDest_long(myridesObj.getString("destination_longitude"));
                        holder.setDate_Time(myridesObj.getString("date"));
                        holder.setFirstname(myridesObj.getString("name"));
                        holder.setMobile(myridesObj.getString("mobile"));
                        holder.setEmail(myridesObj.getString("email"));
                        holder.setBookingId(myridesObj.getString("booking_id"));
                        holder.setTaxi_type(myridesObj.getString("taxi"));
                        holder.setStatus(myridesObj.getString("status"));
                        holder.setC_Reason(myridesObj.getString("cancel_reason"));
                        holder.setDate_Time(myridesObj.getString("date"));
                        list.add(holder);
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
            if (list.size() == 0) {
                showMessage("No rides");
                itemsAdapter = new MyRidesListAdapter(getActivity(), list);
                mListView.setAdapter(itemsAdapter);
            } else {
                hideMessage();
                itemsAdapter = new MyRidesListAdapter(getActivity(), list);
                mListView.setAdapter(itemsAdapter);
            }
            mItemsContainer.setRefreshing(false);
        }
    }



    protected void showpDialog() {

        if (!pDialog.isShowing())
            pDialog.show();
    }

    protected void hidepDialog() {

        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    public void listViewItemChange() {

        if (mListView.getCount() == 0) {

            showMessage("No rides");
        }
    }

    @Override
    public void onRefresh() {

        if (ConnectionDetector.getInstance().isConnectingToInternet()) {
//            myrides();
            new DriverRides().execute();

        } else {

            mItemsContainer.setRefreshing(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == QUESTION_ANSWER && resultCode == getActivity().RESULT_OK && null != data) {

//            list.remove(data.getIntExtra("listPosition", -1));
            itemsAdapter.notifyDataSetChanged();

            this.listViewItemChange();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void loadingComplete() {
        if (list.size() == 0) {
            showMessage("No rides");
        } else {
            hideMessage();
            itemsAdapter = new MyRidesListAdapter(getActivity(), list);
            mListView.setAdapter(itemsAdapter);
        }
        mItemsContainer.setRefreshing(false);
    }

    public void showMessage(String message) {

        mMessage.setText(message);
        mMessage.setVisibility(View.VISIBLE);
    }

    public void hideMessage() {

        mMessage.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class MyRidesListAdapter extends BaseAdapter {

        LayoutInflater inflater;
        ArrayList<Holder> list;

        public MyRidesListAdapter(FragmentActivity fragmentActivity, ArrayList<Holder> list) {
            inflater = LayoutInflater.from(fragmentActivity);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int paramInt) {
            return paramInt;
        }

        class ViewHolder {
            TextView source, dest, taxi_type, status, reason,date_time, txtcenter;
            Button btn_trip_details, btn_trip_cancel;
        }

        @Override
        public long getItemId(int paramInt) {
            return paramInt;
        }

        @Override
        public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {

            MyRidesListAdapter.ViewHolder holder;
            if (paramView == null) {
                paramView = inflater.inflate(R.layout.driver_myrides_listitem, paramViewGroup, false);
                holder = new MyRidesListAdapter.ViewHolder();
                holder.source = (TextView) paramView.findViewById(R.id.txtsource);
                holder.dest = (TextView) paramView.findViewById(R.id.txtdest);
                holder.taxi_type = (TextView) paramView.findViewById(R.id.txtride_type);
                holder.status = (TextView) paramView.findViewById(R.id.txtstatus);
                holder.reason = (TextView) paramView.findViewById(R.id.txtreason);
                holder.date_time = (TextView) paramView.findViewById(R.id.date_time);
                holder.txtcenter = (TextView) paramView.findViewById(R.id.txtcenter);
                holder.btn_trip_details = (Button) paramView.findViewById(R.id.btn_trip_details);
                holder.btn_trip_cancel = (Button) paramView.findViewById(R.id.btn_cancel_trip);
                paramView.setTag(holder);
            } else {
                holder = (MyRidesListAdapter.ViewHolder) paramView.getTag();
            }

            Holder h = list.get(paramInt);
            holder.source.setText("Start Point: " + h.getSrc_address().replace("\n", " "));
            holder.dest.setText("Destination Point: " + h.getDest_address());
            holder.taxi_type.setText("Ride Type: " + h.getTaxi_type());
            holder.reason.setText("Reason: " + h.getC_Reason());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
            Date convertedDate = new Date();
            try {
                convertedDate = df.parse(h.getDate_Time());
                String datetime = dateformat.format(convertedDate);
                holder.date_time.setText("Date: " + datetime);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

//            holder.date_time.setText("Date: " + h.getDate_Time());
            if (h.getStatus().equalsIgnoreCase("0")) {
                holder.status.setText("Status: New");
                holder.txtcenter.setVisibility(View.VISIBLE);
                holder.txtcenter.setVisibility(View.VISIBLE);
                holder.btn_trip_cancel.setVisibility(View.VISIBLE);
                holder.reason.setVisibility(View.GONE);
            } else if (h.getStatus().equalsIgnoreCase("1")) {
                holder.status.setText("Status: Cancel");
                holder.txtcenter.setVisibility(View.GONE);
                holder.btn_trip_cancel.setVisibility(View.GONE);
                holder.btn_trip_details.setVisibility(View.VISIBLE);
                holder.reason.setVisibility(View.VISIBLE);
            } else if (h.getStatus().equalsIgnoreCase("2")) {
                holder.status.setText("Status: Ongoing");
                holder.txtcenter.setVisibility(View.VISIBLE);
                holder.btn_trip_cancel.setVisibility(View.VISIBLE);
                holder.btn_trip_details.setVisibility(View.VISIBLE);
                holder.reason.setVisibility(View.GONE);
            } else if (h.getStatus().equalsIgnoreCase("3")) {
                holder.status.setText("Status: Processing");
                holder.txtcenter.setVisibility(View.GONE);
                holder.btn_trip_cancel.setVisibility(View.GONE);
                holder.btn_trip_details.setVisibility(View.VISIBLE);
                holder.reason.setVisibility(View.GONE);
            } else if (h.getStatus().equalsIgnoreCase("4")) {
                holder.status.setText("Status: complete");
                holder.txtcenter.setVisibility(View.GONE);
                holder.btn_trip_cancel.setVisibility(View.GONE);
                holder.btn_trip_details.setVisibility(View.VISIBLE);
                holder.reason.setVisibility(View.GONE);
            }

            holder.btn_trip_details.setTag(paramInt);
            holder.btn_trip_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
                    Holder h1 = (Holder) list.get(pos1);
                    BookingId = h1.getBookingId();

//                    BookingDetailMap fragment = new BookingDetailMap();
//                    Bundle args = new Bundle();
//                    args.putString("source_lat", h1.getSrc_lat());
//                    args.putString("source_lng", h1.getSrc_long());
//                    args.putString("dest_lat", h1.getDest_lat());
//                    args.putString("dest_lng", h1.getDest_long());
//                    args.putString("driver_name", h1.getFirstname());
//                    args.putString("driver_mob", h1.getMobile());
//                    args.putString("booking_id", h1.getBookingId());
//                    args.putString("status", h1.getStatus());
//                    fragment.setArguments(args);
//                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                    ft.add(R.id.frame, fragment);
//                    ft.addToBackStack("add");
//                    ft.commit();
                    Intent in=new Intent(getActivity(),DriverMyRidesDetails.class);
                    in.putExtra("source_lat", h1.getSrc_lat());
                    in.putExtra("source_lng", h1.getSrc_long());
                    in.putExtra("dest_lat", h1.getDest_lat());
                    in.putExtra("dest_lng", h1.getDest_long());
                    in.putExtra("source_add", h1.getSrc_address());
                    in.putExtra("dest_add", h1.getDest_address());
                    in.putExtra("driver_name", h1.getFirstname());
                    in.putExtra("driver_mob", h1.getMobile());
                    in.putExtra("booking_id", h1.getBookingId());
                    in.putExtra("status", h1.getStatus());
                    startActivity(in);




                }
            });
            holder.btn_trip_cancel.setTag(paramInt);
            holder.btn_trip_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
                    Holder h1 = (Holder) list.get(pos1);
                    BookingId = h1.getBookingId();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.trip_cancel_popup, null);
                    btn_confirm = (Button) convertView.findViewById(R.id.btn_confirm);
                    btn_cancel = (Button) convertView.findViewById(R.id.btn_cancel);
                    edt_cancel_trip = (EditText) convertView.findViewById(R.id.edt_reason);
                    txtmessage = (TextView) convertView.findViewById(R.id.txtmessage);
                    alertDialog.setView(convertView);
                    alertDialog.setCancelable(false);
                    staticSpinner = (Spinner) convertView.findViewById(R.id.static_spinner);

                    // Create an ArrayAdapter using the string array and a default spinner
                    ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                            .createFromResource(getContext(), R.array.reason_cancel_array_driver,
                                    android.R.layout.simple_spinner_item);

                    // Specify the layout to use when the list of choices appears
                    staticAdapter
                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // Apply the adapter to the spinner
                    staticSpinner.setAdapter(staticAdapter);
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.cancel();
                        }
                    });
                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancel_reason = staticSpinner.getSelectedItem().toString();
                            Comment = edt_cancel_trip.getText().toString();
                            if (cancel_reason.equalsIgnoreCase("---Select reason of cancel---")) {
                                txtmessage.setText("Please Select reason of cancellation");
                                txtmessage.setVisibility(View.VISIBLE);
                            } else {
                                if (edt_cancel_trip.getText().toString().length() < 1) {
                                    txtmessage.setText("Please fill the comments");
                                    txtmessage.setVisibility(View.VISIBLE);
                                } else {
                                    txtmessage.setVisibility(View.GONE);
                                    mDialog.cancel();
                                    if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                                        Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                                    }
                                    else
                                    {
                                        bookingCancel();
                                    }

                                }
                            }
                        }
                    });
                    mDialog = alertDialog.show();

                }
            });
            return paramView;
        }
    }

    public void bookingCancel() {
        showpDialog();
        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_BOOKING_CANCEL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Profile", "Malformed JSON: \"" + response.toString() + "\"");
                        try {
                            JSONArray successArray = response.getJSONArray("success");
//                            myrides();
                            new DriverRides().execute();
                            arrayLength = successArray.length();
                            list.clear();
                            if (arrayLength > 0) {

                                for (int i = 0; i < successArray.length(); i++) {

                                    JSONObject myridesObj = (JSONObject) successArray.get(i);
                                    message = myridesObj.getString("msg");
                                    if (myridesObj.has("msg")) {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                        LayoutInflater inflater = getActivity().getLayoutInflater();
                                        View convertView = (View) inflater.inflate(R.layout.rides_popup, null);
                                        btn_close = (Button) convertView.findViewById(R.id.btn_close);
                                        txt_title = (TextView) convertView.findViewById(R.id.txttitle);
                                        txt_message = (TextView) convertView.findViewById(R.id.txtmessage);
                                        txt_message.setText(message);
                                        txt_title.setText("Confirmation Message");
                                        alertDialog.setView(convertView);
                                        alertDialog.setCancelable(false);
                                        btn_close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mDialog.cancel();
                                            }
                                        });
                                        mDialog = alertDialog.show();
//                                        myrides();
                                    }

                                }
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            hidepDialog();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(getActivity(), "When loading data error has occurred. Check your network connection.", Toast.LENGTH_LONG).show();

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                Map<String, String> params = new HashMap<String, String>();
                params.put("login_id", Long.toString(sharedpreferences.getLong("customer_driver_id", 0)));
                params.put("booking_id", BookingId);
                params.put("cancel_by", sharedpreferences.getString("login_as", ""));
                params.put("comments", Comment);
                params.put("cancel_reason", cancel_reason);
                return params;
            }
        };

        ConnectionDetector.getInstance().addToRequestQueue(jsonReq);
    }
    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(myRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
//        new DriverRides().execute();
        handler = new Handler();
        myRunnable = new Runnable() {
            @Override
            public void run() {
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    url = "&login_id=" + URLEncoder.encode(Long.toString(sharedpreferences.getLong("customer_driver_id", 0)));
                    new DriverRides().execute();
                }
                handler.postDelayed(this, 50 * 1000);
            }
        };
        handler.post(myRunnable);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }
}