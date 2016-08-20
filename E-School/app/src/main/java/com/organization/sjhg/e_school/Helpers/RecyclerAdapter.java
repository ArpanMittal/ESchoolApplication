package com.organization.sjhg.e_school.Helpers;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.organization.sjhg.e_school.ListStructure.DashBoardList;
import com.organization.sjhg.e_school.ListStructure.InternalList;
import com.organization.sjhg.e_school.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<View_Holder> {

    private List<InternalList> list;
    private Context context;

    public RecyclerAdapter(Context context, List<InternalList> list) {
        this.list = list;
        this.context = context;
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