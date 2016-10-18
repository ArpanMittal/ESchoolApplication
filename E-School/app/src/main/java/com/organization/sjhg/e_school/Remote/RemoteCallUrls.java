package com.organization.sjhg.e_school.Remote;

import android.content.Context;

/**
 * Created by Sir Ravi on 9/26/2015.
 */
public class RemoteCallUrls {

    public static String ACTIVE_SESSIONS, UPDATE_STUDENT_STATUS, UPDATE_SESSION_STATUS,
            GET_STUDENT_STATUS, SUBSCRIPTION_SUBJECTS;

    public RemoteCallUrls(Context context) {
        ACTIVE_SESSIONS = ServerAddress.getServerAddress(context) + "/" + "queryAllClassSessions.php";
        UPDATE_STUDENT_STATUS = ServerAddress.getServerAddress(context) + "/" + "updateClientInfo.php";
        UPDATE_SESSION_STATUS = ServerAddress.getServerAddress(context) + "/" + "updateClassSessionInfo.php";
        GET_STUDENT_STATUS = ServerAddress.getServerAddress(context) + "/" + "queryClientInfo.php";
        SUBSCRIPTION_SUBJECTS = ServerAddress.getServerAddress(context) + "/" + "querySubscribtionSubjects.php";
    }
}
