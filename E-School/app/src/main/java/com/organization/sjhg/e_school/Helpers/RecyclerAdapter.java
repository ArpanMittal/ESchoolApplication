package com.organization.sjhg.e_school.Helpers;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.organization.sjhg.e_school.ListStructure.InternalList;
import com.organization.sjhg.e_school.R;

import java.security.PrivateKey;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<View_Holder>{

    private List<InternalList> list;
    private Context context;
    private String title;
    private Activity activity;
    private RecyclerView recyclerView;


    public RecyclerAdapter(Context context, List<InternalList> list,String title,Activity activity,RecyclerView recyclerView) {
        this.list = list;
        this.context = context;
        this.title=title;
        this.activity=activity;
        this.recyclerView=recyclerView;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //inflate your layout and pass it to view holder
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.activitycardview, viewGroup, false);

        View_Holder view_Holder = new View_Holder(view);


        return view_Holder;
    }

    @Override
    public void onBindViewHolder(View_Holder viewHolder, int position) {
        viewHolder.name.setText(list.get(position).name);
        viewHolder.count.setText(list.get(position).count);
        viewHolder.imageView.setImageResource(R.drawable.notechathead);
        viewHolder.title=this.title;
        viewHolder.id=list.get(position).id;
        viewHolder.recyclerView=recyclerView;
        viewHolder.activity=activity;
       // viewHolder.setClickListener(new View.OnClickListener() {


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



}