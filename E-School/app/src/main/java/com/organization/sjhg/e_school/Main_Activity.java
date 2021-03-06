package com.organization.sjhg.e_school;


import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.widget.RecyclerView;

import android.view.MenuItem;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;

import com.organization.sjhg.e_school.Fragments.Notes_Listing_Fragment;
import com.organization.sjhg.e_school.Helpers.Custom_Pager_Adapter;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.Helpers.RecyclerAdapter;
import com.organization.sjhg.e_school.Helpers.Recycler_View_Adapter;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.ListStructure.DashBoardList;
import com.organization.sjhg.e_school.ListStructure.InternalList;

import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;

import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.organization.sjhg.e_school.Utils.ProgressBarActivity;
import com.organization.sjhg.e_school.Utils.ToastActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import me.relex.circleindicator.CircleIndicator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Main_Activity extends MainParentActivity{


    private View mDashboardView;
    private View mProgressView,mNoInternet;

    private ProgressBarActivity progressBarActivity=new ProgressBarActivity();
    private ToastActivity toastActivity=new ToastActivity();
    private Timer timer;
    private static int count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
     //  setContentView(R.layout.main_activity);

        ViewStub view_Stub=(ViewStub)findViewById(R.id.viewstub);
        view_Stub.setLayoutResource(R.layout.app_bar_main);
        view_Stub.inflate();

//        ViewStub viewStub=(ViewStub)findViewById(R.id.view_stub_bar);
//        viewStub.setLayoutResource(R.layout.content_main);
//       viewStub.inflate();


        mDashboardView=findViewById(R.id.recycler);
        mProgressView=findViewById(R.id.dashboard_progress);
        mNoInternet = findViewById(R.id.noInternetScreen);
        Button retry = (Button) findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoInternet.setVisibility(View.GONE);
                progressBarActivity.showProgress(mDashboardView,mProgressView,true,getApplicationContext());
                new RemoteHelper(getApplicationContext()).getDashBoardDetails(Main_Activity.this, RemoteCalls.GET_DASHBOARD_LIST);
                new RemoteHelper(getApplicationContext()).getDashBoardImageDetails(RemoteCalls.GET_DASHBOARD_IMAGE_LIST,Main_Activity.this);
            }
        });
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // code repeated in all activity
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);



//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//                Intent intent=new Intent(getApplicationContext(), Notes_Listing_Fragment.class);
//                startActivity(intent);
//            }
//        });

        //remote call to access data from server
        if(savedInstanceState!=null)
        {
            dataList=(List<DashBoardList>) savedInstanceState.getSerializable("LIST");
            imageList=(List<ChapterList>) savedInstanceState.getSerializable("IMAGELIST");
            if (dataList!=null && dataList.size()!=0){
                showView(dataList,imageList);
            }else{
                mNoInternet.setVisibility(View.VISIBLE);
            }

        }
        else {
            progressBarActivity.showProgress(mDashboardView,mProgressView,true,getApplicationContext());
            new RemoteHelper(getApplicationContext()).getDashBoardImageDetails(RemoteCalls.GET_DASHBOARD_IMAGE_LIST,this);
        }


    }




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.menu_search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this,SearchActivity.class)));
//
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.menu_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }






    /*
        convert json response into desired list format
     */




    private void showView(List<DashBoardList> dataList, final List<ChapterList> imageList)
    {


        final ViewPager viewPager=(ViewPager)findViewById(R.id.viewpager);
        //viewPager.setAdapter(new Custom_Pager_Adapter(getSupportFragmentManager(),imageList));
       // AutoScrollViewPager viewPager = (AutoScrollViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new Custom_Pager_Adapter(getSupportFragmentManager(),imageList));
       // viewPager.setInterval(5000);
        //viewPager.startAutoScroll();
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);


        // Timer for auto sliding
//        timer  = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(viewPager.getCurrentItem()<imageList.size()){
//                            viewPager.setCurrentItem(viewPager.getCurrentItem()+1,true);
//                            //count++;
//                        }else{
//                            //count = 0;
//                            viewPager.setCurrentItem(0,true);
//                        }
//                    }
//                });
//            }
//        },1000,3000);

        /*
        show recycler view
         */
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setNestedScrollingEnabled(false);

        Recycler_View_Adapter adapter = new Recycler_View_Adapter(dataList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        // for animation in listview
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);


    }

    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        super.HandleRemoteCall(isSuccessful,callFor,response,exception);
        mNoInternet.setVisibility(View.GONE);
        if(!isSuccessful)
        {
            progressBarActivity.showProgress(mDashboardView,mProgressView,false,getApplicationContext());
            mNoInternet.setVisibility(View.VISIBLE);
        }
        else
        {

            try {
                progressBarActivity.showProgress(mDashboardView,mProgressView,false,getApplicationContext());
                if (response.get("success").toString().equals("false")) {
                    toastActivity.makeToastMessage(response,this);
                }
                else
                {
                    if(imageList!=null&&dataList!=null)
                    showView(dataList,imageList);

                }
            }catch (Exception e)
            {
                LogHelper logHelper=new LogHelper(e);
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
    //    timer.cancel();
        super.onDestroy();
    }
}