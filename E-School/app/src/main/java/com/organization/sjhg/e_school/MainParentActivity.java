package com.organization.sjhg.e_school;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.organization.sjhg.e_school.Helpers.ConnectivityReceiver;
import com.organization.sjhg.e_school.Helpers.ExpandListAdapter;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.ListStructure.DashBoardList;
import com.organization.sjhg.e_school.ListStructure.InternalList;
import com.organization.sjhg.e_school.Profile.ProfileActivity;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Remote.VolleyController;
import com.organization.sjhg.e_school.Utils.SharedPrefrence;
import com.organization.sjhg.e_school.Utils.ToastActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by arpan on 8/24/2016.
 */
public class MainParentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ConnectivityReceiver.ConnectivityReceiverListener,RemoteCallHandler {

    protected CircleIndicator indicator;

    //protected List<DashBoardList> dataList=new ArrayList<>();
    protected ActionBarDrawerToggle toggle;
    protected CollapsingToolbarLayout collapsingToolbar;
    protected Toolbar toolbar;
    private NavigationView navigationView;
    protected static List<DashBoardList> dataList=new ArrayList<>();
    protected List<ChapterList>imageList =new ArrayList<>();
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    Map content = new HashMap();
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    protected  DrawerLayout drawer;
    int key=0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);







        if(savedInstanceState!=null)
        {
            dataList=(List<DashBoardList>) savedInstanceState.getSerializable("LIST");
            fillNavigationDrawer(dataList, navigationView);

        }
        else {

            new RemoteHelper(getApplicationContext()).getDashBoardDetails(this, RemoteCalls.GET_DASHBOARD_LIST);
        }
        View view=findViewById(R.id.home);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainParentActivity.this,Main_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        ConnectivityReceiver.isConnected();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("LIST", (Serializable) dataList);
        outState.putSerializable("IMAGELIST",(Serializable)imageList);
    }

    private void fillNavigationDrawer(final List<DashBoardList> dataList, NavigationView navigationView) {

        expListView=(ExpandableListView)navigationView.findViewById(R.id.navigationmenu);
        // preparing list data
        prepareListData(dataList);

        listAdapter = new ExpandListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Expanded",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener()
        {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Collapsed",
//                        Toast.LENGTH_SHORT).show();
//
            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                List<InternalList> internalList=dataList.get(groupPosition).internalLists;
//                String childId= internalList.get(childPosition).id;
//                // TODO Auto-generated method stub
//                Toast.makeText(
//                        getApplicationContext(),
//                        listDataHeader.get(groupPosition)
//                                + " : "
//                                +internalList.get(childPosition).id, Toast.LENGTH_SHORT)
//                        .show();
                if(listDataHeader.get(groupPosition).equals("Exams"))
                {
                    Intent intent = new Intent(getApplicationContext(), ExaminationParent.class);
                    intent.putExtra(getString(R.string.jsontitle), listDataHeader.get(groupPosition));
                    intent.putExtra(getString(R.string.jsonid), internalList.get(childPosition).id);
                    intent.putExtra(getString(R.string.jsonname), internalList.get(childPosition).name);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                    startActivity(intent);

                }
                else if (internalList.get(childPosition).isActive!=null && internalList.get(childPosition).isActive.equals("1")){
                    Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                    intent.putExtra(getString(R.string.title), listDataHeader.get(groupPosition));
                    intent.putExtra(getString(R.string.jsonid), internalList.get(childPosition).id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


            }
                return false;
        }});

        FrameLayout logout =(FrameLayout) navigationView.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPrefrence().logout(getApplicationContext());
                Intent intent = new Intent(MainParentActivity.this,Main_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainParentActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        };

        SharedPrefrence sharedPrefrence = new SharedPrefrence();
        String token = sharedPrefrence.getAccessToken(this);
        View headerLayout = navigationView.getHeaderView(0);
        TextView username = (TextView)headerLayout.findViewById(R.id.userName);
        if (username!=null && token!=null){
            username.setVisibility(View.VISIBLE);
            String user = sharedPrefrence.getUserName(getApplicationContext());
            username.setText(user);
            username.setOnClickListener(listener);
        }


        TextView email = (TextView)headerLayout.findViewById(R.id.eMail);
        Button login = (Button)headerLayout.findViewById(R.id.login);
        if (email!=null && token!=null){
            email.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
        }
        CircleImageView profile_pic = (CircleImageView) headerLayout.findViewById(R.id.profile_image);
        if (sharedPrefrence.getUserEmail(getApplicationContext()) != null && email!=null && token!=null){
            email.setText(sharedPrefrence.getUserEmail(getApplicationContext()));
            login.setVisibility(View.GONE);
            if (!sharedPrefrence.getUserPic(getApplicationContext()).equals("")){
                Picasso.with(this)
                        .load(sharedPrefrence.getUserPic(getApplicationContext()))
                        .placeholder(R.drawable.ic_account_circle_white_48dp)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .into(profile_pic);
            }
            profile_pic.setOnClickListener(listener);
            email.setOnClickListener(listener);
        }else if (email!= null)
            {
            username.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainParentActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void prepareListData(List<DashBoardList> dataList)
    {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        for(int i=0;i<dataList.size();i++)
        {
            if (dataList.get(i).isActive!=null && dataList.get(i).isActive.equals("1")){
                listDataHeader.add(dataList.get(i).title.toString());
                List<InternalList> internalList=dataList.get(i).internalLists;
                List<String>dataChild=new ArrayList<String>();
                for(int j=0;j<internalList.size();j++)
                {
                    dataChild.add(internalList.get(j).name);
                }
                listDataChild.put(listDataHeader.get(i),dataChild);
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        VolleyController.getInstance().setConnectivityListener(this);
        if (dataList!=null){
            fillNavigationDrawer(dataList, navigationView);

        }

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
            toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        toggle.onConfigurationChanged(newConfig);
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



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        int well=id;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
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
                for(int j=0;j<list.length();j++)
                {
                    JSONObject internalListObject=list.getJSONObject(j);
                    String image=internalListObject.getString(getString(R.string.jsonimage));

                    internalLists.add(new InternalList(internalListObject.getString(getString(R.string.jsonid)),internalListObject.getString(getString(R.string.jsonname)),internalListObject.getString(getString(R.string.jsoncount)),internalListObject.getString(getString(R.string.jsonimage)),internalListObject.getString(getString(R.string.jsonIsActive))));

                }

                dashBoardLists.add(new DashBoardList(dashBoardObject.getString(getString(R.string.jsontitle)),internalLists,dashBoardObject.getString(getString(R.string.jsonIsActive))));
            }

            //for (int i = 0; i <)
        }catch (JSONException jsonException) {
            new ToastActivity().makeJsonException(this);
            LogHelper logHelper = new LogHelper(jsonException);
            jsonException.printStackTrace();
        }
        return dashBoardLists;
    }

    protected void showSnack(boolean isConnected) {
        String message;
        int color;
        Snackbar snackbar;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
            snackbar = Snackbar
                    .make(findViewById(R.id.coordinatorLayout), message, Snackbar.LENGTH_LONG);
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            snackbar = Snackbar
                    .make(findViewById(R.id.coordinatorLayout), message, Snackbar.LENGTH_INDEFINITE);
        }

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }
    private void fetchImageData(JSONObject response) {
        try {
            JSONArray data = response.getJSONArray(getString(R.string.data));
            for(int i=0;i<data.length();i++)
            {
                JSONObject jsonObject=data.getJSONObject(i);
                String image=jsonObject.getString(getString(R.string.jsonimage));
                String text=jsonObject.getString(getString(R.string.jsonText));
                imageList.add(new ChapterList(image,text));
            }

        }catch (Exception e)
        {
            new ToastActivity().makeJsonException(this);
            LogHelper logHelper = new LogHelper(e);
            e.printStackTrace();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        if(isSuccessful)
        {
            switch (callFor) {
                case GET_DASHBOARD_LIST: {
                    try {
                        if ((response.get("success").toString()).equals("true")) {

                            if(response.getString(getString(R.string.jsoncode)).equals(getString(R.string.nocontentcode)))
                            {
                                new ToastActivity().makeToastMessage(response,this);
                            }
                            else {
                                dataList = fetchData(response);
                                fillNavigationDrawer(dataList, navigationView);
                            }
                        }
                    } catch (Exception e) {
                        LogHelper logHelper = new LogHelper(e);
                        e.printStackTrace();
                    }
                    break;
                }
                case GET_DASHBOARD_IMAGE_LIST:
                {
                    try {
                        if ((response.get("success").toString()).equals("true")) {

                            if(response.getString(getString(R.string.jsoncode)).equals(getString(R.string.nocontentcode)))
                            {
                                new ToastActivity().makeToastMessage(response,this);
                            }
                            else {
                                fetchImageData(response);
                                //fillNavigationDrawer(dataList, navigationView);
                            }
                        }
                    } catch (Exception e) {
                        LogHelper logHelper = new LogHelper(e);
                        e.printStackTrace();
                    }
                    break;

                }

            }


        }
    }
}




