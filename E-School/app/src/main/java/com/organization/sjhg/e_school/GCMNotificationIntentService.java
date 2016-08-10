package com.organization.sjhg.e_school;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Sync.NotificationBar;
import com.organization.sjhg.e_school.TakeEvents.EventListingActivity;

/**
 * Created by Arpan on 2/27/2016.
 */
public class GCMNotificationIntentService extends IntentService {

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        String sender=(String)extras.get("From");
        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                NotificationBar.sendNotification("Send error: " + extras.toString(), this);
               // sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
              //NotificationBar.sendNotification("Message Received from Google GCM Server: " + extras.get(Bitmap.Config.MESSAGE_KEY),this);
                if(sender.equals("diary"))
                    NotificationBar.sendNotification("Diary Updated",this);
                else
                    NotificationBar.sendNotification("New Event Added",this);
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}
