package com.organization.sjhg.e_school;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.organization.sjhg.e_school.Utils.SharedPrefrence;

/**
 * Created by arpan on 9/22/2016.
 */
public class Splash_Activity extends AppCompatActivity {
    private SharedPrefrence sharedPrefrence;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goToMain();
                //new LoginBackgroundHelper(this, SplashActivity.this).execute(spUsername, spPassword);
            }
        }, 3000);
    }
    private void goToMain()
    {
        SharedPrefrence sharedPrefrence=new SharedPrefrence();
        if(sharedPrefrence.getShowTourGuide(getApplicationContext())==null)
        {
            Intent intent=new Intent(this,Tour_Guide.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(this, Main_Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
