package com.organization.sjhg.e_school;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.organization.sjhg.e_school.Fragments.Notes_Listing_Fragment;
import com.organization.sjhg.e_school.Helpers.Custom_Pager_Adapter;
import com.organization.sjhg.e_school.Helpers.Grid_Exam_Fragment;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.Helpers.StudentApplicationUserData;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.ListStructure.DashBoardList;
import com.organization.sjhg.e_school.ListStructure.ExamPrepareList;
import com.organization.sjhg.e_school.ListStructure.InternalList;
import com.organization.sjhg.e_school.ListStructure.InternalListData;
import com.organization.sjhg.e_school.ListStructure.ItemDataList;
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


import me.relex.circleindicator.CircleIndicator;

/**
 * Created by arpan on 8/31/2016.
 */
public class ExaminationParent extends MainParentActivity {

    Context context;

    ViewPager viewPager;


    private View mProgressView,mNoInternet;
    private String title;
    private String id;
    TabLayout tabLayout;
    private String name;
    private List<DashBoardList> list = new ArrayList<>();
    private SharedPrefrence sharedPrefrence=new SharedPrefrence();
    private ProgressBarActivity progressBarActivity = new ProgressBarActivity();
    private ToastActivity toastActivity = new ToastActivity();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        title = intent.getStringExtra(getString(R.string.jsontitle));
        id = intent.getStringExtra(getString(R.string.jsonid));
        name=intent.getStringExtra(getString(R.string.jsonname));
        ViewStub view_Stub = (ViewStub) findViewById(R.id.viewstub);
        view_Stub.setLayoutResource(R.layout.exam_app_bar);
        view_Stub.inflate();
        context = getApplicationContext();
        mProgressView = findViewById(R.id.dashboard_progress);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        viewPager = (ViewPager) findViewById(R.id.viewpager_fragment);
        // code repeated in all activity
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Notes_Listing_Fragment.class);
//                startActivity(intent);
//            }
//        });


        mNoInternet = findViewById(R.id.noInternetScreen);
        Button retry = (Button) findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoInternet.setVisibility(View.GONE);
                progressBarActivity.showProgress(viewPager, mProgressView, true, getApplicationContext());
                new RemoteHelper(context).getItemDetails(ExaminationParent.this, RemoteCalls.GET_EXAM_PREPARE_LIST, title, id);
            }
        });


        if (savedInstanceState != null) {
            list = (List<DashBoardList>) savedInstanceState.getSerializable("Exam List");
            if (list!=null && !list.isEmpty()){
                showView(list);
            }else{
                mNoInternet.setVisibility(View.VISIBLE);
            }
        } else {
            progressBarActivity.showProgress(viewPager, mProgressView, true, getApplicationContext());
            new RemoteHelper(context).getItemDetails(this, RemoteCalls.GET_EXAM_PREPARE_LIST, title, id);

        }


    }
    // Returns the page title for the top indicator


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(list!=null) {
            outState.putSerializable("Exam List", (Serializable) list);
        }
        outState.putSerializable("LIST",(Serializable)dataList);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.menu_search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this,SearchActivity.class)));
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<DashBoardList> getList(JSONObject response) {
        try {
            title = response.getString(getString(R.string.jsontitle));
            JSONArray data = response.getJSONArray(context.getString(R.string.data));
            List<ExamPrepareList> examPrepareLists = new ArrayList<>();
            List<ChapterList> chapterList=new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonObject = data.getJSONObject(i);
                String cost = jsonObject.getString(getString(R.string.cost));
                String id = jsonObject.getString(getString(R.string.jsonid));
                String duration = jsonObject.getString(getString(R.string.duration));
                JSONArray subject = jsonObject.getJSONArray(context.getString(R.string.jsonsubject));
                List<InternalListData> internalListDatas = new ArrayList<>();
                for (int j = 0; j < subject.length(); j++) {
                    JSONObject jsonObject1 = subject.getJSONObject(j);
                    String name = jsonObject1.getString(context.getString(R.string.jsonname));
                    String id_subject = jsonObject1.getString(context.getString(R.string.jsonid));
                    JSONArray chapter = jsonObject1.getJSONArray(context.getString(R.string.json_chapter));
                    List<ChapterList> chapterLists = new ArrayList<>();

                    for (int k = 0; k < chapter.length(); k++) {
                        JSONObject jsonObject2 = chapter.getJSONObject(k);
                        String name1 = jsonObject2.getString(context.getString(R.string.jsonname));
                        String id_chapter = jsonObject2.getString(context.getString(R.string.jsonid));
                        chapterLists.add(new ChapterList(id_chapter,name1));
                    }

                    internalListDatas.add(new InternalListData(name, id_subject, chapterLists));

                }
                examPrepareLists.add(new ExamPrepareList(cost, duration, id, internalListDatas));
                JSONArray samplePaper = jsonObject.getJSONArray(context.getString(R.string.jsonsamplepaper));
                for(int j=0;j<samplePaper.length();j++)
                {
                    JSONObject jsonObject1=samplePaper.getJSONObject(j);
                    String name=jsonObject1.getString(context.getString(R.string.jsonname));
                    String id1=jsonObject1.getString(context.getString(R.string.jsonid));
                    chapterList.add(new ChapterList(id1,name));
                }
            }
            list.add(new DashBoardList(title, examPrepareLists, chapterList));


        } catch (Exception e) {
            e.printStackTrace();
            new ToastActivity().makeJsonException((Activity) context);
            new LogHelper(e);
        }
        return list;

    }

    private void showView(List<DashBoardList> list)
    {
       // Custom_Pager_Adapter custom_pager_adapter=new Custom_Pager_Adapter(getSupportFragmentManager());
       // viewPager.setAdapter(custom_pager_adapter);
        Grid_Exam_Fragment grid_exam_fragment=new Grid_Exam_Fragment(getSupportFragmentManager(),list,context,id,title,this.id,name);
        viewPager.setAdapter(grid_exam_fragment);

        tabLayout = (TabLayout) findViewById(R.id.id_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        super.HandleRemoteCall(isSuccessful,callFor,response,exception);
        progressBarActivity.showProgress(viewPager,mProgressView,false,getApplicationContext());
        mNoInternet.setVisibility(View.GONE);
        if(!isSuccessful)
        {
            mNoInternet.setVisibility(View.VISIBLE);

        }
        else
        {
            switch (callFor) {
                case GET_EXAM_PREPARE_LIST:
                {
                    try {
                        if (response.getString("success").equals("false")) {
                            toastActivity.makeToastMessage(response,this);
                        } else {
                            if (response.getString(getString(R.string.jsoncode)).equals(getString(R.string.nocontentcode))) {
                                toastActivity.makeToastMessage(response,(Activity)context);
                            } else {
                                list = getList(response);
                                if(sharedPrefrence.getAccessToken(context)!=null)
                                {
//                                    progressBarActivity.showProgress(viewPager,mProgressView,true,getApplicationContext());
//                                    new RemoteHelper(context).getUserAttemptDetails(this,RemoteCalls.GET_USER_ATTEMPT_DETAILS,sharedPrefrence.getAccessToken(context));
                                }
                                else {
                                    showView(list);
                                }
                               // List<DashBoardList>dashBoardLists=list;

                            }


                        }
                    } catch (Exception e) {
                        LogHelper logHelper = new LogHelper(e);
                        e.printStackTrace();
                    }
                }
                case GET_USER_ATTEMPT_DETAILS:
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
                                new RemoteHelper(getApplicationContext()).getAccessToken(this,RemoteCalls.GET_ACCESS_TOKEN,sharedPrefrence.getRefreshToken(getApplicationContext()));
                            }

                        }
                        else
                        {
                            JSONObject jsonObject = response;
                            if (list != null)
                                showView(list);
                        }
                    }catch (Exception e)
                    {
                        LogHelper logHelper = new LogHelper(e);
                        e.printStackTrace();
                    }
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
                            progressBarActivity.showProgress(viewPager,mProgressView,true,getApplicationContext());
                            //new RemoteHelper(context).getUserAttemptDetails(this,RemoteCalls.GET_USER_ATTEMPT_DETAILS,sharedPrefrence.getAccessToken(context));
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


