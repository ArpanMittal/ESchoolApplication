package com.organization.sjhg.e_school.Remote;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.organization.sjhg.e_school.Helpers.ConnectivityReceiver;
import com.organization.sjhg.e_school.Helpers.FontsOverride;

/**
 * Created by Punit Chhajer on 11-07-2016.
 * This class is used to create single volley request queue for the application
 */
public class VolleyController extends Application {

    public static final String TAG = VolleyController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    private static VolleyController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "TitilliumWeb-LightItalic.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "TitilliumWeb-LightItalic.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "TitilliumWeb-LightItalic.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "TitilliumWeb-LightItalic.ttf");
        mInstance = this;
    }

    public static synchronized VolleyController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}