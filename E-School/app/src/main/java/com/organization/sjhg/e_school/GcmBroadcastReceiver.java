package com.organization.sjhg.e_school;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.TakeEvents.EventDetailTable;
import com.organization.sjhg.e_school.TakeEvents.EventListingActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Arpan on 2/27/2016.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver implements RemoteCallHandler{
    Context context;
    Intent intent;
   //static EventListingActivity eventlist;
    @Override
    public void onReceive(Context context, Intent intent) {


        this.context = context;
        this.intent = intent;
        Bundle extras = intent.getExtras();
       // String d=(String)extras.get("message");
        //callNotificationService(context,intent);
       String check=(String)extras.get("From");
        //if(check==null)
        //{
          //  Toast toast=
        //}

        //check from where it receive notification
        if(check!=null) {
            if (check.equals("broadcast")) {
                callNotificationService(context, intent);
            } else {
                String DiaryId = (String) extras.get("message");

                //extras.get("From")="Diary Updated";
                new RemoteHelper(context).updateDiary(this, RemoteCalls.CHECK_REMOTE_CALL, DiaryId);

            }
        }
    }

    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {

        if(isSuccessful)
        {
            switch (callFor) {
                case CHECK_REMOTE_CALL:
                    EventDetailTable table=new EventDetailTable(context);
                    table.open();
                    try {
                        table.insertNote(response);

                    }catch (JSONException j){
                        Log.e("error","Exception Occured Here");
                    }
                    callNotificationService(context, intent);
                    //EventListingActivity eventListingActivity=new EventListingActivity();
                    EventListingActivity eventlist=EventListingActivity.inst();
                    // if app is not open then no object of eventlist there
                    if(eventlist!=null)
                    eventlist.populateListView();
            }
        }
    }

    public void callNotificationService(Context context,Intent intent)
    {
        ComponentName comp=new ComponentName(context.getPackageName(), GCMNotificationIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
