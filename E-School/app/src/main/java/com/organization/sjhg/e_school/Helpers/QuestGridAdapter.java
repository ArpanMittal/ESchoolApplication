package com.organization.sjhg.e_school.Helpers;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.organization.sjhg.e_school.Content.AudioVideoPlayerActivity;
import com.organization.sjhg.e_school.Content.PdfDisplayActivity;
import com.organization.sjhg.e_school.Content.Quest.QuestListActivity;
import com.organization.sjhg.e_school.ListStructure.Topic;
import com.organization.sjhg.e_school.ListStructure.TopicList;
import com.organization.sjhg.e_school.LoginActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.ServerAddress;
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
        public TextView name, progress;
        public Button video,doc,analytics,worksheet;
        public ImageView image;
        public View view;
        public QuestListViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            video = (Button) itemView.findViewById(R.id.video);
            doc = (Button) itemView.findViewById(R.id.pdf);
            analytics = (Button) itemView.findViewById(R.id.analytics);
            worksheet = (Button) itemView.findViewById(R.id.worksheet);
            image = (ImageView) itemView.findViewById(R.id.background);
            progress = (TextView) itemView.findViewById(R.id.progress);
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
        holder.progress.setText("Progress: "+detail.getProgress()+"%");

        if (position==0 ||(!detail.islock() && detail.isSubscribed())){
            if(detail.video_path!="" && detail.video_path!="null"){
                holder.video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String token = new SharedPrefrence().getAccessToken(context);
                        if(token==null){
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }else{
                            Intent intent = new Intent(context, AudioVideoPlayerActivity.class);
                            intent.putExtra("path",detail.video_path);
                            context.startActivity(intent);
                        }
                    }
                });
            }else{
                holder.video.setVisibility(View.INVISIBLE);
            }

            if(detail.pdf_path!="" && detail.pdf_path!="null"){
                holder.doc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String token = new SharedPrefrence().getAccessToken(context);
                        if(token==null){
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }else{
                            final FileHelper pdf = new FileHelper(context,detail.pdf_path,detail.pdf_hash,detail.hash);
                            if (pdf.isExist()){
                                pdf.openFile();
                            }else{
                                pdf.download();
                            }
                        }
                    }
                });
            }else{
                holder.doc.setVisibility(View.INVISIBLE);
            }
        } else{
            holder.view.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return list.topics.size();
    }
}
