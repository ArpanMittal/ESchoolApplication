package com.organization.sjhg.e_school.Content;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.organization.sjhg.e_school.Helpers.ProtectionHelper;
import com.organization.sjhg.e_school.HideNavigationBar;
import com.organization.sjhg.e_school.Remote.ExceptionHandler;
import com.organization.sjhg.e_school.deviceadmin.DeviceAdminUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by Gaurav Rawat.
 * Email: gauravrawat.official@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class PdfDisplayActivity extends Activity {

    public String localFilePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceAdminUtil.checkAndPrompt(this);
        HideNavigationBar hideNavigationBar=new HideNavigationBar();
        hideNavigationBar.hideNavigationBar(getWindow());
        Intent intent = getIntent();
        this.localFilePath = intent.getStringExtra("localFilePath");
        byte[] protectionData = intent.getByteArrayExtra("protectionData");

        if (this.localFilePath == null) {
            ExceptionHandler.showAlertDialogContent(this, "Error Opening Content",
                    "The file path of the content is invalid, Please contact your IT admin");
            finish();
        }

        File file = new File(this.localFilePath);
        if (file.exists()) {
            try {
                ProtectionHelper.AccessFile(this, this.localFilePath, protectionData);
            } catch (IOException e) {
                e.printStackTrace();
                ExceptionHandler.showAlertDialogContent(this, "Error Opening Content",
                        "Could not open the file. PLease contact your IT admin.");
                finish();
            }
            Intent openPdfIntent = new Intent(Intent.ACTION_VIEW);
            openPdfIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            openPdfIntent.setDataAndType(Uri.fromFile(file), "application/pdf");
            startActivity(openPdfIntent);
        } else {
            ExceptionHandler.showAlertDialogContent(this, "Error Opening Content",
                    "File not found. Please delete and download again.");
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        HideNavigationBar hideNavigationBar=new HideNavigationBar();
        hideNavigationBar.hideNavigationBar(getWindow());
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        try {
            ProtectionHelper.ProtectFile(this.localFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}