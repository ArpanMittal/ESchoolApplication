package com.organization.sjhg.e_school.Content.Test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.organization.sjhg.e_school.Content.Content_Type;
import com.organization.sjhg.e_school.MainActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.Structure.Question;
import com.organization.sjhg.e_school.Structure.TestDetail;
import com.organization.sjhg.e_school.Sync.SyncManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arpan on 1/28/2016.
 */
public class AdaptiveTestActivity extends ParentTestActivity implements RemoteCallHandler {
    // TextView timerDisplay;

    ImageView nxtbtn,btnsubmit;

    //int testID=test.contentFileId;
    //int numberOfExits = 0;
    String Answer;
    //layout of Adaptive test on which fragment is load fragment load as view pager from parent activity
    private Dialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(test.isTestTimed)
            EnableTimerFunction();
        int flag=0;
        testLayout(flag);
    }
    protected  void testLayout(int flag)
    {
        if(flag!=0)
        {
            loadFragment();
        }
        nxtbtn = (ImageView) findViewById(R.id.nextButton);
        btnsubmit = (ImageView) findViewById(R.id.submitButton);
        if(test.lastQuestion!=true) {
            nxtbtn.setVisibility(View.VISIBLE);
        }
        else {
            nxtbtn.setVisibility(View.GONE);
            btnsubmit.setVisibility(View.VISIBLE);
        }

        nxtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!test.lastQuestion)
                    onClickSubmit();
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickSubmit();
            }
        });

    }
    private void checkAnswer()
    {
        for(int i=0;i<test.questions.size();i++) {
            if (test.questions.get(i).correctAnswer == test.questions.get(i).selectedAnswer)
                Answer = "True";
            else
                Answer = "False";
        }
    }
    private void onClickSubmit()
    {
        checkAnswer();
        new RemoteHelper(getApplicationContext()).uploadAdaptiveTest(this, RemoteCalls.UPLOAD_ADAPTIVE_TEST, test.contentFileId, Answer, test.questions.get(0).questionId);
        pDialog = new Dialog(this);
        pDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(getLayoutInflater().inflate(R.layout.pdialog, null));
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        ImageView mImageViewFilling = (ImageView) pDialog.findViewById(R.id.pdialogimageview);
        ((AnimationDrawable) mImageViewFilling.getBackground()).start();
        pDialog.show();

    }
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        //In case receive JSON Object null
        if (!isSuccessful) {
            pDialog.dismiss();
            if (ServerAddress.isConnectedToInternet(getApplicationContext())) {
                //end the test here
                Toast toast = Toast.makeText(getApplicationContext(),R.string.network_error, Toast.LENGTH_LONG);
                toast.show();
                //SAVE REMAINING TIME OF TIMER
                if (test.isTestTimed == true) {
                    String doubleQuotes = ":";
                    int hour = remainingTimeInSeconds / GlobalConstants.SECONDS_IN_HOUR;
                    String remainingtime = String.valueOf(hour);
                    remainingtime = remainingtime.concat(doubleQuotes);
                    remainingTimeInSeconds = remainingTimeInSeconds % GlobalConstants.SECONDS_IN_HOUR;
                    int min = remainingTimeInSeconds / GlobalConstants.MINUTES_IN_HOUR;
                    String temp = String.valueOf(min);
                    temp = temp.concat(doubleQuotes);
                    remainingtime = remainingtime.concat(temp);
                    remainingTimeInSeconds = remainingTimeInSeconds % GlobalConstants.MINUTES_IN_HOUR;
                    temp = String.valueOf(remainingTimeInSeconds);
                    test.timeLimit = remainingtime.concat(temp);
                    test.questions.clear();
                    finish();
                }
            }
            //IN CASE OF MANUALLY STOPPING THE TEST
            else {
                submitAdaptiveTest();
            }
        }
        else
        {
            pDialog.dismiss();
            if (response.isNull("Lastquestion") && !(response.isNull("Question_Objects"))) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.basic_error, Toast.LENGTH_LONG);
                Intent goToHome = new Intent(AdaptiveTestActivity.this, MainActivity.class);
                startActivity(goToHome);
                toast.show();
            }
            switch (callFor) {
                case UPLOAD_ADAPTIVE_TEST:
                    //for last question this will be true
                    if (test.lastQuestion == true) {
                        submitAdaptiveTest();
                        break;
                    } else {
                        new RemoteHelper(getApplicationContext()).getAdaptiveContent(this, RemoteCalls.GET_ADAPTIVE_CONTENT, test.contentFileId);
                        break;
                    }

                case GET_ADAPTIVE_CONTENT: {
                    try {
                        test.questions.clear();
                        test.getAdaptiveQuestionDetails(response, getApplicationContext());
                        testLayout(1);
                    } catch (IOException | JSONException i) {
                        Log.e("error", "Exception Occured" + i);
                    }
                }
            }

        }
    }
}