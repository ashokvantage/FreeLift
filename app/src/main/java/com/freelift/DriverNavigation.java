package com.freelift;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.freelift.driver.navigation.Home;
import com.freelift.driver.navigation.MyRides;
import com.freelift.driver.navigation.PrivacyPolicy;
import com.freelift.driver.navigation.UpdateProfile;
import com.pkmmte.view.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DriverNavigation extends AppCompatActivity implements Constants {

    //Defining Variables
    public static Toolbar toolbar;
    private NavigationView navigationView;
    public static DrawerLayout drawerLayout;

    Home home = new Home();
    MyRides myrides = new MyRides();
    PrivacyPolicy policy = new PrivacyPolicy();
    UpdateProfile updateprofile = new UpdateProfile();
    String[] title;
    //    String[] subtitle;
    int[] icon;
    public static ListView mDrawerList;
    MenuListAdapter mMenuAdapter;
    MenuItem menu_item;
    int page = 0;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    TextView txtusername, txtuseremail;
    CircularImageView profile_image;
    String username, useremail, user_profileImage = "";
    AlertDialog mDialog;
    Button btn_cancel, btn_logout;
    boolean doubleBackToExitPressedOnce = false;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        pDialog = new ProgressDialog(DriverNavigation.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        username = sharedpreferences.getString("username", "");
        useremail = sharedpreferences.getString("useremail", "");
        user_profileImage = sharedpreferences.getString("userpic", "");
        title = new String[]{"Taxi", "My Rides", "Privacy Policy", "Update Profile", "Logout"};

        // Generate icon
        icon = new int[]{R.drawable.home, R.drawable.myrides, R.drawable.privacypolicy, R.drawable.updateprofile, R.drawable.logout};

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerList = (ListView) findViewById(R.id.lst_menu_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        profile_image = (CircularImageView) findViewById(R.id.profile_image);
        txtusername = (TextView) findViewById(R.id.username);
        txtuseremail = (TextView) findViewById(R.id.useremail);
        txtusername.setText(username);
        txtuseremail.setText(useremail);
        if (!user_profileImage.equalsIgnoreCase("")) {
            if (user_profileImage.length() > 0) {

                ImageLoader imageLoader = ConnectionDetector.getInstance().getImageLoader();

                imageLoader.get(user_profileImage, ImageLoader.getImageListener(profile_image, R.drawable.taxi, R.drawable.taxi));
            }
        } else {
//            profile_image.setBackgroundResource(R.drawable.taxi);
            profile_image.setImageResource(R.drawable.taxi);
        }
        mMenuAdapter = new MenuListAdapter(DriverNavigation.this, title, icon);

        // Set the MenuListAdapter to the ListView
        mDrawerList.setAdapter(mMenuAdapter);
        if (SplashPage.click_on_notification) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, myrides);
            ft.commit();
            getSupportActionBar().setTitle(title[1]);
            SplashPage.click_on_notification = false;
            for (int i = 0; i < mDrawerList.getChildCount(); i++) {
                if (i ==1) {
                    mDrawerList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.drawecolor));
                } else {
                    mDrawerList.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                }
            }
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, home);
            ft.commit();
            getSupportActionBar().setTitle(title[0]);
        }

//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.frame, home);
//        ft.commit();
//        setTitle(title[0]);
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // Locate Position
                switch (position) {
                    case 0:
                        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                            Toast.makeText(DriverNavigation.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                        } else {
                            ft.replace(R.id.frame, home);
                            page = 0;
                            getSupportActionBar().setTitle(title[position]);
                        }
                        break;
                    case 1:
                        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                            Toast.makeText(DriverNavigation.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                        } else {
                            ft.replace(R.id.frame, myrides);
                            page = 1;
                            getSupportActionBar().setTitle(title[position]);
                        }
                        break;
                    case 2:
                        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                            Toast.makeText(DriverNavigation.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                        } else {
                            ft.replace(R.id.frame, policy);
                            page = 2;
                            getSupportActionBar().setTitle(title[position]);
                        }
                        break;
                    case 3:
                        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                            Toast.makeText(DriverNavigation.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                        } else {
                            ft.replace(R.id.frame, updateprofile);
                            page = 3;
                            getSupportActionBar().setTitle(title[position]);
                        }
                        break;
                    case 4:
                        if (!ConnectionDetector.getInstance().isConnectingToInternet()) {

                            Toast.makeText(DriverNavigation.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                        } else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DriverNavigation.this);
                            LayoutInflater inflater = DriverNavigation.this.getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.logout_popup, null);
                            btn_logout = (Button) convertView.findViewById(R.id.btn_logout);
                            btn_cancel = (Button) convertView.findViewById(R.id.btn_logout);
                            alertDialog.setView(convertView);
                            btn_logout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.cancel();
                                    logOut();
                                    stopService();
                                }
                            });
                            btn_cancel = (Button) convertView.findViewById(R.id.btn_cancel);
                            alertDialog.setView(convertView);
                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.cancel();
                                }
                            });
                            mDialog = alertDialog.show();
                        }
                        break;
                }

                ft.commit();
//                mDrawerList.setItemChecked(position, true);
                for (int i = 0; i < mDrawerList.getChildCount(); i++) {
                    if (position == i) {
                        mDrawerList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.drawecolor));
                    } else {
                        mDrawerList.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }
                // Get the title followed by the position
                // Close drawer
                drawerLayout.closeDrawers();
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
                SplashPage.click_on_notification = false;
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                username = sharedpreferences.getString("username", "");
                useremail = sharedpreferences.getString("useremail", "");
                user_profileImage = sharedpreferences.getString("userpic", "");
                txtusername.setText(username);
                txtuseremail.setText(useremail);
                if (!user_profileImage.equalsIgnoreCase("")) {
                    if (user_profileImage.length() > 0) {

                        ImageLoader imageLoader = ConnectionDetector.getInstance().getImageLoader();

                        imageLoader.get(user_profileImage, ImageLoader.getImageListener(profile_image, R.drawable.taxi, R.drawable.taxi));
                    }
                }
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save, menu);

        menu_item = menu.findItem(R.id.btnsave);
        if (page != 3) {
            menu.clear();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case android.R.id.home: {

                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            // If there are back-stack entries, leave the FragmentActivity
            // implementation take care of them.
            manager.popBackStack();

        } else {
            // Otherwise, ask user if he wants to leave :)
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }

    }

    public void stopService() {
        stopService(new Intent(getBaseContext(), MyService.class));
    }

    public void logOut() {

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_LOGOUT, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray successArray = response.getJSONArray("success");


                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            hidepDialog();
                            loadingComplete();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(getApplicationContext(), "When loading data error has occurred. Check your network connection.", Toast.LENGTH_LONG).show();

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("logout_as", sharedpreferences.getString("login_as", ""));
                params.put("login_id", Long.toString(sharedpreferences.getLong("customer_driver_id", 0)));
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

    public void loadingComplete() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putLong("customer_driver_id", 0);
        editor.putString("login_as", "");
        editor.commit();
//        SplashPage.customer_driver_login_id = Long.parseLong("0");
        stopService();
        Intent in = new Intent(DriverNavigation.this, LoginActivity.class);
        startActivity(in);
        DriverNavigation.this.finish();
    }
}
