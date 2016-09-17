package com.organization.sjhg.e_school.Content.NewTest;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.organization.sjhg.e_school.Fragments.Notes_Listing_Fragment;
import com.organization.sjhg.e_school.Helpers.BarGraphAdapter;
import com.organization.sjhg.e_school.Helpers.Custom_Pager_Adapter;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.Helpers.TestPaperAttemptAdapter;
import com.organization.sjhg.e_school.ListStructure.BarGraphList;
import com.organization.sjhg.e_school.ListStructure.InternalList;
import com.organization.sjhg.e_school.LoginActivity;
import com.organization.sjhg.e_school.MainParentActivity;
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

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by arpan on 9/7/2016.
 */
public class TestReportActivity extends AppCompatActivity implements RemoteCallHandler{

    private View mDashboardView;
    private View mProgressView;
    private Button button;
    List<BarGraphList> barGraphLists=new ArrayList<>();
    private ProgressBarActivity progressBarActivity=new ProgressBarActivity();
    private ToastActivity toastActivity=new ToastActivity();
    private SharedPrefrence sharedPrefrence=new SharedPrefrence();
    private String access_token;
    String id="";
    String parent_id=null;
    private Button btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent=getIntent();
        id=intent.getStringExtra("Id");
        parent_id=intent.getStringExtra("parent_id");
        setContentView(R.layout.activity_test_instruction_activity);
        mProgressView= findViewById(R.id.login_progress);
        mDashboardView=findViewById(R.id.dashboard_form);
        btn=(Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(TestReportActivity.this,TestAnswerActivity.class);
                intent1.putExtra("Id",id);
                intent1.putExtra("parent_id",parent_id);
                startActivity(intent1);
            }
        });
        access_token=sharedPrefrence.getAccessToken(getApplicationContext());
        if(savedInstanceState==null)
        {
            progressBarActivity.showProgress(mDashboardView,mProgressView,true,this);
           //showView();
           new RemoteHelper(getApplicationContext()).getTestSummary(this, RemoteCalls.GET_TEST_RESPONSE,"Test_Detail",id, access_token);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(parent_id!=null) {
                    Intent intent = new Intent(this, TestSummaryActivity.class);
                    intent.putExtra("Id", parent_id);
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

    private void showView()
    {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        BarGraphAdapter barGraphAdapter=new BarGraphAdapter(barGraphLists,this);
        recyclerView.setAdapter(barGraphAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // for animation in listview
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

    }

    private void makeList(JSONObject response)
    {

        try {
            String hj=response.getString("success");
            //JSONObject jsonObject1=response.getJSONObject(getString(R.string.data));
            JSONArray jsonArray = response.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                int correct_attempt=jsonObject.getInt(getString(R.string.jsoncorrectattempt));
                int attempt_question=jsonObject.getInt(getString(R.string.jsonattemptquestion));
                int total_question=jsonObject.getInt(getString(R.string.jsontotalquestion));
                String title=jsonObject.get(getString(R.string.jsontitle)).toString();
                barGraphLists.add(new BarGraphList(correct_attempt,attempt_question,total_question,title));
            }
        }catch (Exception e)
        {
            new LogHelper(e);
            e.printStackTrace();
        }

    }


    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        progressBarActivity.showProgress(mDashboardView,mProgressView,false,getApplicationContext());
        if(!isSuccessful)
        {
            toastActivity.makeUknownErrorMessage(this);
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
                            makeList(response);
                            showView();
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
                            new RemoteHelper(getApplicationContext()).getTestSummary(this, RemoteCalls.GET_TEST_RESPONSE,"Attempt_Number",id, access_token);
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
