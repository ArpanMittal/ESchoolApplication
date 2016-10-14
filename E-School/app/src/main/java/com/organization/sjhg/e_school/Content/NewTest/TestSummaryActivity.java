package com.organization.sjhg.e_school.Content.NewTest;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.organization.sjhg.e_school.Fragments.Notes_Listing_Fragment;
import com.organization.sjhg.e_school.Helpers.Custom_Pager_Adapter;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.Helpers.TestPaperAttemptAdapter;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.ListStructure.ItemDataList;
import com.organization.sjhg.e_school.LoginActivity;
import com.organization.sjhg.e_school.MainParentActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.SearchActivity;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.Utils.ProgressBarActivity;
import com.organization.sjhg.e_school.Utils.SharedPrefrence;
import com.organization.sjhg.e_school.Utils.ToastActivity;

import net.sf.andpdf.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by arpan on 9/13/2016.
 */
public class TestSummaryActivity extends AppCompatActivity implements RemoteCallHandler {
    private View mDashboardView;
    private View mProgressView, mNoInternet;
    private ProgressBarActivity progressBarActivity=new ProgressBarActivity();
    private SharedPrefrence sharedPrefrence=new SharedPrefrence();
    private ToastActivity toastActivity=new ToastActivity();
    private String tag;
    private String id;
    private String access_token;
    private String title;
    private String parent_id;
    private String parent_title;
    private String activity_name;
    private List<ChapterList> chapterListList=new ArrayList<>();
    private ImageView empty_state;
    private RelativeLayout empty_state_layout;
    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_summary);
        Intent intent=getIntent();
        id=intent.getStringExtra("Id");
        tag = intent.getStringExtra("Tag");
        title=intent.getStringExtra("Title");
        parent_id=intent.getStringExtra("Parent_id");
        parent_title=intent.getStringExtra("Parent_title");
        activity_name=intent.getStringExtra("Activity_Name");
        empty_state=(ImageView)findViewById(R.id.emptyState);
        empty_state_layout=(RelativeLayout) findViewById(R.id.emptyStateLayout);
        if(title!=null)
            getSupportActionBar().setTitle(title);
        else
            getSupportActionBar().setTitle("TestSummary");

        mDashboardView=findViewById(R.id.dashboard_form);
        mProgressView=findViewById(R.id.dashboard_progress);
        mNoInternet = findViewById(R.id.noInternetScreen);
        Button retry = (Button) findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoInternet.setVisibility(View.GONE);
                progressBarActivity.showProgress(mDashboardView,mProgressView,true,getApplicationContext());
                new RemoteHelper(getApplicationContext()).getTestSummary(TestSummaryActivity.this, RemoteCalls.GET_TEST_RESPONSE, tag, "Attempt_Number", id, access_token);
            }
        });

        access_token=sharedPrefrence.getAccessToken(getApplicationContext());

            if (savedInstanceState != null) {

                chapterListList = (List<ChapterList>) savedInstanceState.getSerializable("INTERNAL LIST");
                if(chapterListList!=null && !chapterListList.isEmpty()){
                    showView();
                }else{
                    mNoInternet.setVisibility(View.VISIBLE);
                }

            }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(parent_id!=null) {
                    Intent intent = new Intent(this, TestSummaryActivity.class);
                    intent.putExtra(getString(R.string.jsonid), parent_id);
                    intent.putExtra(getString(R.string.jsontitle),parent_title);
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
        if(chapterListList.isEmpty())
        {
            access_token=sharedPrefrence.getAccessToken(getApplicationContext());
            progressBarActivity.showProgress(mDashboardView, mProgressView, true, getApplicationContext());
            new RemoteHelper(getApplicationContext()).getTestSummary(this, RemoteCalls.GET_TEST_RESPONSE, tag, "Attempt_Number", id, access_token);
        }
        else
        {
           // chapterListList = (List<ChapterList>) savedInstanceState.getSerializable("INTERNAL LIST");
            showView();
        }
    }





    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("INTERNAL LIST", (Serializable) chapterListList);
    }


    private void makeList(JSONObject response)
    {
        try
        {
            JSONArray jsonArray=response.getJSONArray(getString(R.string.data));
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String id=jsonObject.getString(getString(R.string.jsonid));
                String time=jsonObject.getString(getString(R.string.jsontime));
                Date date = new Date(Integer.parseInt(time)*1000L);
                SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
                String times = localDateFormat.format(date);
                DateFormat outputFormatter = new SimpleDateFormat("EEE, d MMM yyyy");
                String output = outputFormatter.format(date);
                chapterListList.add(new ChapterList(id,output+" "+times));
            }

        }catch (Exception e)
        {
            LogHelper logHelper=new LogHelper(e);
            e.printStackTrace();
        }
    }

    private void showView()
    {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setVisibility(View.VISIBLE);
        empty_state_layout.setVisibility(View.GONE);
        recyclerView.setHasFixedSize(true);

        TestPaperAttemptAdapter testPaperAttemptAdapter=new TestPaperAttemptAdapter(this,chapterListList,id,tag,title,activity_name);
        recyclerView.setAdapter(testPaperAttemptAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // for animation in listview
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

    }

    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        progressBarActivity.showProgress(mDashboardView,mProgressView,false,getApplicationContext());
        mNoInternet.setVisibility(View.GONE);
        if(!isSuccessful)
        {
            mNoInternet.setVisibility(View.VISIBLE);
        }
        else
        {
            switch (callFor){
                case GET_TEST_RESPONSE:
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

                        }
                        else
                        {
                            if(response.get("code").toString().equals("401"))
                            {

                               // Toast.makeText(TestSummaryActivity.this, "no attempt found", Toast.LENGTH_SHORT).show();

                                //finish();
                            }

                            else {
                                makeList(response);
                                showView();
                            }
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
                            new RemoteHelper(getApplicationContext()).getTestSummary(this, RemoteCalls.GET_TEST_RESPONSE, tag, "Attempt_Number", id, access_token);
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

    private void showEmptyView() {
        mDashboardView.setVisibility(View.GONE);
        ImageView imageView=(ImageView)findViewById(R.id.emptyState);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.test_empty_state);

    }


}
