package com.organization.sjhg.e_school.Content;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.organization.sjhg.e_school.HideNavigationBar;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.deviceadmin.DeviceAdminUtil;

import java.io.File;

/**
 * Created by Gaurav Rawat.
 * Email: gauravrawat.official@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class ImageDisplayActivity extends ContentViewer
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceAdminUtil.checkAndPrompt(this);
        HideNavigationBar hideNavigationBar=new HideNavigationBar();
        hideNavigationBar.hideNavigationBar(getWindow());
        setContentView(R.layout.activity_display_image);

        super.onResume();
        imageView(localFilePath);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        HideNavigationBar hideNavigationBar=new HideNavigationBar();
        hideNavigationBar.hideNavigationBar(getWindow());
    }



    @Override
    protected void onPause()
    {
        super.onPause();
    }

    private void imageView(String localFilePath)
    {
        File imgFile = new File(localFilePath);
        if (imgFile.exists())
        {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView imageView = (ImageView)findViewById(R.id.imageView);
            imageView.setImageBitmap(myBitmap);
        }
    }
}
