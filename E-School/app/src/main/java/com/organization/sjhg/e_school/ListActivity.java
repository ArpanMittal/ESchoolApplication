package com.organization.sjhg.e_school;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.organization.sjhg.e_school.Fragments.Notes_Listing_Fragment;
import com.organization.sjhg.e_school.Helpers.Custom_Pager_Adapter;
import com.organization.sjhg.e_school.Helpers.GridDataAdapter;
import com.organization.sjhg.e_school.Helpers.GridParentDataAdapter;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.Helpers.RecyclerAdapter;
import com.organization.sjhg.e_school.Helpers.Recycler_Child_Adapter;
import com.organization.sjhg.e_school.Helpers.Recycler_View_Adapter;
import com.organization.sjhg.e_school.ListStructure.AndroidVersion;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.ListStructure.DashBoardList;
import com.organization.sjhg.e_school.ListStructure.InternalList;
import com.organization.sjhg.e_school.ListStructure.InternalListData;
import com.organization.sjhg.e_school.ListStructure.ItemDataList;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Utils.ProgressBarActivity;
import com.organization.sjhg.e_school.Utils.ToastActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by arpan on 8/24/2016.
 */
public class ListActivity extends MainParentActivity implements RemoteCallHandler {
    private View mDashboardView;
    private View mProgressView;
    private List<ItemDataList> internalList=new ArrayList<>();
    private List<InternalListData> internalListDatas=new ArrayList<>();
    private ProgressBarActivity progressBarActivity=new ProgressBarActivity();
    private ToastActivity toastActivity=new ToastActivity();

    Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub view_Stub=(ViewStub)findViewById(R.id.viewstub);
        view_Stub.setLayoutResource(R.layout.normal_app_bar);
        view_Stub.inflate();
        ViewStub viewStub = (ViewStub) findViewById(R.id.view_stub_bar);
        viewStub.setLayoutResource(R.layout.content_main);
        viewStub.inflate();
        Intent intent = getIntent();
        mContext = getApplicationContext();
        String title = (String) intent.getExtras().get(mContext.getString(R.string.title));
        String id = (String) intent.getExtras().get(mContext.getString(R.string.jsonid));
        String name = (String) intent.getExtras().get(mContext.getString(R.string.jsonname));
        mDashboardView = findViewById(R.id.dashboard_form);
        mProgressView = findViewById(R.id.dashboard_progress);


        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(name);
        // code repeted in all activity
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
////        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
////        collapsingToolbar.setTitle(getString(R.string.expand));
////        collapsingToolbar.setVisibility(View.INVISIBLE);
////        AutoScrollViewPager viewPager = (AutoScrollViewPager) findViewById(R.id.viewpager);
////        viewPager.setAdapter(new Custom_Pager_Adapter(getSupportFragmentManager()));
////        viewPager.setInterval(5000);
////        viewPager.startAutoScroll();
////        indicator = (CircleIndicator) findViewById(R.id.indicator);
////        indicator.setViewPager(viewPager);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//                Intent intent=new Intent(getApplicationContext(), Notes_Listing_Fragment.class);
//                startActivity(intent);
//            }
//        });


        if (savedInstanceState != null) {
            internalList = (List<ItemDataList>) savedInstanceState.getSerializable("INTERNAL LIST");
            showView(internalList);
        } else {
            progressBarActivity.showProgress(mDashboardView, mProgressView, true, getApplicationContext());
            new RemoteHelper(mContext).getItemDetails(this, RemoteCalls.GET_ITEM_DETAILS, title, id);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this,SearchActivity.class)));

        return true;
    }

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



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(internalList!=null) {
            outState.putSerializable("INTERNAL LIST", (Serializable) internalList);
        }
        outState.putSerializable("LIST",(Serializable)dataList);

    }

    private void showView(List<ItemDataList> list)
    {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        internalListDatas=list.get(0).internalListDatas;
//        ArrayList<AndroidVersion> androidVersions = prepareData();
        GridParentDataAdapter adapter = new GridParentDataAdapter(getApplicationContext(),internalListDatas);
        recyclerView.setAdapter(adapter);


        // for animation in listview
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

    }

    //put data from json to list
    private List<ItemDataList> getList(JSONObject response)
    {
        String title;
        //return dataList;
        List<ItemDataList> dataList=new ArrayList<>();
        try {
            title=response.getString(getString(R.string.jsontitle));
            JSONArray data = response.getJSONArray(mContext.getString(R.string.data));
            List<InternalListData> internalListDatas=new ArrayList<>();


            for(int i=0;i<data.length();i++)
            {
                List<ChapterList> chapterLists=new ArrayList<>();
                JSONObject internalListObject=data.getJSONObject(i);
                String id=internalListObject.getString(mContext.getString(R.string.jsonid));
                String name=internalListObject.getString(mContext.getString(R.string.jsonname));
                JSONArray chapterArray=internalListObject.getJSONArray(getString(R.string.jsonchapter));
                for(int j=0;j<chapterArray.length();j++)
                {
                    JSONObject chapterObject=chapterArray.getJSONObject(j);
                    String chapter_id=chapterObject.getString(mContext.getString(R.string.jsonid));
                    String chapter_name=chapterObject.getString(mContext.getString(R.string.jsonname));
                    chapterLists.add(new ChapterList(chapter_id,chapter_name));
                }
                internalListDatas.add(new InternalListData(id,name,chapterLists));
            }
            dataList.add(new ItemDataList(title,internalListDatas));

        }catch (Exception e) {
            e.printStackTrace();
            new ToastActivity().makeJsonException((Activity) mContext);
            new LogHelper(e);

        }
        return dataList;
    }


    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        super.HandleRemoteCall(isSuccessful,callFor,response,exception);
        progressBarActivity.showProgress(mDashboardView,mProgressView,false,getApplicationContext());
        if(!isSuccessful)
        {
            toastActivity.makeUknownErrorMessage((this));

        }
        else
        {
            switch (callFor) {
                case GET_ITEM_DETAILS: {
                    try {
                        if (response.getString("success").equals("false")) {
                            toastActivity.makeToastMessage(response,this);
                        } else {
                            if (response.getString(getString(R.string.jsoncode)).equals(getString(R.string.nocontentcode))) {
                                toastActivity.makeToastMessage(response,this);
                            } else {
                                internalList = getList(response);
                                List<DashBoardList> dash = dataList;
                                showView(internalList);
                            }


                        }
                    } catch (Exception e) {
                        LogHelper logHelper = new LogHelper(e);
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
