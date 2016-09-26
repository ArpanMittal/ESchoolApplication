package com.organization.sjhg.e_school.Helpers;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.organization.sjhg.e_school.Fragments.Image_View_Fragment;
import com.organization.sjhg.e_school.ListStructure.ChapterList;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by arpan on 8/18/2016.
 */
public class Custom_Pager_Adapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private List<ChapterList> imageList=new ArrayList<>();


    public Custom_Pager_Adapter(FragmentManager fm,List<ChapterList>chapterLists)
    {
        super(fm);
        this.imageList=chapterLists;
    }
    @Override
    public Fragment getItem(int position) {
        if(position==0)
        {
            return Image_View_Fragment.newInstance();
        }
        else {
            return Image_View_Fragment.newInstance(imageList.get(position-1).id, imageList.get(position-1).name);
        }


    }

    @Override
    public int getCount() {
        return imageList.size()+1;
    }
}
