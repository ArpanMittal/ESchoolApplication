package com.organization.sjhg.e_school;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.organization.sjhg.e_school.Helpers.StudentApplicationUserData;
import com.organization.sjhg.e_school.deviceadmin.DeviceAdminUtil;

import java.text.SimpleDateFormat;


public class CommonFragmentTheme extends FragmentActivity {
    protected RelativeLayout Screen;
    TextView helloUser, DateDisplay;
    Typeface font;
    BottomActionBar fragment;
    ImageView MenuBtn;
    public View mainview,mainview2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceAdminUtil.checkAndPrompt(this);
        setContentView(R.layout.activity_main);
       // mainview=findViewById(R.id.contentList);
        //mainview2=findViewById(R.id.tabs);

        String studentName = StudentApplicationUserData.getInstance(this).getStudentName();
        Screen = (RelativeLayout) findViewById(R.id.screen);

        DateDisplay = (TextView) findViewById(R.id.date);
        DateDisplay.setBackgroundResource(R.drawable.stroke);

        helloUser = (TextView) findViewById(R.id.helloUser);
        helloUser.setTextColor(Color.WHITE);
        helloUser.setText("Hi, " + studentName);

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy ");
        String dateString = sdf.format(date);
        DateDisplay.setTextColor(Color.WHITE);
        DateDisplay.setText(dateString);

        if (savedInstanceState == null) {
            fragment = new BottomActionBar();
            final FragmentManager fm = getFragmentManager();
            final FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(R.id.bottomBarFragment, fragment);
            fragmentTransaction.commit();
        }

      /*  MenuBtn = (ImageView) findViewById(R.id.menuBtn);
        MenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewGroup bottomBar = (ViewGroup) findViewById(R.id.bottomBarFragment);
                if (bottomBar.getVisibility() == View.VISIBLE) {
                    bottomBar.setVisibility(View.INVISIBLE);
                    MenuBtn.setImageResource(0);
                    MenuBtn.setImageResource(R.drawable.menu_button);
                } else {
                    bottomBar.setVisibility(View.VISIBLE);
                    MenuBtn.setImageResource(0);
                    MenuBtn.setImageResource(R.drawable.close);
                }
            }
        });*/
    }
    

    @TargetApi(19)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }
}
