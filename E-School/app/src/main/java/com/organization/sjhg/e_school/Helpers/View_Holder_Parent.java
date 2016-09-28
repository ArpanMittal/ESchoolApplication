package com.organization.sjhg.e_school.Helpers;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.organization.sjhg.e_school.ListStructure.InternalList;
import com.organization.sjhg.e_school.R;

import java.util.List;

/**
 * Created by arpan on 8/9/2016.
 */
public class View_Holder_Parent extends RecyclerView.ViewHolder {
    private RecyclerView recyclerView;
    private RecyclerAdapter mAdapter;
    private Context mContext;
    public TextView title;
    public RecyclerView recyclerChildView;

    public View_Holder_Parent(Context mContext,View view) {
        super(view);
        this.mContext = mContext;
        title = (TextView) view.findViewById(R.id.title);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext.getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    public void addList(List<InternalList> list){

        mAdapter = new RecyclerAdapter(mContext, list,title.getText().toString(),recyclerChildView);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}