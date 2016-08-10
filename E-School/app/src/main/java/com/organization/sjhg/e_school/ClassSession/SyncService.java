package com.organization.sjhg.e_school.ClassSession;

import android.accounts.NetworkErrorException;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.organization.sjhg.e_school.Helpers.StudentApplicationUserData;
import com.organization.sjhg.e_school.Remote.ExceptionHandler;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.SplashActivity;
import com.organization.sjhg.e_school.Structure.ActiveSessions;
import com.organization.sjhg.e_school.Structure.ContentDetailBase;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.Structure.RemoteResponse;
import com.organization.sjhg.e_school.Structure.TeacherSession;
import com.organization.sjhg.e_school.Structure.TestDetail;
import com.organization.sjhg.e_school.Structure.UserData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import static android.app.AlarmManager.ELAPSED_REALTIME;
import static android.os.SystemClock.elapsedRealtime;
import static com.organization.sjhg.e_school.Helpers.StudentApplicationUserData.getInstance;
import static java.util.concurrent.TimeUnit.SECONDS;

public class SyncService extends IntentService implements Runnable, RemoteCallHandler {
    public static final String SYNC = "sync";
    private static String previousProcess;

    public SyncService() {
        super(SYNC);
    }

    @Override
    public void run() {
        Log.i("RUNNABLE", getMsg());
    }

    @NonNull
    private String getMsg() {

        // This is called every 30 seconds.
        // In case app is not connected to session; skip any action
        final ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> recentTasks = activityManager.getRunningAppProcesses();
        String currentProcess = recentTasks.get(0).processName;
        if (!currentProcess.equals(previousProcess)) {
            SessionLogs.foreground(currentProcess);
        }
        previousProcess = currentProcess;
        //List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (getInstance(getApplicationContext()) == null
                || !getInstance(getApplicationContext()).isConnectedToSession()) {
            return "Not Connected to session, Skipping 30 second update";
        }


        // Update the running tasks on server
        SendRunningTasks(getApplicationContext());
        try {
        // Send the battery status to the server
        new RemoteHelper(getApplicationContext()).SendBatteryStatus(
                this, RemoteCalls.SEND_BATTERY_STATUS, StudentApplicationUserData.getInstance(this).getSavedBatteryStatus());
            Thread.sleep(500);

        // Get the student session status from the server
        new RemoteHelper(getApplicationContext()).getStudentSessionStatus(this, RemoteCalls.GET_STUDENT_SESSION_STATUS);
        Thread.sleep(500);
        // Get teacher session status from the server
        new RemoteHelper(getApplicationContext()).getActiveSessions(this, RemoteCalls.GET_ACTIVE_SESSIONS);
        Thread.sleep(500);
        //Get active live voting session
        new RemoteHelper(getApplicationContext()).getLiveVotingTestList(this, RemoteCalls.GET_SERVER_TEST_LIST);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Fired at " + (elapsedRealtime() / 1000l);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        if (SYNC.equals(intent.getAction())) {
            Log.i("ALARM", getMsg());
        }

        if (getInstance(getApplicationContext()) == null
                || getInstance(getApplicationContext()).isConnectedToSession())
        {

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            PendingIntent pi = SplashActivity.getSyncPendingIntent(intent, getApplicationContext());
            alarmManager.set(ELAPSED_REALTIME, elapsedRealtime() + SECONDS.toMillis
                    (SplashActivity.SYNC_FREQUENCY), pi);

        }
    }

    private void SendRunningTasks(Context ctx) {
        UserData studentData = getInstance(ctx);
        if (studentData == null) {
            throw new IllegalStateException("No student data found during sync");
        }

        final ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> recentTasks = activityManager.getRunningAppProcesses();
        String runningTasks = "\"";
        for (ActivityManager.RunningAppProcessInfo runningTask : recentTasks) {
            if (runningTask.importance <= ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE)
                runningTasks += runningTask.processName + " ";
        }
        runningTasks += "\"";
        new RemoteHelper(ctx).SendRunningTasks(this, RemoteCalls.SEND_RUNNING_TASKS, runningTasks);
    }

    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        if (!isSuccessful) {
            SessionLogs.LogEntry(this, this.getClass().getName(), Level.SEVERE,
                    "Remote Call failed: " + callFor.toString() + " " + exception.getMessage());
            return;
        }
        switch (callFor) {
            case GET_STUDENT_SESSION_STATUS:
                if (response != null) {
                    Boolean handRaisedStatusReceived = StudentApplicationUserData.getInstance(getApplication()).isHandRaised();
                    try {
                        handRaisedStatusReceived = response.getInt("HandRaised") != 0;
                        SessionLogs.LogEntry(this, this.getClass().getName(), Level.INFO,
                                "Raise Hand Status Received: " + handRaisedStatusReceived);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        SessionLogs.LogEntry(this, this.getClass().getName(), Level.SEVERE,
                                e.toString());
                    }

                    SessionLogs.LogEntry(this, this.getClass().getName(), Level.INFO,
                            "Calling for: Update Raise Hand Status");
                    getInstance(getApplicationContext()).
                            updateRaiseHandStatus(handRaisedStatusReceived);
                }
                break;
            case GET_ACTIVE_SESSIONS:

                TeacherSession currentSession = null;
                try {
                    currentSession = ActiveSessions.getCurrentSession(
                            ActiveSessions.getSessionList(response),
                            getApplicationContext()
                    );
                    SessionLogs.LogEntry(this, this.getClass().getName(), Level.INFO,
                            "Current Session Connected: " + currentSession);
                } catch (JSONException e) {
                    e.printStackTrace();
                    SessionLogs.LogEntry(this, this.getClass().getName(), Level.SEVERE,
                            e.toString());
                    return;
                }

                Date sessionEndTime = new Date();
                Date currentTime = new Date();

                if (currentSession != null) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        sessionEndTime = sdf.parse(currentSession.endTime);
                        currentTime = sdf.parse(sdf.format(currentTime));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (currentSession == null || currentTime.getTime() > sessionEndTime.getTime()) {
                    SessionLogs.LogEntry(this, this.getClass().getName(), Level.INFO,
                            "Calling for: Disconnect Class Session");
                    getInstance(getApplicationContext())
                            .disconnectSession();
                } else {
                    // Update tablet lock status
                    SessionLogs.LogEntry(this, this.getClass().getName(), Level.INFO,
                            "Calling for: Update Tablet Lock Status");
                    getInstance(getApplicationContext())
                            .updateLockStatus(currentSession.lockEnabled);


                    // Update Broadcast status
                    String uri = "http://" + currentSession.ipAddress + ":" + currentSession.port + "/teacher.mpg";
                    SessionLogs.LogEntry(this, this.getClass().getName(), Level.INFO,
                            "Calling for: Update Broadcast Status");
                    StudentApplicationUserData.getInstance(this).updateBroadcastStatus(currentSession.broadcastEnabled, uri, this);
                }
                break;
            case SEND_RUNNING_TASKS:
            case SEND_BATTERY_STATUS:
                SessionLogs.LogEntry(this, this.getClass().getName(), Level.INFO,
                        "Sent: " + callFor);
                return;
            case GET_SERVER_TEST_LIST:
                SessionLogs.LogEntry(this, this.getClass().getName(), Level.INFO,
                        "Calling for: " + callFor);
                new LiveVoting(response, getApplicationContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                return;
            default:
                SessionLogs.LogEntry(this, this.getClass().getName(), Level.SEVERE,
                        "Invalid Remote Call: This class doesn't implement the call back " +
                                "for following remote calls: " + callFor);
        }
    }
}
