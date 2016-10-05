package com.organization.sjhg.e_school.Helpers;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.organization.sjhg.e_school.Content.AudioVideoPlayerActivity;

import com.organization.sjhg.e_school.Content.NewTest.TestSummaryActivity;
import com.organization.sjhg.e_school.Content.PdfDisplayActivity;
import com.organization.sjhg.e_school.Content.Quest.QuestListActivity;
import com.organization.sjhg.e_school.Content.NewTest.TestActivity;
import com.organization.sjhg.e_school.ListStructure.Topic;
import com.organization.sjhg.e_school.ListStructure.TopicList;
import com.organization.sjhg.e_school.LoginActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.Utils.SharedPrefrence;
import com.organization.sjhg.e_school.Utils.ToastActivity;

/**
 * Created by Punit Chhajer on 02-09-2016.
 */
public class QuestGridAdapter extends RecyclerView.Adapter<QuestGridAdapter.QuestListViewHolder>{
    private Context context;
    private TopicList list;
    private ProgressDialog mProgressDialog;
    public QuestGridAdapter(Context context, TopicList list) {
        this.context = context;
        this.list =list;
    }

    public class QuestListViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public Button video,doc,analytics,worksheet;
        public ImageView image;
        public View view;
        public RatingBar progress;
        public QuestListViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            video = (Button) itemView.findViewById(R.id.video);
            doc = (Button) itemView.findViewById(R.id.pdf);
            analytics = (Button) itemView.findViewById(R.id.analytics);
            worksheet = (Button) itemView.findViewById(R.id.worksheet);
            image = (ImageView) itemView.findViewById(R.id.background);
            progress = (RatingBar) itemView.findViewById(R.id.progress);
            view = itemView.findViewById(R.id.lockButton);
        }
    }

    @Override
    public QuestListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quest_list_item, parent, false);
        return new QuestListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestListViewHolder holder, int position) {
        final Topic detail = list.topics.get(position);
        holder.name.setText( detail.name);
        double rating = (detail.getProgress()*3.0)/100;
        holder.progress.setRating((float) rating);
        final Boolean[] isRead = {false};
        if (detail.getProgress()>0){
            isRead[0] = true;
        }


        if (position==0 ||(!detail.islock() && detail.isSubscribed())){
            if(!detail.video_path.equals("") && !detail.video_path.equals("null")){
                holder.video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String token = new SharedPrefrence().getAccessToken(context);
                        if(token==null){
                            ((QuestListActivity)context).list = null;
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }else{
                            isRead[0] = true;
                            Intent intent = new Intent(context, AudioVideoPlayerActivity.class);
                            intent.putExtra("path",detail.video_path);
                            intent.putExtra("title",detail.name);
                            context.startActivity(intent);
                        }
                    }
                });
            }else{
                holder.video.setVisibility(View.INVISIBLE);
            }

            if(!detail.pdf_path.equals("") && !detail.pdf_path.equals("null")){
                holder.doc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String token = new SharedPrefrence().getAccessToken(context);
                        if(token==null){
                            ((QuestListActivity)context).list = null;
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }else{
                            isRead[0] = true;
                            final FileHelper pdf = new FileHelper(context,detail.pdf_path,detail.pdf_hash,detail.hash);
                            if (pdf.isExist()){
                                pdf.openFile();
                            }else{
                                pdf.download();
                            }
                        }
                    }
                });

                holder.worksheet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String token = new SharedPrefrence().getAccessToken(context);
                        if(token==null){
                            ((QuestListActivity)context).list = null;
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }else if (isRead[0]){
                            new AlertDialog.Builder(context)
                                    .setTitle("WorkSheet")
                                    .setMessage("Are you sure you want to attempt this worksheet?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            ((QuestListActivity)context).list = null;
                                            Intent intent=new Intent(context, TestActivity.class);
                                            intent.putExtra("Tag", GlobalConstants.WorksheetTag);
                                            intent.putExtra("Id",detail.hash);
                                            context.startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }else{
                            new AlertDialog.Builder(context)
                                    .setTitle("WorkSheet")
                                    .setMessage("Are you sure you want to attempt this worksheet without study?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            ((QuestListActivity)context).list = null;
                                            Intent intent=new Intent(context, TestActivity.class);
                                            intent.putExtra("Tag", GlobalConstants.WorksheetTag);
                                            intent.putExtra("Id",detail.hash);
                                            context.startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    }
                });

                holder.analytics.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, TestSummaryActivity.class);
                        intent.putExtra("Tag", GlobalConstants.WorksheetTag);
                        intent.putExtra("Id",detail.hash);
                        context.startActivity(intent);
                    }
                });

            }else{
                holder.doc.setVisibility(View.INVISIBLE);
                holder.analytics.setVisibility(View.INVISIBLE);
                holder.worksheet.setVisibility(View.INVISIBLE);
            }
        }else if (!detail.islock() && !detail.isSubscribed()){
            holder.video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ToastActivity().showMessage("Subscribe for more content",(Activity) context);
                }
            });
            holder.doc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ToastActivity().showMessage("Subscribe for more content",(Activity) context);
                }
            });
            holder.analytics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ToastActivity().showMessage("Subscribe for more content",(Activity) context);
                }
            });
            holder.worksheet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ToastActivity().showMessage("Subscribe for more content",(Activity) context);
                }
            });
        }else{
            holder.view.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return list.topics.size();
    }
}
