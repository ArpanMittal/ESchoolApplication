package com.organization.sjhg.e_school.Content.Quest;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.organization.sjhg.e_school.Content.NewTest.TestActivity;
import com.organization.sjhg.e_school.Content.NewTest.TestSummaryActivity;
import com.organization.sjhg.e_school.Fragments.Notes_Listing_Fragment;
import com.organization.sjhg.e_school.Helpers.ConnectivityReceiver;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.Helpers.QuestGridAdapter;
import com.organization.sjhg.e_school.ListStructure.Topic;
import com.organization.sjhg.e_school.ListStructure.TopicList;
import com.organization.sjhg.e_school.LoginActivity;
import com.organization.sjhg.e_school.MainParentActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.SearchActivity;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.TakeNotes.AddSmallNotesActivity;
import com.organization.sjhg.e_school.TakeNotes.whiteboard.WhiteBoardActivity;
import com.organization.sjhg.e_school.Utils.SharedPrefrence;
import com.organization.sjhg.e_school.Utils.ToastActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Punit Chhajer on 24-08-2016.
 */
public class QuestListActivity extends MainParentActivity implements View.OnClickListener{

    private String id, name;
    public TopicList list;
    private ProgressBar mLoading;
    private View mProgressDialog;
    private ImageView mProgress;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub view_Stub=(ViewStub)findViewById(R.id.viewstub);
        view_Stub.setLayoutResource(R.layout.activity_quest_list);
        view_Stub.inflate();

        id = getIntent().getStringExtra("chapterId");
        name = getIntent().getStringExtra("name");

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // code repeated in all activity
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        FloatingActionButton fab1 = (FloatingActionButton)findViewById(R.id.fabSimpleNote);
        FloatingActionButton fab2 = (FloatingActionButton)findViewById(R.id.fabWhiteBoard);
        FloatingActionButton fab3 = (FloatingActionButton)findViewById(R.id.fablist);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);

        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = new SharedPrefrence().getAccessToken(QuestListActivity.this);
                if (token==null){
                    Intent intent = new Intent(QuestListActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else{
                    new AlertDialog.Builder(QuestListActivity.this,R.style.AppTheme_AlertDialog)
                            .setTitle("Test")
                            .setMessage("Are you sure you want to attempt this Test?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    list = null;
                                    Intent intent=new Intent(QuestListActivity.this, TestActivity.class);
                                    intent.putExtra("Tag", GlobalConstants.ChapterTag);
                                    intent.putExtra("Id",id);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            }
        });

        findViewById(R.id.attempt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = new SharedPrefrence().getRefreshToken(QuestListActivity.this);
                if (token==null){
                    Intent intent = new Intent(QuestListActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(QuestListActivity.this, TestSummaryActivity.class);
                    intent.putExtra("Tag", GlobalConstants.ChapterTag);
                    intent.putExtra("Id",id);
                    startActivity(intent);
                }
            }
        });

        mLoading = (ProgressBar) findViewById(R.id.progress);
        mProgress = (ImageView) findViewById(R.id.completeProgress);
        mProgressDialog = findViewById(R.id.progressDialog);
        getSupportActionBar().setTitle(name);
        if (savedInstanceState != null) {
            list = (TopicList) savedInstanceState.getSerializable("INTERNAL LIST");
            if (list!=null){
                showView();
            }
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to manually check connection status
    private void checkConnection() {
        ConnectivityReceiver.isConnected();
    }

    private void showView()
    {
        mLoading.setVisibility(View.GONE);

        int total = list.topics.size();
        int cmp = -1;
        for (int i=0;i<total;i++){
            if (!list.topics.get(i).islock()){
                cmp++;
            }
        }
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            int width = (cmp*size.x)/total;
            if (width!=0){
                mProgress.getLayoutParams().width = width;
                mProgress.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
        else{
            int height = (cmp*(size.y-100))/total;
            if (height!=0){
                mProgress.getLayoutParams().height = height;
                mProgress.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);

        QuestGridAdapter adapter = new QuestGridAdapter(this,list);
        recyclerView.setAdapter(adapter);


        // for animation in listview
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

    }

    //put data from json to list
    private TopicList getList(JSONObject response)
    {
        TopicList list = null;
        try {
            JSONArray data = response.getJSONArray(this.getString(R.string.data));
            List<Topic> topics=new ArrayList<>();
            int length=data.length();

            for(int i=0;i<length;i++)
            {
                JSONObject internalListObject=data.getJSONObject(i);
                String name=""+internalListObject.getString("topic_name");
                String hash=""+internalListObject.getString("hash");
                String pdf_path=""+internalListObject.getString("pdf_path");
                String video_path=""+internalListObject.getString("video_path");
                String pdf_hash=""+internalListObject.getString("pdf_hash");
                Boolean subscribed= Boolean.valueOf(internalListObject.getString("is_subscribed"));
                Boolean lock= Boolean.valueOf(internalListObject.getString("is_locked"));
                int progress= Integer.parseInt(internalListObject.getString("progress"));
                topics.add(new Topic(name, hash,pdf_path, video_path, pdf_hash, subscribed, lock,progress));
            }
            list = new TopicList(id,topics);

        }catch (Exception e) {
            e.printStackTrace();
            new ToastActivity().makeJsonException(this);
            new LogHelper(e);

        }
        return list;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String token = new SharedPrefrence().getAccessToken(this);
        if(list!=null && token!=null) {
            outState.putSerializable("INTERNAL LIST", (Serializable) list);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        String token = new SharedPrefrence().getAccessToken(this);
        if (list==null && token==null){
            mLoading.setVisibility(View.VISIBLE);
            new RemoteHelper(this).getFreeQuestDetails(this, RemoteCalls.GET_ITEM_DETAILS, id);
        }else if (list == null){
            mLoading.setVisibility(View.VISIBLE);
            new RemoteHelper(this).getQuestDetails(this, RemoteCalls.GET_ITEM_DETAILS, id);
        }
    }

    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        super.HandleRemoteCall(isSuccessful, callFor, response, exception);
        if(!isSuccessful)
        {
            new ToastActivity().makeUknownErrorMessage(this);

        }
        switch (callFor){
            case GET_ITEM_DETAILS:
                try {
                    if (response.get("code").toString().equals(GlobalConstants.EXPIRED_TOKEN))
                    {

                        if(new SharedPrefrence().getRefreshToken(getApplicationContext())==null)
                        {

                            new ToastActivity().makeToastMessage(response,this);
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
                        new ToastActivity().makeUknownErrorMessage(this);

                    }

                    else
                    {
                        list = getList(response);
                        if(list!=null){
                            showView();
                        }

                    }
                }catch (Exception e){
                    e.printStackTrace();
                    new ToastActivity().makeJsonException(this);
                    new LogHelper(e);
                }
                break;
            case GET_ACCESS_TOKEN:
            {
                try{
                    if(response.get("sucess").toString().equals("false"))
                    {
                        new ToastActivity().makeToastMessage(response,this);
                    }

                    else
                    {
                        new SharedPrefrence().saveAccessToken(getApplicationContext(),response.get("access_token").toString(),response.get("refresh_token").toString());
                        String access_token=response.get("access_token").toString();
                        new RemoteHelper(this).getFreeQuestDetails(this, RemoteCalls.GET_ITEM_DETAILS, id);
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

    public void showDialog() {
        mProgressDialog.setVisibility(View.VISIBLE);
    }
    public void dismissDialog(){
        mProgressDialog.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:


                break;
            case R.id.fabSimpleNote:
                Intent intent=new Intent(getApplicationContext(), AddSmallNotesActivity.class);
                startActivity(intent);
                break;
            case R.id.fabWhiteBoard:
                intent=new Intent(getApplicationContext(), WhiteBoardActivity.class);
                startActivity(intent);
                break;
            case R.id.fablist:
                intent=new Intent(getApplicationContext(), Notes_Listing_Fragment.class);
                startActivity(intent);
                break;
        }
    }
}
