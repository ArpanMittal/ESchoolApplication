package com.organization.sjhg.e_school.Helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.organization.sjhg.e_school.Content.NewTest.TestActivity;
import com.organization.sjhg.e_school.Content.NewTest.TestSummaryActivity;
import com.organization.sjhg.e_school.Fragments.SamplePaperListFragment;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.LoginActivity;
import com.organization.sjhg.e_school.Main_Activity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.Utils.SharedPrefrence;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by arpan on 9/7/2016.
 */

public class SamplePaperListDataAdapter extends RecyclerView.Adapter<SamplePaperListDataAdapter.ViewHolder> {
    private List<ChapterList> chapterLists;
    private Context context;
    private String parent_title;
    private String parent_id;

    public SamplePaperListDataAdapter(Context context,List<ChapterList> android,String parent_title,String parent_id) {
        this.chapterLists = android;
        this.context = context;
        this.parent_title=parent_title;
        this.parent_id=parent_id;
    }


    @Override
    public SamplePaperListDataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_sample_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SamplePaperListDataAdapter.ViewHolder viewHolder, final int position) {

        final SharedPrefrence sharedPrefrence=new SharedPrefrence();
        if(sharedPrefrence.getAccessToken(context)!=null)
        {
            viewHolder.button.setVisibility(View.VISIBLE);
            viewHolder.horizontal_line.setVisibility(View.VISIBLE);
        }
        viewHolder.tv_android.setText((String) chapterLists.get(position).name);
//        viewHolder.tv_android.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showLocationDialog(position);
//            }
//        });
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPrefrence.getAccessToken(context)==null)
                {
                    showLocationDialog(position);
                }
                else
                    showLocationDialog(position);
            }
        });


        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, TestSummaryActivity.class);
                intent.putExtra("Tag", GlobalConstants.SamplePaperTag);
                intent.putExtra("Id",chapterLists.get(position).id);
                intent.putExtra("Title",chapterLists.get(position).name);
                //intent.putExtra("Parent_id",parent_id);
                //intent.putExtra("Parent_title",parent_title);
                //intent.putExtra("Activity_Name",chapterLists.get(position).name);
                context.startActivity(intent);
            }
        });
    }

    private void showLginDialog(final int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.ask_login));
        //builder.setMessage(context.getString(R.string.test_submit_message));

        String positiveText = context.getString(R.string.login);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(context, LoginActivity.class);
                        intent.putExtra("Tag", GlobalConstants.SamplePaperTag);
                        intent.putExtra("Id",chapterLists.get(position).id);
                        intent.putExtra("Title",chapterLists.get(position).name);
                        context.startActivity(intent);
                        // positive button logic
                        // new RemoteHelper(getApplicationContext()).sendQuestionResponse(TestActivity.this, RemoteCalls.SEND_QUESTION_RESPONSE,tag,id, access_token,makeResponseList());
                        // progressBarActivity.showProgress(mViewPagerView,mProgressView,true,getApplicationContext());
                    }
                });

        String negativeText = context.getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    private void showLocationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.test_start_title));
        builder.setMessage(context.getString(R.string.test_submit_message));

        String positiveText = context.getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(context, TestActivity.class);
                        intent.putExtra("Tag", GlobalConstants.SamplePaperTag);
                        intent.putExtra("Id",chapterLists.get(position).id);
                        intent.putExtra("Title",chapterLists.get(position).name);
                        context.startActivity(intent);
                        // positive button logic
                       // new RemoteHelper(getApplicationContext()).sendQuestionResponse(TestActivity.this, RemoteCalls.SEND_QUESTION_RESPONSE,tag,id, access_token,makeResponseList());
                       // progressBarActivity.showProgress(mViewPagerView,mProgressView,true,getApplicationContext());
                    }
                });

        String negativeText = context.getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return chapterLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_android;
        private ImageView img_android;
        public ImageView button;
        public RelativeLayout view;
        public View horizontal_line;
        public ViewHolder(View view) {
            super(view);
            this.view=(RelativeLayout)view.findViewById(R.id.relativeLayout);
            tv_android = (TextView)view.findViewById(R.id.tv_android);
            img_android = (ImageView) view.findViewById(R.id.img_android);
            img_android.setImageResource(R.drawable.sample_icon);
            button=(ImageView) view.findViewById(R.id.btn);
            horizontal_line=view.findViewById(R.id.horizontalLine);
        }
    }

}
