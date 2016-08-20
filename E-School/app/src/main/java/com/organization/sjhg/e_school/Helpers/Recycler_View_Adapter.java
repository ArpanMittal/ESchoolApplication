package com.organization.sjhg.e_school.Helpers;

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

    List<DashBoardList> list = Collections.emptyList();
    Context context;

    public Recycler_View_Adapter(List<DashBoardList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public View_Holder_Parent onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder

        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_helper,parent,false);
        View_Holder_Parent holder_parent=new View_Holder_Parent(v);
        holder_parent.recyclerView.setHasFixedSize(true);
        holder_parent.recyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder_parent.recyclerView.setLayoutManager(linearLayoutManager);
        return holder_parent;

    }

    @Override
    public void onBindViewHolder(View_Holder_Parent holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView

        holder.title.setText(list.get(position).title);
        List<InternalList> data=list.get(position).internalLists;
        RecyclerAdapter recyclerAdapter=new RecyclerAdapter(context,data);
        holder.recyclerView.setAdapter(recyclerAdapter);


    }


    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}