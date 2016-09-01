package com.organization.sjhg.e_school;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.astuetz.PagerSlidingTabStrip;
import com.organization.sjhg.e_school.Helpers.Custom_Pager_Adapter;
import com.organization.sjhg.e_school.ListStructure.InternalList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by arpan on 8/31/2016.
 */
public class ExaminationParent extends MainParentActivity {

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        ViewStub viewStub=(ViewStub)findViewById(R.id.viewstub);
//        viewStub.setLayoutResource(R.layout.examination_layout);
//        viewStub.inflate();
//
////        ViewPager viewPager = (ViewPager) findViewById(R.id.vpPager);
////        viewPager.setAdapter(new Custom_Pager_Adapter(getSupportFragmentManager()));
////        AutoScrollViewPager viewPager = (AutoScrollViewPager) findViewById(R.id.viewpager);
////        viewPager.setAdapter(new Custom_Pager_Adapter(getSupportFragmentManager()));
//        //viewPager.setPageTransformer(true, new RotateUpTransformer());
//        //PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
//        // Attach the view pager to the tab strip
//        //tabsStrip.setViewPager(viewPager);
//
//
//
//    }

    Context context;

    String[] timeSets;
    String[] coreTargets;
    String[] equipments;

    int[] timeImages;
    int[] coreImages;
    int[] equipmentImages;

    MyAdapter adapter1, adapter2, adapter3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Intent intent=getIntent();
//        ViewStub viewStub=(ViewStub)findViewById(R.id.viewstub);
//        viewStub.setLayoutResource(R.layout.examination_item);
//        viewStub.inflate();

        context =getApplicationContext();

        coreTargets = new String[]{"Full body", "Core",
                "Legs", "Upper Body"};

        coreImages = new int[]{R.drawable.ic_launcher, R.drawable.add_note,
                R.drawable.common_google_signin_btn_icon_dark, R.drawable.common_google_signin_btn_icon_light_focused};

        timeSets = new String[]{"15 Minutes", "20 Minutes",
                "30 Minutes", "45 Minutes"};

        timeImages = new int[]{R.drawable.background1withlogo, R.drawable.common_google_signin_btn_icon_light_focused,
                R.drawable.common_full_open_on_phone, R.drawable.common_google_signin_btn_icon_dark_focused};

        equipments = new String[]{"Rope", "Kette Bell",
                "Weight", "Hat"};

        equipmentImages = new int[]{R.drawable.paint, R.drawable.backgrounddesign,
                R.drawable.common_google_signin_btn_text_light_normal, R.drawable.menu_button};

         ViewPager view1 = (ViewPager) findViewById(R.id.viewpager1);
//        ViewPager view2 = (ViewPager) findViewById(R.id.viewpager2);
//        ViewPager view3 = (ViewPager) findViewById(R.id.viewpager3);
//
        adapter1 = new MyAdapter(coreTargets, coreImages);
//        adapter2 = new MyAdapter(timeSets, timeImages);
//        adapter3 = new MyAdapter(equipments, equipmentImages);
//
        view1.setAdapter(adapter1);
//
        view1.setPageTransformer(true, new RotateUpTransformer());
//        view2.setAdapter(adapter2);
//        view2.setPageTransformer(true, new RotateUpTransformer());
//        view3.setAdapter(adapter3);
//        view3.setPageTransformer(true, new RotateUpTransformer());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyAdapter extends PagerAdapter {

        String[] desc;
        int[] image;


        public MyAdapter(String[] desc, int[] image) {

            super();
            this.desc = desc;
            this.image = image;


        }

        @SuppressLint("NewApi")
        @Override
        public void finishUpdate(ViewGroup container) {
            // TODO Auto-generated method stub
            super.finishUpdate(container);

        }

        @Override
        public int getCount() {

            return desc.length;

        }

        @Override
        public boolean isViewFromObject(View collection, Object object) {

            return collection == ((View) object);
        }

        @Override
        public Object instantiateItem(View collection, int position) {

            // Inflating layout
            LayoutInflater inflater = (LayoutInflater) collection.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Setting view you want to display as a row element
            View view = inflater.inflate(R.layout.examination_item, null);

            TextView itemText = (TextView) view.findViewById(R.id.textViewMain);

            ImageView imageView = (ImageView) view.findViewById(R.id.imageViewmain);


            try {

                itemText.setText(desc[position]);

                imageView.setImageResource(image[position]);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            ((ViewPager) collection).addView(view, 0);
            return view;

        }

        @Override
        public void destroyItem(View collection, int position, Object view) {
            ((ViewPager) collection).removeView((View) view);

        }

    }
}


