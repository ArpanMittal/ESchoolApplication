package com.organization.sjhg.e_school;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;

import com.organization.sjhg.e_school.ClassSession.SyncService;
import com.organization.sjhg.e_school.Helpers.LoginBackgroundHelper;
import com.organization.sjhg.e_school.Helpers.StudentApplicationUserData;
import com.organization.sjhg.e_school.Structure.LoginStatusHandler;

import static android.app.AlarmManager.ELAPSED_REALTIME;
import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;
import static android.os.SystemClock.elapsedRealtime;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by Gaurav Rawat.
 * Email: gauravrawat.official@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */


public class SplashActivity extends Activity implements LoginStatusHandler {
    public static final int SYNC_FREQUENCY = 30;
    long startTime;

    public static PendingIntent getSyncPendingIntent(Intent intent, Context context) {
        return PendingIntent.getService(context, 0, intent, FLAG_CANCEL_CURRENT);
    }


    public void goToLoginActivity() {
        Intent playSplash = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(playSplash);
        finish();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //code to hide navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions =  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        startTime = System.currentTimeMillis();
        setContentView(R.layout.activity_splash);
        // WebView wv = (WebView) findViewById(R.id.splash);
        // wv.loadUrl("file:///android_asset/splash.gif");
        // wv.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_INSET);

        String spUsername = StudentApplicationUserData.getPrefUsername(this);
        String spPassword = StudentApplicationUserData.getPrefPassword(this);


        if (spUsername != null && spPassword != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callLoginBackground();
                    //new LoginBackgroundHelper(this, SplashActivity.this).execute(spUsername, spPassword);
                }
            }, 2000);

        } else
            proceedToLogin();

        // startSync();
    }
    public void callLoginBackground()
    {
        String spUsername = StudentApplicationUserData.getPrefUsername(this);
        String spPassword = StudentApplicationUserData.getPrefPassword(this);
        new LoginBackgroundHelper(this, SplashActivity.this).execute(spUsername, spPassword);
    }



    @Override
    public void HandleLoginStatus(boolean isLoginSuccessful) {
        //long currentTime = System.currentTimeMillis();
        //long waitTime = 1500 - ((currentTime - startTime));
        //  waitTime = waitTime < 0 ? 0 : waitTime;
        Boolean isLoggedOut = StudentApplicationUserData.getLogoutStatus(this);
        if (isLoginSuccessful && !isLoggedOut)
        {
            StudentApplicationUserData.SaveLogoutStatus(this, false);
            Intent splashToMain = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(splashToMain);
            finish();
           /* new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, waitTime);*/
        }
        else
        {
            goToLoginActivity();
           /* new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToLoginActivity();
                }
            },waitTime);*/

        }
    }


    // Proceed to LoginActivity
    private void proceedToLogin() {
        goToLoginActivity();
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goToLoginActivity();
            }
        }, 1500);*/
    }
}
