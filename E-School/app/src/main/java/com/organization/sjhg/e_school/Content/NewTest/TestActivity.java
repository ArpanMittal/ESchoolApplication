package com.organization.sjhg.e_school.Content.NewTest;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;

import android.graphics.PorterDuff;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.organization.sjhg.e_school.Database.contracts.UserContract;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.Helpers.QuestionAdapter;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
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
import java.util.concurrent.TimeUnit;

/**
 * Created by arpan on 9/7/2016.
 */
public class TestActivity extends AppCompatActivity implements RemoteCallHandler {
    private ToastActivity toastActivity=new ToastActivity();
    private static Animation fadeIn,fadeOut;
    private static boolean is_fadeOut=false;
    private SharedPrefrence sharedPrefrence=new SharedPrefrence();
    private String access_token;
    private  String tag="";
    private  String id="";
    private List<QuestionList>  questionLists=new ArrayList<>();
    ProgressBarActivity progressBarActivity=new ProgressBarActivity();
    Bundle saveInstances;
    private View mProgressView,mNoInternet;
    double startTime=System.currentTimeMillis();
    double endTime ;
    int lastPageposition=0;

    long countDownTime=720000;

    int pageOffset;
    private static ProgressBar progress;
    private TabLayout tabLayout;
    private String title;
    private Toolbar toolbar;
    private static TextView countDown;
    private Boolean isSubmit;

    private TextView submit_btn;
    private ViewPager mViewPagerView;
    boolean is_submit_active=false;
    private static  CountDownTimer countDownTimer=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
        fadeIn.setDuration(1000);
        fadeIn.setFillAfter(true);

        fadeOut = new AlphaAnimation(1.0f , 0.0f);
        fadeOut.setDuration(1000);
        fadeOut.setFillAfter(true);
        mViewPagerView=(ViewPager)findViewById(R.id.viewpager_fragment);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.getProgressDrawable().setColorFilter(Color.parseColor("#ff5722"), PorterDuff.Mode.SRC_IN);

        if(savedInstanceState==null) {
            getApplicationContext().getContentResolver().delete(UserContract.TestDetail.CONTENT_URI, null, null);

        }
        Intent intent=getIntent();
         tag=intent.getStringExtra("Tag");
         id=intent.getStringExtra("Id");
        title=intent.getStringExtra("Title");
        isSubmit=false;
        saveInstances=savedInstanceState;


        mProgressView=findViewById(R.id.dashboard_progress);



        tabLayout=(TabLayout)findViewById(R.id.id_tabs);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        countDown=(TextView)toolbar.findViewById(R.id.countDown);
        submit_btn=(TextView)toolbar.findViewById(R.id.submitButton);

//       progress.getProgressDrawable().setColorFilter(Color.parseColor("#ff5722"), PorterDuff.Mode.SRC_IN);

//        progress = (ProgressBar) findViewById(R.id.progressBar);
        mNoInternet = findViewById(R.id.noInternetScreen);
        Button retry = (Button) findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoInternet.setVisibility(View.GONE);
                progressBarActivity.showProgress(mViewPagerView,mProgressView,true,getApplicationContext());
                access_token=sharedPrefrence.getAccessToken(getApplicationContext());
                if(access_token==null)
                {
                    Intent intent=new Intent(TestActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                if (!is_submit_active){
                    new RemoteHelper(getApplicationContext()).getQuestion(TestActivity.this, RemoteCalls.GET_QUESTION, tag, id, access_token);
                }else{
                    new RemoteHelper(getApplicationContext()).sendQuestionResponse(TestActivity.this, RemoteCalls.SEND_QUESTION_RESPONSE,tag,id, access_token,makeResponseList());
                }
            }
        });
    }




    JSONObject makeResponseList()
    {
        JSONObject jsonObject=new JSONObject();
        try{
            JSONArray jsonArray=new JSONArray();
            Cursor cursor=getApplicationContext().getContentResolver().query(UserContract.TestDetail.CONTENT_URI,null,null,null,null);
            while(cursor.moveToNext())
            {
                JSONObject jsonObject1=new JSONObject();
                jsonObject1.put(getString(R.string.sendQuestionId),cursor.getString(cursor.getColumnIndex(UserContract.TestDetail.COLUMN_QUESTION_ID)));
                jsonObject1.put(getString(R.string.sendIsCorrect),cursor.getString(cursor.getColumnIndex(UserContract.TestDetail.COLUMN_IS_CORRECT)));
                jsonObject1.put(getString(R.string.sendOptionId),cursor.getString(cursor.getColumnIndex(UserContract.TestDetail.COLUMN_OPTION_ID)));
                jsonObject1.put(getString(R.string.sendTimeTaken),cursor.getString(cursor.getColumnIndex(UserContract.TestDetail.COLUMN_TIME_SPEND)));
                jsonArray.put(jsonObject1);
            }
            cursor.close();
            jsonObject.put("data",jsonArray);
        }catch (Exception e)
        {
            LogHelper logHelper=new LogHelper(e);
            e.printStackTrace();
        }

        return jsonObject;
    }

    private void showLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
        builder.setTitle(getString(R.string.test_submit_title));
        builder.setMessage(getString(R.string.test_submit_message));

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic

                        is_submit_active=true;
                        if(countDownTimer!=null) {
                            countDownTimer.cancel();
                            countDownTimer=null;
                            countDown.setVisibility(View.GONE);
                        }
                        access_token=sharedPrefrence.getAccessToken(getApplicationContext());
                        new RemoteHelper(getApplicationContext()).sendQuestionResponse(TestActivity.this, RemoteCalls.SEND_QUESTION_RESPONSE,tag,id, access_token,makeResponseList());
                        progressBarActivity.showProgress(mViewPagerView,mProgressView,true,getApplicationContext());
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        showLocationDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(saveInstances==null) {
            if (questionLists.isEmpty())
            {
                progressBarActivity.showProgress(mViewPagerView,mProgressView,true,getApplicationContext());

                access_token=sharedPrefrence.getAccessToken(getApplicationContext());
                if(access_token==null)
                {
                    Intent intent=new Intent(this,LoginActivity.class);
                    startActivity(intent);
                }
                new RemoteHelper(getApplicationContext()).getQuestion(this, RemoteCalls.GET_QUESTION, tag, id, access_token);
            }
        }
        else
        {
            questionLists=(List<QuestionList>)saveInstances.getSerializable("Question List");
            is_submit_active= saveInstances.getBoolean("Is_Submit");
            isSubmit=saveInstances.getBoolean("Is_NoInternetState");
            if(isSubmit)
                mNoInternet.setVisibility(View.VISIBLE);
            tag=saveInstances.getString("Tag");
            if(tag.equals(getString(R.string.samplepaper_tag)))
                countDownTime=saveInstances.getLong("CountDown");
            else
                countDown.setVisibility(View.GONE);
            if (questionLists!=null&&!questionLists.isEmpty()){
                showView(questionLists,true);
            }else{
                mNoInternet.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(questionLists!=null) {
            outState.putSerializable("Question List", (Serializable) questionLists);
           outState.putLong("CountDown",countDownTime);
            outState.putBoolean("Is_Submit",is_submit_active);
            outState.putBoolean("Is_NoInternetState",isSubmit);
            outState.putString("Tag",tag);
        }

    }

    private void insertIntoDatabse(List<QuestionList> questions)
    {
        ContentValues [] contentValues=new ContentValues[questions.size()];
        for(int position=0;position<questions.size();position++) {
            contentValues[position]=new ContentValues();
            contentValues[position].put(UserContract.TestDetail.COLUMN_QUESTION_ID, questions.get(position).id);
            contentValues[position].put(UserContract.TestDetail.COLUMN_OPTION_ID, "");
            contentValues[position].put(UserContract.TestDetail.COLUMN_TIME_SPEND, 0.0);
            contentValues[position].put(UserContract.TestDetail.COLUMN_IS_CORRECT, "empty");
        }
       int count= getApplicationContext().getContentResolver().bulkInsert(UserContract.TestDetail.CONTENT_URI,contentValues);
    }

    private void showView(final List<QuestionList> questions,boolean is_resume)
    {


        QuestionAdapter questionAdapter=new QuestionAdapter(getSupportFragmentManager(),questions,getApplicationContext());
        if(!is_resume)
            insertIntoDatabse(questions);
        submit_btn.setVisibility(View.VISIBLE);
        mViewPagerView.setAdapter(questionAdapter);

        tabLayout = (TabLayout) findViewById(R.id.id_tabs);
        tabLayout.setupWithViewPager(mViewPagerView);
       //for sample paper tag show timer

        //TODO: change for practice test
        if(tag.equals(getString(R.string.samplepaper_tag))&&(!is_submit_active)) {
            countDown.setVisibility(View.VISIBLE);
                if(countDownTimer==null) {

                    countDownTimer = new CountDownTimer(countDownTime, 1000) {

                        public void onTick(long millisUntilFinished) {
                            if(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)==0&&TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))<30) {
                               // countDown.setTextColor(Color.parseColor("#ff5722"));
                                if(is_fadeOut==true) {
                                    countDown.startAnimation(fadeIn);
                                    is_fadeOut=false;
                                }
                                else {
                                    countDown.startAnimation(fadeOut);
                                    is_fadeOut=true;
                                }
                            }


                            countDownTime = millisUntilFinished;
                            countDown.setText("" + String.format("%d min, %d sec",
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                        }


                        public void onFinish() {
                            if(countDownTimer!=null) {
                                countDownTimer.cancel();
                                countDownTimer=null;
                                countDown.setVisibility(View.GONE);
                            }
                            access_token = sharedPrefrence.getAccessToken(getApplicationContext());
                            new RemoteHelper(getApplicationContext()).sendQuestionResponse(TestActivity.this, RemoteCalls.SEND_QUESTION_RESPONSE, tag, id, access_token, makeResponseList());
                            progressBarActivity.showProgress(mViewPagerView, mProgressView, true, getApplicationContext());

                            //countDown.setText("done!");

                        }

                    }.start();
                }
        }
        progress.setMax(questionLists.size()-1);
        progress.setProgress(mViewPagerView.getCurrentItem());
        mViewPagerView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {



                progress.setProgress(position);
                // to detect left or right scroll
                if(position!=0||(position+1==1&&lastPageposition==1)) {

                    double diff = 0.0;
                    if (!is_submit_active)
                    {
                        Cursor cursor = getApplicationContext().getContentResolver().query(
                                UserContract.TestDetail.CONTENT_URI, null,
                                UserContract.TestDetail.COLUMN_QUESTION_ID + " =? ",
                                new String[]{questionLists.get(lastPageposition).id},
                                null,
                                null
                        );
                        if (cursor.getCount() > 0)
                            diff = cursor.getColumnIndex(UserContract.TestDetail.COLUMN_TIME_SPEND);
                        cursor.close();
                        endTime = System.currentTimeMillis();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(UserContract.TestDetail.COLUMN_TIME_SPEND, (endTime - startTime + diff));
                        // insert time spend on previous question
                        int result = getApplicationContext().getContentResolver().update(UserContract.TestDetail.CONTENT_URI, contentValues,
                                UserContract.TestDetail.COLUMN_QUESTION_ID + "=?",
                                new String[]{questionLists.get(lastPageposition).id});

                    }

                    startTime = System.currentTimeMillis();

                    lastPageposition = position;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuestionTime(mViewPagerView.getCurrentItem());
                showLocationDialog();
            }
        });

    }
    private void saveQuestionTime(int position)
    {
        double diff = 0.0;
        if (!is_submit_active)
        {
            Cursor cursor = getApplicationContext().getContentResolver().query(
                    UserContract.TestDetail.CONTENT_URI, null,
                    UserContract.TestDetail.COLUMN_QUESTION_ID + " =? ",
                    new String[]{questionLists.get(position).id},
                    null,
                    null
            );
            if (cursor.getCount() > 0)
                diff = cursor.getColumnIndex(UserContract.TestDetail.COLUMN_TIME_SPEND);
            cursor.close();
            endTime = System.currentTimeMillis();

            ContentValues contentValues = new ContentValues();
            contentValues.put(UserContract.TestDetail.COLUMN_TIME_SPEND, (endTime - startTime + diff));
            // insert time spend on previous question
            int result = getApplicationContext().getContentResolver().update(UserContract.TestDetail.CONTENT_URI, contentValues,
                    UserContract.TestDetail.COLUMN_QUESTION_ID + "=?",
                    new String[]{questionLists.get(position).id});

        }
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
        mNoInternet.setVisibility(View.GONE);
        if(!isSuccessful)
        {
            mNoInternet.setVisibility(View.VISIBLE);
            switch (callFor){
                case SEND_QUESTION_RESPONSE:
                {
                    isSubmit=true;
                    break;
                }
            }
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
                            showView(question,false);

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
                case SEND_QUESTION_RESPONSE:
                {
                    try {
                        if (response.get("code").toString().equals(GlobalConstants.EXPIRED_TOKEN)) {

                            if (sharedPrefrence.getRefreshToken(getApplicationContext()) == null) {

                                toastActivity.makeToastMessage(response, this);
                                break;
                            } else {
                                // new RemoteHelper(getApplicationContext()).getAccessToken(this,RemoteCalls.GET_ACCESS_TOKEN,sharedPrefrence.getRefreshToken(getApplicationContext()));
                                Intent intent = new Intent(this, LoginActivity.class);
                                startActivity(intent);
                            }

                        } else if (response.get("code").toString().equals(GlobalConstants.INAVLID_TOKEN)) {
                            toastActivity.makeUknownErrorMessage(this);

                        } else {


                            Intent intent = new Intent(this, TestReportActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("parent_tag",tag);
                            intent.putExtra("parent_id",id);
                            intent.putExtra("parent_title",title);
                            String id= response.get("data").toString();
                            intent.putExtra("Id",id);
                            startActivity(intent);
                            finish();
                        }
                    }catch (Exception e)
                    {
                        LogHelper logHelper=new LogHelper(e);
                        e.printStackTrace();
                    }

                }
            }

        }



    }
}
