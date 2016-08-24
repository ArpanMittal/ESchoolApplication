package com.organization.sjhg.e_school.Helpers;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.organization.sjhg.e_school.ListStructure.InternalList;
import com.organization.sjhg.e_school.R;

import java.util.List;

/**
 * Created by arpan on 8/20/2016.
 */
public class Recycler_Child_Adapter extends RecyclerView.Adapter<Child_View_Holder> {

    private final Context mContext;
    private List<InternalList> itemList;
    private String title;

    public Recycler_Child_Adapter(List<InternalList> dataList, Context mContext, String title) {
        this.itemList=dataList;
        this.mContext=mContext;
        this.title=title;
    }

    @Override
    public Child_View_Holder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //inflate your layout and pass it to view holder
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activitycardview, viewGroup, false);

        return new Child_View_Holder(itemView);
    }


    @Override
    public void onBindViewHolder(Child_View_Holder holder, int position) {
        final InternalList item = itemList.get(position);
        holder.name.setText(item.name);
        holder.count.setText(item.count);
        holder.imageView.setImageResource(R.drawable.notechathead);
        holder.title=this.title;

    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }
}
