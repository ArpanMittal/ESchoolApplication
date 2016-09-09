package com.organization.sjhg.e_school.Helpers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
