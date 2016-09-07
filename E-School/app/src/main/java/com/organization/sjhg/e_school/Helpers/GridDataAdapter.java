package com.organization.sjhg.e_school.Helpers;

/**
 * Created by arpan on 8/29/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.organization.sjhg.e_school.Content.Quest.QuestListActivity;
import com.organization.sjhg.e_school.ListActivity;
import com.organization.sjhg.e_school.ListStructure.AndroidVersion;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.ListStructure.DashBoardList;
import com.organization.sjhg.e_school.ListStructure.InternalList;
import com.organization.sjhg.e_school.ListStructure.InternalListData;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.VolleyController;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridDataAdapter extends RecyclerView.Adapter<GridDataAdapter.ViewHolder> {
    private List<ChapterList> chapterLists;
    private Context context;

    public GridDataAdapter(Context context,List<ChapterList> android) {
        this.chapterLists = android;
        this.context = context;
    }

    @Override
    public GridDataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridDataAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.tv_android.setText((String) chapterLists.get(i).name);
        Picasso.with(context).load("https://s9.postimg.io/al1o9ip5r/image.jpg").resize(50,50).into(viewHolder.img_android);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(context, QuestListActivity.class);
                intent.putExtra("chapterId",(String) chapterLists.get(i).id);
                intent.putExtra("name",(String) chapterLists.get(i).name);
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapterLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_android;
        private ImageView img_android;
        private View view;
        public ViewHolder(View view) {
            super(view);
            this.view = view;
            tv_android = (TextView)view.findViewById(R.id.tv_android);
            img_android = (ImageView) view.findViewById(R.id.img_android);
        }
    }

}