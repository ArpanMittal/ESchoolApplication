package com.organization.sjhg.e_school.Helpers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.organization.sjhg.e_school.ListStructure.AndroidVersion;
import com.organization.sjhg.e_school.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpan on 8/29/2016.
 */
public class GridParentDataAdapter extends RecyclerView.Adapter<Grid_View_Holder>  {

    Context mContext;
    ArrayList<AndroidVersion> list;
    public GridParentDataAdapter(Context applicationContext, ArrayList<AndroidVersion> androidVersions) {
        this.mContext=applicationContext;
        this.list=androidVersions;


    }

    @Override
    public Grid_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_helper, parent, false);

        return new Grid_View_Holder(mContext,itemView);

    }

    @Override
    public void onBindViewHolder(Grid_View_Holder holder, int position) {
        holder.title.setText("android Version");
        holder.addList(list);
    }



    @Override
    public int getItemCount() {
        return 1;
    }
}
