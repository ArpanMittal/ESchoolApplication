package com.organization.sjhg.e_school.Helpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Html;
import android.text.Spanned;

import com.organization.sjhg.e_school.Fragments.Question_Answer_Fragment;
import com.organization.sjhg.e_school.Fragments.Question_Fragment;
import com.organization.sjhg.e_school.ListStructure.QuestionAnswerList;
import com.organization.sjhg.e_school.ListStructure.QuestionList;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.organization.sjhg.e_school.Utils.Latex_Image_Loader;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpan on 9/17/2016.
 */
public class QuestionAnswerAdapter extends FragmentStatePagerAdapter {

    private List<QuestionAnswerList> questionAnswerLists;

    private Context context;
    public QuestionAnswerAdapter(FragmentManager fm, List<QuestionAnswerList> list, Context context) {
        super(fm);
        this.questionAnswerLists=list;
        this.context=context;
    }
    @Override
    public Fragment getItem(int position) {
        List<QuestionAnswerList>  individual_question_answer=new ArrayList<>();
        individual_question_answer.add((questionAnswerLists.get(position)));
        Bundle bundle=new Bundle();
        bundle.putSerializable(context.getString(R.string.sendlist),(Serializable)individual_question_answer);
        Question_Answer_Fragment question_answer_fragment=new Question_Answer_Fragment();
        question_answer_fragment.setArguments(bundle);

        return question_answer_fragment;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return "hello";
       // String val=Integer.toString(position);
       // return Integer.toString(position);
    }

    @Override
    public int getCount() {
        return questionAnswerLists.size();
    }
}
