package com.organization.sjhg.e_school.Sync;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.organization.sjhg.e_school.MainActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.TakeEvents.EventListingActivity;

/**
 * Created by Prateek Tulsyan on 10-04-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class NotificationBar {
    private static NotificationManager mNotificationManager;
    private static int notificationID = 100;
    private static int numMessages = 0;
    private static int failedFiles = 0;
    private static int filenumber = -1;
    public static int progress = -1;

    public static void startNotification(final Context context, Integer pendingFiles) {
        filenumber++;
        progress=-1;
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(context.getResources().getString(R.string.notification_sync));
        //mBuilder.setContentText(context.getResources().getString(R.string.notification_download));
        mBuilder.setContentText((++numMessages - 1) + " : Files downloaded | " + failedFiles + " : Files failed | " +
                (pendingFiles - (numMessages - 1) - failedFiles) + " : Pending Files ");
        mBuilder.setTicker("Downloading!");
        mBuilder.setSmallIcon(R.drawable.sync_notification);
        //mBuilder.setNumber(++numMessages - 1);
        mBuilder.setOngoing(true);
        mBuilder.setProgress(0, 0, true);
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int filenum = filenumber;
                        while (filenum == filenumber) {
                            if (progress != -1){
                                mBuilder.setProgress(100, progress, false);
                                mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                mNotificationManager.notify(notificationID, mBuilder.build());
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Log.d("E-School", "sleep failure");
                            }
                        }
                    }
                }
        ).start();
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationID, mBuilder.build());
    }

//    public static void downloadFailedNotification(Context context) {
//        numMessages = 0;
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
//        mBuilder.setContentTitle(context.getResources().getString(R.string.sync_failed));
//        mBuilder.setContentText(context.getResources().getString(R.string.sync_failed_message));
//        mBuilder.setSmallIcon(R.drawable.sync_notification);
//        //mBuilder.setNumber(1);
//
//        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(notificationID, mBuilder.build());
//    }


    public static void downloadFailedNotification(final Context context, Integer pendingFiles) {
        filenumber++;
        progress=-1;
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(context.getResources().getString(R.string.notification_sync));
        mBuilder.setContentText((numMessages - 1) + " : Files downloaded | " + failedFiles + " : Files failed | " +
                (pendingFiles - (numMessages - 1) - failedFiles) + " : Pending Files ");
        mBuilder.setTicker("Downloading!");
        mBuilder.setSmallIcon(R.drawable.sync_notification);
        mBuilder.setOngoing(true);
        mBuilder.setProgress(0, 0, true);
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int filenum = filenumber;
                        while (filenum == filenumber) {
                            if (progress != -1){
                                mBuilder.setProgress(100, progress, false);
                                mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                mNotificationManager.notify(notificationID, mBuilder.build());
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Log.d("E-School", "sleep failure");
                            }
                        }
                    }
                }
        ).start();
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationID, mBuilder.build());

    }


    public static void updateNotification(Context context, Boolean lastFileFailed) {
        filenumber ++;
        progress=-1;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(context.getResources().getString(R.string.sync_complete));
        Intent myintent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, myintent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        //mBuilder.setContentText(context.getResources().getString(R.string.download_complete));
        if (lastFileFailed)
            mBuilder.setContentText((numMessages - 1) +" : Files downloaded | "+ (failedFiles + 1) +  " : Files failed " );
        else
            mBuilder.setContentText(numMessages +" : Files downloaded | "+ failedFiles +  " : Files failed " );
        mBuilder.setSmallIcon(R.drawable.sync_notification);
        //mBuilder.setNumber(numMessages);

        numMessages = 0;
        failedFiles = 0;

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationID, mBuilder.build());


    }

    public static void stopNotification(Context context) {
        filenumber ++;
        progress=-1;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(context.getResources().getString(R.string.sync_stop));
        Intent myintent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, myintent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setSmallIcon(R.drawable.sync_notification);

        numMessages = 0;
        failedFiles = 0;

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationID, mBuilder.build());


    }

    public static void sendNotification(String msg,Context context)
    {
        Intent myintent;
        if(msg.equals("Diary Updated"))
        {
             myintent = new Intent(context, EventListingActivity.class);
        }
        else
        {
            myintent=new Intent(context, MainActivity.class);

        }
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, myintent, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.eurovision)
                .setContentTitle("New Event Added")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(notificationID, mBuilder.build());
    }

    public static void deletingNotification(final Context context, int deleted, int total) {
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(context.getResources().getString(R.string.notification_delete));
        mBuilder.setContentText((deleted) + " : Files deleted | " + total  + " : Pending Files ");
        mBuilder.setTicker("Deleting!");
        mBuilder.setSmallIcon(R.drawable.sync_notification);
        mBuilder.setOngoing(true);
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationID, mBuilder.build());

    }

    public static void deleteCompleteNotification(Context context, int total) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(context.getResources().getString(R.string.delete_complete));
        Intent myintent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, myintent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setContentText(total+" : Files deleted " );
        mBuilder.setSmallIcon(R.drawable.sync_notification);
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationID, mBuilder.build());
    }

}
