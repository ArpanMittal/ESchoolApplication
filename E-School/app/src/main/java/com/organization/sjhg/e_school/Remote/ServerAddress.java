package com.organization.sjhg.e_school.Remote;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.organization.sjhg.e_school.R;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Prateek Tulsyan on 26-03-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class ServerAddress {

    private static Boolean doesFileExistOnServer(String url, int timeout) {
        Boolean returnValue = false;
        try {
            URL myUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            if (connection.getResponseCode() == 200)
                returnValue = true;
        } catch (Exception e) {
            e.printStackTrace();
            returnValue = false;
        }
        return returnValue;
    }

    public static Boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String getServerAddress(Context context) {
        return context.getResources().getString(R.string.cloud_base_address);
    }

    public static String getLocalServerAddress(Context context) {
        return context.getResources().getString(R.string.local_base_address);
    }


    public static String getRemoteContentPath(Context context, String relativePath) {
        String CLOUD_ADDRESS = context.getResources().getString(R.string.cloud_base_address);
        String CLOUD_CODE_FOLDER = context.getResources().getString(R.string.cloud_content_relative_path);
        String LOCAL_ADDRESS = context.getResources().getString(R.string.local_base_address);
        String LOCAL_CODE_FOLDER = context.getResources().getString(R.string.local_content_relative_path);

        // If file exists on local server; return local path
        String filePathOnLocalServer = LOCAL_ADDRESS + "/" + LOCAL_CODE_FOLDER + "/" + relativePath;
        if (ServerAddress.doesFileExistOnServer(filePathOnLocalServer, 10000)) {
            return filePathOnLocalServer;
        }

        // if file does not exist on local; check file on cloud
        String filePathOnCloudServer = CLOUD_ADDRESS + "/" + CLOUD_CODE_FOLDER + "/" + relativePath;
        if (ServerAddress.doesFileExistOnServer(filePathOnCloudServer, 10000)) {
            return filePathOnCloudServer;
        }

        // If file doesn't exist on any server; return null
        return null;
    }
}
