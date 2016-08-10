package com.organization.sjhg.e_school;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;

import com.organization.sjhg.e_school.deviceadmin.DeviceAdminUtil;

/**
 * Created by arpan on 7/5/2016.
 */
//TODO: ADD moving back option Activty feature
public class SignupActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private EditText phno;
    private EditText name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //DeviceAdminUtil.checkAndPrompt(this);
        setContentView(R.layout.signupactivity);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        confirmPassword=(EditText)findViewById(R.id.confirmpassword);
        name=(EditText)findViewById(R.id.name);
        phno=(EditText)findViewById(R.id.phno);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return false;
        }

}
