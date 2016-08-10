package com.organization.sjhg.e_school.Structure;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.organization.sjhg.e_school.Content.Content_Type;
import com.organization.sjhg.e_school.Helpers.StorageManager;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.organization.sjhg.e_school.Sync.FileDownloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gaurav Rawat.
 * Email: gauravrawat.official@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class TestDetail extends ContentDetailBase implements RemoteCallHandler{

    public String timeLimit;
    public int testTypeId;
    public int totalTestScore;
    public Boolean isPractice;
    public Boolean isTestTimed;
    public boolean isLiveVoting;
    public TestStatus status;
    public List<Question> questions;
    public List<String> answerIDs;
    public String localImagePath;
    public boolean lastQuestion=false;

    protected TestDetail() {
    }


    protected TestDetail(JSONObject jsonObj) throws JSONException {
        super.contentFileId = jsonObj.getInt("TestId");
        super.contentTypeId = jsonObj.getInt("ContentTypeId");
        super.contentName = jsonObj.getString("TestName");
        super.assignedPublishedDate = jsonObj.getString("TestDate");
        super.contentIdentifier = jsonObj.getString("TestId");
        super.sectionId = jsonObj.getInt("SectionId");
        super.subjectId = jsonObj.getInt("SubjectId");
        super.subjectName = jsonObj.getString("Subject");
        super.teachersId = jsonObj.getInt("TeacherId");
        this.testTypeId = jsonObj.getInt("TestTypeId");
        this.totalTestScore = jsonObj.getInt("TotalScore");
        this.questions = new ArrayList<>();
        this.status = TestStatus.TEST_NOT_STARTED;
        this.isPractice = !(jsonObj.getInt("Practice") == 0);
        this.isTestTimed = !(jsonObj.getInt("Timed") == 0);
        this.timeLimit = jsonObj.getString("TimeLimit");
        this.isLiveVoting = false;
   //     this.isTestAssigned = !(jsonObj.getInt("AssignStatus") == 0);
        this.answerIDs = new ArrayList<>();
        this.localImagePath = "";

        super.localFilePath = null;

    }

    public static TestDetail getTesDetailObjectFromLocal(String localFilePath) throws IOException, ClassNotFoundException {
       String path=localFilePath;
        TestDetail returnObject = null;

        InputStream file = null;
        InputStream buffer = null;
        ObjectInput input = null;

        try {
            file = new FileInputStream(localFilePath);
            buffer = new BufferedInputStream(file);
            input = new ObjectInputStream(buffer);
            returnObject = (TestDetail) input.readObject();
        } finally {
            if (input != null) input.close();
            if (buffer != null) buffer.close();
            if (file != null) file.close();
        }
        return returnObject;
    }

    @Override
    public InternalContentType getInternalContentType() {
        {
            return InternalContentType.getContentTypeFromContentTypeId(super.contentTypeId);
        }
    }


    public void getAdaptiveQuestionDetails(JSONObject testData,Context context) throws JSONException,IOException {



            questions.clear();
            JSONArray lastQuestion=testData.getJSONArray("Lastquestion");
            //here the exception occur make it right
            JSONArray arrayOfQuestions = testData.getJSONArray("Question_Objects");

            //for last question this token contain value

          // JSONObject last_question= lastQuestion.getJSONObject(0);
       // int length=lastQuestion.length();
        for(int i=0;i<lastQuestion.length();i++)
        {
            JSONObject lastquestion=lastQuestion.getJSONObject(i);
            this.lastQuestion=lastquestion.getBoolean("Lastquestion");
        }
            //this.lastQuestion=last_question.getBoolean("Lastquestion");
            //if(lastQuestion!=null)


            //for accessing Adaptive test which is already submitted
           // JSONArray alreadySubmitted=testData.getJSONArray("Test_Submitted");
           // if(alreadySubmitted!=null)
            //    this.status=TestStatus.ADAPTIVETEST_SUBMITTED;
            getObjectArray(arrayOfQuestions, context);
            SaveTestToLocal();



    }


    public void getObjectArray(JSONArray arrayOfQuestions,Context context)throws JSONException,IOException
    {

        for (int iterator = 0; iterator < arrayOfQuestions.length(); iterator++) {
            this.questions.add(new Question(arrayOfQuestions.getJSONObject(iterator)));
            if (this.questions.get(iterator).questionType == 7) {
                String remotePathUrl = ServerAddress.getRemoteContentPath(context, questions.get(iterator).diagramPath);

                // Get the local path to store the file
                this.localImagePath = StorageManager.geLocalFilePathfromRemote(remotePathUrl);

                // Download the file
                new FileDownloader(localImagePath, remotePathUrl).Download();
            }
        }
    }



    public void getQuestionDetails(JSONObject testData,Context context)throws JSONException,IOException
    {

        if(contentTypeId!= Content_Type.ADAPTIVE_TEST.getValue())
         {
            JSONArray arrayOfQuestions = testData.getJSONArray("Question_Objects");
            getObjectArray(arrayOfQuestions, context);
            //SaveTestToLocal();
        }
    }

    @Override
    public void SaveContentToLocal(Context context) throws IOException, JSONException, NetworkErrorException {

        // Fetch complete test
       // if(super.contentTypeId==Content_Type.TEST.getValue()) {
            JSONObject testData = new RemoteHelper(context).getCompleteTestData(super.contentFileId);
            getQuestionDetails(testData, context);
            // Populate the questions

            // Populate the answer array if test is already submitted before
            if (!testData.getString("AnswerIds").equals("null")) {
                String [] ans =  testData.getString("AnswerIds").split("(?=@{2})");

                for (int i = 0; i < this.questions.size(); i++) {
                    this.answerIDs.add(ans[i+1]);
                    this.questions.get(i).selectedAnswer = this.answerIDs.get(i);
                }

                this.status = TestStatus.TEST_UPLOADED;
            }
            SaveTestToLocal();
        //}
    }

    @Override
    public void SaveVideoContentToLocal(Context context) throws IOException, JSONException, NetworkErrorException {

    }

    public void SaveTestToLocal() throws IOException {
        if (localFilePath == null)
            this.localFilePath = StorageManager.getLocalTestFilePath(this.contentFileId);
        else {
            File testFile = new File(localFilePath);
            testFile.delete();
        }
        ObjectOutput output = null;
        OutputStream buffer = null;
        OutputStream file = null;
        try {
            file = new FileOutputStream(localFilePath);
            buffer = new BufferedOutputStream(file);
            output = new ObjectOutputStream(buffer);
            output.writeObject(this);
        } finally {
            if (output != null) output.close();
            if (buffer != null) buffer.close();
            if (file != null) file.close();
        }
    }

    public void uploadVoting(String answer, int question,Context context){
        try {
            new RemoteHelper(context).uploadAnswer(this.contentFileId,answer,question,this,RemoteCalls.UPLOAD_LIVE_VOTING_ANSWER);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NetworkErrorException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        if (!isSuccessful){
            Log.v(GlobalConstants.LOG_TAG,"Answer Not Submit");
        }
        switch (callFor){
            case UPLOAD_LIVE_VOTING_ANSWER:
                return;
            default:
                return;
        }
    }

    public enum TestStatus {
        TEST_NOT_STARTED,
        TEST_STARTED,
        TEST_SUBMITTED,
        TEST_UPLOADED,
        ADAPTIVETEST_SUBMITTED
    }
}