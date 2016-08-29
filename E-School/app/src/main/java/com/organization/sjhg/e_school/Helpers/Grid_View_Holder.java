package com.organization.sjhg.e_school.Helpers;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.organization.sjhg.e_school.ListStructure.AndroidVersion;
import com.organization.sjhg.e_school.ListStructure.InternalList;
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
    public ArrayList<AndroidVersion> list;
    private GridDataAdapter mAdapter;

    public Grid_View_Holder(Context context, View itemView) {
        super(itemView);
        //this.list=list;
        this.mContext=context;
        title = (TextView) itemView.findViewById(R.id.title);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext.getApplicationContext(),4));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void addList(ArrayList<AndroidVersion> list){

        mAdapter = new GridDataAdapter(mContext, list);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
