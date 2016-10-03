package com.organization.sjhg.e_school.Helpers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.organization.sjhg.e_school.Content.NewTest.TestReportActivity;
import com.organization.sjhg.e_school.Content.NewTest.TestSummaryActivity;
import com.organization.sjhg.e_school.Fragments.SamplePaperListFragment;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.MainActivity;
import com.organization.sjhg.e_school.Main_Activity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by arpan on 9/7/2016.
 */

public class TestPaperAttemptAdapter extends RecyclerView.Adapter<TestPaperAttemptAdapter.ViewHolder> {
    private List<ChapterList> chapterLists;
    private Context context;
    private String parent_id;
    private String parent_tag;
    private String parent_title;
    private String grand_parent_title;

    public TestPaperAttemptAdapter(Context context,List<ChapterList> android,String parent_id,String parent_tag,String parent_title,String grand_parent_title) {
        this.chapterLists = android;
        this.context = context;
        this.parent_id=parent_id;
        this.parent_tag=parent_tag;
        this.parent_title=parent_title;
        this.grand_parent_title=grand_parent_title;
    }

    @Override
    public TestPaperAttemptAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TestPaperAttemptAdapter.ViewHolder viewHolder, final int position) {


        viewHolder.tv_android.setText((String) chapterLists.get(position).name);
        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, TestReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("parent_id", parent_id);
                intent.putExtra("parent_tag",parent_tag);
                intent.putExtra("parent_title",parent_title);
                intent.putExtra("Id",chapterLists.get(position).id);
                intent.putExtra("Grand_Parent_Title",grand_parent_title);
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
        public RelativeLayout relativeLayout;
        public ViewHolder(View view) {
            super(view);
            relativeLayout=(RelativeLayout)view.findViewById(R.id.relativeLayout);
            tv_android = (TextView)view.findViewById(R.id.tv_android);
            img_android = (ImageView) view.findViewById(R.id.img_android);
            img_android.setImageResource(R.drawable.sample_icon);
        }
    }

}
