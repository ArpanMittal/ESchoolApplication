package com.organization.sjhg.e_school.ClassSession;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.organization.sjhg.e_school.Content.Test.TestActivity;
import com.organization.sjhg.e_school.Database.DatabaseOperations;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Structure.ContentDetailBase;
import com.organization.sjhg.e_school.Structure.RemoteResponse;
import com.organization.sjhg.e_school.Structure.TestDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by punit on 01-03-2016.
 * Organization : Eurovision Hitech Gurukul
 */
public class LiveVoting extends AsyncTask<String, String, String>{
    private JSONObject response;
    private Context context;
    public static boolean isVoting = false;

    public LiveVoting(JSONObject response,Context context){
        Initialize(response,context);
    }

    public void Initialize(JSONObject response,Context context) {
        this.context = context;
        this.response = response;
    }

    @Override
    protected void onPreExecute() {
        if (isVoting){
            this.cancel(true);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        List<ContentDetailBase> allLiveVotingList = new ArrayList<>();
        allLiveVotingList.addAll(RemoteResponse.getServerContentList(response, "TestAvailable"));
        Date startTime = new Date();
        Date currentTime = new Date();
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            JSONArray a = response.getJSONArray("StartTime");
            startTime = sdf.parse(a.getString(0));
            currentTime = sdf.parse(sdf.format(currentTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (startTime == null || currentTime.getTime() < startTime.getTime()) {
            isVoting = true;
            TestDetail test = (TestDetail)allLiveVotingList.get(0);;
            try {
                JSONObject testData = new RemoteHelper(context).getCompleteLiveVotingData(test.contentFileId);
                test.getQuestionDetails(testData, context);
                test.isLiveVoting = true;
                test.SaveTestToLocal();
                DatabaseOperations.addContentToDatabase(test, context);
                long waittime = startTime.getTime()-currentTime.getTime();
                Thread.sleep(waittime);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NetworkErrorException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent openContentIntent = new Intent(
                    context, TestActivity.class);
            openContentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            openContentIntent.putExtra("localFilePath", test.localFilePath);
            openContentIntent.putExtra("protectionData", test.protectionData);
            openContentIntent.putExtra("subjectId", test.subjectId);
            openContentIntent.putExtra("subjectName", test.subjectName);
            openContentIntent.putExtra("contentIdentifier", test.contentIdentifier);
            openContentIntent.putExtra("contentName", test.contentName);
            context.startActivity(openContentIntent);
        }
        return "";
    }
}
