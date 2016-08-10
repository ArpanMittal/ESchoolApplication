package com.organization.sjhg.e_school.Content.Test;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import com.organization.sjhg.e_school.Content.Content_Type;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Structure.Question;

import java.util.Arrays;

/**
 * Created by Bharat Lodha on 10/3/2015.
 * Organization : Eurovision Hitech Gurukul
 */
public class TestCollectionPagerAdapter extends FragmentStatePagerAdapter {
 //  public String[] correctAnswer;
    Fragment fragment;
    Fragment[] allFragments;
    //private  FragmentManager mFragmentManager;

    //private Fragment mCurrentPrimaryItem = null;
    //private FragmentTransaction mCurTransaction = null;
    public TestCollectionPagerAdapter(FragmentManager fm) {
        super(fm);
        this.allFragments = new Fragment[TestActivity.test.questions.size()];
    }




    @Override
    public Fragment getItem(int position) {

        if (allFragments[position] == null) {
            Fragment fragment = QuestionFragment.getFragment(position);
            //if(TestActivity.test.contentTypeId==6)
              //  return fragment;
            //else
                allFragments[position] = fragment;
  //              correctAnswer[position]=TestActivity.test.questions.get(position).correctAnswer;
       }


        return allFragments[position];
    }

    @Override
    public int getCount() {
        return TestActivity.test.questions.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position + 1);
    }

    @Override
    public float getPageWidth(int position) {
        if (position == getCount() - 1)
            return (1f);
        else
            return (1f);
    }
}
