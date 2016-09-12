package com.organization.sjhg.e_school.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.organization.sjhg.e_school.Content.Quest.QuestListActivity;
import com.organization.sjhg.e_school.Database.contracts.UserContract;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Utils.Latex_Image_Loader;
import com.organization.sjhg.e_school.Utils.ProgressBarActivity;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by arpan on 9/9/2016.
 */
public class QuestionOptionAdapter extends RecyclerView.Adapter<QuestionOptionAdapter.ViewHolder>{
    private List<ChapterList> chapterLists;
    private Context context;
    private  CheckBox lastChecked = null;
    public String lastCheckedId=null ;
    public  int lastcheckposition;
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

    private void makeCheckbox(View v,int position)
    {
        CheckBox cb = (CheckBox) v.findViewById(R.id.chkSelected);
//               // cb.setChecked(true);
        String clickId=chapterLists.get(position).id;
        if(lastCheckedId!=clickId)
        {
            if(lastCheckedId==null)
            {
                cb.setChecked(true);
                chapterLists.get(position).checked_option_id=clickId;
                chapterLists.get(position).checkBox=cb;
                lastCheckedId=clickId;
                lastcheckposition=position;
                lastChecked=cb;
            }
            else {
                lastChecked.setChecked(false);

                cb.setChecked(true);
                chapterLists.get(lastcheckposition).checked_option_id=null;
                chapterLists.get(lastcheckposition).checkBox=null;
                chapterLists.get(position).checked_option_id = clickId;
                chapterLists.get(position).checkBox = cb;
                lastCheckedId=clickId;
                lastChecked=cb;
                lastcheckposition=position;
            }

            Cursor cursor = context.getContentResolver().query(
                    UserContract.TestDetail.CONTENT_URI, null,
                    UserContract.TestDetail.COLUMN_QUESTION_ID + " =? ",
                    new String[]{questionId},
                    null,
                    null
            );
            ContentValues contentValues = new ContentValues();
            contentValues.put(UserContract.TestDetail.COLUMN_OPTION_ID, clickId);
            if (clickId.equals(answer)) {
                is_correct = "true";
            }
            else {
                is_correct = "false";
            }
            contentValues.put(UserContract.TestDetail.COLUMN_TIME_SPEND, 0.0);
            contentValues.put(UserContract.TestDetail.COLUMN_IS_CORRECT, is_correct);

            int count = cursor.getCount();
            if (count > 0) {

                int result = context.getContentResolver().update(UserContract.TestDetail.CONTENT_URI, contentValues,
                        UserContract.TestDetail.COLUMN_QUESTION_ID + "=?",
                        new String[]{questionId});
            }
        }
        else
        {
            lastChecked.setChecked(true);
        }
    }



    @Override
    public void onBindViewHolder(final QuestionOptionAdapter.ViewHolder holder, final int position) {
        String code=chapterLists.get(position).name;
        Spanned spanned = Html.fromHtml(code, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                LevelListDrawable d = new LevelListDrawable();
                Drawable empty = context.getResources().getDrawable(R.drawable.ic_launcher);
                d.addLevel(0, 0, empty);
                d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                new Latex_Image_Loader().execute(source, d,holder.option_text);
                return d;
            }
        }, null);
        holder.option_text.setText(spanned);
        //holder.option_text.setText(Html.fromHtml(chapterLists.get(position).name));

        if(chapterLists.get(position).checked_option_id!=null)
        {
           // chapterLists.get(position).checkBox.setChecked(true);
           //holder.chkSelected.setChecked(true);
            lastcheckposition=position;
           lastChecked=holder.chkSelected;
            lastCheckedId=chapterLists.get(position).checked_option_id;
        }
        if(lastChecked!=null)
            lastChecked.setChecked(true);
        holder.chkSelected.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                makeCheckbox(v,position);
               holder.chkSelected.setBackgroundColor(0);
               // notifyDataSetChanged();

            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                makeCheckbox(v,position);
                holder.chkSelected.setBackgroundColor(0);
                //notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return chapterLists.size();
    }

//    @Override
//    public Drawable getDrawable(String source) {
//        LevelListDrawable d = new LevelListDrawable();
//        Drawable empty = context.getResources().getDrawable(R.drawable.ic_launcher);
//        d.addLevel(0, 0, empty);
//        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
//        new LoadImage().execute(source, d,textView);
//        return d;
//    }





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
