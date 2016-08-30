package com.organization.sjhg.e_school.Helpers;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.organization.sjhg.e_school.ListStructure.DashBoardList;
import com.organization.sjhg.e_school.ListStructure.InternalList;
import com.organization.sjhg.e_school.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by arpan on 8/8/2016.
 */
public class Recycler_View_Adapter extends RecyclerView.Adapter<View_Holder_Parent> {

    private final Context mContext;
    private List<DashBoardList> dataList = Collections.emptyList();;

    public Recycler_View_Adapter(List<DashBoardList> list, Context context) {
        this.dataList = list;
        this.mContext = context;
    }

    @Override
    public View_Holder_Parent onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_helper, parent, false);

        return new View_Holder_Parent(mContext,itemView);

    }

    @Override
    public void onBindViewHolder(View_Holder_Parent holder, int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView

        holder.title.setText(dataList.get(position).title);
        holder.addList(dataList.get(position).internalLists);
    }


    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return dataList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}