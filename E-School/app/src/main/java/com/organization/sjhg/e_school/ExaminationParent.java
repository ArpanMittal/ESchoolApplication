package com.organization.sjhg.e_school;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.organization.sjhg.e_school.Fragments.Notes_Listing_Fragment;
import com.organization.sjhg.e_school.Helpers.Custom_Pager_Adapter;
import com.organization.sjhg.e_school.Helpers.Grid_Exam_Fragment;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.ListStructure.DashBoardList;
import com.organization.sjhg.e_school.ListStructure.ExamPrepareList;
import com.organization.sjhg.e_school.ListStructure.InternalList;
import com.organization.sjhg.e_school.ListStructure.InternalListData;
import com.organization.sjhg.e_school.ListStructure.ItemDataList;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Utils.ProgressBarActivity;
import com.organization.sjhg.e_school.Utils.ToastActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by arpan on 8/31/2016.
 */
public class ExaminationParent extends MainParentActivity {

    Context context;

    ViewPager viewPager;


    private View mProgressView;
    private String title;
    private String id;
    TabLayout tabLayout;
    private List<DashBoardList> list = new ArrayList<>();

    private ProgressBarActivity progressBarActivity = new ProgressBarActivity();
    private ToastActivity toastActivity = new ToastActivity();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        title = intent.getStringExtra(getString(R.string.jsontitle));
        id = intent.getStringExtra(getString(R.string.jsonid));
        ViewStub view_Stub = (ViewStub) findViewById(R.id.viewstub);
        view_Stub.setLayoutResource(R.layout.exam_app_bar);
        view_Stub.inflate();
        context = getApplicationContext();
        mProgressView = findViewById(R.id.dashboard_progress);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager_fragment);
        // code repeated in all activity
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Notes_Listing_Fragment.class);
                startActivity(intent);
            }
        });





        if (savedInstanceState != null) {

        } else {
            progressBarActivity.showProgress(viewPager, mProgressView, true, getApplicationContext());
            new RemoteHelper(context).getItemDetails(this, RemoteCalls.GET_EXAM_PREPARE_LIST, title, id);
        }


    }
    // Returns the page title for the top indicator


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

    private List<DashBoardList> getList(JSONObject response) {
        try {
            title = response.getString(getString(R.string.jsontitle));
            JSONArray data = response.getJSONArray(context.getString(R.string.data));
            List<ExamPrepareList> examPrepareLists = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonObject = data.getJSONObject(i);
                String cost = jsonObject.getString(getString(R.string.cost));
                String id = jsonObject.getString(getString(R.string.jsonid));
                String duration = jsonObject.getString(getString(R.string.duration));
                JSONArray subject = jsonObject.getJSONArray(context.getString(R.string.jsonsubject));
                List<InternalListData> internalListDatas = new ArrayList<>();
                for (int j = 0; j < subject.length(); j++) {
                    JSONObject jsonObject1 = subject.getJSONObject(j);
                    String name = jsonObject1.getString(context.getString(R.string.jsonname));
                    String id_subject = jsonObject1.getString(context.getString(R.string.jsonid));
                    JSONArray chapter = jsonObject1.getJSONArray(context.getString(R.string.json_chapter));
                    List<ChapterList> chapterLists = new ArrayList<>();

                    for (int k = 0; k < chapter.length(); k++) {
                        JSONObject jsonObject2 = chapter.getJSONObject(k);
                        String name1 = jsonObject2.getString(context.getString(R.string.jsonname));
                        String id_chapter = jsonObject2.getString(context.getString(R.string.jsonid));
                        chapterLists.add(new ChapterList(id_chapter,name1));
                    }

                    internalListDatas.add(new InternalListData(name, id_subject, chapterLists));

                }
                examPrepareLists.add(new ExamPrepareList(cost, duration, id, internalListDatas));

            }
            list.add(new DashBoardList(title, examPrepareLists, 1));
        } catch (Exception e) {
            e.printStackTrace();
            new ToastActivity().makeJsonException((Activity) context);
            new LogHelper(e);
        }
        return list;

    }

    private void showView(List<DashBoardList> list)
    {
       // Custom_Pager_Adapter custom_pager_adapter=new Custom_Pager_Adapter(getSupportFragmentManager());
       // viewPager.setAdapter(custom_pager_adapter);
        Grid_Exam_Fragment grid_exam_fragment=new Grid_Exam_Fragment(getSupportFragmentManager(),list,context);
        viewPager.setAdapter(grid_exam_fragment);

        tabLayout = (TabLayout) findViewById(R.id.id_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        super.HandleRemoteCall(isSuccessful,callFor,response,exception);
        progressBarActivity.showProgress(viewPager,mProgressView,false,getApplicationContext());
        if(!isSuccessful)
        {
            toastActivity.makeUknownErrorMessage((Activity) context);

        }
        else
        {
            switch (callFor) {
                case GET_EXAM_PREPARE_LIST: {
                    try {
                        if (response.getString("success").equals("false")) {
                            toastActivity.makeToastMessage(response,this);
                        } else {
                            if (response.getString(getString(R.string.jsoncode)).equals(getString(R.string.nocontentcode))) {
                                toastActivity.makeToastMessage(response,(Activity)context);
                            } else {
                                list = getList(response);
                                showView(list);
                               // List<DashBoardList>dashBoardLists=list;

                            }


                        }
                    } catch (Exception e) {
                        LogHelper logHelper = new LogHelper(e);
                        e.printStackTrace();
                    }
                }
            }
        }
    }

//    private class MyAdapter extends PagerAdapter {
//
//        String[] desc;
//        int[] image;
//
//
//        public MyAdapter(String[] desc, int[] image) {
//
//            super();
//            this.desc = desc;
//            this.image = image;
//
//
//        }
//
//        @SuppressLint("NewApi")
//        @Override
//        public void finishUpdate(ViewGroup container) {
//            // TODO Auto-generated method stub
//            super.finishUpdate(container);
//
//        }
//
//        @Override
//        public int getCount() {
//
//            return desc.length;
//
//        }
//
//        @Override
//        public boolean isViewFromObject(View collection, Object object) {
//
//            return collection == ((View) object);
//        }
//
//        @Override
//        public Object instantiateItem(View collection, int position) {
//
//            // Inflating layout
//            LayoutInflater inflater = (LayoutInflater) collection.getContext()
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            // Setting view you want to display as a row element
//
//            View view = inflater.inflate(R.layout.examination_item, null);
//
//            TextView itemText = (TextView) view.findViewById(R.id.textViewMain);
//
//            ImageView imageView = (ImageView) view.findViewById(R.id.imageViewmain);
//
//
//            try {
//
//                itemText.setText(desc[position]);
//
//                imageView.setImageResource(image[position]);
//            } catch (Exception e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//            }
//            ((ViewPager) collection).addView(view, 0);
//            return view;
//
//        }
//
//        @Override
//        public void destroyItem(View collection, int position, Object view) {
//            ((ViewPager) collection).removeView((View) view);
//
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return "TAB";
//        }
//    }
}


