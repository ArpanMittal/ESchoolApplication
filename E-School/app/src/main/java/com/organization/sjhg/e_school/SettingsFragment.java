package com.organization.sjhg.e_school;


import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.widget.Toast;

import com.organization.sjhg.e_school.Content.Content_Type;
import com.organization.sjhg.e_school.Database.DatabaseOperations;
import com.organization.sjhg.e_school.Helpers.StorageManager;
import com.organization.sjhg.e_school.Helpers.StudentApplicationUserData;
import com.organization.sjhg.e_school.Remote.ExceptionHandler;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.organization.sjhg.e_school.Structure.ContentDetailBase;
import com.organization.sjhg.e_school.Structure.TestDetail;
import com.organization.sjhg.e_school.TakeNotes.ChatHeadService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceActivity implements RemoteCallHandler {

    static MultiSelectListPreference manageSubjectsPref;


    public static void UpdateSubscriptionList(JSONObject response) {
        if(manageSubjectsPref == null){
            return;
        }
        if (response != null && response.has("SubscriptionSubjects")) {
            manageSubjectsPref.setSummary(R.string.manage_subjects_summary);
            manageSubjectsPref.setEnabled(true);
            JSONArray subscriptionSubjects = null;
            try {

                subscriptionSubjects = response.getJSONArray("SubscriptionSubjects");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            List<String> entries = new ArrayList<>();
            List<String> entryValues = new ArrayList<>();

            for (int i = 0; i < subscriptionSubjects.length(); i++) {
                try {
                    entries.add(subscriptionSubjects.getJSONObject(i).getString("Subject"));
                    entryValues.add(subscriptionSubjects.getJSONObject(i).getString("SubjectId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            manageSubjectsPref.setEntries(entries.toArray(new CharSequence[entries.size()]));
            manageSubjectsPref.setEntryValues(entryValues.toArray(new CharSequence[entryValues.size()]));
        } else {
            manageSubjectsPref.setSummary(R.string.offline_manage_subjects_message);
            manageSubjectsPref.setEnabled(false);
        }
    }

    public Date convertStringToDate(String val) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date eventdate = null;
        try {
            eventdate = sdf.parse(val);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return eventdate;


    }

    //To create a new Calendar Account.
    public void createCalendar() {
        ContentValues values = new ContentValues();
        values.put(
                CalendarContract.Calendars.ACCOUNT_NAME,
                "HiTechGurukul");
        values.put(
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(
                CalendarContract.Calendars.NAME,
                "HiTechGurukul");
        values.put(
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                "HiTechGurukul");
        values.put(
                CalendarContract.Calendars.CALENDAR_COLOR,
                0x00ff00);
        values.put(
                CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.CAL_ACCESS_OWNER);

        values.put(
                CalendarContract.Calendars.CALENDAR_TIME_ZONE,
                TimeZone.getDefault().getID());
        values.put(
                CalendarContract.Calendars.SYNC_EVENTS,
                1);
        Uri.Builder builder =
                CalendarContract.Calendars.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(
                CalendarContract.Calendars.ACCOUNT_NAME,
                "com.organization.sjhg.e_school");
        builder.appendQueryParameter(
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(
                CalendarContract.CALLER_IS_SYNCADAPTER,
                "true");
        Uri uri =
                this.getContentResolver().insert(builder.build(), values);

    }


    //Get the List of Available Calendars.
    public long getCalendars() {
        long ID = 1;
        String[] projection =
                new String[]{
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.NAME,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.ACCOUNT_TYPE};
        //To point to the list of available calendars.
        Cursor calCursor =
                this.getContentResolver().
                        query(CalendarContract.Calendars.CONTENT_URI,
                                projection,
                                CalendarContract.Calendars.VISIBLE + " = 1",
                                null,
                                CalendarContract.Calendars._ID + " ASC");
        int var = 0;
        //To retrieve the ID of the Created Account.
        if (calCursor.moveToFirst()) {
            do {
                long id = calCursor.getLong(0);
                String displayName = calCursor.getString(1);
                if (displayName != null && displayName.equalsIgnoreCase("HiTechGurukul")) {
                    var = 1;
                    ID = id;
                }
            } while (calCursor.moveToNext());
            if (var == 0) {
                createCalendar();
                ID = getCalendars();
            }

        }
        return ID;
    }

    //To add the events to the calendar.

    public void AddEvents(JSONObject response) {

        if (response != null && response.has("CalendarDetails")) {

            long calendarId = getCalendars();

            JSONArray EventDetails = null;
            try {
                EventDetails = response.getJSONArray("CalendarDetails");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Creation of List and retrieving the Server values into these lists.
            ContentValues eventsList[] = new ContentValues[EventDetails.length()];
            ContentResolver cr = this.getApplicationContext().getContentResolver();
            Uri CALENDAR_URI = Uri.parse("content://com.android.calendar/events");
            this.getContentResolver().delete(CALENDAR_URI, "calendar_id=" + calendarId, null);

            for (int i = 0; i < EventDetails.length(); i++) {

                ContentValues event = new ContentValues();
                try {

                    event.put(CalendarContract.Events.CALENDAR_ID, calendarId);
                    event.put(CalendarContract.Events.TITLE, EventDetails.getJSONObject(i).getString("Title"));
                    event.put(CalendarContract.Events.DTSTART, convertStringToDate(EventDetails.getJSONObject(i).getString("StartTime")).getTime());
                    event.put(CalendarContract.Events.DTEND, convertStringToDate(EventDetails.getJSONObject(i).getString("EndTime")).getTime());
                    event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

                    eventsList[i] = event;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

           cr.bulkInsert(CalendarContract.Events.CONTENT_URI, eventsList);


            Toast.makeText(this, R.string.CylenderEvent, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Intent.ACTION_VIEW);
           // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage("com.android.calendar");
            this.startActivity(intent);

        } else
            Toast.makeText(this, R.string.basic_error, Toast.LENGTH_LONG).show();
    }



    @Override
    public void onCreate(final Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

       HideNavigationBar hideNavigationBar=new HideNavigationBar();
        hideNavigationBar.hideSettingNavigationBar(getWindow());

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.fragment_settings);
       // View setting=(View)findViewById()


        // Manage elective subjects (subscription module)
        manageSubjectsPref = (MultiSelectListPreference) findPreference(getString(R.string.pref_key_manage_subjects));

        manageSubjectsPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                new RemoteHelper(getApplicationContext()).updateStudentSubscriptionSubjects(SettingsFragment.this,
                        RemoteCalls.UPDATE_STUDENT_SUBSCRIBED_SUBJECTS, newValue);
                return true;
            }
        });

        //To Sync the events to the default calendar within the device.
        Preference SyncCalendar = findPreference(getString(R.string.pref_key_Sync_Calendar));
        SyncCalendar.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                new RemoteHelper(getApplicationContext()).getEventDetails(SettingsFragment.this, RemoteCalls.GET_EVENT_DETAILS);
                return true;
            }
        });


        // Clear all application data
        Preference clearDataPref = findPreference(getString(R.string.pref_key_clear_data));
        if (!isTestStatusClear()) {
            clearDataPref.setEnabled(false);
            clearDataPref.setSummary("Upload all tests before clearing data.");
        }
        clearDataPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                // Show alert dialog for clear application data
                new AlertDialog.Builder(new ContextThemeWrapper( SettingsFragment.this, android.R.style.Theme_Holo_Light_Dialog)).setTitle("Clear Application Data!")
                        .setMessage(R.string.clear_application_data_message)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Clear external storage directory
                                File dir = StorageManager.getLocalStorageDirectory();
                                File[] allFiles = dir.listFiles();
                                if (allFiles != null) {
                                    for (File file : allFiles)
                                        file.delete();
                                }

                                // Clear all tables and shared preferences
                                Runtime runtime = Runtime.getRuntime();
                                try {
                                    runtime.exec("pm clear com.organization.sjhg.e_school");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .create().show();

                return true;
            }
        });

      final Preference chatHead=(SwitchPreference)findPreference(getString(R.string.chathead));
       chatHead.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
       {
           @Override
           public boolean onPreferenceChange(Preference preference, Object o) {
              boolean isenabled=((Boolean)o).booleanValue();
               if(isenabled)
               {
                   stopService(new Intent(getApplicationContext(), ChatHeadService.class));
                   StudentApplicationUserData.putNoteHeadServiceStatus(getApplicationContext(),false);
                   return  true;

               }
               else
               {
                   startService(new Intent(getApplicationContext(),ChatHeadService.class));
                   StudentApplicationUserData.putNoteHeadServiceStatus(getApplicationContext(),true);
                   return true;

               }

           }
       });


        // Clear session logs
       /* Preference clearLogs = findPreference(getString(R.string.pref_key_clear_logs));
        clearLogs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                // Get log file
                File root = Environment.getExternalStorageDirectory();

                File sessionLog = new File(root.getAbsolutePath() + "/E-SchoolContent/sessionLogs.txt");
                // Delete log file if exists
                if (sessionLog.exists()) {
                    sessionLog.delete();
                    Toast.makeText(getApplicationContext(), R.string.log_cleared_message, Toast.LENGTH_LONG)
                            .show();
                } else
                    Toast.makeText(getApplicationContext(), R.string.no_logs_exist_message, Toast.LENGTH_LONG)
                            .show();

                return true;
            }
        });*/

        // Send feedback about application
        final Preference feedbackPref = findPreference(getString(R.string.pref_key_feedback));
        feedbackPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //Send mail
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                // set the type to 'email'
                emailIntent .setType("vnd.android.cursor.dir/email");
                String to[] = {"developer@hitechgurukul.com"};
                emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
                // the mail subject
                emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Feedback "+getApplicationContext().getResources().getString(R.string.Version));
                startActivity(Intent.createChooser(emailIntent , ""));
                return true;
            }
        });

        // Logout from application
        final Preference logoutPref = findPreference(getString(R.string.pref_key_logout));
        logoutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // Change logout preference value to true
                StudentApplicationUserData.SaveLogoutStatus(getApplicationContext(), true);
                stopService(new Intent(getApplicationContext(), ChatHeadService.class));
                // Move to Login Activity
                Intent moveToSplash = new Intent(getApplicationContext(), SplashActivity.class);
                moveToSplash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(moveToSplash);
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        HideNavigationBar hideNavigationBar=new HideNavigationBar();
        hideNavigationBar.hideSettingNavigationBar(getWindow());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return false;
    }
    // Function to check if there are any tests that need to be uploaded
    private Boolean isTestStatusClear() {
        try {
            List<ContentDetailBase> allLocalContent = DatabaseOperations
                    .getLocalContentList(this);
            for (ContentDetailBase contentObject : allLocalContent) {
                // Check if content is of type test
                if (contentObject.contentTypeId == Content_Type.TEST.getValue()) {
                    TestDetail test = TestDetail.getTesDetailObjectFromLocal(contentObject.localFilePath);
                    // Check if test is started or submitted
                    if (test.status == TestDetail.TestStatus.TEST_STARTED
                            || test.status == TestDetail.TestStatus.TEST_SUBMITTED) {
                        return false;
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        // If remote call is unsuccessful
        if (!isSuccessful) {
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        switch (callFor) {
            case UPDATE_STUDENT_SUBSCRIBED_SUBJECTS:
                Toast.makeText(this, R.string.success_message, Toast.LENGTH_SHORT)
                        .show();
                break;

            case GET_EVENT_DETAILS:
                // createCalendar(response);
                AddEvents(response);
                break;
        }

    }

}
