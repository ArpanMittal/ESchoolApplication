package com.organization.sjhg.e_school.Content.Test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;


import com.organization.sjhg.e_school.CommonFragmentTheme;
import com.organization.sjhg.e_school.Content.Content_Type;
import com.organization.sjhg.e_school.Helpers.StudentApplicationUserData;
import com.organization.sjhg.e_school.HideNavigationBar;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Structure.TestDetail;

import junit.framework.Test;

import java.io.IOException;

/**
 * Created by Arpan on 3/5/2016.
 */
public class TestInstructionActivity extends Activity{
   static Button startTest;
    String contentTypeId;
    String localFilePath;
    TextView testDetail;
    static TestDetail test;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        Intent i=getIntent();
        HideNavigationBar hideNavigationBar=new HideNavigationBar();
        hideNavigationBar.hideNavigationBar(getWindow());
        //contentTypeId = i.getStringExtra("subjectId");
        localFilePath=i.getStringExtra("localFilePath");
        try {
            test = TestDetail.getTesDetailObjectFromLocal(localFilePath);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this,R.string.basic_error, Toast.LENGTH_LONG).show();
            finish();
        }
        setContentView(R.layout.activity_test_start);
        testDetail=(TextView)findViewById(R.id.textView3);
        TextView timeInstruction=(TextView)findViewById(R.id.timetext);
        testDetail.setText(test.contentName);
        if(test.isTestTimed==true)
        {
            TextView testtime=(TextView)findViewById(R.id.timed);
            testtime.setText(test.timeLimit);
            //timeInstruction.setText();
        }
        else
        {
            TextView testtime=(TextView)findViewById(R.id.timed);
            testtime.setText(null);
            timeInstruction.setText(null);
        }
        TextView adaptiveInstruction=(TextView)findViewById(R.id.adaptivetextView1);
        TextView numberofquestionlabel=(TextView)findViewById(R.id.numberofquestionslabel);
        TextView numberofquestion=(TextView)findViewById(R.id.numberofquestions);
        int numberofquestions=test.questions.size();
        if(test.contentTypeId!=Content_Type.ADAPTIVE_TEST.getValue())
        {

            TextView testtype=(TextView)findViewById(R.id.testtype);
            testtype.setText("Normal Test");

            numberofquestion.setText(String.valueOf(numberofquestions));
            adaptiveInstruction.setText(null);
        }
        else
        {
            TextView testtype=(TextView)findViewById(R.id.testtype);
            numberofquestionlabel.setText(null);
            numberofquestion.setText(null);
            testtype.setText("Adaptive Test");

        }

        startTest=(Button)findViewById(R.id.startTest);
        if (test.status == TestDetail.TestStatus.TEST_SUBMITTED || test.status == TestDetail.TestStatus.TEST_UPLOADED) {
            startTest.setText("View Test Answers");
        } else if (!test.isLiveVoting && StudentApplicationUserData.getInstance(getApplicationContext()).isConnectedToSession()){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog));
            builder1.setMessage(this.getResources().getString(R.string.error_message_test_while_session));
            builder1.setCancelable(false);
            builder1.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
            return;
        }
        startTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (test.contentTypeId==Content_Type.TEST.getValue()) {
                    Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                    intent.putExtra("localFilePath", localFilePath);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), AdaptiveContent.class);
                    intent.putExtra("localFilePath", localFilePath);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        HideNavigationBar hideNavigationBar=new HideNavigationBar();
        hideNavigationBar.hideNavigationBar(getWindow());
    }
}
