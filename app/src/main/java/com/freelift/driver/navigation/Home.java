package com.freelift.driver.navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.freelift.ConnectionDetector;
import com.freelift.Constants;
import com.freelift.CustomRequest;
import com.freelift.DriverNavigation;
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

public class Home extends Fragment implements Constants, SwipeRefreshLayout.OnRefreshListener {
    private ProgressDialog pDialog;
    int arrayLength;
    ListView mListView;
    TextView mMessage, txtheading;
    SwipeRefreshLayout mItemsContainer;
    private MyRidesListAdapter itemsAdapter;

    private Boolean restore = false;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    ArrayList<Holder> list = new ArrayList<Holder>();
    String BookingId = "";
    Button rl_close;
    TextView txttitle, txtmessage;
    AlertDialog mDialog;
    private PopupWindow pwindo;
    View rootView;
    Handler handler;
    Runnable myRunnable;
    boolean backend_execution = false;
    String url = "";

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        rootView = inflater.inflate(R.layout.driver_pending_list, container, false);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        backend_execution = false;
        handler = new Handler();
        myRunnable = new Runnable() {
            @Override
            public void run() {
                if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                    Toast.makeText(getActivity(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
//                    myrides();
                    url = "&login_id=" + URLEncoder.encode(Long.toString(sharedpreferences.getLong("customer_driver_id", 0)));
                    new DriverPendinglist().execute();

                }
                handler.postDelayed(this, 60 * 1000);
            }
        };
        handler.post(myRunnable);

        mItemsContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.container_items);
        mItemsContainer.setOnRefreshListener(this);


        mMessage = (TextView) rootView.findViewById(R.id.message);
        txtheading = (TextView) rootView.findViewById(R.id.txtheading);
        mListView = (ListView) rootView.findViewById(R.id.listView);
//        itemsAdapter = new MyRidesListAdapter(getActivity(), list);
//        mListView.setAdapter(itemsAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                MapActivity fragment = new MapActivity();
//                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.add(R.id.frame, fragment);
//                ft.addToBackStack("add");
//                ft.commit();
            }
        });
        return rootView;
    }

    public class DriverPendinglist extends AsyncTask<String, String, String> {
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
            JSONObject json = jParser.getJSONFromUrl(METHOD_DRIVER_PENDING_LIST + url);
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
                        holder.setFirstname(myridesObj.getString("firstname"));
                        holder.setLastname(myridesObj.getString("lastname"));
                        holder.setMobile(myridesObj.getString("mobile"));
                        holder.setEmail(myridesObj.getString("email"));
                        holder.setBookingId(myridesObj.getString("booking_id"));
                        holder.setTaxi_type(myridesObj.getString("taxi"));
                        holder.setStatus(myridesObj.getString("status"));
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
                showMessage("No pending rides");
                txtheading.setVisibility(View.GONE);
                hidepDialog();
            } else {
                hideMessage();
                itemsAdapter = new MyRidesListAdapter(getActivity(), list);
                mListView.setAdapter(itemsAdapter);
                txtheading.setVisibility(View.VISIBLE);
                hidepDialog();
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

            showMessage("No pending rides");
        }
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//
//        super.onSaveInstanceState(outState);
//
//        outState.putBoolean("restore", true);
//        outState.putInt("addedToListAt", addedToListAt);
//        outState.putParcelableArrayList(STATE_LIST, list);
//    }

    @Override
    public void onRefresh() {

        if (ConnectionDetector.getInstance().isConnectingToInternet()) {
//            myrides();
            new DriverPendinglist().execute();

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
            showMessage("No pending rides");
            txtheading.setVisibility(View.GONE);
            hidepDialog();
        } else {
            hideMessage();
            itemsAdapter = new MyRidesListAdapter(getActivity(), list);
            mListView.setAdapter(itemsAdapter);
            txtheading.setVisibility(View.VISIBLE);
            hidepDialog();
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
            TextView source, dest, taxi_type, status,date_time;
            Button btn_trip_accept;
        }

        @Override
        public long getItemId(int paramInt) {
            return paramInt;
        }

        @Override
        public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {

            MyRidesListAdapter.ViewHolder holder;
            if (paramView == null) {
                paramView = inflater.inflate(R.layout.driver_panding_listitem, paramViewGroup, false);
                holder = new MyRidesListAdapter.ViewHolder();
                holder.source = (TextView) paramView.findViewById(R.id.txtsource);
                holder.dest = (TextView) paramView.findViewById(R.id.txtdest);
                holder.taxi_type = (TextView) paramView.findViewById(R.id.txtride_type);
                holder.status = (TextView) paramView.findViewById(R.id.txtstatus);
                holder.date_time = (TextView) paramView.findViewById(R.id.date_time);
                holder.btn_trip_accept = (Button) paramView.findViewById(R.id.btn_trip_accept);

                paramView.setTag(holder);
            } else {
                holder = (MyRidesListAdapter.ViewHolder) paramView.getTag();
            }

            Holder h = list.get(paramInt);
            holder.source.setText("Start Point: " + h.getSrc_address().replace("\n", " "));
            holder.dest.setText("Destination Point: " + h.getDest_address());
            holder.taxi_type.setText("Ride Type: " + h.getTaxi_type());
            holder.status.setText("Status: " + h.getStatus());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
            Date convertedDate = new Date();
            try {
                convertedDate = df.parse(h.getDate_Time());
                String datetime = dateformat.format(convertedDate);
                holder.date_time.setText("Date_Time: " + datetime);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

//            holder.date_time.setText("Date_Time: " + h.getDate_Time());
            holder.btn_trip_accept.setTag(paramInt);
            holder.btn_trip_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
                    Holder h1 = (Holder) list.get(pos1);
                    BookingId = h1.getBookingId();
                    acceptBooking();
                }
            });


            return paramView;
        }
    }

    public void acceptBooking() {

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_DRIVER_BOOKING_ACCEPT, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Profile", "Malformed JSON: \"" + response.toString() + "\"");
                        try {
                            JSONArray successArray = response.getJSONArray("success");

                            arrayLength = successArray.length();

                            if (arrayLength > 0) {

                                message();
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("login_id", Long.toString(sharedpreferences.getLong("customer_driver_id", 0)));
                params.put("booking_id", BookingId);
                return params;
            }
        };

        ConnectionDetector.getInstance().addToRequestQueue(jsonReq);
    }

    public void message() {
//        myrides();
        new DriverPendinglist().execute();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.rides_popup, null);
        txttitle = (TextView) convertView.findViewById(R.id.txttitle);
        txtmessage = (TextView) convertView.findViewById(R.id.txtmessage);
        rl_close = (Button) convertView.findViewById(R.id.btn_close);
        alertDialog.setView(convertView);
        alertDialog.setCancelable(false);
        txttitle.setText("SUCCESS");
        txtmessage.setText("Please start the trip to destination");
        rl_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                MyRides myrides=new MyRides();
                ft.replace(R.id.frame, myrides);
                ft.commit();
                DriverNavigation.toolbar.setTitle("My Rides");
                mDialog.cancel();
                for (int i = 0; i < DriverNavigation.mDrawerList.getCount(); i++) {
                    if (i == 1) {
                        DriverNavigation.mDrawerList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.drawecolor));
                    } else {
                        DriverNavigation.mDrawerList.getChildAt(i).setBackgroundColor(Color.WHITE);
                    }
                }
            }
        });
        mDialog = alertDialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(myRunnable);
    }
}