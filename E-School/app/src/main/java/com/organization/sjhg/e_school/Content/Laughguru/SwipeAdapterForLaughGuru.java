package com.organization.sjhg.e_school.Content.Laughguru;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.organization.sjhg.e_school.Database.old.DatabaseOperations;
import com.organization.sjhg.e_school.Structure.LaughguruContentDetailBase;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ishan on 1/28/2016.
 */
public class SwipeAdapterForLaughGuru extends FragmentPagerAdapter {

    public List<LaughguruContentDetailBase> lgList;
    public String cfid;
    public SwipeAdapterForLaughGuru(FragmentManager fm, Context context,String cfid) throws SQLException, JSONException {
        super(fm);
        this.cfid=cfid;
        lgList = DatabaseOperations.getPath(context, cfid);
    }

    @Override
    public Fragment getItem(int position) {
       Fragment fragment = new LaughGuruFragment();
        String imagePath="";
        String audioPath="";
        String order="";
        byte[] protectionDataI = new byte[0];
        byte[] protectionDataA=new byte[0];
        Bundle args = new Bundle();
        for(int j=0;j<lgList.size();++j) {
            if(lgList.get(j).Order1.equals(""+(position+1)))
            {
                imagePath=lgList.get(j).imagePath;
                audioPath=lgList.get(j).audioPath;
                order=lgList.get(j).Order1;
              protectionDataI=lgList.get(j).protectionDataI;

              //  protectionDataA=lgList.get(j).protectionDataA;
            }
        }
       // args.putInt(ContentListPageFragment.SUBJECT_ID_ARG_NAME, subjectList.get(position).first);
        args.putString(LaughGuruFragment.IMAGE_PATH, imagePath);
        args.putString(LaughGuruFragment.AUDIO_PATH, audioPath);
        args.putString(LaughGuruFragment.ORDER,order);
       args.putByteArray(LaughGuruFragment.PROTECTIONDATAI, protectionDataI);
//        args.putByteArray(LaughGuruFragment.PROTECTIONDATAA,protectionDataA);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return lgList.size();
    }

}
