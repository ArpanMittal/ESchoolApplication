package com.organization.sjhg.e_school;

import android.app.ActionBar;
import android.view.View;
import android.view.Window;

/**
 * Created by Arpan on 4/26/2016.
 */
public  class HideNavigationBar {
    //static HideNavigationBar hideNavigationBar;
    public void hideNavigationBar(Window window)
    {

            View decorView = window.getDecorView();
            //hide navigation bar
            int uiOptions =
                     View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);

    }
    public void hideSettingNavigationBar(Window window)
    {
        View decorView = window.getDecorView();
        //hide navigation bar
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;




        decorView.setSystemUiVisibility(uiOptions);


    }
}
