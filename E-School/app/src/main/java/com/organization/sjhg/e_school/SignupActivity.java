package com.organization.sjhg.e_school;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.Utils.ProgressBarActivity;
import com.organization.sjhg.e_school.Utils.ShaGenrate;
import com.organization.sjhg.e_school.Utils.ToastActivity;
import com.organization.sjhg.e_school.deviceadmin.DeviceAdminUtil;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by arpan on 7/5/2016.
 */
//TODO: ADD moving back option Activty feature
public class SignupActivity extends AppCompatActivity implements RemoteCallHandler {
    private TextInputLayout mEmailView;
    private TextInputLayout mPasswordView;
    private TextInputLayout mConfirmPasswordView;
    private TextInputLayout mNameView;
    private View focusView;
    private View mProgressView;
    private View mSignUpFormView;
    private Button signup;
    private ProgressBarActivity progressBarActivity=new ProgressBarActivity();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupactivity);
        mEmailView=(TextInputLayout)findViewById(R.id.email);
        mPasswordView=(TextInputLayout)findViewById(R.id.password);
        mConfirmPasswordView=(TextInputLayout)findViewById(R.id.confirmpassword);
        mNameView=(TextInputLayout) findViewById(R.id.name);
        mSignUpFormView=findViewById(R.id.signup_form);
        mProgressView=findViewById(R.id.signup_progress);
        signup=(Button)findViewById(R.id.sign_up_button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

    }

    private void attemptSignUp()
    {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mConfirmPasswordView.setError(null);
        mNameView.setError(null);

        String email=mEmailView.getEditText().getText().toString();
        String password = mPasswordView.getEditText().getText().toString();
        String confirmPassword=mConfirmPasswordView.getEditText().getText().toString();
        String name=mNameView.getEditText().getText().toString();
        focusView = null;
        boolean cancel=false;
        // validate email
        cancel=validateEmptyField(mEmailView,email);
        if (!cancel&&!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        //validate password
        else if(!cancel)
        {
            cancel=validateEmptyField(mPasswordView,password);
            if(!cancel&&!isPasswordValid(password)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }
        }

        //validate confirmpassword
        if(!cancel)
        {
            cancel=validateEmptyField(mConfirmPasswordView,confirmPassword);
            if(!cancel&&!isCnfPasswordValid(confirmPassword,password))
            {
                mConfirmPasswordView.setError(getString(R.string.error_invalid_password));
            }
        }
        //validate name
        if(!cancel)
        {
            cancel=validateEmptyField(mNameView,name);
        }

        if(cancel)
        {
            focusView.requestFocus();
        }
        else
        {
            progressBarActivity.showProgress(mSignUpFormView,mProgressView,true,getApplicationContext());
            // convert password in SHA1 hash
            ShaGenrate shaGenrate=new ShaGenrate();
            password=shaGenrate.generate(password);
            new RemoteHelper(getApplicationContext()).signUp(RemoteCalls.SIGN_UP,this,email,password,name);
        }



    }
    private boolean isCnfPasswordValid(String confirmPassword,String Password)
    {
        return confirmPassword.equals(Password);
    }
    private boolean validateEmptyField(TextInputLayout view,String toMatch)
    {
        if(TextUtils.isEmpty(toMatch))
        {
            view.setError(getString(R.string.error_field_required));
            focusView=(View)view;
            return true;
        }
        return false;
    }
    private boolean isEmailValid(String email) {
        if(email.contains(".")) {
            return email.contains("@");
        }
        else
            return false;
    }
    private boolean isPasswordValid(String password)
    {
        Pattern pattern;
        Matcher matcher;
        //final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return false;
    }

    //response of remote call to server
    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        if(!isSuccessful)
        {

            progressBarActivity.showProgress(mSignUpFormView,mProgressView,false,getApplicationContext());
            showSnackBar();
        }
        else
        {
            progressBarActivity.showProgress(mSignUpFormView,mProgressView,false,getApplicationContext());
            try {
                if (response.get("sucess").toString()=="false")
                {
                    ToastActivity toastActivity=new ToastActivity();
                    toastActivity.makeToastMessage(response,this);
                }
                else {

                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                   Toast.makeText(getApplicationContext(),getString(R.string.signupconfirm),Toast.LENGTH_LONG).show();
                    finish();
                }
            }catch (Exception e)
            {
                //showProgress(false);
                new LogHelper(exception);
                exception.printStackTrace();
            }
        }

    }
    private void showSnackBar(){
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, getResources().getText(R.string.noInternetError2), Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        attemptSignUp();
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.parseColor("#ff5722"));

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        snackbar.show();
    }
}
