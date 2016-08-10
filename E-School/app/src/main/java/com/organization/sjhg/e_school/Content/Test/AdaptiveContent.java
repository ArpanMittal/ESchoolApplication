package com.organization.sjhg.e_school.Content.Test;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.organization.sjhg.e_school.CommonFragmentTheme;
import com.organization.sjhg.e_school.MainActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.ExceptionHandler;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Structure.Question;
import com.organization.sjhg.e_school.Structure.TestDetail;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Arpan on 1/27/2016.
 */
public class AdaptiveContent extends FragmentActivity implements RemoteCallHandler {
    //public  class AdaptiveContent extends Activity{
    static TestDetail test;
    String localFilePath;
    private Dialog pDialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        localFilePath = intent.getStringExtra("localFilePath");
        try {
            test = TestDetail.getTesDetailObjectFromLocal(localFilePath);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error getting test details from local storage. Try again later!", Toast.LENGTH_LONG).show();
            finish();
        }
        int testID = test.contentFileId;


            //to download question in list
            new RemoteHelper(getApplicationContext()).getAdaptiveContent(this, RemoteCalls.GET_ADAPTIVE_CONTENT, testID);
            //wait till it fetch question from server
            pDialog = new Dialog(this);
            pDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            pDialog.setContentView(getLayoutInflater().inflate(R.layout.pdialog
                    , null));
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pDialog.setCancelable(false);
            ImageView mImageViewFilling = (ImageView) pDialog.findViewById(R.id.pdialogimageview);
            ((AnimationDrawable) mImageViewFilling.getBackground()).start();
            pDialog.show();


    }
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        //dismiss the dialog box here
        pDialog.dismiss();
        //JSON object send this value only when Question_Objects is not null

        if(response.isNull("Lastquestion"))
        {

            Toast toast = Toast.makeText(getApplicationContext(),R.string.basic_error, Toast.LENGTH_LONG);
            Intent goToHome = new Intent(this, MainActivity.class);
            startActivity(goToHome);
            toast.show();
        }
        if (!isSuccessful) {
           // pDialog.dismiss();
            finish();
            Toast toast = Toast.makeText(getApplicationContext(),R.string.network_error, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            switch (callFor) {
                //load fragment for that question for a particulat test
                case GET_ADAPTIVE_CONTENT:
                    try {

                            test.getAdaptiveQuestionDetails(response, getApplicationContext());
                            Intent openContentIntent = new Intent(this, AdaptiveTestActivity.class);
                            openContentIntent.putExtra("localFilePath", localFilePath);
                            finish();
                            startActivity(openContentIntent);
                        }catch(JSONException | IOException i){
                            Log.e("error", "Exception occur in AdaptiveContent");
                        }
                    }
            }
        }
    }
