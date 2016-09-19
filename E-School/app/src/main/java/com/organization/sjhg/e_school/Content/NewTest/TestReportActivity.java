package com.organization.sjhg.e_school.Content.NewTest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.numetriclabz.numandroidcharts.ChartData;
import com.numetriclabz.numandroidcharts.StackBarChart;
import com.organization.sjhg.e_school.Helpers.BarGraphAdapter;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.ListStructure.BarGraphList;
import com.organization.sjhg.e_school.ListStructure.CountList;
import com.organization.sjhg.e_school.ListStructure.StackGraphList;
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
    String parent_tag="";
    private Button btn;
    List<StackGraphList>stackGraphLists=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent=getIntent();
        id=intent.getStringExtra("Id");
        parent_tag=intent.getStringExtra("parent_tag");
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
                intent1.putExtra("parent_tag",parent_tag);
                startActivity(intent1);
            }
        });
        access_token=sharedPrefrence.getAccessToken(getApplicationContext());
        if(savedInstanceState==null)
        {
            progressBarActivity.showProgress(mDashboardView,mProgressView,true,this);
           //showView();
           new RemoteHelper(getApplicationContext()).getTestSummary(this, RemoteCalls.GET_TEST_RESPONSE,"1" , "Test_Detail", id, access_token);
        }
        else
        {
            barGraphLists=(List<BarGraphList>)savedInstanceState.getSerializable("LIST");
            stackGraphLists=(List<StackGraphList>)savedInstanceState.getSerializable("Ser_List");
            showView();
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("LIST",(Serializable)barGraphLists);
        outState.putSerializable("Ser_List",(Serializable)stackGraphLists);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(parent_id!=null) {
                    Intent intent = new Intent(this, TestSummaryActivity.class);
                    intent.putExtra("Id", parent_id);
                    intent.putExtra("Tag",parent_tag);
                    startActivity(intent);
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

        StackBarChart stackBarChart=(StackBarChart)findViewById(R.id.stackbarchart);
        List<ChartData> value = new ArrayList<>();

        Float[] value1 = {2f,3f,6f,5f };
        Float[] value2 = {3f,5f,7f,9f };

        value.add(new ChartData(value1, "Jone"));
        value.add(new ChartData(value2, "Joe"));

        List<String> h_lables = new ArrayList<>();
        h_lables.add("sun");
        h_lables.add("mon");
        h_lables.add("tue");
        h_lables.add("wed");

        stackBarChart.setHorizontal_label(h_lables);

        stackBarChart.setData(value);

        stackBarChart.setDescription("Stacked bar Chart");


    }

    private void makeList(JSONObject response)
    {

        try {
            String hj=response.getString("success");
            //JSONObject jsonObject1=response.getJSONObject(getString(R.string.data));
            JSONArray jsonArray = response.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                JSONArray jsonArray1=jsonObject.getJSONArray(getString(R.string.groupdata));
                for(int j=0;j<jsonArray1.length();j++) {
                    JSONObject jsonObject1=jsonArray1.getJSONObject(j);
                    int correct_attempt = jsonObject.getInt(getString(R.string.jsoncorrectattempt));
                    int attempt_question = jsonObject.getInt(getString(R.string.jsonattemptquestion));
                    int total_question = jsonObject.getInt(getString(R.string.jsontotalquestion));
                    String title = jsonObject.get(getString(R.string.jsontitle)).toString();
                    barGraphLists.add(new BarGraphList(correct_attempt, attempt_question, total_question, title));
                }
                JSONArray jsonArray2=jsonObject.getJSONArray(getString(R.string.stackgraphdata));

                for(int j=0;j<jsonArray2.length();j++)
                {
                    JSONObject jsonObject1=jsonArray2.getJSONObject(j);
                    String head=jsonObject1.getString(getString(R.string.jsonhead));
                    JSONArray jsonArray3=jsonObject1.getJSONArray(getString(R.string.jsonesy_count));
                    List<CountList>countLists=new ArrayList<>();
                    for(int k=0;k<jsonArray3.length();k++)
                    {
                        JSONObject jsonObject2=jsonArray3.getJSONObject(k);
                        int count=jsonObject2.getInt(getString(R.string.jsoncount));
                        String title=jsonObject2.getString(getString(R.string.jsontitle));
                        countLists.add(new CountList(count,title));
                    }
                    stackGraphLists.add(new StackGraphList(countLists,head));
                }
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
                            new RemoteHelper(getApplicationContext()).getTestSummary(this, RemoteCalls.GET_TEST_RESPONSE,"1" , "Test_Detail", id, access_token);
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
