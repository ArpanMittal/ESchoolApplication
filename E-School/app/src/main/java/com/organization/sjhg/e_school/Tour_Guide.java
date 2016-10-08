package com.organization.sjhg.e_school;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.organization.sjhg.e_school.Helpers.MyTourPagerAdapter;
import com.organization.sjhg.e_school.Utils.SharedPrefrence;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by arpan on 10/8/2016.
 */
public class Tour_Guide extends AppCompatActivity {
    final SharedPrefrence sharedPrefrence=new SharedPrefrence();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_layout);
        final ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        MyTourPagerAdapter adapterViewPager = new MyTourPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        CircleIndicator circleIndicator=(CircleIndicator)findViewById(R.id.indicator);
        circleIndicator.setViewPager(vpPager);
        final Button skip=(Button)findViewById(R.id.skip);
        final Button next=(Button)findViewById(R.id.next);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               skip();
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vpPager.getAdapter().getCount()==vpPager.getCurrentItem()+1)
                {
                    skip();
                }
                else
                vpPager.setCurrentItem(vpPager.getCurrentItem()+1,true);
            }
        });

       // next.setOnClickListener();
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if( vpPager.getAdapter().getCount()==position+1)
                {
                    next.setText(getString(R.string.done));

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void skip()
    {
        sharedPrefrence.saveTour_Guide_Mode(getApplicationContext(),"true");
        Intent intent=new Intent(Tour_Guide.this,Main_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
