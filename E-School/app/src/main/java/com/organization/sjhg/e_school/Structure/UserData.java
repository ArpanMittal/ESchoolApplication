package com.organization.sjhg.e_school.Structure;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.organization.sjhg.e_school.ClassSession.SessionLogs;
import com.organization.sjhg.e_school.Remote.ExceptionHandler;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.SplashActivity;
import com.organization.sjhg.e_school.deviceadmin.DeviceAdminUtil;

import org.json.JSONObject;

import java.util.logging.Level;

/**
 * Created by Prateek Tulsyan on 19-02-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class UserData implements RemoteCallHandler {
    protected String studentName;
    protected int studentId;
    protected int sectionId;
    protected boolean isConnectedToClass;
    protected String classSessionId;
    protected boolean isHandRaised;
    protected boolean isLockEnabled = false;
    protected boolean isBroadcastEnabled = false;
    protected int batteryStatus;
    private Handler raiseHandHandler;
    private Handler sessionStatusHandler;



    private Context context;

    protected UserData(Context context) {
        this.context = context;
    }

    public int getSavedBatteryStatus() {
        return this.batteryStatus;
    }

    public void setBatteryStatus(int batteryStatus) {
        this.batteryStatus = batteryStatus;
    }

    public String getStudentName() {
        return this.studentName;
    }

    public int getStudentId() {
        return this.studentId;
    }

    public int getSectionId() {
        return this.sectionId;
    }

    public String getClassSessionId() {
        return this.classSessionId;
    }

    public boolean isConnectedToSession() {
        return this.isConnectedToClass;
    }

    public boolean isHandRaised() {
        return this.isHandRaised;
    }

    public boolean isBroadcastEnabled() {
        return this.isBroadcastEnabled;
    }

    public void setBroadcastStatus(boolean newStatus) {
        this.isBroadcastEnabled = newStatus;
    }

    public void updateSessionStatus(boolean newStatus, String sessionId) {
        if (this.isConnectedToSession() == newStatus)
            return;

        this.classSessionId = sessionId;
        (new RemoteHelper(this.context)).updateStudentSessionStatus(this, RemoteCalls.UPDATE_STUDENT_SESSION_STATUS);
    }


    public void updateRaiseHandStatus(boolean newStatus) {
        if (this.isHandRaised() == newStatus)
            return;

        (new RemoteHelper(this.context)).updateRaiseHandStatus(this, RemoteCalls.UPDATE_RAISE_HAND_STATUS, newStatus);
    }


    public void updateLockStatus(boolean newStatus) {

        // Return if the status has not changed
        if (this.isLockEnabled == newStatus) {
            SessionLogs.LogEntry(this.context, this.getClass().getName(), Level.INFO,
                    "Tablet Lock Status: " + this.isLockEnabled);
            return;
        }

        // If status changes
        this.isLockEnabled = newStatus;

        if (this.isLockEnabled)
            DeviceAdminUtil.lockNow(this.context);
        else
            DeviceAdminUtil.unlockNow(this.context);

        SessionLogs.LogEntry(this.context, this.getClass().getName(), Level.INFO,
                "Tablet Lock Status: " + this.isLockEnabled);

    }

    public void updateBroadcastStatus(Boolean newStatus, String uri, Context context) {
        if (newStatus) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setPackage("org.videolan.vlc");
            i.setDataAndType(
                    Uri.parse(uri),
                    "video/h264"
            );
            context.startActivity(i);
        } else if(this.isBroadcastEnabled()){
            // Run this code only once the broadcast status is changed
            Intent i = new Intent(context, SplashActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setAction(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
        }
        this.setBroadcastStatus(newStatus);

        SessionLogs.LogEntry(this.context, this.getClass().getName(), Level.INFO,
                "Broadcast Status: " + newStatus);
    }


    public void disconnectSession() {
        updateSessionStatus(false, null);
        // updateRaiseHandStatus(false);
        updateLockStatus(false);
        updateBroadcastStatus(false, null, this.context);
        SessionLogs.LogEntry(this.context, this.getClass().getName(), Level.INFO,
                "Session Status: Session disconnected");
    }

    // Register the listener here.
    public void register(Handler raiseHandHandler, Handler sessionStatusHandler) {
        this.raiseHandHandler = raiseHandHandler;
        this.sessionStatusHandler = sessionStatusHandler;
    }


    public void unregister() {
        this.raiseHandHandler = null;
        this.sessionStatusHandler = null;
    }

    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {

        if (!isSuccessful) {
            SessionLogs.LogEntry(this.context, this.getClass().getName(), Level.SEVERE,
                    "Remote Call failed: " + callFor.toString() + " " + exception.getMessage());
        }

        switch (callFor) {
            case UPDATE_STUDENT_SESSION_STATUS:
                // The call to this function only when the current status is different as called status
                if (isSuccessful) {
                    this.isConnectedToClass = !this.isConnectedToSession();
                    SessionLogs.LogEntry(this.context, this.getClass().getName(), Level.INFO,
                            "Session connection status: " + this.isConnectedToClass);
                }
                if (this.sessionStatusHandler != null) {
                    Message msg = sessionStatusHandler.obtainMessage();
                    msg.what = this.isConnectedToClass ? 1 : 0;
                    sessionStatusHandler.sendMessage(msg);
                }
                return;
            case UPDATE_RAISE_HAND_STATUS:
                if (isSuccessful) {
                    this.isHandRaised = !this.isHandRaised();
                    SessionLogs.LogEntry(this.context, this.getClass().getName(), Level.INFO,
                            "Raise hand status: " + this.isHandRaised);
                }
                if (this.raiseHandHandler != null) {
                    Message msg = raiseHandHandler.obtainMessage();
                    msg.what = this.isHandRaised ? 1 : 0;
                    raiseHandHandler.sendMessage(msg);
                }
                return;
            default:
                SessionLogs.LogEntry(this.context, this.getClass().getName(), Level.SEVERE,
                        "Invalid Remote Call: This class doesn't implement the call back " +
                                "for following remote calls: " + callFor);
        }
    }

}