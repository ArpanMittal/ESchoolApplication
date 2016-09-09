package com.organization.sjhg.e_school.Helpers;

import android.content.Context;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.organization.sjhg.e_school.Fragments.ExamListFragment;
import com.organization.sjhg.e_school.Fragments.Question_Fragment;
import com.organization.sjhg.e_school.ListStructure.QuestionList;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Structure.Question;

import java.io.Serializable;
import java.util.ArrayList;

import com.organization.sjhg.e_school.ListStructure.QuestionList;


import java.util.List;

/**
 * Created by arpan on 9/9/2016.
 */
public class QuestionAdapter extends FragmentStatePagerAdapter {

    private List<QuestionList> questionLists;

    private Context context;
    public QuestionAdapter(FragmentManager fm, List<QuestionList> list, Context context) {
        super(fm);
        this.questionLists=list;
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {

        List<QuestionList>  individual_question=new ArrayList<>();
        individual_question.add((questionLists.get(position)));
        Bundle bundle=new Bundle();
        bundle.putSerializable(context.getString(R.string.sendlist),(Serializable)individual_question);
        Question_Fragment question_fragment=new Question_Fragment();
        question_fragment.setArguments(bundle);
        return question_fragment;

    }

    @Override
    public int getCount() {

        return questionLists.size();
    }

}
