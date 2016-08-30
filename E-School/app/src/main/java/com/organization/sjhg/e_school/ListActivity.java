package com.organization.sjhg.e_school;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

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
        ViewStub viewStub = (ViewStub) findViewById(R.id.viewstub);
        viewStub.setLayoutResource(R.layout.content_main);
        viewStub.inflate();
        Intent intent = getIntent();
        mContext = getApplicationContext();
        String title = (String) intent.getExtras().get(mContext.getString(R.string.title));
        String id = (String) intent.getExtras().get(mContext.getString(R.string.jsonid));

        mDashboardView = findViewById(R.id.dashboard_form);
        mProgressView = findViewById(R.id.dashboard_progress);

        if (savedInstanceState != null) {
            internalList = (List<ItemDataList>) savedInstanceState.getSerializable("INTERNAL LIST");
            showView(internalList);
        } else {
            progressBarActivity.showProgress(mDashboardView, mProgressView, true, getApplicationContext());
            new RemoteHelper(mContext).getItemDetails(this, RemoteCalls.GET_ITEM_DETAILS, title, id);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("INTERNAL LIST", (Serializable) internalList);
        outState.putSerializable("LIST",(Serializable)dataList);

    }

    private void showView(List<ItemDataList> list)
    {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextView textView=(TextView) findViewById(R.id.maintitle);
        textView.setText(list.get(0).title);

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
            int length=data.length();

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
            new ToastActivity().makeUknownErrorMessage((Activity) mContext);

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
                                toastActivity.makeToastMessage(response,(Activity)mContext);
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
