package com.organization.sjhg.e_school;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.organization.sjhg.e_school.Content.ContentCollectionPagerAdapter;
import com.organization.sjhg.e_school.Database.old.DatabaseOperations;
import com.organization.sjhg.e_school.Helpers.StudentApplicationUserData;
import com.organization.sjhg.e_school.TakeNotes.ChatHeadService;

import java.sql.SQLException;

/**
 * Created by Prateek Tulsyan on 11-02-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

/**
 * Edited by Gaurav Rawat.
 * Email: gauravrawat.official@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */
public class MainActivity extends CommonFragmentTheme {

    ContentCollectionPagerAdapter mContentCollectionPagerAdapter;
    ViewPager mViewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


            super.onCreate(savedInstanceState);

            // Instantiate a ViewPager and a PagerAdapter.
            try {
                if (!DatabaseOperations.getLocalSubjectDetails(getApplicationContext()).isEmpty()) {

                    mViewPager = (ViewPager) findViewById(R.id.contentList);
                    mContentCollectionPagerAdapter = new ContentCollectionPagerAdapter(
                            getSupportFragmentManager(),
                            this);
                    mViewPager.setAdapter(mContentCollectionPagerAdapter);

                    // Bind the tabs to the ViewPager
                    final PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
                    tabs.setViewPager(mViewPager);

                }
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong. Restart your device", Toast.LENGTH_LONG).show();
                finish();
            }

    }



    @Override
    protected void onResume() {
        try {
            if (StudentApplicationUserData.getNoteHeadServiceStatus(getApplicationContext())) {
                getApplicationContext().startService(new Intent(getApplicationContext(), ChatHeadService.class));
            }
        }catch (Exception e)
        {
            getApplicationContext().startService(new Intent(getApplicationContext(), ChatHeadService.class));
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog));
        builder1.setMessage(getResources().getString(R.string.do_you_want_to_exit));
        builder1.setTitle(getResources().getString(R.string.attention));
        builder1.setCancelable(true);
        builder1.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
