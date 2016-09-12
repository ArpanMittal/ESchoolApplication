package com.organization.sjhg.e_school.Content.Quest;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;

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
import com.organization.sjhg.e_school.Structure.GlobalConstants;
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
public class QuestListActivity extends MainParentActivity {

    private String id, name;
    public TopicList list;
    private ProgressBar mLoading,mProgress;
    private View mProgressDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub view_Stub=(ViewStub)findViewById(R.id.viewstub);
        view_Stub.setLayoutResource(R.layout.activity_quest_list);
        view_Stub.inflate();

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // code repeated in all activity
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent=new Intent(getApplicationContext(), Notes_Listing_Fragment.class);
                startActivity(intent);
            }
        });

        mLoading = (ProgressBar) findViewById(R.id.progress);
        mProgress = (ProgressBar) findViewById(R.id.completeProgress);
        mProgressDialog = findViewById(R.id.progressDialog);
        id = getIntent().getStringExtra("chapterId");
        name = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(name);
        if (savedInstanceState != null) {
            list = (TopicList) savedInstanceState.getSerializable("INTERNAL LIST");
            showView();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showView()
    {
        mLoading.setVisibility(View.GONE);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        }

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
            new RemoteHelper(this).getFreeQuestDetails(this, RemoteCalls.GET_ITEM_DETAILS, id);
        }else if (list == null){
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
                        int total = list.topics.size();
                        int cmp = -1;
                        for (int i=0;i<total;i++){
                            if (!list.topics.get(i).islock()){
                                cmp++;
                            }
                        }
                        mProgress.setProgress((cmp*100)/total);
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
}
