package com.organization.sjhg.e_school.Structure;

import android.content.Context;

import com.organization.sjhg.e_school.Helpers.StudentApplicationUserData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sir Ravi on 9/26/2015.
 */
public class RemoteResponse {

    public static List<TeacherSession> getSessionList(JSONObject object) throws JSONException {
        List<TeacherSession> sessionList = new ArrayList<>();

        if (object != null && object.has("SessionList")) {
            JSONArray arrayOfSessionDetails = object.getJSONArray("SessionList");

            int lengthOfArray = arrayOfSessionDetails.length();
            for (int i = 0; i < lengthOfArray; i++) {

                JSONObject sessionDetailAsJson = arrayOfSessionDetails.getJSONObject(i);
                sessionList.add(new TeacherSession(sessionDetailAsJson));
            }
        }

        return sessionList;
    }


    public static TeacherSession getCurrentSession(List<TeacherSession> allSessions, Context context) {
        TeacherSession currentSession = null;

        if(StudentApplicationUserData.getInstance(context).isConnectedToSession()) {
            for (TeacherSession session : allSessions) {
                if (session.sessionId.equals(StudentApplicationUserData.getInstance(context).getClassSessionId())) {
                    currentSession = session;
                }
            }
        }
        return currentSession;
    }


    public static List<ContentDetailBase> getServerContentList(JSONObject object, String jsonArrayTag) {
        List<ContentDetailBase> contentList = new ArrayList<>();

        if (object != null) {
            try {
                JSONArray arrayOfContentDetails = object.getJSONArray(jsonArrayTag);

                int lengthOfArray = arrayOfContentDetails.length();
                for (int i = 0; i < lengthOfArray; i++) {
                    JSONObject contentDetailAsJson = arrayOfContentDetails.getJSONObject(i);
                    contentList.add(ContentDetailBase.getInstance(contentDetailAsJson));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return contentList;
    }
}
