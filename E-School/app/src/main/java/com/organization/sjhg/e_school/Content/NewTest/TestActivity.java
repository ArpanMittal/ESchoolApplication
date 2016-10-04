package com.organization.sjhg.e_school.Content.NewTest;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;

import android.graphics.PorterDuff;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.organization.sjhg.e_school.Database.contracts.UserContract;
import com.organization.sjhg.e_school.Helpers.ConnectivityReceiver;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.Helpers.QuestionAdapter;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.ListStructure.QuestionList;

import com.organization.sjhg.e_school.LoginActivity;
import com.organization.sjhg.e_school.Main_Activity;
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
 * Created by arpan on 9/7/2016.
 */
public class TestActivity extends AppCompatActivity implements RemoteCallHandler, ConnectivityReceiver.ConnectivityReceiverListener {
    private ToastActivity toastActivity=new ToastActivity();
    private SharedPrefrence sharedPrefrence=new SharedPrefrence();
    private String access_token;
    private  String tag="";
    private  String id="";
    private List<QuestionList>  questionLists=new ArrayList<>();
    ProgressBarActivity progressBarActivity=new ProgressBarActivity();
    Bundle saveInstances;
    private View mProgressView;
    double startTime=System.currentTimeMillis();
    double endTime ;
    int lastPageposition=0;
    int pageOffset;
    ProgressBar progress;
    private TabLayout tabLayout;
    private String title;
    private Toolbar toolbar;


    private TextView submit_btn;
    private ViewPager mViewPagerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationContext().getContentResolver().delete(UserContract.TestDetail.CONTENT_URI, null, null);
        Intent intent=getIntent();
         tag=intent.getStringExtra("Tag");
         id=intent.getStringExtra("Id");
        title=intent.getStringExtra("Title");

        saveInstances=savedInstanceState;
        setContentView(R.layout.activity_test);
        mProgressView=findViewById(R.id.dashboard_progress);

        mViewPagerView=(ViewPager)findViewById(R.id.viewpager_fragment);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        tabLayout=(TabLayout)findViewById(R.id.id_tabs);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        submit_btn=(TextView)toolbar.findViewById(R.id.submitButton);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocationDialog();
            }
        });
       progress.getProgressDrawable().setColorFilter(Color.parseColor("#ff5722"), PorterDuff.Mode.SRC_IN);

        progress = (ProgressBar) findViewById(R.id.progressBar); progress = (ProgressBar) findViewById(R.id.progressBar);
        ConnectivityReceiver.isConnected();
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
            if (questionLists.isEmpty()) {
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
            showView(questionLists);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(questionLists!=null) {
            outState.putSerializable("Question List", (Serializable) questionLists);
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

    private void showView(final List<QuestionList> questions)
    {


        QuestionAdapter questionAdapter=new QuestionAdapter(getSupportFragmentManager(),questions,getApplicationContext());
        insertIntoDatabse(questions);
        mViewPagerView.setAdapter(questionAdapter);
        tabLayout = (TabLayout) findViewById(R.id.id_tabs);
        tabLayout.setupWithViewPager(mViewPagerView);
        mViewPagerView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {


                progress.setMax(questionLists.size()-1);
                progress.setProgress(position);
                // to detect left or right scroll
                if(position!=0||(position+1==1&&lastPageposition==1))
                {
//                    if(position<lastPageposition)
//                        pageOffset=-1;
//                    else
//                        pageOffset=1;
                    // for timer of each question
                    double diff=0.0;
                    Cursor cursor = getApplicationContext().getContentResolver().query(
                            UserContract.TestDetail.CONTENT_URI, null,
                            UserContract.TestDetail.COLUMN_QUESTION_ID+" =? ",
                            new String[]{questionLists.get(lastPageposition).id},
                            null,
                            null
                    );
                    if(cursor.getCount()>0)
                        diff = cursor.getColumnIndex(UserContract.TestDetail.COLUMN_TIME_SPEND);
                    cursor.close();
                        endTime = System.currentTimeMillis();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(UserContract.TestDetail.COLUMN_TIME_SPEND,(endTime - startTime+diff));
                    // insert time spend on previous question
                        int result = getApplicationContext().getContentResolver().update(UserContract.TestDetail.CONTENT_URI, contentValues,
                                UserContract.TestDetail.COLUMN_QUESTION_ID + "=?",
                                new String[]{questionLists.get(lastPageposition).id});

                }

                startTime=System.currentTimeMillis();

                lastPageposition=position;

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
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("parent_tag",tag);
                            intent.putExtra("parent_id",id);
                            intent.putExtra("parent_title",title);
                            String id=(String)response.get("data").toString();
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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    protected void showSnack(boolean isConnected) {
        String message;
        int color;
        Snackbar snackbar;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
            snackbar = Snackbar
                    .make(findViewById(R.id.coordinatorLayout), message, Snackbar.LENGTH_LONG);
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            snackbar = Snackbar
                    .make(findViewById(R.id.coordinatorLayout), message, Snackbar.LENGTH_INDEFINITE);
        }

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }
}
