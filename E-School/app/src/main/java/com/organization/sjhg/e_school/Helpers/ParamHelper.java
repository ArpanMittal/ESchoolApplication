package com.organization.sjhg.e_school.Helpers;

import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sir Ravi on 9/26/2015.
 */
public class ParamHelper {

    public static List<NameValuePair> GetParams(Context context) {

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("StudentId",
                String.valueOf(StudentApplicationUserData.getInstance(context).getStudentId())));
        params.add(new BasicNameValuePair("SessionId",
                StudentApplicationUserData.getInstance(context).getClassSessionId()));
        params.add(new BasicNameValuePair("SectionId",
                String.valueOf(StudentApplicationUserData.getInstance(context).getSectionId())));

        return params;

    }
}
