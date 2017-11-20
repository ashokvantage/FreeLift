package com.freelift;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.freelift.driver.navigation.LruBitmapCache;

public class ConnectionDetector extends Application implements Constants {
 
    private Context _context;
 public static ConnectionDetector connection;
    public static final String TAG = ConnectionDetector.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    String  gcmToken = "";
    @Override
    public void onCreate() {

        super.onCreate();
        connection = this;
    }
    public static synchronized ConnectionDetector getInstance() {
        return connection;
    }
    /**
     * Checking for all possible internet providers
     * **/
    public boolean isConnectingToInternet(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            return true;
        }

        return false;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
    public RequestQueue getRequestQueue() {

        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }
    public void setGcmToken(String gcmToken) {

        this.gcmToken = gcmToken;
    }

    public String getGcmToken() {

        return this.gcmToken;
    }
}
