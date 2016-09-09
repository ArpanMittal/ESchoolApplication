package com.organization.sjhg.e_school.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.organization.sjhg.e_school.Content.Quest.QuestListActivity;
import com.organization.sjhg.e_school.Database.contracts.UserContract;
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
    private String questionId;
    private String answer;
    private String is_correct;

    public QuestionOptionAdapter(Context context,List<ChapterList> chapterLists,String questionId,String answer) {
        this.chapterLists = chapterLists;
        this.context = context;
        this.questionId=questionId;
        this.answer=answer;
    }

    @Override
    public QuestionOptionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_option_fragment, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(QuestionOptionAdapter.ViewHolder holder, final int position) {
        holder.option_text.setText(Html.fromHtml(chapterLists.get(position).name));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v.findViewById(R.id.chkSelected);
                cb.setChecked(true);
                String clickId=chapterLists.get(position).id;
                if(cb.isChecked())
                {
                    if(lastChecked != null)
                    {
                        lastChecked.setChecked(false);
                    }

                    lastChecked = cb;
                    lastCheckedId =clickId;

                    Cursor cursor = context.getContentResolver().query(
                            UserContract.TestDetail.CONTENT_URI, null,
                            UserContract.TestDetail.COLUMN_QUESTION_ID+" =? ",
                            new String[]{questionId},
                            null,
                            null
                    );
                    int count = cursor.getCount();
                    if(count >0){
                        ContentValues contentValues=new ContentValues();
                        contentValues.put(UserContract.TestDetail.COLUMN_OPTION_ID,lastCheckedId);
                        if(lastCheckedId.equals(answer))
                        {
                            is_correct="true";
                        }
                        else
                        {
                            is_correct="false";
                        }
                        contentValues.put(UserContract.TestDetail.COLUMN_IS_CORRECT,is_correct);

                        int result = context.getContentResolver().update(UserContract.TestDetail.CONTENT_URI,contentValues,
                                UserContract.TestDetail.COLUMN_QUESTION_ID+"=?",
                                new String[]{questionId});
                    }
                    else
                    {
                        //TODO: insert into databse
                    }

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
        public RelativeLayout view;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            option_text = (TextView) itemLayoutView.findViewById(R.id.option_text);

            chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.chkSelected);
            view=(RelativeLayout) itemLayoutView.findViewById(R.id.option);




        }

    }
}
