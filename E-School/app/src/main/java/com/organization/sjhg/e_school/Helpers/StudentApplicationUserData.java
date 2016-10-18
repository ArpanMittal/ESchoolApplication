package com.organization.sjhg.e_school.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Structure.UserData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Prateek Tulsyan on 20-03-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class StudentApplicationUserData extends UserData {
    public static final String PREFS_NAME = "StudentData";
    public static final String PREFS_KEY_SectionId = "SectionId";
    public static final String PREFS_KEY_StudentName = "StudentName";
    public static final String PREFS_KEY_StudentID = "StudentId";
    public static final String PREF_KEY_LAST_ACCESSED_FILE = "LastAccessed";

    public static final String PREFS_NAME_USER_CREDENTIAL = "UserCredential";
    public static final String PREF_USERNAME = "Username";
    public static final String PREF_PASSWORD = "Password";
    private static final String PREF_LAUGHGURU_PASSWORD = "LaughguruPassword";
    private static UserData userData;
    private static final String GCM_REGISTRATION_ID="GcmRegId";
    private static final String NOTEHEADSERVICE_STATUS="noteheadservicestatus";

    private StudentApplicationUserData(SharedPreferences settings, Context context) {
        super(context);
        this.studentName = settings.getString(PREFS_KEY_StudentName, null);
        this.studentId = settings.getInt(PREFS_KEY_StudentID, -1);
        this.sectionId = settings.getInt(PREFS_KEY_SectionId, -1);
        this.isConnectedToClass = false;
        this.classSessionId = null;
        this.isHandRaised = false;

    }


    public static UserData getInstance(Context ctx) {
        if (userData != null)
            return userData;

        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (settings.contains(PREFS_KEY_StudentID)) {
            userData = new StudentApplicationUserData(settings, ctx);
            return userData;
        }
        return null;
    }

    public static void save(Context context, JSONObject json_data) throws JSONException {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Editor editor = settings.edit();

        editor.putInt(PREFS_KEY_SectionId, json_data.getInt("SectionId"));
        editor.putString(PREFS_KEY_StudentName, json_data.getString("Name"));
        editor.putInt(PREFS_KEY_StudentID, json_data.getInt("StudentId"));

        editor.apply();
        userData = null;
    }

/*    public static void save(Context context, String sessionId) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(PREFS_KEY_SessionID, String.valueOf(sessionId));

        editor.apply();
    }*/

    public static void SaveUserCredentials(Context context, String username, String password) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME_USER_CREDENTIAL, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(PREF_USERNAME, username).apply();
        editor.putString(PREF_PASSWORD, password).apply();
    }


    public static void SaveLogoutStatus(Context context, Boolean status) {
        // Change preference logout value to false
        SharedPreferences.Editor editPreferences = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editPreferences.putBoolean(context.getString(R.string.pref_key_logout), status).apply();
    }

    public static String getPrefUsername(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME_USER_CREDENTIAL, Context.MODE_PRIVATE);
        return settings.getString(PREF_USERNAME, null);
    }

    public static String getPrefPassword(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME_USER_CREDENTIAL, Context.MODE_PRIVATE);
        return settings.getString(PREF_PASSWORD, null);
    }

    public static Boolean getLogoutStatus(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(context.getString(R.string.pref_key_logout), true);
    }

    public static Boolean getSessionLogsStatus(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean returnValue = sharedPreferences.getBoolean(context.getString(R.string.pref_key_log_status), false);
        return returnValue;
    }

    //getGcmRegistration id for user
    public static String getGcmRegistrationId(Context context){
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME_USER_CREDENTIAL, Context.MODE_PRIVATE);
        //return settings.getString(PREF_USERNAME, null);
        return settings.getString(GCM_REGISTRATION_ID,null);
    }
    public static boolean getNoteHeadServiceStatus(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME_USER_CREDENTIAL, Context.MODE_PRIVATE);
        return settings.getBoolean(NOTEHEADSERVICE_STATUS,true);
    }

    public static String getLaughguruUsername(Context ctx) {
        //return "icse6@laughguru.com";
        return getInstance(ctx).getStudentId() + "@hitechgurukul.com";
    }
    //put value of regid in shared prefrences not working
    public static void putGcmRegistrationId(Context context,String Regid)
    {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME_USER_CREDENTIAL, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(GCM_REGISTRATION_ID,Regid).apply();
       // editor.commit();

    }

    public static void putNoteHeadServiceStatus(Context context,boolean status)
    {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME_USER_CREDENTIAL, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putBoolean(NOTEHEADSERVICE_STATUS,status).apply();
    }
    public static void setLaughguruPassword(Context context, String password) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME_USER_CREDENTIAL, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(PREF_LAUGHGURU_PASSWORD, password).apply();
    }

    public static String getLaughguruPassword(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME_USER_CREDENTIAL, Context.MODE_PRIVATE);
        return settings.getString(PREF_LAUGHGURU_PASSWORD, "asdas");
    }

    public static void SaveLastAccessedFile(Context context, String localFilePath) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(PREF_KEY_LAST_ACCESSED_FILE, localFilePath).apply();
    }

    public static String getLastAccessedFile(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(PREF_KEY_LAST_ACCESSED_FILE, null);
    }
}
