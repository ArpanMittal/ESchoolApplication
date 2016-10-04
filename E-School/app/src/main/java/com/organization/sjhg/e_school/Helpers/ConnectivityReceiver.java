package com.organization.sjhg.e_school.Helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.organization.sjhg.e_school.Remote.VolleyController;

import java.net.InetAddress;

/**
 * Created by Punit Chhajer on 22-08-2016.
 */
public class ConnectivityReceiver
        extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;

    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        isConnected();
    }

    public static void isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) VolleyController.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null
                && activeNetwork.isConnected();

        Thread thread = new Thread() {
            @Override
            public void run() {
                if (isConnected==true){
                    Boolean isOnline = isOnline();
                    if (connectivityReceiverListener != null) {
                        connectivityReceiverListener.onNetworkConnectionChanged(isOnline);
                    }
                }else{
                    if (connectivityReceiverListener != null) {
                        connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
                    }
                }
            }
        };

        thread.start();

    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

    public static boolean isInternetAvailable() {
        try {
            if (InetAddress.getByName("www.google.com").isReachable(30000))
                return true;

        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static Boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}
