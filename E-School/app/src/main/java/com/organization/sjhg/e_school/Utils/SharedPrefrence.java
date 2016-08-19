package com.organization.sjhg.e_school.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by arpan on 8/16/2016.
 */
public class SharedPrefrence
{
    private static final String ACCESS_TOKEN="Access_Token";
    private static final String REFRESH_TOKEN="Refresh_Token";
    public static final String PREFS_NAME = "AOP_PREFS";
    public static final String USER_EMAIL="User_Email";
    public static final String USER_PASSWORD="User_Password";

    public void saveAccessToken(Context context,String accessToken, String refreshToken)
    {
        //save access token and refresh token
        SharedPreferences.Editor editor;
        SharedPreferences  settings =context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor=settings.edit();
        editor.putString(ACCESS_TOKEN,accessToken);
        editor.putString(REFRESH_TOKEN,refreshToken);
        editor.commit();

    }
    public void saveUserCredentials(Context context,String email,String password)
    {
        //save user credentials
        SharedPreferences.Editor editor;
        SharedPreferences  settings =context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor=settings.edit();
        editor.putString(USER_EMAIL,email);
        editor.putString(USER_PASSWORD,password);
        editor.commit();
    }
    public String getUserEmail(Context context)
    {
        //return email
        SharedPreferences  settings =context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(USER_EMAIL,null);
    }
    public String getUserPassword(Context context)
    {
        SharedPreferences  settings =context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(USER_PASSWORD,null);
    }
    public String getAccessToken(Context context)
    {
    // retreive access token
        SharedPreferences  settings =context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(ACCESS_TOKEN,null);
    }
    public String getRefreshToken(Context context)
    {
    // retereive refresh token
        SharedPreferences  settings =context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(REFRESH_TOKEN,null);
    }


}
