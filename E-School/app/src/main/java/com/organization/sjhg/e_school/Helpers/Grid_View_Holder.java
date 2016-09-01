package com.organization.sjhg.e_school.Helpers;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.organization.sjhg.e_school.ListStructure.AndroidVersion;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.ListStructure.DashBoardList;
import com.organization.sjhg.e_school.ListStructure.InternalList;
import com.organization.sjhg.e_school.ListStructure.InternalListData;
import com.organization.sjhg.e_school.ListStructure.ItemDataList;
import com.organization.sjhg.e_school.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpan on 8/29/2016.
 */
public class Grid_View_Holder extends RecyclerView.ViewHolder {
    private RecyclerView recyclerView;
    private Context mContext;
    public TextView title;
    public List<ChapterList> list;
    private GridDataAdapter mAdapter;

    public Grid_View_Holder(Context context, View itemView) {
        super(itemView);
        //this.list=list;
        this.mContext=context;
        title = (TextView) itemView.findViewById(R.id.title);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(mContext.getApplicationContext(),2));
        }
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(mContext.getApplicationContext(),4));
        }
//        recyclerView.setLayoutManager(new GridLayoutManager(mContext.getApplicationContext(),2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void addList(List<ChapterList> list){

        mAdapter = new GridDataAdapter(mContext, list);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
