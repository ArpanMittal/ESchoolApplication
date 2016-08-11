package com.organization.sjhg.e_school;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
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
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private EditText mNameView;
    private View focusView;
    private View mProgressView;
    private View mSignUpFormView;
    private Button signup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupactivity);
        mEmailView=(EditText)findViewById(R.id.email);
        mPasswordView=(EditText)findViewById(R.id.password);
        mConfirmPasswordView=(EditText)findViewById(R.id.confirmpassword);
        mNameView=(EditText)findViewById(R.id.name);
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
        String email=mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirmPassword=mConfirmPasswordView.getText().toString();
        String name=mNameView.getText().toString();
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
        else if(!cancel)
        {
            cancel=validateEmptyField(mConfirmPasswordView,confirmPassword);
            if(!cancel&&!isCnfPasswordValid(confirmPassword,password))
            {
                mConfirmPasswordView.setError(getString(R.string.error_invalid_password));
            }
        }
        //validate name
        else if(!cancel)
        {
            cancel=validateEmptyField(mNameView,name);
        }

        if(cancel)
        {
            focusView.requestFocus();
        }
        else
        {
            showProgress(true);
            // convert password in SHA1 hash
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(password.getBytes());
                byte byteData[] = md.digest();

                //convert the byte to hex format method 1
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < byteData.length; i++) {
                    sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                }
                password=sb.toString();
            }catch (NoSuchAlgorithmException e)
            {
                // loghelper class to record all the logs
                Log.d(GlobalConstants.LOG_TAG,e.getMessage());
                new LogHelper(e);
            }

            new RemoteHelper(getApplicationContext()).signUp(RemoteCalls.SIGN_UP,this,email,password,name);
        }



    }
    private boolean isCnfPasswordValid(String confirmPassword,String Password)
    {
        return confirmPassword.equals(Password);
    }
    private boolean validateEmptyField(EditText view,String toMatch)
    {
        if(TextUtils.isEmpty(toMatch))
        {
            view.setError(getString(R.string.error_invalid_password));
            focusView=view;
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
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
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
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignUpFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    //response of remote call to server
    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        if(!isSuccessful)
        {
            showProgress(false);
            new LogHelper(exception);
            exception.printStackTrace();
        }
        else
        {
            showProgress(false);
            Toast.makeText(this,response.toString(),Toast.LENGTH_LONG);
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
        }

    }
}
