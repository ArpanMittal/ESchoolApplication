package com.organization.sjhg.e_school.Profile;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.organization.sjhg.e_school.Fragments.Notes_Listing_Fragment;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.LoginActivity;
import com.organization.sjhg.e_school.MainParentActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.Utils.SharedPrefrence;
import com.organization.sjhg.e_school.Utils.ToastActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Punit Chhajer on 21-09-2016.
 */
public class ProfileEditActivity extends MainParentActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    private ProgressBar mLoading;
    private ImageView profilePic;
    private EditText userName, userPnumber;
    private TextView userEmail, userDob;
    private ImageButton editProPic, editDOB;
    private Spinner userCountry, userState, userCity, userSchool;
    private HashMap<String, String> data;
    private List<String> state, city, school, country;
    private static final int MAX_SIZE = 1024;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            Picasso.with(this)
                    .load(selectedImage)
                    .resize(MAX_SIZE, MAX_SIZE  )
                    .centerInside()
                    .into(profilePic);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub view_Stub = (ViewStub) findViewById(R.id.viewstub);
        view_Stub.setLayoutResource(R.layout.activity_edit_profile);
        view_Stub.inflate();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // code repeted in all activity
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        mLoading = (ProgressBar) findViewById(R.id.progress);
        final SharedPrefrence sharedPrefrence = new SharedPrefrence();
        profilePic = (ImageView) findViewById(R.id.profile_pic);
        Picasso.with(this)
                .load(sharedPrefrence.getUserPic(getApplicationContext()))
                .placeholder(R.drawable.ic_launcher)
                .into(profilePic);
        userName = (EditText) findViewById(R.id.user_name);
        userName.setText(sharedPrefrence.getUserName(getApplicationContext()));
        userEmail = (TextView) findViewById(R.id.user_email);
        userEmail.setText(sharedPrefrence.getUserEmail(getApplicationContext()));
        userDob = (TextView) findViewById(R.id.user_dob);
        userCountry = (Spinner) findViewById(R.id.user_country);
        userState = (Spinner) findViewById(R.id.user_state);
        userCity = (Spinner) findViewById(R.id.user_city);
        userPnumber = (EditText) findViewById(R.id.user_pnumber);
        userSchool = (Spinner) findViewById(R.id.user_school);
        editProPic = (ImageButton) findViewById(R.id.edit_pro_pic);
        editDOB = (ImageButton) findViewById(R.id.edit_user_dob);
        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<String, String>();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap pic = ((BitmapDrawable)profilePic.getDrawable()).getBitmap();
                pic.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();
                String profile_pic = Base64.encodeToString(bitmapdata, Base64.DEFAULT);
                params.put("profile_pic",profile_pic);
                params.put("name", String.valueOf(userName.getText()));
                params.put("date_of_birth", String.valueOf(userDob.getText()));
                params.put("country",country.get(userCountry.getSelectedItemPosition()));
                params.put("state",state.get(userState.getSelectedItemPosition()));
                params.put("city",city.get(userCity.getSelectedItemPosition()));
                params.put("school",school.get(userSchool.getSelectedItemPosition()));
                params.put("phone_number", String.valueOf(userPnumber.getText()));
                new RemoteHelper(getApplicationContext()).saveProfile(ProfileEditActivity.this, RemoteCalls.SAVE_PROFILE,params, sharedPrefrence.getAccessToken(ProfileEditActivity.this));
            }
        });


            new RemoteHelper(getApplicationContext()).getUserDetails(this, RemoteCalls.GET_USER_DETAILS, sharedPrefrence.getAccessToken(this));
            new RemoteHelper(getApplicationContext()).getProfileDetails(this, RemoteCalls.GET_PROFILE_EDIT_DATA, sharedPrefrence.getAccessToken(this));



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(data!=null ) {
            outState.putSerializable("DATA LIST", (Serializable)data);
        }
        if(country!=null ) {
            outState.putSerializable("COUNTRY LIST", (Serializable)country);
        }
        if(state!=null ) {
            outState.putSerializable("STATE LIST", (Serializable)state);
        }
        if(city!=null ) {
            outState.putSerializable("CITY LIST", (Serializable)city);
        }
        if(school!=null ) {
            outState.putSerializable("SCHOOL LIST", (Serializable)school);
        }
    }

    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        super.HandleRemoteCall(isSuccessful, callFor, response, exception);
        if (!isSuccessful) {
            mLoading.setVisibility(View.GONE);
            new LogHelper(exception);
            exception.printStackTrace();
        } else {
            SharedPrefrence sharedPrefrence = new SharedPrefrence();
            ToastActivity toastActivity = new ToastActivity();
            try {
                if (response.get("code").toString().equals(GlobalConstants.EXPIRED_TOKEN)) {

                    if (sharedPrefrence.getRefreshToken(getApplicationContext()) == null) {

                        toastActivity.makeToastMessage(response, this);
                        return;
                    } else {
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                    }

                }else if (response.get("code").toString().equals(GlobalConstants.INAVLID_TOKEN)) {
                    mLoading.setVisibility(View.GONE);
                    toastActivity.makeToastMessage(response, this);
                }
            } catch (JSONException e) {
                LogHelper logHelper = new LogHelper(e);
                e.printStackTrace();
            }
            switch (callFor) {
                case GET_USER_DETAILS: {
                    try {
                            mLoading.setVisibility(View.GONE);
                            Toast.makeText(this, response.toString(), Toast.LENGTH_LONG);
                            this.data = getHasMap(response);
                            if (this.data != null && this.country != null && this.state != null && this.city != null && this.school != null) {
                                showView();

                        }
                    } catch (Exception e) {
                        LogHelper logHelper = new LogHelper(e);
                        e.printStackTrace();
                    }

                    break;
                }
                case GET_PROFILE_EDIT_DATA: {
                    try {
                            mLoading.setVisibility(View.GONE);
                            Toast.makeText(this, response.toString(), Toast.LENGTH_LONG);
                            getList(response);
                            if (this.data != null && this.country != null && this.state != null && this.city != null && this.school != null) {
                                showView();
                            }

                    } catch (Exception e) {
                        LogHelper logHelper = new LogHelper(e);
                        e.printStackTrace();
                    }

                    break;
                }
                case SAVE_PROFILE: {
                    try {
                         if (response.get("code").toString().equals(GlobalConstants.SUCCESS)) {
                             finish();
                        }else{
                            toastActivity.makeUknownErrorMessage(this);
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

    private void showView() {
        mLoading.setVisibility(View.GONE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, country);
        userCountry.setAdapter(adapter);
        for (int i = 0; i < country.size(); i++) {
            if (country.get(i).equals(data.get("country"))) {
                userCountry.setSelection(i);
            }
        }
        userCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (country.get(position).equals("Add")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ProfileEditActivity.this);
                    final EditText edittext = new EditText(getApplicationContext());
                    alert.setTitle("Enter");

                    alert.setView(edittext);
                    alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String value = edittext.getText().toString();
                            if (!value.equals("")) {
                                country.add(value);
                                userCountry.setSelection(country.indexOf(value));
                            }
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            userCountry.setSelection(0);
                        }
                    });

                    alert.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, state);
        userState.setAdapter(adapter);
        for (int i = 0; i < state.size(); i++) {
            if (state.get(i).equals(data.get("state"))) {
                userState.setSelection(i);
            }
        }
        userState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (state.get(position).equals("Add")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ProfileEditActivity.this);
                    final EditText edittext = new EditText(getApplicationContext());
                    alert.setTitle("Enter");

                    alert.setView(edittext);
                    alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String value = edittext.getText().toString();
                            if (!value.equals("")) {
                                state.add(value);
                                userState.setSelection(state.indexOf(value));
                            }
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            userState.setSelection(0);
                        }
                    });
                    alert.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, city);
        userCity.setAdapter(adapter);
        for (int i = 0; i < city.size(); i++) {
            if (city.get(i).equals(data.get("city"))) {
                userCity.setSelection(i);
            }
        }
        userCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (city.get(position).equals("Add")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ProfileEditActivity.this);
                    final EditText edittext = new EditText(getApplicationContext());
                    alert.setTitle("Enter");

                    alert.setView(edittext);
                    alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String value = edittext.getText().toString();
                            if (!value.equals("")) {
                                city.add(value);
                                userCity.setSelection(city.indexOf(value));
                            }
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            userCity.setSelection(0);
                        }
                    });
                    alert.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, school);
        userSchool.setAdapter(adapter);
        for (int i = 0; i < school.size(); i++) {
            if (school.get(i).equals(data.get("school_name"))) {
                userSchool.setSelection(i);
            }
        }
        userSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (school.get(position).equals("Add")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ProfileEditActivity.this);
                    final EditText edittext = new EditText(getApplicationContext());
                    alert.setTitle("Enter");

                    alert.setView(edittext);
                    alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String value = edittext.getText().toString();
                            if (!value.equals("")) {
                                school.add(value);
                                userSchool.setSelection(school.indexOf(value));
                            }
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            userSchool.setSelection(0);
                        }
                    });
                    alert.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (!data.get("photo_path").equals("null")) {
            Picasso.with(this).invalidate(data.get("photo_path"));
            Picasso.with(this)
                    .load(data.get("photo_path"))
                    .placeholder(R.drawable.ic_launcher)
                    .into(profilePic);
        }
        if (!data.get("name").equals("null")) {
            userName.setText(data.get("name"));
        }
        if (!data.get("email").equals("null")) {
            userEmail.setText(data.get("email"));
        }
        if (!data.get("phone_number").equals("null")) {
            userPnumber.setText(data.get("phone_number"));
        }
        if (!data.get("date_of_birth").equals("null")) {
            userDob.setText(data.get("date_of_birth"));
        }
        editDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(ProfileEditActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        userDob.setText(parser.format(newDate.getTime()));
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        editProPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    private void getList(JSONObject response) throws JSONException {
        JSONObject data = response.getJSONObject("data");
        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        countries.add("");
        for (Locale loc : locale) {
            country = loc.getDisplayCountry();
            if (country.length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
        this.country = countries;

        JSONArray arr = data.getJSONArray("state");
        ArrayList<String> list = new ArrayList<>();
        list.add("");
        for (int i = 0; i < arr.length(); i++) {
            String temp = arr.getJSONObject(i).getString("state_name");
            if (!temp.equals("null")) {
                list.add(temp);
            }
        }
        list.add("Add");
        this.state = list;


        arr = data.getJSONArray("city");
        list = new ArrayList<>();
        list.add("");
        for (int i = 0; i < arr.length(); i++) {
            String temp = arr.getJSONObject(i).getString("city_name");
            if (!temp.equals("null")) {
                list.add(temp);
            }
        }
        list.add("Add");
        this.city = list;

        arr = data.getJSONArray("school");
        list = new ArrayList<>();
        list.add("");
        for (int i = 0; i < arr.length(); i++) {
            String temp = arr.getJSONObject(i).getString("school_name");
            if (!temp.equals("null")) {
                list.add(temp);
            }
        }
        list.add("Add");
        this.school = list;
    }

    private HashMap<String, String> getHasMap(JSONObject response) throws JSONException {
        JSONObject data = response.getJSONObject("data");
        HashMap<String, String> d = new HashMap<>();
        d.put("email", data.getString("email"));
        d.put("name", data.getString("name"));
        d.put("photo_path", data.getString("photo_path"));
        d.put("date_of_birth", data.getString("date_of_birth"));
        d.put("country", data.getString("country"));
        d.put("state", data.getString("state"));
        d.put("city", data.getString("city"));
        d.put("phone_number", data.getString("phone_number"));
        d.put("school_name", data.getString("school_name"));
        return d;
    }

}
