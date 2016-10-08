package com.organization.sjhg.e_school.Helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.ImageView;

import com.organization.sjhg.e_school.Fragments.Image_View_Fragment;
import com.organization.sjhg.e_school.Fragments.Tour_Image_Fragment;

/**
 * Created by arpan on 10/8/2016.
 */
public class MyTourPagerAdapter extends FragmentStatePagerAdapter {

    private static int NUM_ITEMS = 3;
    public MyTourPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return Tour_Image_Fragment.newInstance("image");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return Tour_Image_Fragment.newInstance("Page # 2");
            case 2: // Fragment # 1 - This will show SecondFragment
                return Tour_Image_Fragment.newInstance("Page # 3");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

}
