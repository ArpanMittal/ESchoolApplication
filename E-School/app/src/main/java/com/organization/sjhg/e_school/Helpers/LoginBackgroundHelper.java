package com.organization.sjhg.e_school.Helpers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.ExceptionHandler;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.organization.sjhg.e_school.Structure.LoginStatusHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Prateek Tulsyan on 12-03-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class LoginBackgroundHelper extends AsyncTask<String, String, String> {
    private Context context;
    private Dialog pDialog;
    private boolean isLoginSuccessful = false;
    private LoginStatusHandler listener;
    private Boolean isLaughguruLogin = false;
    private String username;
    private String password;
    GoogleCloudMessaging gcm;;

    public LoginBackgroundHelper(LoginStatusHandler listener, Context ctx) {
        this.context = ctx;
        this.listener = listener;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //WebView wv = (WebView) findViewById(R.id.splash);
        //wv.loadUrl("file:///android_asset/splash.gif");
        //wv.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_INSET);

        pDialog = new Dialog(context);
        pDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = LayoutInflater.from(context);
        pDialog.setContentView(inflater.inflate(R.layout.pdialog, null));
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        ImageView mImageViewFilling = (ImageView) pDialog.findViewById(R.id.pdialogimageview);
        ((AnimationDrawable) mImageViewFilling.getBackground()).start();
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {
        String returnValue;
        String gcmRegId;
        username = args[0];
        password = args[1];

        if (args.length > 2)
            isLaughguruLogin = args[2].equals(Boolean.toString(true));

        if (ServerAddress.isConnectedToInternet(context)) {
            try {

                    Map<String,String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("password", password);
                   // String text= StudentApplicationUserData.getGcmRegistrationId(context);
                    if(StudentApplicationUserData.getGcmRegistrationId(context)==null)
                    {
                        gcm= GoogleCloudMessaging.getInstance(context);
                        gcmRegId = gcm.register("405938383495");
                        StudentApplicationUserData.putGcmRegistrationId(context, gcmRegId);
                       //
                        params.put("GcmRegId",gcmRegId);

                    }
                    returnValue = (new RemoteHelper(context)).VerifyLoginAndSetUserData(params);


                isLoginSuccessful = true;
            } catch (Exception e) {
                isLoginSuccessful = false;
                returnValue = e.getMessage();
                e.printStackTrace();
            }
            return returnValue;
        } else {
            return null;
        }
    }

    protected void onPostExecute(String message) {
        if (isLoginSuccessful) {
            if (isLaughguruLogin) {
                StudentApplicationUserData.setLaughguruPassword(context, password);
            } else {
                StudentApplicationUserData.SaveUserCredentials(context, username, password);
            }
        } else {
            if (isLaughguruLogin) ;

            else {
                if (message != null) {
                    ExceptionHandler.thrownExceptions(context,
                            context.getResources().getString(R.string.login_failure),
                            message);
                } else {
                    ExceptionHandler.thrownExceptions(context,
                            context.getResources().getString(R.string.network_error),
                            context.getResources().getString(R.string.error_message_internet));
                }
            }
        }
        pDialog.dismiss();
        listener.HandleLoginStatus(isLoginSuccessful);
    }
}