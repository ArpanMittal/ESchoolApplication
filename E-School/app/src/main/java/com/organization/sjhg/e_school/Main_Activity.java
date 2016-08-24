package com.organization.sjhg.e_school;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
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
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
//import com.organization.sjhg.e_school.Animation.RecycleViewAnimation;

import com.organization.sjhg.e_school.Helpers.ConnectivityReceiver;
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
import com.organization.sjhg.e_school.Remote.VolleyController;
import com.organization.sjhg.e_school.Utils.ProgressBarActivity;
import com.organization.sjhg.e_school.Utils.ToastActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class Main_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,RemoteCallHandler,
        ConnectivityReceiver.ConnectivityReceiverListener {

    private RecyclerAdapter adapter;
    private ArrayList<String> stringArrayList;
    private View mDashboardView;
    private View mProgressView;
    private CircleIndicator indicator;
    private ProgressBarActivity progressBarActivity=new ProgressBarActivity();
    private ToastActivity toastActivity=new ToastActivity();
    private List<DashBoardList>dataList=new ArrayList<>();
    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.main_activity);
        ViewStub viewStub=(ViewStub)findViewById(R.id.viewstub);
        viewStub.setLayoutResource(R.layout.app_bar_main);
        View inflated = viewStub.inflate();
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDashboardView=findViewById(R.id.dashboard_form);
        mProgressView=findViewById(R.id.dashboard_progress);



        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle(getString(R.string.expand));
        //remote call to access data from server
        progressBarActivity.showProgress(mDashboardView,mProgressView,true,getApplicationContext());
        AutoScrollViewPager viewPager=(AutoScrollViewPager) findViewById(R.id.viewpager);

        viewPager.setAdapter(new Custom_Pager_Adapter(getSupportFragmentManager()));
        viewPager.setInterval(5000);
        viewPager.startAutoScroll();
        indicator=(CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        new RemoteHelper(getApplicationContext()).getDashBoardDetails(this, RemoteCalls.CHECK_LOGIN_CREDENTIALS);


    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Bundle args = new Bundle();
        // args.pu
        outState.putSerializable("LIST", (Serializable) dataList);
    }

    @Override
    protected void onResume(){
        super.onResume();
        VolleyController.getInstance().setConnectivityListener(this);

    }

    /*
            Do server call to fetch data
         */
    @Override
    protected void onStart() {
        super.onStart();
    }



    /*
        convert json response into desired list format
     */
    private List<DashBoardList> fetchData(JSONObject response)
    {
        List<DashBoardList> dashBoardLists = new ArrayList<>();
        try {


            JSONArray data = response.getJSONArray(getString(R.string.data));
            int length=data.length();
            for(int i=0;i<data.length();i++)
            {
                JSONObject dashBoardObject=data.getJSONObject(i);
                List<InternalList> internalLists = new ArrayList<>();
                JSONArray list=dashBoardObject.getJSONArray(getString(R.string.jsonlist));
                for(int j=0;j<list.length();j++)
                {
                    JSONObject internalListObject=list.getJSONObject(j);
                    internalLists.add(new InternalList(internalListObject.getString(getString(R.string.jsonid)),internalListObject.getString(getString(R.string.jsonname)),internalListObject.getString(getString(R.string.jsoncount))));

                }
                //internalLists.add(new InternalList(list.getString(R.string.jsonid),list.getString(R.string.jsonname),list.getString(R.string.jsoncount)));

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
    private void showView(List<DashBoardList> dataList)
    {
        /*
        show recycler view
         */
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);





        Recycler_View_Adapter adapter = new Recycler_View_Adapter(dataList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


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

    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        if(!isSuccessful)
        {

            toastActivity.makeUknownErrorMessage(this);
        }
        else
        {
            try {
                progressBarActivity.showProgress(mDashboardView,mProgressView,false,getApplicationContext());
                if (response.get("success").equals("false")) {
                    toastActivity.makeToastMessage(response,this);
                }
                else
                {
                    dataList=fetchData(response);
                    List<DashBoardList>hell=dataList;
                    showView(dataList);
                    //

                }
            }catch (Exception e)
            {
                LogHelper logHelper=new LogHelper(e);
                e.printStackTrace();
            }
        }
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}