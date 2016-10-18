package com.organization.sjhg.e_school.Helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.ImageView;

import com.organization.sjhg.e_school.Fragments.Image_View_Fragment;
import com.organization.sjhg.e_school.Fragments.Tour_Image_Fragment;
import com.organization.sjhg.e_school.R;

/**
 * Created by arpan on 10/8/2016.
 */
public class MyTourPagerAdapter extends FragmentStatePagerAdapter {

    private static int NUM_ITEMS = 2;

    public MyTourPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0: // Fragment # 0 - This will show FirstFragment different title
                String string="1. Attempt practice sheets and sample papers and gear up for an examination ";
                return Tour_Image_Fragment.newInstance(R.drawable.intro_home,string);
            case 1: // Fragment # 1 - This will show SecondFragment
                 String string1="1. Attempt a sample paper and see where you stand " +System.lineSeparator()+
                         "2. View your previous performances";
                return Tour_Image_Fragment.newInstance(R.drawable.intro_exam,string1);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

}
