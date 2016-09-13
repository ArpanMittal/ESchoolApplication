package com.organization.sjhg.e_school.Content.NewTest;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.Toast;

import com.organization.sjhg.e_school.Fragments.Notes_Listing_Fragment;
import com.organization.sjhg.e_school.Helpers.Custom_Pager_Adapter;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.ListStructure.InternalList;
import com.organization.sjhg.e_school.LoginActivity;
import com.organization.sjhg.e_school.MainParentActivity;
import com.organization.sjhg.e_school.Main_Activity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.Utils.ProgressBarActivity;
import com.organization.sjhg.e_school.Utils.SharedPrefrence;
import com.organization.sjhg.e_school.Utils.ToastActivity;

import org.json.JSONObject;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by arpan on 9/7/2016.
 */
public class TestInstructionActivity extends MainParentActivity {

    private View mDashboardView;
    private View mProgressView;
    private Button button;

    private ProgressBarActivity progressBarActivity=new ProgressBarActivity();
    private ToastActivity toastActivity=new ToastActivity();
    private SharedPrefrence sharedPrefrence=new SharedPrefrence();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent=getIntent();
        final String tag=intent.getStringExtra("Tag");
        final String id=intent.getStringExtra("Id");
        //framework code
        ViewStub view_Stub=(ViewStub)findViewById(R.id.viewstub);
        view_Stub.setLayoutResource(R.layout.app_bar_main);
        view_Stub.inflate();
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // code repeted in all activity
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle(getString(R.string.expand));
        AutoScrollViewPager viewPager = (AutoScrollViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new Custom_Pager_Adapter(getSupportFragmentManager()));
        viewPager.setInterval(5000);
        viewPager.startAutoScroll();
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent=new Intent(getApplicationContext(), Notes_Listing_Fragment.class);
                startActivity(intent);
            }
        });
        ViewStub viewStub = (ViewStub) findViewById(R.id.view_stub_bar);
        viewStub.setLayoutResource(R.layout.activity_test_instruction_activity);
        viewStub.inflate();

        button=(Button)findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPrefrence.getAccessToken(getApplicationContext())==null)
                {
                    Intent intent1=new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent1);
                }
                else
                {
                    Intent intent1=new Intent(getApplicationContext(),TestActivity.class);
                    intent1.putExtra("Tag",tag);
                    intent1.putExtra("Id",id);
                    startActivity(intent1);
                    finish();
                }
            }
        });

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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        } else if (id == R.id.menu_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
