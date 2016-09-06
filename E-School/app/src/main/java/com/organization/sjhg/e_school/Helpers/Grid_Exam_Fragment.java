package com.organization.sjhg.e_school.Helpers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.organization.sjhg.e_school.Fragments.ExamListFragment;
import com.organization.sjhg.e_school.Fragments.Image_View_Fragment;
import com.organization.sjhg.e_school.Fragments.SamplePaperListFragment;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.ListStructure.DashBoardList;
import com.organization.sjhg.e_school.ListStructure.ExamPrepareList;
import com.organization.sjhg.e_school.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpan on 9/2/2016.
 */
public class Grid_Exam_Fragment extends FragmentStatePagerAdapter {

    List<DashBoardList> list;
    List<ChapterList> chapterLists;
    Context context;
    List<ExamPrepareList> examPrepareLists=new ArrayList<>();


    public Grid_Exam_Fragment(FragmentManager fm, List<DashBoardList> list,Context context) {
        super(fm);
        this.list=list;
        this.chapterLists=list.get(0).chapterLists;
        this.examPrepareLists=list.get(0).examPrepareLists;
        this.context=context;
    }


    @Override
    public Fragment getItem(int position) {



        if(position==1)
        {

            Bundle bundle=new Bundle();
            bundle.putSerializable(context.getString(R.string.sendlist),(Serializable)examPrepareLists);
            ExamListFragment examListFragment=new ExamListFragment();
            examListFragment.setArguments(bundle);
            return examListFragment;
        }
        else if(position==2)
        {
            Bundle bundle=new Bundle();
            bundle.putSerializable(context.getString(R.string.sendlist),(Serializable)chapterLists);
            SamplePaperListFragment samplePaperListFragment=new SamplePaperListFragment();
            samplePaperListFragment.setArguments(bundle);
            return samplePaperListFragment;
        }
        else if(position==0)
        {
            return new Image_View_Fragment();
        }
        else
        {
            return new Image_View_Fragment();
        }

    }
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        if(position==1)
        return context.getString(R.string.prepare);
        else if(position==2)
            return  context.getString(R.string.jsonsamplepaper);
        else if(position==0)
            return context.getString(R.string.practcice);
        else
            return "fgehg";
    }

    @Override
    public int getCount() {

        return 3;
    }
}
