package com.organization.sjhg.e_school;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by arpan on 9/22/2016.
 */
public class Splash_Activity extends AppCompatActivity {

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
        }, 2000);
    }
    private void goToMain()
    {
        Intent intent=new Intent(this,Main_Activity.class);
        startActivity(intent);
    }
}
