package com.organization.sjhg.e_school.Helpers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.organization.sjhg.e_school.Content.NewTest.TestInstructionActivity;
import com.organization.sjhg.e_school.Fragments.SamplePaperListFragment;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.Main_Activity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by arpan on 9/7/2016.
 */

public class SamplePaperListDataAdapter extends RecyclerView.Adapter<SamplePaperListDataAdapter.ViewHolder> {
    private List<ChapterList> chapterLists;
    private Context context;

    public SamplePaperListDataAdapter(Context context,List<ChapterList> android) {
        this.chapterLists = android;
        this.context = context;
    }

    @Override
    public SamplePaperListDataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SamplePaperListDataAdapter.ViewHolder viewHolder, final int position) {


        viewHolder.tv_android.setText((String) chapterLists.get(position).name);
        viewHolder.tv_android.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, TestInstructionActivity.class);
                intent.putExtra("Tag", GlobalConstants.SamplePaperTag);
                intent.putExtra("Id",chapterLists.get(position).id);
                context.startActivity(intent);
            }
        });
        Picasso.with(context).load("https://s9.postimg.io/al1o9ip5r/image.jpg").resize(50,50).into(viewHolder.img_android);
    }


    @Override
    public int getItemCount() {
        return chapterLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_android;
        private ImageView img_android;
        public ViewHolder(View view) {
            super(view);

            tv_android = (TextView)view.findViewById(R.id.tv_android);
            img_android = (ImageView) view.findViewById(R.id.img_android);
        }
    }

}
