package com.organization.sjhg.e_school.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.organization.sjhg.e_school.Fragments.Notes_Listing_Fragment;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.ListStructure.TopicList;
import com.organization.sjhg.e_school.LoginActivity;
import com.organization.sjhg.e_school.MainParentActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.Utils.SharedPrefrence;
import com.organization.sjhg.e_school.Utils.ToastActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Punit Chhajer on 17-09-2016.
 */
public class ProfileActivity extends MainParentActivity {
    private ImageView profilePic;
    private TextView userName, userEmail, userDob, userCountry, userState, userCity, userPnumber, userSchool;
    private ProgressBar mLoading;
    private JSONObject response;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub view_Stub=(ViewStub)findViewById(R.id.viewstub);
        view_Stub.setLayoutResource(R.layout.activity_profile);
        view_Stub.inflate();

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // code repeted in all activity
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent=new Intent(getApplicationContext(), ProfileEditActivity.class);
                startActivity(intent);
            }
        });

        mLoading = (ProgressBar) findViewById(R.id.progress);
        SharedPrefrence sharedPrefrence = new SharedPrefrence();
        profilePic = (ImageView) findViewById(R.id.profile_pic);
        Picasso.with(this)
                .load(sharedPrefrence.getUserPic(getApplicationContext()))
                .placeholder(R.drawable.ic_launcher)
                .into(profilePic);
        userName = (TextView) findViewById(R.id.user_name);
        userName.setText(sharedPrefrence.getUserName(getApplicationContext()));
        userEmail = (TextView) findViewById(R.id.user_email);
        userEmail.setText(sharedPrefrence.getUserEmail(getApplicationContext()));
        userDob = (TextView) findViewById(R.id.user_dob);
        userCountry = (TextView) findViewById(R.id.user_country);
        userState = (TextView) findViewById(R.id.user_state);
        userCity = (TextView) findViewById(R.id.user_city);
        userPnumber= (TextView) findViewById(R.id.user_pnumber);
        userSchool= (TextView) findViewById(R.id.user_school);



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(response!=null ) {
            outState.putString("INTERNAL LIST", response.toString());
        }
    }

    protected void onResume() {
        super.onResume();
        new RemoteHelper(getApplicationContext()).getUserDetails(this, RemoteCalls.GET_USER_DETAILS,new SharedPrefrence().getAccessToken(this));
    }

    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        super.HandleRemoteCall(isSuccessful, callFor, response, exception);
        if(!isSuccessful)
        {
            mLoading.setVisibility(View.GONE);
            new LogHelper(exception);
            exception.printStackTrace();
        }
        else
        {
            SharedPrefrence sharedPrefrence = new SharedPrefrence();
            ToastActivity toastActivity=new ToastActivity();
            switch (callFor) {
                case GET_USER_DETAILS:
                {
                    try {
                        if (response.get("code").toString().equals(GlobalConstants.EXPIRED_TOKEN))
                        {

                            if(sharedPrefrence.getRefreshToken(getApplicationContext())==null)
                            {

                                toastActivity.makeToastMessage(response,this);
                                break;
                            }
                            else
                            {
                                Intent intent=new Intent(this,LoginActivity.class);
                                startActivity(intent);
                            }

                        }
                        else if(response.get("code").toString().equals(GlobalConstants.INAVLID_TOKEN))
                        {
                            mLoading.setVisibility(View.GONE);
                            toastActivity.makeToastMessage(response,this);
                        }
                        else
                        {
                            mLoading.setVisibility(View.GONE);
                            Toast.makeText(this, response.toString(), Toast.LENGTH_LONG);
                            this.response = response;
                            JSONObject data = response.getJSONObject("data");
                            sharedPrefrence.saveUserCredentials(getApplicationContext(),data.getString("email"),data.getString("password"),data.getString("name"),data.getString("photo_path"));
                            showView();
                        }
                    }catch (Exception e)
                    {
                        LogHelper logHelper=new LogHelper(e);
                        e.printStackTrace();
                    }

                    break;
                }
            }
        }
    }

    private void showView() throws JSONException {
        mLoading.setVisibility(View.GONE);
        JSONObject data = response.getJSONObject("data");
        String dob = data.getString("date_of_birth");
        if (!dob.equals("null")){
            userDob.setText(dob);
        }
        String country = data.getString("country");
        if (!country.equals("null")){
            userCountry.setText(country);
        }
        String state = data.getString("state");
        if (!state.equals("null")){
            userState.setText(state);
        }
        String city = data.getString("city");
        if (!city.equals("null")){
            userCity.setText(city);
        }
        String phone_number = data.getString("phone_number");
        if (!phone_number.equals("null")){
            userPnumber.setText(phone_number);
        }

        String school_name = data.getString("school_name");
        if (!school_name.equals("null")){
            userSchool.setText(school_name);
        }

        String profile_pic = data.getString("photo_path");
        if (!profile_pic.equals("null")){
            Picasso.with(this).invalidate(profile_pic);
            Picasso.with(this)
                    .load(profile_pic)
                    .placeholder(R.drawable.ic_launcher)
                    .into(profilePic);
        }

        String name = data.getString("name");
        if (!name.equals("null")){
            userName.setText(name);
        }
    }
}
