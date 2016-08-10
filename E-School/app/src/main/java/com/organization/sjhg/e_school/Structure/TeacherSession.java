package com.organization.sjhg.e_school.Structure;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bharat Lodha on 6/29/2015.
 */
public class TeacherSession {
    public String sessionId;
    public int sectionId;
    public int teacherId;
    public int subjectId;
    public String subjectName;
    public String classDisplayString;
    public String teacherName;
    public String startTime;
    public String endTime;
    public boolean lockEnabled;
    public boolean broadcastEnabled;
    public String ipAddress;
    public int port;
    public boolean isActive;

    public TeacherSession (JSONObject json_data) throws JSONException {
        sessionId = json_data.getString("SessionId");
        sectionId = json_data.getInt("SectionId");
        teacherId = json_data.getInt("TeacherId");
        subjectId = json_data.getInt("SubjectId");
        classDisplayString = json_data.getString("Grade");
        subjectName = json_data.getString("Subject");
        teacherName = json_data.getString("Name");
        startTime = json_data.getString("StartTime");
        endTime = json_data.getString("EndTime");
        lockEnabled = json_data.getInt("LockEnabled") == 1;
        broadcastEnabled = json_data.getInt("BroadcastEnabled") == 1;
        ipAddress = json_data.getString("IPAddress");
        port = 8080; // Hardcoding the post both at client and server side. This is used for broadcasting teacher's screen.
        isActive = json_data.getInt("IsActive") == 1;
    }
}
