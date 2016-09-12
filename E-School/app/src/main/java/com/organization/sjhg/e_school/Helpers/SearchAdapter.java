package com.organization.sjhg.e_school.Helpers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.organization.sjhg.e_school.Content.Quest.QuestListActivity;
import com.organization.sjhg.e_school.ListStructure.Topic;
import com.organization.sjhg.e_school.ListStructure.TopicList;
import com.organization.sjhg.e_school.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Punit Chhajer on 10-09-2016.
 */
public class SearchAdapter  extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private final Context mContext;
    private List<TopicList> dataList = new ArrayList<>();

    public SearchAdapter(List<TopicList> list, Context context, HashMap<String, Boolean> check) {
        this.mContext = context;
        for(int i=0 ; i< list.size();i++){
            TopicList chapter = list.get(i);
            if (check== null ||(check.get("0__"+chapter.class_name) || check.get("1__"+chapter.subject_name) || check.get("2__"+chapter.chapter_name))){
                this.dataList.add(chapter);
            }
        }
        if (this.dataList.size() ==0){
            this.dataList = list;
        }
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        public TextView chapter_name, class_name, subject_name;
        public ImageView pic;
        public LinearLayout listView;
        public View view;
        public SearchViewHolder(View itemView) {
            super(itemView);
            chapter_name = (TextView) itemView.findViewById(R.id.title);
            class_name = (TextView) itemView.findViewById(R.id.className);
            subject_name = (TextView) itemView.findViewById(R.id.subjectName);
            pic = (ImageView) itemView.findViewById(R.id.pic);
            listView = (LinearLayout) itemView.findViewById(R.id.list);
            view = itemView;
        }

        public void addList(List<Topic> list){
            for (int i=0; i<list.size(); i++) {
                Topic topic = list.get(i);
                TextView msg = new TextView(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                msg.setLayoutParams(params);
                msg.setText(topic.name);
                listView.addView(msg);
            }
        }
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list_item, parent, false);

        return new SearchViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        final TopicList chapter = dataList.get(position);
        holder.chapter_name.setText(chapter.chapter_name);
        holder.class_name.setText(chapter.class_name);
        holder.subject_name.setText(chapter.subject_name);
        holder.addList(chapter.topics);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(mContext, QuestListActivity.class);
                intent.putExtra("chapterId",(String) chapter.chapterId);
                intent.putExtra("name",(String) chapter.chapter_name);
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return dataList.size();
    }
}