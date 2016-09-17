package com.organization.sjhg.e_school.Content.NewTest;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.organization.sjhg.e_school.ListStructure.QuestionList;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Utils.ProgressBarActivity;
import com.organization.sjhg.e_school.Utils.SharedPrefrence;
import com.organization.sjhg.e_school.Utils.ToastActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpan on 9/16/2016.
 */
public class TestAnswerActivity extends AppCompatActivity {
    private ToastActivity toastActivity=new ToastActivity();
    private SharedPrefrence sharedPrefrence=new SharedPrefrence();
    private String access_token;
    private  String parent_id="";
    private  String id="";
    private List<QuestionList> questionLists=new ArrayList<>();
    ProgressBarActivity progressBarActivity=new ProgressBarActivity();
    Bundle saveInstances;
    private View mProgressView;
    double startTime=System.currentTimeMillis();
    double endTime ;
    int lastPageposition=0;
    int pageOffset;
    ProgressBar progress;
    private ViewPager mViewPagerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        parent_id=intent.getStringExtra("parent_id");
        id=intent.getStringExtra("Id");

        saveInstances=savedInstanceState;
        setContentView(R.layout.activity_test);
        mProgressView=findViewById(R.id.dashboard_progress);
        mViewPagerView=(ViewPager)findViewById(R.id.viewpager_fragment);
        progress = (ProgressBar) findViewById(R.id.progressBar);
    }
}
