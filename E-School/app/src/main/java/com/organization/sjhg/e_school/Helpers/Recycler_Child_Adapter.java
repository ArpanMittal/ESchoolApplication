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
    private List<InternalList> list;
    private Context context;
    private Activity activity;
    private String title;

    public Recycler_Child_Adapter(List<InternalList> list,Activity activity,Context context,String title)
    {
        this.list=list;
        this.activity=activity;
        this.context=context;
        this.title=title;
    }

    @Override
    public Child_View_Holder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //inflate your layout and pass it to view holder
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.activitycardview, viewGroup, false);
        Child_View_Holder child_view_holder=new Child_View_Holder(view);


        return child_view_holder;
    }


    @Override
    public void onBindViewHolder(Child_View_Holder holder, int position) {

        holder.name.setText(list.get(position).name);
        holder.count.setText(list.get(position).count);
        holder.imageView.setImageResource(R.drawable.notechathead);
        holder.title=this.title;

    }

    @Override
    public int getItemCount() {

        return list.size();
    }
}
