package com.organization.sjhg.e_school.Content;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.organization.sjhg.e_school.HideNavigationBar;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.deviceadmin.DeviceAdminUtil;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Gaurav Rawat.
 * Email: gauravrawat.official@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class ImageDisplayActivity extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        String title= getIntent().getStringExtra("title");
        if (title!=null){
            setTitle(title);
        }
        imageView(getIntent().getStringExtra("path"));
    }

    private void imageView(String filePath) {
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            Picasso.with(this)
                    .load(filePath)
                    .placeholder(R.drawable.ic_account_circle_white_24dp)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(imageView);
    }
}



