package com.organization.sjhg.e_school;

import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import com.google.android.gms.auth.api.Auth;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.ExceptionHandler;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.Utils.ShaGenrate;
import com.organization.sjhg.e_school.Utils.SharedPrefrence;
import com.organization.sjhg.e_school.Utils.ToastActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements RemoteCallHandler,
        GoogleApiClient.OnConnectionFailedListener
        {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
            private static final int REQUEST_READ_CONTACTS = 0;
            private static final String TAG = "SignInActivity";
            private static final int RC_SIGN_IN = 9001;
            private static final int RC_GET_TOKEN = 9002;

            private GoogleApiClient mGoogleApiClient;


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        //populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        signup=(Button)findViewById(R.id.signupbtn);
        signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
            }
        });
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        // [START configure_signin]

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        // Request only the user's ID token, which can be used to identify the
        // user securely to your backend. This will contain the user's basic
        // profile (name, profile picture URL, etc) so you should not need to
        // make an additional call to personalize your application.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        // [END configure_signin]


        // Build GoogleAPIClient with the Google Sign-In API and the above options.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
      /*  if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;



        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if(!cancel) {
            //check for a valid password
            if (TextUtils.isEmpty(password)) {
                mPasswordView.setError(getString(R.string.error_field_required));
                focusView = mPasswordView;
                cancel = true;
            }
            else if (!isPasswordValid(password)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            ShaGenrate shaGenrate=new ShaGenrate();
            password=shaGenrate.generate(password);
            //Do login process
            new RemoteHelper(getApplicationContext()).verifyLogin(this, RemoteCalls.CHECK_LOGIN_CREDENTIALS,email,password);
            //mAuthTask = new UserLoginTask(email, password);
          //  mAuthTask.execute((Void) null);
        }
    }
            /*
            check email format
             */
            private boolean isEmailValid(String email) {
            if(email.contains(".")) {
                return email.contains("@");
            }
            else
                return false;

            }
            /*
             check the password pattern
             */
        private boolean isPasswordValid(String password) {
            Pattern pattern;
            Matcher matcher;
            final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}$";
            pattern = Pattern.compile(PASSWORD_PATTERN);
            matcher = pattern.matcher(password);
            return matcher.matches();
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

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

            @Override
            protected void onStart() {
                super.onStart();
                OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
                if (opr.isDone()) {
                    // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                    // and the GoogleSignInResult will be available instantly.
                    Log.d(TAG, "Got cached sign-in");
                    GoogleSignInResult result = opr.get();
                    handleSignInResult(result);
                } else {
                    // If the user has not previously signed in on this device or the sign-in has expired,
                    // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                    // single sign-on will occur in this branch.
                    showProgress(true);
                    opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                        @Override
                        public void onResult(GoogleSignInResult googleSignInResult) {
                            showProgress(false);
                            handleSignInResult(googleSignInResult);
                        }
                    });
                }
            }
            // [START handleSignInResult]
            private void handleSignInResult(GoogleSignInResult result) {
                Log.d(TAG, "handleSignInResult:" + result.isSuccess());
                if (result.isSuccess()) {
                    // Signed in successfully, show authenticated UI.
                    //Details of user signed by google account.
                    GoogleSignInAccount acct = result.getSignInAccount();
                    String personName = acct.getDisplayName();
                    String personEmail = acct.getEmail();
                    String personId = acct.getId();
                    Uri personPhoto = acct.getPhotoUrl();
                    String idToken = acct.getIdToken();
                    new RemoteHelper(getApplicationContext()).getGoogleAuthDetails(this,RemoteCalls.GET_GOOGLE_USER_DETAILS,idToken);
                    //Intent intent =new Intent(this,Main_Activity.class);
                    //startActivity(intent);
                    //Toast.makeText(getApplicationContext(),R.string.TAG_LOGIN_SUCCESS,Toast.LENGTH_LONG).show();


                    //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
                    //updateUI(true);
                }
                else {
                    signOut();
//                    Intent intent =new Intent(this,Main_Activity.class);
//                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),R.string.TAG_LOGIN_FAILURE,Toast.LENGTH_LONG).show();

                }
            }
            // [END handleSignInResult]

            // [START signIn]
            private void signIn() {
                // Show an account picker to let the user choose a Google account from the device.
                // If the GoogleSignInOptions only asks for IDToken and/or profile and/or email then no
                // consent screen will be shown here.
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_GET_TOKEN);
            }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
                if (requestCode == RC_GET_TOKEN) {
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    handleSignInResult(result);
                }

            }
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
            SharedPrefrence sharedPrefrence=new SharedPrefrence();
            ToastActivity toastActivity=new ToastActivity();
            switch (callFor) {
                case GET_GOOGLE_USER_DETAILS:{
                    try{
                        if(response.get("sucess").equals("false"))
                        {
                            toastActivity.makeToastMessage(response,this);
                        }
                        else
                        {
                            String email=response.get("email").toString();
                            String password=response.get("password").toString();
                            new RemoteHelper(getApplicationContext()).verifyLogin(this, RemoteCalls.CHECK_LOGIN_CREDENTIALS,email,password);
                        }
                    }catch (Exception e)
                    {
                        LogHelper logHelper=new LogHelper(e);
                        e.printStackTrace();
                    }
                    break;
                }
                case CHECK_LOGIN_CREDENTIALS: {
                    String access_token ="";
                    String refresh_token="";
                    Log.d(GlobalConstants.LOG_TAG,"Autenticated sucessfully");
                   // Log.d(GlobalConstants.LOG_TAG,response.toString());
                    //access_token=response.toString();

                    try {
                        access_token=response.get("access_token").toString();
                        refresh_token=response.get("refresh_token").toString();
                    }catch (Exception e)
                    {
                        Log.d(GlobalConstants.LOG_TAG,e.getMessage());
                        LogHelper loghelper=new LogHelper(e);
                        e.printStackTrace();
                    }

                    sharedPrefrence.saveAccessToken(getApplicationContext(),access_token,refresh_token);
                    new RemoteHelper(getApplicationContext()).getUserDetails(this,RemoteCalls.GET_USER_DETAILS,access_token);
                    //new RemoteCallHandler(getApplicationContext(),RemoteCalls.GET_USER_DETAILS,)
                   break;
                }
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
                                new RemoteHelper(getApplicationContext()).getAccessToken(this,RemoteCalls.GET_ACCESS_TOKEN,sharedPrefrence.getRefreshToken(getApplicationContext()));
                            }

                        }
                        else
                        {
                            showProgress(false);
                            Toast.makeText(this, response.toString(), Toast.LENGTH_LONG);
                            Intent intent = new Intent(this, Main_Activity.class);
                            startActivity(intent);
                        }
                    }catch (Exception e)
                    {
                        LogHelper logHelper=new LogHelper(e);
                        e.printStackTrace();
                    }

                    break;
                }
                case GET_ACCESS_TOKEN:
                {
                    try{
                        if(response.get("sucess").toString().equals("false"))
                        {
                            toastActivity.makeToastMessage(response,this);
                        }
                        else
                        {
                            sharedPrefrence.saveAccessToken(getApplicationContext(),response.get("access_token").toString(),response.get("refresh_token").toString());

                            new RemoteHelper(getApplicationContext()).getUserDetails(this,RemoteCalls.GET_USER_DETAILS,response.get("access_token").toString());
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

            // [START signOut]
            private void signOut() {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // [START_EXCLUDE]
                               // updateUI(false);
                                Log.d(GlobalConstants.LOG_TAG,"Signout");
                                // [END_EXCLUDE]
                            }
                        });
            }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(GlobalConstants.LOG_TAG,"Connection failed");
    }

}
