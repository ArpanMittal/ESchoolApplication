package com.organization.sjhg.e_school.Content.NewTest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.organization.sjhg.e_school.Content.Test.*;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.ListStructure.BarGraphList;
import com.organization.sjhg.e_school.ListStructure.StackGraphList;
import com.organization.sjhg.e_school.ListStructure.TimeGraphList;
import com.organization.sjhg.e_school.LoginActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.Utils.ProgressBarActivity;
import com.organization.sjhg.e_school.Utils.SharedPrefrence;
import com.organization.sjhg.e_school.Utils.ToastActivity;
import com.organization.sjhg.e_school.Utils.ValueFormatter;

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
    private View mProgressView,mNoInternet;
    private Button button;
    List<BarGraphList> barGraphLists=new ArrayList<>();
    List<TimeGraphList> timeGraphLists=new ArrayList<>();
    private ProgressBarActivity progressBarActivity=new ProgressBarActivity();
    private ToastActivity toastActivity=new ToastActivity();
    private SharedPrefrence sharedPrefrence=new SharedPrefrence();
    private String access_token;
    String id="";
    String parent_id=null;
    String parent_tag="";
    String parent_title="";
    String grand_parent_id="";
    private Button btn;

    List<StackGraphList>stackGraphLists=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent=getIntent();
        id=intent.getStringExtra("Id");
        parent_tag=intent.getStringExtra("parent_tag");
        parent_id=intent.getStringExtra("parent_id");
        parent_title=intent.getStringExtra("parent_title");
        grand_parent_id=intent.getStringExtra("Grand_Parent_Title");
        setContentView(R.layout.activity_test_instruction_activity);
        mProgressView= findViewById(R.id.login_progress);
        mDashboardView=findViewById(R.id.dashboard_form);


        btn=(Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(TestReportActivity.this,TestAnswerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("Id",id);
                intent1.putExtra("parent_id",parent_id);
                intent1.putExtra("parent_tag",parent_tag);
                intent1.putExtra("parent_title",parent_title);

                startActivity(intent1);
                finish();
            }
        });
        access_token=sharedPrefrence.getAccessToken(getApplicationContext());
        mNoInternet = findViewById(R.id.noInternetScreen);
        Button retry = (Button) findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoInternet.setVisibility(View.GONE);
                TestReportActivity.this.onResume();
            }
        });
       if(savedInstanceState!=null)
        {
            barGraphLists=(List<BarGraphList>)savedInstanceState.getSerializable("LIST");
            stackGraphLists=(List<StackGraphList>)savedInstanceState.getSerializable("Ser_List");
            timeGraphLists=(List<TimeGraphList>)savedInstanceState.getSerializable("Tim_List");
            if (barGraphLists!=null&& !barGraphLists.isEmpty()&&stackGraphLists!=null&& !stackGraphLists.isEmpty()&&timeGraphLists!=null&& !timeGraphLists.isEmpty()){
                showView();
            }else{
                mNoInternet.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(barGraphLists.isEmpty())
        {
            access_token=sharedPrefrence.getAccessToken(getApplicationContext());
            progressBarActivity.showProgress(mDashboardView,mProgressView,true,this);
            //showView();
            new RemoteHelper(getApplicationContext()).getTestSummary(this, RemoteCalls.GET_TEST_RESPONSE,"1" , "Test_Detail", id, access_token);
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
        outState.putSerializable("LIST",(Serializable)barGraphLists);
        outState.putSerializable("Ser_List",(Serializable)stackGraphLists);
        outState.putSerializable("Tim_List",(Serializable)timeGraphLists);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(parent_id!=null) {
                    Intent intent = new Intent(this, TestSummaryActivity.class);
                    intent.putExtra("Id", parent_id);
                    intent.putExtra("Tag",parent_tag);
                    intent.putExtra("Title",parent_title);
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
    private void loadPieChart()
    {
        //pie chart data
        PieChart pieChart=(PieChart)findViewById(R.id.piechart);
        ArrayList<Entry> entries=new ArrayList<>();

        entries.add(new Entry(barGraphLists.get(3).correct_attempt,0));
        entries.add(new Entry((barGraphLists.get(3).attempt_question-barGraphLists.get(3).correct_attempt),1));
        entries.add(new Entry((barGraphLists.get(3).total_question-barGraphLists.get(3).attempt_question),2));

        ArrayList<String> label=new ArrayList<>();
        label.add("Correct");
        label.add("Wrong");
        label.add("Not Attempted");
        PieDataSet pieDataSet=new PieDataSet(entries,"");
        int[] color={Color.rgb(76,175,80),Color.rgb(255,82,82),Color.rgb(158,158,158)};
        pieDataSet.setColors(color);
        PieData data = new PieData(label, pieDataSet);
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);
        pieChart.animateY(2000);
        pieChart.animateX(2000);

        data.setValueFormatter(new ValueFormatter());
        pieChart.setDrawSliceText(false);
        pieChart.setDescription("");
    }
    private void loadStackGraph()
    {
        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        for(int i=0;i<stackGraphLists.size();i++) {
            valueSet1.add(new BarEntry(stackGraphLists.get(i).getArray(), i));
        }

        int[] color={Color.rgb(120, 211,244),Color.rgb(57 ,169,244),Color.rgb(16,87,155)};
        dataSets.add(new BarDataSet(valueSet1,""));
        dataSets.get(0).setColors(color);
        dataSets.get(0).setStackLabels(new String[]{"Easy", "Medium", "Hard"});

        BarChart barChart=(BarChart)findViewById(R.id.chart);

        String[] str={"All","Correct","Wrong","Not Attempted"};
        BarData data = new BarData(str,dataSets);
        barChart.setData(data);
        barChart.setDescription("");
        barChart.animateXY(2000, 2000);
//        barChart.setGridBackgroundColor(Color.TRANSPARENT);
        barChart.setDrawGridBackground(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        //barChart.getXAxis().setDrawAxisLine(false);
        data.setValueFormatter(new ValueFormatter());
        barChart.invalidate();
    }

    private void loadHorizontalGraph()
    {
        ArrayList<BarDataSet> dataSets2 = new ArrayList<>();
        ArrayList<BarEntry> valueSet11 = new ArrayList<>();
        ArrayList<BarEntry> valueSet21 = new ArrayList<>();
        for(int i=0;i<timeGraphLists.size();i++)
        {
            valueSet11.add(new BarEntry((float)timeGraphLists.get(i).total_avg/1000,i));
            valueSet21.add(new BarEntry((float)timeGraphLists.get(i).user_avg/1000,i));
        }
        BarDataSet barDataSet1 = new BarDataSet(valueSet11, "Total Avg(in sec)");
        barDataSet1.setColor(Color.rgb(250 ,137,0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet21, "User Avg(in sec)");
        barDataSet2.setColor(Color.rgb(0,167,250));
        dataSets2.add(barDataSet1);
        dataSets2.add(barDataSet2);
        String[] str1={"Total","Easy","Medium","Hard"};
        HorizontalBarChart horizontalBarChart=(HorizontalBarChart)findViewById(R.id.horizontalchart);
        BarData data1 = new BarData(str1,dataSets2);
        horizontalBarChart.setData(data1);
        horizontalBarChart.setDescription("");
        horizontalBarChart.animateXY(2000, 2000);
        horizontalBarChart.setDrawGridBackground(false);
        horizontalBarChart.getAxisLeft().setEnabled(false);
        horizontalBarChart.getAxisRight().setEnabled(false);
        horizontalBarChart.invalidate();

    }

    private void showView()
    {
        //for summary
        loadPieChart();

        //for accuracy graph
        loadBarGraph();


        // for stack graph
       loadStackGraph();



        // for horizontal chart
       loadHorizontalGraph();


    }

    private void loadBarGraph()
    {
        BarChart barChart=(BarChart)findViewById(R.id.barchart);
        BarData data = new BarData(getXAxisValues(), getDataSet(barGraphLists));
        barChart.setData(data);
        barChart.setDescription("");
        barChart.animateXY(2000, 2000);
        data.setValueFormatter(new ValueFormatter());
        barChart.invalidate();
        barChart.setDrawGridBackground(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
    }

    private ArrayList<BarDataSet> getDataSet(List<BarGraphList> dataList) {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        ArrayList<BarEntry> valueSet3 = new ArrayList<>();
        for (int i=0;i<dataList.size();i++){
            BarEntry v1e1 = new BarEntry((dataList.get(i).correct_attempt), i); // Jan
            valueSet1.add(v1e1);
            BarEntry v1e2 = new BarEntry((dataList.get(i).attempt_question), i); // Jan
            valueSet2.add(v1e2);
            BarEntry v1e3 = new BarEntry(new float[]{(dataList.get(i).total_question)}, i); // Jan
            valueSet3.add(v1e3);
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Correct");
        barDataSet1.setColor(Color.rgb(120, 211,244));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Attempted");
        barDataSet2.setColor(Color.rgb(57 ,169,244));
        BarDataSet barDataSet3 = new BarDataSet(valueSet3, "All");
        barDataSet3.setColor(Color.rgb(16,87,155));
        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);
        return dataSets;
    }
    private ArrayList<String> getXAxisValues()
    {
        String[] str1={"Easy","Medium","Hard","Total"};
        ArrayList<String> xAxis = new ArrayList<>();
        for(int i=0;i<4;i++) {
            xAxis.add(str1[i]);
        }
        return xAxis;
    }


    private void makeList(JSONObject response)
    {

        try {
            String hj=response.getString("success");
            //JSONObject jsonObject1=response.getJSONObject(getString(R.string.data));
            JSONObject jsonObject=response.getJSONObject(getString(R.string.data));
            JSONArray jsonArray=jsonObject.getJSONArray(getString(R.string.groupdata));
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                int correct_attempt = jsonObject1.getInt(getString(R.string.jsoncorrectattempt));
                int attempt_question = jsonObject1.getInt(getString(R.string.jsonattemptquestion));
                int total_question = jsonObject1.getInt(getString(R.string.jsontotalquestion));
                String title = jsonObject1.get(getString(R.string.jsontitle)).toString();
                barGraphLists.add(new BarGraphList(correct_attempt, attempt_question, total_question, title));
            }
            JSONArray jsonArray2=jsonObject.getJSONArray(getString(R.string.stackgraphdata));

            for(int j=0;j<jsonArray2.length();j++)
            {
                JSONObject jsonObject1=jsonArray2.getJSONObject(j);
                String head=jsonObject1.getString(getString(R.string.jsonhead));
                JSONObject jsonObject2=jsonObject1.getJSONObject(Integer.toString(0));
                int easy_count=jsonObject2.getInt(getString(R.string.jsoncount));
                JSONObject jsonObject3=jsonObject1.getJSONObject(Integer.toString(1));
                int medium_count=jsonObject3.getInt(getString(R.string.jsoncount));
                JSONObject jsonObject4=jsonObject1.getJSONObject(Integer.toString(2));
                int hard_count=jsonObject4.getInt(getString(R.string.jsoncount));


                stackGraphLists.add(new StackGraphList(head,easy_count,medium_count,hard_count));
            }

            JSONArray jsonArray3=jsonObject.getJSONArray(getString(R.string.jsontimegraphdata));
            for(int j=0;j<jsonArray3.length();j++)
            {
                JSONObject jsonObject1=jsonArray3.getJSONObject(j);
                double total_avg=jsonObject1.getDouble(getString(R.string.jsontotal_avg));
                double user_avg=jsonObject1.getDouble(getString(R.string.jsonuser_avg));
                timeGraphLists.add(new TimeGraphList(total_avg,user_avg));
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
