package com.organization.sjhg.e_school.Content;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.organization.sjhg.e_school.Helpers.ProtectionHelper;
import com.organization.sjhg.e_school.Remote.ExceptionHandler;

import java.io.IOException;

/**
 * Created by Prateek Tulsyan on 30-03-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class ContentViewer extends Activity {
    String localFilePath;
    byte[] protectionData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        localFilePath = intent.getStringExtra("localFilePath");
        protectionData = intent.getByteArrayExtra("protectionData");

        if (localFilePath == null) {
            ExceptionHandler.showAlertDialogContent(this, "Error Opening Content",
                    "The file path of the content is invalid, Please contact your IT admin");
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            ProtectionHelper.AccessFile(this, localFilePath, protectionData);
        } catch (IOException e) {
            ExceptionHandler.showAlertDialogContent(this, "Error Opening Content",
                    "Could not open the file. PLease contact your IT admin.");
            finish();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        ProtectFile();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ProtectFile();
    }

    private void ProtectFile() {
        try {
            ProtectionHelper.ProtectFile(localFilePath);
        } catch (IOException e) {
            // Ignoring the exception while protecting the file again.
            e.printStackTrace();
        }
    }
}
