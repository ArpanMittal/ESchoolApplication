package com.organization.sjhg.e_school.Helpers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.organization.sjhg.e_school.Content.Quest.QuestListActivity;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by arpan on 9/9/2016.
 */
public class QuestionOptionAdapter extends RecyclerView.Adapter<QuestionOptionAdapter.ViewHolder> {
    private List<ChapterList> chapterLists;
    private Context context;
    private static CheckBox lastChecked = null;
    public static String lastCheckedId ;

    public QuestionOptionAdapter(Context context,List<ChapterList> chapterLists) {
        this.chapterLists = chapterLists;
        this.context = context;
    }

    @Override
    public QuestionOptionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_option_fragment, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(QuestionOptionAdapter.ViewHolder holder, final int position) {
        holder.option_text.setText(Html.fromHtml(chapterLists.get(position).name));

        holder.chkSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                String clickId=chapterLists.get(position).id;
                if(cb.isChecked())
                {
                    if(lastChecked != null)
                    {
                        lastChecked.setChecked(false);
                    }

                    lastChecked = cb;
                    lastCheckedId =clickId;
                }
                else
                    lastChecked = null;


            }
        });
    }


    @Override
    public int getItemCount() {
        return chapterLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView option_text;


        public CheckBox chkSelected;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            option_text = (TextView) itemLayoutView.findViewById(R.id.option_text);

            chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.chkSelected);




        }

    }
}
