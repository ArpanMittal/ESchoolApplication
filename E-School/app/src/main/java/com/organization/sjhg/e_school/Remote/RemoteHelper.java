package com.organization.sjhg.e_school.Remote;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.organization.sjhg.e_school.Content.Quest.QuestListActivity;
import com.organization.sjhg.e_school.ExaminationParent;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.Structure.LaughguruContentDetailBase;
import com.organization.sjhg.e_school.Sync.FileManager;
import com.organization.sjhg.e_school.Helpers.StudentApplicationUserData;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Structure.ContentDetailBase;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.Utils.SharedPrefrence;


import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Prateek Tulsyan on 10-03-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class RemoteHelper {
    Context context;
    String SIGNUP_PAGE;
    String GET_ACESS_TOKEN;
    String GET_USER_DETAIL;
    String SEND_QUESTION_RESPONSE;
    String GET_DASHBOARD_DETAILS;
    String GET_ITEM_DETAILS;
    String GET_FREE_QUEST_DETAILS;
    String GET_QUEST_DETAILS;
    String GET_QUESTION;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    String LOGIN_PAGE;
    String FETCH_CONTENT_PAGE;
    String INSERT_MAC;
    String ACTIVE_SESSIONS;
    String UPDATE_STUDENT_STATUS;
    String UPDATE_SESSION_STATUS;
    String TEST_AVAILABLE_PAGE;
    String FETCH_LAUGHGURU_PAGE;
    String ADAPTIVE_TEST_PAGE;
    String FETCH_NOTES_PAGE;
    String GET_SINGLE_TEST;
    String GET_SINGLEADAPTIVE_TEST;
    String GET_STUDENT_STATUS;
    String SUBSCRIPTION_SUBJECTS;
    String EVENT_DETAILS;
    String GET_GOOGLE_AUTH_DETAILS;

    public RemoteHelper(Context context) {
        this.context = context;
        SEND_QUESTION_RESPONSE="api/v1/saveQuestionResponse";
        SIGNUP_PAGE=this.context.getResources().getString(R.string.get_sign_up_page);
        GET_ACESS_TOKEN=this.context.getResources().getString(R.string.getaccesstoken);
        GET_USER_DETAIL=this.context.getResources().getString(R.string.getuserdetail);
        GET_GOOGLE_AUTH_DETAILS=context.getResources().getString(R.string.getGoogleAccountDetails);
        GET_DASHBOARD_DETAILS="api/v1/getDashBoardDetails";
        GET_ITEM_DETAILS="api/v1/getDetails";
        GET_FREE_QUEST_DETAILS = "api/v1/freetopics/chapter";
        GET_QUEST_DETAILS = "api/v1/topics/chapter";
        GET_QUESTION="api/v1/getQuestion";
        //////////////////////////////////////////////////////////////////////////////////////
        GET_SINGLEADAPTIVE_TEST=this.context.getResources().getString(R.string.get_single_adaptive_test);
        LOGIN_PAGE = this.context.getResources().getString(R.string.login_page);
        FETCH_CONTENT_PAGE = this.context.getResources().getString(R.string.fetch_content_page);
        INSERT_MAC = this.context.getResources().getString(R.string.insert_mac_address);
        ACTIVE_SESSIONS = this.context.getResources().getString(R.string.active_sessions);
        UPDATE_STUDENT_STATUS = this.context.getResources().getString(R.string.update_student_status);
        UPDATE_SESSION_STATUS = this.context.getResources().getString(R.string.update_session_status);
        TEST_AVAILABLE_PAGE = context.getResources().getString(R.string.test_available_page);
        FETCH_LAUGHGURU_PAGE=this.context.getResources().getString(R.string.fetch_laughguru_page);
        FETCH_NOTES_PAGE = this.context.getResources().getString(R.string.fetch_notes_page);
        GET_SINGLE_TEST = this.context.getResources().getString(R.string.get_single_test);
        GET_STUDENT_STATUS = this.context.getResources().getString(R.string.get_student_status);
        SUBSCRIPTION_SUBJECTS = this.context.getResources().getString(R.string.subscription_subjects);
        EVENT_DETAILS = this.context.getResources().getString(R.string.event_details);
        ADAPTIVE_TEST_PAGE=this.context.getResources().getString(R.string.adaptive_test_page);
    }

    public void signUp(RemoteCalls remoteCalls,RemoteCallHandler remoteCallHandler,String email,String password,String name)
    {
        // for inserting signup data in database using api
        final Map<String, String> params = new HashMap<String, String>();

        String URL = ServerAddress.getServerAddress(context) + "/" + SIGNUP_PAGE;
        //String URL=ServerAddress.getLocalServerAddress(context)+"/"+SIGNUP_PAGE;
        params.put("client_id",GlobalConstants.CLIENT_ID);
        params.put("client_secret",GlobalConstants.CLINET_SECRET);
        params.put("email",email);
        params.put("password",password);
        params.put("name",name);
        params.put("role_id",GlobalConstants.STUDENT_ROLE_ID);
        new JSONParserAsync(URL, params, null, remoteCallHandler, remoteCalls);
    }

    // To verify login status
    public void verifyLogin(RemoteCallHandler caller,RemoteCalls functionCalled,String email,String password)
    {
        String verifyLoginurl=ServerAddress.getServerAddress(context)+GET_ACESS_TOKEN;
        Map<String, String> params = new HashMap<String, String>();
        params.put("client_id",GlobalConstants.CLIENT_ID);
        params.put("client_secret",GlobalConstants.CLINET_SECRET);
        params.put("grant_type",GlobalConstants.PASSWORD_GRANTTYPE);
        params.put("username", email);
        params.put("password", password);
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type","application/x-www-form-urlencoded");
        new JSONParserAsync(verifyLoginurl,params,header,caller,functionCalled);

    }
    //get user details
    public void getUserDetails(RemoteCallHandler caller,RemoteCalls functionCalled,String access_token)
    {
        String url=ServerAddress.getServerAddress(context)+GET_USER_DETAIL;
        Map<String, String> params = new HashMap<String, String>();
        params.put("client_id",GlobalConstants.CLIENT_ID);
        params.put("client_secret",GlobalConstants.CLINET_SECRET);
        params.put("access_token",access_token);
        Map<String, String> header = new HashMap<String, String>();
       // header.put("Content-Type","application/x-www-form-urlencoded");
        new JSONParserAsync(url,params,header,caller,functionCalled);
    }


    public void getQuestion(RemoteCallHandler caller, RemoteCalls functionCalled,String tag,String key,String access_token)
    {
        String url=ServerAddress.getServerAddress(context)+GET_QUESTION+"/"+tag+"/"+key;
        Map<String, String> params = new HashMap<String, String>();
        params.put("client_id",GlobalConstants.CLIENT_ID);
        params.put("client_secret",GlobalConstants.CLINET_SECRET);
        params.put("access_token",access_token);
        Map<String, String> header = new HashMap<String, String>();
       // header.put("Content-Type","application/x-www-form-urlencoded");
        new JSONParserAsync(url,params,header,caller,functionCalled);
    }

    //get refresh token
    public void getAccessToken(RemoteCallHandler caller,RemoteCalls functionCalled,String refresh_token)
    {
        String url=ServerAddress.getServerAddress(context)+GET_ACESS_TOKEN;
        Map<String, String> params = new HashMap<String, String>();
        params.put("client_id",GlobalConstants.CLIENT_ID);
        params.put("grant_type",GlobalConstants.REFRESH_TOKEN_GRANTTYPE);
        params.put("client_secret",GlobalConstants.CLINET_SECRET);
        params.put("refresh_token",refresh_token);
        Map<String, String> header = new HashMap<String, String>();
//        header.put("Content-Type","application/x-www-form-urlencoded");
        new JSONParserAsync(url,params,header,caller,functionCalled);
    }



    //get google auth details
    public void getGoogleAuthDetails(RemoteCallHandler caller,RemoteCalls functionCalled,String code)
    {
        String url=ServerAddress.getServerAddress(context)+GET_GOOGLE_AUTH_DETAILS;
        Map<String, String> params = new HashMap<String, String>();
        params.put("client_id",GlobalConstants.CLIENT_ID);
        params.put("client_secret",GlobalConstants.CLINET_SECRET);
        params.put("code",code);
        params.put("role_id",GlobalConstants.STUDENT_ROLE_ID);
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type","application/x-www-form-urlencoded");
        new JSONParserAsync(url,params,header,caller,functionCalled);
    }

    //get dashboard details

    public void getDashBoardDetails(RemoteCallHandler caller,RemoteCalls functionCalled)
    {
        String url=ServerAddress.getServerAddress(context)+GET_DASHBOARD_DETAILS;
        Map<String, String> params = new HashMap<String, String>();
        params.put("client_id",GlobalConstants.CLIENT_ID);
        params.put("client_secret",GlobalConstants.CLINET_SECRET);
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type","application/x-www-form-urlencoded");
        new JSONParserAsync(url,params,header,caller,functionCalled);
    }

    //get item details
    public void getItemDetails(RemoteCallHandler caller,RemoteCalls functionCalled,String title,String id)
    {
        String url=ServerAddress.getServerAddress(context)+GET_ITEM_DETAILS+"/"+title+"/"+id;
        Map<String, String> params = new HashMap<String, String>();
        params.put("client_id",GlobalConstants.CLIENT_ID);
        params.put("client_secret",GlobalConstants.CLINET_SECRET);
       // params.put("title",title);
       // params.put("Id",id);
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type","application/x-www-form-urlencoded");
        new JSONParserAsync(url,params,header,caller,functionCalled);
    }


    public void getFreeQuestDetails(QuestListActivity questListActivity, RemoteCalls getItemDetails, String id) {

        String url=ServerAddress.getServerAddress(context)+GET_FREE_QUEST_DETAILS+"/"+id;
        Map<String, String> params = new HashMap<String, String>();
        params.put("client_id",GlobalConstants.CLIENT_ID);
        params.put("client_secret",GlobalConstants.CLINET_SECRET);

        Map<String, String> header = new HashMap<String, String>();
        //header.put("Content-Type","application/x-www-form-urlencoded");

        new JSONParserAsync(url,params,header,questListActivity,getItemDetails);
    }
    public void getQuestDetails(QuestListActivity questListActivity, RemoteCalls getItemDetails, String id) {

        String url=ServerAddress.getServerAddress(context)+GET_QUEST_DETAILS+"/"+id;
        String access_taken = new SharedPrefrence().getAccessToken(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("client_id",GlobalConstants.CLIENT_ID);
        params.put("client_secret",GlobalConstants.CLINET_SECRET);
        params.put("access_token",access_taken);
        Map<String, String> header = new HashMap<String, String>();
        //header.put("Content-Type","application/x-www-form-urlencoded");

        new JSONParserAsync(url,params,header,questListActivity,getItemDetails);
    }
    public void getUserAttemptDetails(RemoteCallHandler caller, RemoteCalls functionCalled, String access_token) {
        String url=ServerAddress.getServerAddress(context)+GET_USER_DETAIL;
        Map<String, String> params = new HashMap<String, String>();
        params.put("client_id",GlobalConstants.CLIENT_ID);
        params.put("client_secret",GlobalConstants.CLINET_SECRET);
        params.put("access_token",access_token);
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type","application/x-www-form-urlencoded");
        new JSONParserAsync(url,params,header,caller,functionCalled);
    }

    public void sendQuestionResponse(RemoteCallHandler caller,RemoteCalls functionCalled,String tag, String id, String access_token,JSONObject jsonObject)
    {
        String url=ServerAddress.getServerAddress(context)+SEND_QUESTION_RESPONSE+'/'+tag+'/'+id;
        Map<String, String> params = new HashMap<String, String>();
        params.put("client_id",GlobalConstants.CLIENT_ID);
        params.put("client_secret",GlobalConstants.CLINET_SECRET);
        params.put("access_token",access_token);
        params.put("data",jsonObject.toString());
        Map<String, String> header = new HashMap<String, String>();
        new JSONParserAsync(url,params,header,caller,functionCalled);
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String VerifyLoginAndSetUserData(Map<String, String> params) throws Exception {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddressDevice = wInfo.getMacAddress();

        String TAG_SUCCESS = this.context.getResources().getString(R.string.TAG_LOGIN_SUCCESS);
        String TAG_MESSAGE = this.context.getResources().getString(R.string.TAG_LOGIN_FAILURE);
        String TAG_DATA = this.context.getResources().getString(R.string.TAG_LOGIN_DATA);
        String TAG_MAC = this.context.getResources().getString(R.string.TAG_DEVICE_MACID);
        String loginURL = ServerAddress.getServerAddress(context) + "/" + LOGIN_PAGE;
        //here dend login information to server
        JSONObject jsonUserDetails = null;//HttpHelper.getInstance().MakeHttpRequestWithRetries(loginURL, params);
        int success = jsonUserDetails.getInt(TAG_SUCCESS);
        String macAddressServer = jsonUserDetails.getString(TAG_MAC);


        if (success == 1) {
            if (macAddressServer.equals(macAddressDevice) || GlobalConstants.IS_DEBUG_MODE) {
                JSONObject jsonObject = jsonUserDetails.getJSONObject(TAG_DATA);
                StudentApplicationUserData.save(context, jsonObject);
                return jsonUserDetails.getString(TAG_MESSAGE);
            } else {
                throw new Exception("Hey! This device belongs to someone else. Please contact IT admin for more details.");
            }
        }
        throw new Exception("Error Logging in: " + jsonUserDetails.getString(TAG_MESSAGE));
    }





    // Get both test and non test content
    public List<ContentDetailBase> getServerContentList() throws IOException, JSONException, NetworkErrorException {
        List<ContentDetailBase> returnValue = new ArrayList<>();
        returnValue.addAll(getServerContentList(TEST_AVAILABLE_PAGE, "TestAvailable"));
        returnValue.addAll(getServerContentList(FETCH_CONTENT_PAGE, "FetchFile"));
        return returnValue;
    }

    // Get test data
    public void getServerTestList(RemoteCallHandler caller, RemoteCalls functionCalled) {

        String testDetailUrl = ServerAddress.getServerAddress(context) + "/" + TEST_AVAILABLE_PAGE;

        Map<String, String> params = new HashMap<String, String>();

        new JSONParserAsync(testDetailUrl, params, null, caller, functionCalled);;
    }
    // get live voting test
    public void getLiveVotingTestList(RemoteCallHandler caller, RemoteCalls functionCalled) {

        String testDetailUrl = ServerAddress.getLocalServerAddress(context) + "/" + TEST_AVAILABLE_PAGE;

        Map<String, String> params = new HashMap<String, String>();
        params.put("SessionId",
                StudentApplicationUserData.getInstance(context).getClassSessionId());
        params.put("SectionId",
                String.valueOf(StudentApplicationUserData.getInstance(context).getSectionId()));

        new JSONParserAsync(testDetailUrl, params, null, caller, functionCalled);;
    }
    public void uploadAdaptiveTest(RemoteCallHandler caller,RemoteCalls functionCalled,int testID,String response,int questionid)
    {
        String TEST_STORE_ANSWER_PAGE = context.getResources().getString(R.string.upload_adaptve_answer);
        String URL = ServerAddress.getServerAddress(context) + "/" + TEST_STORE_ANSWER_PAGE;
        final Map<String, String> params = new HashMap<String, String>();
        params.put("TestId", String.valueOf(testID));
        params.put("QuestionId", String.valueOf(questionid));
        params.put("Response",response);
        new JSONParserAsync(URL, params, null, caller, functionCalled);
    }

    public void updateDiary(RemoteCallHandler caller,RemoteCalls functionCalled,String diaryId){
        String checkurl="updatediary.php";
        String url=ServerAddress.getServerAddress(context) + "/"+checkurl;
        final Map<String, String>params=new HashMap<>();
        params.put("DiaryId",diaryId);
        new JSONParserAsync(url, params, null, caller, functionCalled);

    }

    public List<LaughguruContentDetailBase> getLaughguruContentList(
            int contentId)
            throws IOException, JSONException, NetworkErrorException {
        String laughguruContentDetailURL = ServerAddress.getServerAddress(context) + "laughgurudatafetchfile.php";
        List<LaughguruContentDetailBase> returnValue = new ArrayList<>();

        Map<String, String> params = new HashMap<String, String>();
        params.put("lgdata", String.valueOf(contentId));
        JSONObject jsonFetchFile = null;//HttpHelper.getInstance().MakeHttpRequestWithRetries(laughguruContentDetailURL, params);
        JSONArray arrayOfContentDetails = jsonFetchFile.getJSONArray("LgFile");

        int lengthOfArray = arrayOfContentDetails.length();
        for (int i = 0; i < lengthOfArray; i++) {
            JSONObject contentDetailAsJson = arrayOfContentDetails.getJSONObject(i);
            LaughguruContentDetailBase row = new LaughguruContentDetailBase(contentDetailAsJson.get("ContentFileId").toString(),
                    contentDetailAsJson.get("Order").toString()   ,
                    contentDetailAsJson.get("imagePath").toString(),
                    contentDetailAsJson.get("audioPath").toString(),
                    contentDetailAsJson.get("LaughguruContentTypeId").toString());


            returnValue.add(row);
        }

        return returnValue;
    }


    public void getAdaptiveContent(RemoteCallHandler caller, RemoteCalls functionCalled,int testId)
    {
        String adaptiveDetailUrl=ServerAddress.getServerAddress(context)+"/"+GET_SINGLEADAPTIVE_TEST;
        Map<String, String> params=new HashMap<>();
        params.put("TestId", String.valueOf(testId));


        new JSONParserAsync(adaptiveDetailUrl, params, null, caller, functionCalled);
    }
    // Get Content data
    public void getServerContent(RemoteCallHandler caller, RemoteCalls functionCalled) {

        String contentDetailUrl = ServerAddress.getServerAddress(context) + "/" + FETCH_CONTENT_PAGE;

        Map<String, String> params = new HashMap<String, String>();

        new JSONParserAsync(contentDetailUrl, params, null, caller, functionCalled);
    }


    private List<ContentDetailBase> getServerContentList(
            String contentFetchPage,
            String jsonArrayTag)
            throws IOException, JSONException, NetworkErrorException {

        String contentDetailURL = ServerAddress.getServerAddress(context) + "/" + contentFetchPage;
        List<ContentDetailBase> returnValue = new ArrayList<>();

        Map<String, String> fetchFile = new HashMap<>();
        JSONObject jsonFetchFile = null;//HttpHelper.getInstance().MakeHttpRequestWithRetries(contentDetailURL, fetchFile);

        if (jsonFetchFile == null)
            return returnValue;

        JSONArray arrayOfContentDetails = jsonFetchFile.getJSONArray(jsonArrayTag);

        int lengthOfArray = arrayOfContentDetails.length();
        for (int i = 0; i < lengthOfArray; i++) {
            JSONObject contentDetailAsJson = arrayOfContentDetails.getJSONObject(i);
            returnValue.add(ContentDetailBase.getInstance(contentDetailAsJson));
        }
        return returnValue;
    }

    public JSONObject getCompleteTestData(int testId) throws IOException, JSONException, NetworkErrorException {

        String testDataUrl = ServerAddress.getServerAddress(context) + "/" + GET_SINGLE_TEST;

        Map<String, String> params = new HashMap<String, String>();
        params.put("TestId", String.valueOf(testId));

        return null;//HttpHelper.getInstance().MakeHttpRequestWithRetries(testDataUrl, params);
    }
    //Get Live voting data from local server
    public JSONObject getCompleteLiveVotingData(int testId) throws IOException, JSONException, NetworkErrorException {

        String testDataUrl = ServerAddress.getLocalServerAddress(context) + "/" + GET_SINGLE_TEST;

        Map<String, String> params = new HashMap<String, String>();
        params.put("TestId", String.valueOf(testId));
        params.put("StudentId",
                String.valueOf(StudentApplicationUserData.getInstance(context).getStudentId()));
        return null;//HttpHelper.getInstance().MakeHttpRequestWithRetries(testDataUrl, params);
    }

    //send answer for live voting
    public void uploadAnswer(int testId,String ans,int questionId,RemoteCallHandler caller, RemoteCalls functionCalled) throws JSONException, IOException, NetworkErrorException {
        String TEST_STORE_ANSWER_PAGE = context.getResources().getString(R.string.test_store_answer_page);
        String URL = ServerAddress.getLocalServerAddress(context) + "/" + TEST_STORE_ANSWER_PAGE;

        Map<String, String> params = new HashMap<String, String>();

        params.put("StudentId",
                String.valueOf(StudentApplicationUserData.getInstance(context).getStudentId()));
        params.put("TestId", String.valueOf(testId));
        params.put("QuestionId", String.valueOf(questionId));
        params.put("AnswerId", ans.split("##")[1]);

        new JSONParserAsync(URL, params, null, caller, functionCalled);
    }

    // Get all active sessions
    public void getActiveSessions(RemoteCallHandler caller, RemoteCalls functionCalled) {

        String listSessionURL = ServerAddress.getLocalServerAddress(context) + "/" + ACTIVE_SESSIONS;
        Map<String, String> params = new HashMap<String, String>();
        params.put("SectionId",
                String.valueOf(StudentApplicationUserData.getInstance(context).getSectionId()));
        new JSONParserAsync(listSessionURL, params, null, caller, functionCalled);

    }


    // To get student session status.
    public void getStudentSessionStatus(RemoteCallHandler caller, RemoteCalls functionCalled) {

        String studentStatusURL = ServerAddress.getLocalServerAddress(context) + "/" + GET_STUDENT_STATUS;

        Map<String, String> params = new HashMap<String, String>();
        params.put("SessionId",
                StudentApplicationUserData.getInstance(context).getClassSessionId());
        params.put("StudentId",
                String.valueOf(StudentApplicationUserData.getInstance(context).getStudentId()));
        params.put("operation", "GetStudentSessionStatus");

        new JSONParserAsync(studentStatusURL, params, null, caller, functionCalled);
    }


    public void updateStudentSessionStatus(RemoteCallHandler caller, RemoteCalls functionCalled) {
        String listSessionURL = ServerAddress.getLocalServerAddress(context) + "/" + UPDATE_STUDENT_STATUS;

        Map<String, String> params = new HashMap<String, String>();
        params.put("SessionId",
                StudentApplicationUserData.getInstance(context).getClassSessionId());
        params.put("StudentId",
                String.valueOf(StudentApplicationUserData.getInstance(context).getStudentId()));
        params.put("operation", "UpdateStudentSessionStatus");

        new JSONParserAsync(listSessionURL, params, null, caller, functionCalled);
    }


    public void updateRaiseHandStatus(RemoteCallHandler caller, RemoteCalls functionCalled, boolean raiseHand) {
        String listSessionURL = ServerAddress.getLocalServerAddress(context) + "/" + UPDATE_STUDENT_STATUS;

        Map<String, String> params = new HashMap<String, String>();
        params.put("SessionId",
                StudentApplicationUserData.getInstance(context).getClassSessionId());
        params.put("StudentId",
                String.valueOf(StudentApplicationUserData.getInstance(context).getStudentId()));
        params.put("operation", "UpdateHandStatus");
        params.put("HandRaised", raiseHand ? "1" : "0");

        new JSONParserAsync(listSessionURL, params, null, caller, functionCalled);
    }


    public void sendStudentQuestion(RemoteCallHandler caller, RemoteCalls functionCalled, String question) {
        String questionToSend = StudentApplicationUserData.getInstance(context).getStudentName() +
                " : " + question;

        String sendQuestionUrl = ServerAddress.getLocalServerAddress(context) + "/" + UPDATE_SESSION_STATUS;

        Map<String, String> params = new HashMap<String, String>();
        params.put("SessionId",
                StudentApplicationUserData.getInstance(context).getClassSessionId());
        params.put("StudentId",
                String.valueOf(StudentApplicationUserData.getInstance(context).getStudentId()));
        params.put("operation", "insertStudentQuestion");
        params.put("questionAsked", questionToSend);

        new JSONParserAsync(sendQuestionUrl, params, null, caller, functionCalled);
    }

    public void SendBatteryStatus(RemoteCallHandler caller, RemoteCalls functionCalled, int batteryLevel) {
        String batteryStatus = String.valueOf(batteryLevel);

        String sendStatusUrl = ServerAddress.getLocalServerAddress(context) + "/" + UPDATE_STUDENT_STATUS;

        Map<String, String> params = new HashMap<String, String>();
        params.put("SessionId",
                StudentApplicationUserData.getInstance(context).getClassSessionId());
        params.put("StudentId",
                String.valueOf(StudentApplicationUserData.getInstance(context).getStudentId()));
        params.put("operation", "UpdateBatteryStatus");
        params.put("BatteryLevel", batteryStatus);

        new JSONParserAsync(sendStatusUrl, params, null, caller, functionCalled);
    }

    public void SendRunningTasks(RemoteCallHandler caller, RemoteCalls functionCalled, String taskList) {
        String sendStatusUrl = ServerAddress.getLocalServerAddress(context) + "/" + UPDATE_STUDENT_STATUS;

        Map<String, String> params = new HashMap<String, String>();
        params.put("SessionId",
                StudentApplicationUserData.getInstance(context).getClassSessionId());
        params.put("StudentId",
                String.valueOf(StudentApplicationUserData.getInstance(context).getStudentId()));
        params.put("operation", "UpdateRunningTasks");
        params.put("AppList", taskList);

        new JSONParserAsync(sendStatusUrl, params, null, caller, functionCalled);
    }



    //Get remote notes
    public void getServerNotes(RemoteCallHandler caller, RemoteCalls functionCalled) {

        String notesURL = ServerAddress.getServerAddress(context) + "/" + FETCH_NOTES_PAGE;
        Map<String, String> params = new HashMap<String, String>();
        new JSONParserAsync(notesURL, params, null, caller, functionCalled);
    }


    // Get subscription subject list
    public void getSubscriptionSubjects(RemoteCallHandler caller, RemoteCalls functionCalled) {

        String subscriptionUrl = ServerAddress.getServerAddress(context) + "/" + SUBSCRIPTION_SUBJECTS;

        Map<String, String> params = new HashMap<String, String>();
        params.put("operation", "GetSubscriptionSubjectList");
        params.put("SectionId", String.valueOf(StudentApplicationUserData.getInstance(context).getSectionId()));

        new JSONParserAsync(subscriptionUrl, params, null, caller, functionCalled);
    }


    // Update student subscription subject list
    public void updateStudentSubscriptionSubjects(RemoteCallHandler caller, RemoteCalls functionCalled, Object newValues) {

        String subscriptionUrl = ServerAddress.getServerAddress(context) + "/" + SUBSCRIPTION_SUBJECTS;

        Map<String, String> params = new HashMap<String, String>();

        params.put("operation", "UpdateStudentSubscriptionList");
        params.put("SubscribedSubjects", String.valueOf(newValues));

        new JSONParserAsync(subscriptionUrl, params, null, caller, functionCalled);
    }

    public void getEventDetails(RemoteCallHandler caller, RemoteCalls functioncalled) {
        String calendarURL = ServerAddress.getServerAddress(context) + "/" + EVENT_DETAILS;

        Map<String, String> params = new HashMap<String, String>();

        new JSONParserAsync(calendarURL, params, null, caller, functioncalled);

    }


}
