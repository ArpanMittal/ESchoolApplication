package com.organization.sjhg.e_school.Content.NewTest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.organization.sjhg.e_school.Fragments.ExamListFragment;
import com.organization.sjhg.e_school.Helpers.Grid_Exam_Fragment;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.Helpers.QuestionAdapter;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.ListStructure.QuestionList;

import com.organization.sjhg.e_school.ListStructure.QuestionResponseList;

import com.organization.sjhg.e_school.LoginActivity;
import com.organization.sjhg.e_school.Main_Activity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.Structure.Question;
import com.organization.sjhg.e_school.Utils.ProgressBarActivity;
import com.organization.sjhg.e_school.Utils.SharedPrefrence;
import com.organization.sjhg.e_school.Utils.ToastActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpan on 9/7/2016.
 */
public class TestActivity extends AppCompatActivity implements RemoteCallHandler {
    private ToastActivity toastActivity=new ToastActivity();
    private SharedPrefrence sharedPrefrence=new SharedPrefrence();
    private String access_token;
    private  String tag="";
    private  String id="";
    private List<QuestionList>  questionLists=new ArrayList<>();
    ProgressBarActivity progressBarActivity=new ProgressBarActivity();
    Bundle saveInstances;
    private View mProgressView;

    public List<QuestionResponseList>questionResponseLists=new ArrayList<>();

    private ViewPager mViewPagerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
         tag=intent.getStringExtra("Tag");
         id=intent.getStringExtra("Id");

        saveInstances=savedInstanceState;
        setContentView(R.layout.activity_test);
        mProgressView=findViewById(R.id.dashboard_progress);
        mViewPagerView=(ViewPager)findViewById(R.id.viewpager_fragment);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(saveInstances==null) {
            if (questionLists.isEmpty()) {
                progressBarActivity.showProgress(mViewPagerView,mProgressView,true,getApplicationContext());

                access_token=sharedPrefrence.getAccessToken(getApplicationContext());

                new RemoteHelper(getApplicationContext()).getQuestion(this, RemoteCalls.GET_QUESTION, tag, id, access_token);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO: save list
    }

    private void showView(List<QuestionList> questions)
    {


        QuestionAdapter questionAdapter=new QuestionAdapter(getSupportFragmentManager(),questions,getApplicationContext());
        //Grid_Exam_Fragment grid_exam_fragment=new Grid_Exam_Fragment(getSupportFragmentManager(),li,context);
        mViewPagerView.setAdapter(questionAdapter);
        mViewPagerView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private List<QuestionList> getList(JSONObject response)
    {
        List<QuestionList> questionLists=new ArrayList<>();
        try{
            JSONArray data = response.getJSONArray(getString(R.string.data));
            for(int i=0;i<data.length();i++)
            {
                JSONObject jsonObject=data.getJSONObject(i);
                String id=jsonObject.getString(getString(R.string.jsonid));
                String hash=jsonObject.getString(getString(R.string.jsonhash));
                String question_type=jsonObject.getString(getString(R.string.jsonquestiontype));
                String question_text=jsonObject.getString(getString(R.string.jsonquestiontext));
                String solution_path=jsonObject.getString(getString(R.string.jsonsolutonpath));
                String difficulty=jsonObject.getString(getString(R.string.jsondifficulty));
                String question_image_path=jsonObject.getString(getString(R.string.jsonquestionimage));
                String answer=jsonObject.getString(getString(R.string.jsonanswer));
                JSONArray jsonArray=jsonObject.getJSONArray(getString(R.string.jsonoption));
                List<ChapterList> chapterLists=new ArrayList<>();
                for(int j=0;j<jsonArray.length();j++)
                {
                    JSONObject jsonObject1=jsonArray.getJSONObject(j);
                    String id1=jsonObject1.getString(getString(R.string.jsonid));
                    String option=jsonObject1.getString(getString(R.string.jsonoption));
                    chapterLists.add(new ChapterList(id1,option));
                }
               questionLists.add(new QuestionList(id,hash,question_type,question_text,solution_path,difficulty,question_image_path,answer,chapterLists));
            }
        }catch (Exception e)
        {
            LogHelper logHelper=new LogHelper(e);
            e.printStackTrace();
        }
        return questionLists;
    }

    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        progressBarActivity.showProgress(mViewPagerView,mProgressView,false,getApplicationContext());
        if(!isSuccessful)
        {
            toastActivity.makeUknownErrorMessage(this);
        }
        else
        {

            switch (callFor) {
                case GET_QUESTION:
                {
                    try {
                        if (response.get("code").toString().equals(GlobalConstants.EXPIRED_TOKEN))
                        {

                            if(sharedPrefrence.getRefreshToken(getApplicationContext())==null)
                            {

                                toastActivity.makeToastMessage(response,this);
                                break;
                            }
                            else
                            {
                               // new RemoteHelper(getApplicationContext()).getAccessToken(this,RemoteCalls.GET_ACCESS_TOKEN,sharedPrefrence.getRefreshToken(getApplicationContext()));
                                Intent intent=new Intent(this,LoginActivity.class);
                                startActivity(intent);
                            }

                        }
                        else if(response.get("code").toString().equals(GlobalConstants.INAVLID_TOKEN))
                        {
                            toastActivity.makeUknownErrorMessage(this);

                        }

                        else
                        {
                            toastActivity.makeToastMessage(response,this);
                            questionLists=getList(response);
                            List<QuestionList>question=questionLists;
                            showView(question);
                            //TODO: show test view
                        }
                    }catch (Exception e)
                    {
                        LogHelper logHelper=new LogHelper(e);
                        e.printStackTrace();
                    }

                    break;
                }
                case GET_ACCESS_TOKEN:
                {
                    try{
                        if(response.get("sucess").toString().equals("false"))
                        {
                            toastActivity.makeToastMessage(response,this);
                        }

                        else
                        {
                            sharedPrefrence.saveAccessToken(getApplicationContext(),response.get("access_token").toString(),response.get("refresh_token").toString());
                            access_token=response.get("access_token").toString();
                            new RemoteHelper(getApplicationContext()).getQuestion(this, RemoteCalls.GET_QUESTION, tag, id, access_token);
                        }
                    }catch (Exception e)
                    {
                        LogHelper logHelper=new LogHelper(e);
                        e.printStackTrace();
                    }
                    break;
                }
            }

        }



    }

}
