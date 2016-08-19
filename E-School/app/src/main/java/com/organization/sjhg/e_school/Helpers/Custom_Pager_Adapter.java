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


/**
 * Created by arpan on 8/18/2016.
 */
public class Custom_Pager_Adapter extends FragmentStatePagerAdapter {
    private Context mContext;


    public Custom_Pager_Adapter(FragmentManager fm)
    {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch(position) {

            case 0: return Image_View_Fragment.newInstance("FirstFragment, Instance 1");
            case 1: return Image_View_Fragment.newInstance("SecondFragment, Instance 1");
            case 2: return Image_View_Fragment.newInstance("ThirdFragment, Instance 1");
            case 3: return Image_View_Fragment.newInstance("ThirdFragment, Instance 2");
            case 4: return Image_View_Fragment.newInstance("ThirdFragment, Instance 3");
            default: return Image_View_Fragment.newInstance("ThirdFragment, Default");
        }

    }

    @Override
    public int getCount() {
        return 5;
    }
}
