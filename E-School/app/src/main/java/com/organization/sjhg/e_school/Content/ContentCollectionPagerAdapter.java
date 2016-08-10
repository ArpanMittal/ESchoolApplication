package com.organization.sjhg.e_school.Content;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Pair;

import com.organization.sjhg.e_school.Database.old.DatabaseOperations;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Bharat Lodha on 9/9/2015.
 * Organization : Eurovision Hitech Gurukul
 */
public class ContentCollectionPagerAdapter extends FragmentPagerAdapter {

    public List<Pair<Integer, String>> subjectList;

    public ContentCollectionPagerAdapter(FragmentManager fm, Context context) throws SQLException {
        super(fm);
        subjectList = DatabaseOperations.getLocalSubjectDetails(context);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new ContentListPageFragment();
        Bundle args = new Bundle();

        args.putInt(ContentListPageFragment.SUBJECT_ID_ARG_NAME, subjectList.get(position).first);
        args.putString(ContentListPageFragment.SUBJECT_NAME_ARG_NAME, subjectList.get(position).second);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return subjectList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return subjectList.get(position).second;
    }

    @Override
    public float getPageWidth(int position) {
        if (position == getCount() - 1)
            return (1f);
        else
            return (1f);
    }
}
