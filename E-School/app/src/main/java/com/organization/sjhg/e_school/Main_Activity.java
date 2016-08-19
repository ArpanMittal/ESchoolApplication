package com.organization.sjhg.e_school;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.organization.sjhg.e_school.Helpers.Custom_Pager_Adapter;
import com.organization.sjhg.e_school.Helpers.Data;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.Helpers.RecyclerAdapter;
import com.organization.sjhg.e_school.Helpers.Recycler_View_Adapter;
import com.organization.sjhg.e_school.ListStructure.DashBoardList;
import com.organization.sjhg.e_school.ListStructure.InternalList;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.ExceptionHandler;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Utils.ProgressBarActivity;
import com.organization.sjhg.e_school.Utils.ToastActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class Main_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,RemoteCallHandler {
    private RecyclerAdapter adapter;
    private ArrayList<String> stringArrayList;
    private View mDashboardView;
    private View mProgressView;
    private ProgressBarActivity progressBarActivity=new ProgressBarActivity();
    private ToastActivity toastActivity=new ToastActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDashboardView=findViewById(R.id.dashboard_form);
        mProgressView=findViewById(R.id.dashboard_progress);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CircleIndicator indicator=(CircleIndicator)findViewById(R.id.indicator);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle(getString(R.string.expand));
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        AutoScrollViewPager viewPager=(AutoScrollViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new Custom_Pager_Adapter(getSupportFragmentManager()));
        viewPager.setInterval(5000);
        viewPager.startAutoScroll();
        indicator.setViewPager(viewPager);
        //RecyclerView recyclerViewtoolbar=(RecyclerView)findViewById(R.id.recycler_appbar);
//        recyclerViewtoolbar.setHasFixedSize(true);
       /* RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.recycler1);
        recyclerView.setHasFixedSize(true);
        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.recycler2);
        recyclerView.setHasFixedSize(true);*/
        List<Data> data = fill_with_data();

       // RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        Recycler_View_Adapter adapter = new Recycler_View_Adapter(data, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerAdapter adapter1=new RecyclerAdapter(getApplicationContext(),data);
//      recyclerViewtoolbar.setAdapter(adapter1);
//        recyclerViewtoolbar.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // for animation in listview
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /*
        Do server call to fetch data
     */
    @Override
    protected void onStart() {
        super.onStart();
        progressBarActivity.showProgress(mDashboardView,mProgressView,true,getApplicationContext());
        new RemoteHelper(getApplicationContext()).getDashBoardDetails(this, RemoteCalls.CHECK_LOGIN_CREDENTIALS);


    }

    /* private void setData() {
            stringArrayList = new ArrayList<>();

            for (int i = 0; i < 100; i++) {
                stringArrayList.add("Item " + (i + 1));
            }
        }*/
    public List<Data> fill_with_data() {

        List<Data> data = new ArrayList<>();

        data.add(new Data("Batman vs Superman", "Following the destruction of Metropolis, Batman embarks on a personal vendetta against Superman ", R.drawable.common_google_signin_btn_text_dark_pressed));
        data.add(new Data("X-Men: Apocalypse", "X-Men: Apocalypse is an upcoming American superhero film based on the X-Men characters that appear in Marvel Comics ", R.drawable.chat_text_disabled));
        data.add(new Data("Captain America: Civil War", "A feud between Captain America and Iron Man leaves the Avengers in turmoil.  ", R.drawable.common_google_signin_btn_text_dark_pressed));
        data.add(new Data("Kung Fu Panda 3", "After reuniting with his long-lost father, Po  must train a village of pandas", R.drawable.chat_text_disabled));
        data.add(new Data("Warcraft", "Fleeing their dying home to colonize another, fearsome orc warriors invade the peaceful realm of Azeroth. ", R.drawable.common_google_signin_btn_text_dark_pressed));
        data.add(new Data("Alice in Wonderland", "Alice in Wonderland: Through the Looking Glass ", R.drawable.chat_text_disabled));
        data.add(new Data("Batman vs Superman", "Following the destruction of Metropolis, Batman embarks on a personal vendetta against Superman ", R.drawable.common_google_signin_btn_text_dark_pressed));
        data.add(new Data("X-Men: Apocalypse", "X-Men: Apocalypse is an upcoming American superhero film based on the X-Men characters that appear in Marvel Comics ", R.drawable.chat_text_disabled));
        data.add(new Data("Captain America: Civil War", "A feud between Captain America and Iron Man leaves the Avengers in turmoil.  ", R.drawable.common_google_signin_btn_text_dark_pressed));
        data.add(new Data("Kung Fu Panda 3", "After reuniting with his long-lost father, Po  must train a village of pandas", R.drawable.chat_text_disabled));
        data.add(new Data("Warcraft", "Fleeing their dying home to colonize another, fearsome orc warriors invade the peaceful realm of Azeroth. ", R.drawable.common_google_signin_btn_text_dark_pressed));
        data.add(new Data("Alice in Wonderland", "Alice in Wonderland: Through the Looking Glass ", R.drawable.chat_text_disabled));

        return data;
    }
    /*
        convert json response into desired list format
     */
    private List<DashBoardList> fetchData(JSONObject response)
    {
        List<DashBoardList> dashBoardLists = new ArrayList<>();
        try {


            JSONArray data = response.getJSONArray(getString(R.string.data));
            for(int i=0;i<data.length();i++)
            {
                JSONObject dashBoardObject=data.getJSONObject(i);
                List<InternalList> internalLists = new ArrayList<>();
                JSONArray list=dashBoardObject.getJSONArray(getString(R.string.jsonlist));
                internalLists.add(new InternalList(list.getString(R.string.jsonid),list.getString(R.string.jsonname),list.getString(R.string.jsoncount)));
                dashBoardLists.add(new DashBoardList(dashBoardObject.getString(getString(R.string.jsontitle)),internalLists));
            }

            //for (int i = 0; i <)
        }catch (JSONException jsonException) {
            toastActivity.makeJsonException(this);
            LogHelper logHelper = new LogHelper(jsonException);
            jsonException.printStackTrace();
        }
        return dashBoardLists;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id==R.id.menu_search){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        if(!isSuccessful)
        {

            toastActivity.makeUknownErrorMessage(this);
        }
        else
        {
            try {
                if (response.get("sucess").equals("false")) {
                    toastActivity.makeToastMessage(response,this);
                }
                else
                {
                    List<DashBoardList>list=fetchData(response);
                }
            }catch (Exception e)
            {
                LogHelper logHelper=new LogHelper(e);
                e.printStackTrace();
            }
        }
    }

}