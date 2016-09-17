package com.organization.sjhg.e_school.Content.NewTest;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.Helpers.QuestionAdapter;
import com.organization.sjhg.e_school.Helpers.QuestionAnswerAdapter;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.ListStructure.QuestionAnswerList;
import com.organization.sjhg.e_school.ListStructure.QuestionList;
import com.organization.sjhg.e_school.LoginActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.Utils.ProgressBarActivity;
import com.organization.sjhg.e_school.Utils.SharedPrefrence;
import com.organization.sjhg.e_school.Utils.ToastActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpan on 9/16/2016.
 */
public class TestAnswerActivity extends AppCompatActivity implements RemoteCallHandler {
    private ToastActivity toastActivity=new ToastActivity();
    private SharedPrefrence sharedPrefrence=new SharedPrefrence();
    private String access_token;
    private  String parent_id="";
    private  String id="";
    private List<QuestionAnswerList> questionAnswerLists=new ArrayList<>();
    ProgressBarActivity progressBarActivity=new ProgressBarActivity();
    Bundle saveInstances;
    private View mProgressView;
    double startTime=System.currentTimeMillis();
    double endTime ;
    int lastPageposition=0;
    int pageOffset;
    ProgressBar progress;
    private ViewPager mViewPagerView;
    private TabLayout tabLayout;
    String parent_tag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        parent_id=intent.getStringExtra("parent_id");
        parent_tag=intent.getStringExtra("parent_tag");
        id=intent.getStringExtra("Id");

        saveInstances=savedInstanceState;
        setContentView(R.layout.activity_test_answer);

        mProgressView=findViewById(R.id.dashboard_progress);
        mViewPagerView=(ViewPager)findViewById(R.id.viewpager_fragment);
        progress = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(parent_id!=null) {
                    Intent intent = new Intent(this, TestReportActivity.class);
                    intent.putExtra("Id", id);
                    intent.putExtra("parent_id",parent_id);
                    intent.putExtra("parent_tag",parent_tag);
                    startActivity(intent);
                    finish();
                }else
                {
                    finish();
                }
                return  true;

        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(saveInstances==null) {
            if (questionAnswerLists.isEmpty()) {
                progressBarActivity.showProgress(mViewPagerView,mProgressView,true,getApplicationContext());

                access_token=sharedPrefrence.getAccessToken(getApplicationContext());
                if(access_token==null)
                {
                    Intent intent=new Intent(this,LoginActivity.class);
                    startActivity(intent);
                }
                new RemoteHelper(getApplicationContext()).getTestSummary(this, RemoteCalls.GET_TEST_RESPONSE,"1","Attempt_Question", id, access_token);
            }
        }
        else
        {
            questionAnswerLists=(List<QuestionAnswerList>)saveInstances.getSerializable("Question List");
            showView(questionAnswerLists);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Question List",(Serializable)questionAnswerLists);
    }

    private void showView(List<QuestionAnswerList>questionAnswerLists)
    {
   ;
        QuestionAnswerAdapter questionAnswerAdapter=new QuestionAnswerAdapter(getSupportFragmentManager(),questionAnswerLists,getApplicationContext());
        mViewPagerView.setAdapter(questionAnswerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.id_tabs);
        tabLayout.setupWithViewPager(mViewPagerView);
    }

    private List<QuestionAnswerList> getList(JSONObject response)
    {
        List<QuestionAnswerList> questionAnswerLists=new ArrayList<>();
        try{
            JSONArray data = response.getJSONArray(getString(R.string.data));
            for(int i=0;i<data.length();i++)
            {
                JSONObject jsonObject=data.getJSONObject(i);
                String id=jsonObject.getString(getString(R.string.jsonid));
                String time_taken=jsonObject.getString(getString(R.string.jsontimetaken));
                String response1=jsonObject.getString(getString(R.string.jsonresponse));
                String question_id=jsonObject.getString(getString(R.string.jsonQuestionid));
                String option_id=jsonObject.getString(getString(R.string.jsonoptionid));
                String question_text=jsonObject.getString(getString(R.string.jsonquestiontext));
                String solution_path=jsonObject.getString(getString(R.string.jsonsolutonpath));
                String difficulty=jsonObject.getString(getString(R.string.jsondifficulty));
                String question_image=jsonObject.getString(getString(R.string.jsonquestion_image));
                JSONArray jsonArray=jsonObject.getJSONArray(getString(R.string.jsoncorrect));
                List<ChapterList> chapterLists=new ArrayList<>();
                for(int j=0;j<jsonArray.length();j++)
                {
                    JSONObject jsonObject1=jsonArray.getJSONObject(j);
                    String id1=jsonObject1.getString(getString(R.string.jsonid));
                    String option=jsonObject1.getString(getString(R.string.jsonname));
                    chapterLists.add(new ChapterList(id1,option));
                }
                List<ChapterList> chapterLists1=new ArrayList<>();
                if(!response1.equals("empty"))
                {
                    JSONArray jsonArray1=jsonObject.getJSONArray(getString(R.string.jsonuseroption));

                    for(int j=0;j<jsonArray.length();j++)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(j);
                        String id1=jsonObject1.getString(getString(R.string.jsonid));
                        String option=jsonObject1.getString(getString(R.string.jsonname));
                        chapterLists1.add(new ChapterList(id1,option));
                    }
                }

                questionAnswerLists.add(new QuestionAnswerList(id,time_taken,response1,question_id,option_id,question_text,question_image,solution_path,difficulty,chapterLists,chapterLists1));
            }
        }catch (Exception e)
        {
            LogHelper logHelper=new LogHelper(e);
            e.printStackTrace();
        }
        return questionAnswerLists;
    }

    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        progressBarActivity.showProgress(mViewPagerView,mProgressView,false,getApplicationContext());
        if(!isSuccessful)
        {
            toastActivity.makeUknownErrorMessage(this);
        }
        else {

            switch (callFor) {
                case GET_TEST_RESPONSE:
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
                            questionAnswerLists=getList(response);
                            showView(questionAnswerLists);

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
                            new RemoteHelper(getApplicationContext()).getTestSummary(this, RemoteCalls.GET_TEST_RESPONSE,"1","Attempt_Question", id, access_token);
                           // new RemoteHelper(getApplicationContext()).getQuestion(this, RemoteCalls.GET_QUESTION, tag, id, access_token);
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
