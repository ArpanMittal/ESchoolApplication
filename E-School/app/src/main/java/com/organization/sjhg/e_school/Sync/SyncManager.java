package com.organization.sjhg.e_school.Sync;


import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.organization.sjhg.e_school.Content.Content_Type;
import com.organization.sjhg.e_school.Database.DatabaseOperations;
import com.organization.sjhg.e_school.MainActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.ExceptionHandler;
import com.organization.sjhg.e_school.Remote.HttpHelper;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.organization.sjhg.e_school.Structure.ContentDetailBase;
import com.organization.sjhg.e_school.Structure.LaughguruContentDetailBase;
import com.organization.sjhg.e_school.Structure.NotesDetail;
import com.organization.sjhg.e_school.Structure.TestDetail;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prateek Tulsyan on 09-03-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class SyncManager extends AsyncTask<String, String, String> {
    boolean isCalledFromMainActivity;
    Boolean syncFailed = false;
    public List<LaughguruContentDetailBase> lgList;
    List<ContentDetailBase> allRemoteContentList;
    private Context context;
    private static boolean isSyncing = false;

    private static Thread t;


    private static SyncManager syncManager;


    public SyncManager(Context ctx, boolean isCalledFromMainActivity, List<ContentDetailBase> contentToSync) {
        Initialize(ctx, isCalledFromMainActivity, contentToSync);
    }

    public void Initialize(Context ctx, boolean isPassedFromMainActivity, List<ContentDetailBase> contentToSync) {
        this.context = ctx;
        this.isCalledFromMainActivity = isPassedFromMainActivity;
        this.allRemoteContentList = contentToSync;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.syncManager = this;
        Toast.makeText(context.getApplicationContext(),
                context.getResources().getString(R.string.toast_progress),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params) {
        return syncContent();
    }

    private String syncContent() {
        this.isSyncing = true;
        this.t = Thread.currentThread();
        //List<ContentDetailBase> allRemoteContentList;
        List<ContentDetailBase> allLocalContentList;

        // Get the remote content list
        try {
            //allRemoteContentList = (new RemoteHelper(context)).getServerContentList();
            // Get local content list
            allLocalContentList = DatabaseOperations.getLocalContentList(context);

            // Convert lists to arrays
            ContentDetailBase[] allLocalContent =
                    allLocalContentList.toArray(new ContentDetailBase[allLocalContentList.size()]);
            ContentDetailBase[] allRemoteContent =
                    allRemoteContentList.toArray(new ContentDetailBase[allRemoteContentList.size()]);

            // Arrays to mark the files which are common in both remote and local
            boolean[] isFilePresentInRemote = new boolean[allLocalContent.length];
            boolean[] isFilePresentInLocal = new boolean[allRemoteContent.length];
            int totalLocal=0;
            // Match the list to figure out which local files are in remote and which remote files are in local
            for (int localIterator = 0; localIterator < allLocalContent.length; localIterator++) {
                for (int remoteIterator = 0; remoteIterator < allRemoteContent.length; remoteIterator++) {
                    if (allLocalContent[localIterator].contentFileId
                            == allRemoteContent[remoteIterator].contentFileId
                            && allLocalContent[localIterator].getInternalContentType().contentType
                            == allRemoteContent[remoteIterator].getInternalContentType().contentType) {
                        isFilePresentInRemote[localIterator] = true;
                        isFilePresentInLocal[remoteIterator] = true;
                        totalLocal++;
                    }
                }
            }

            // Upload submitted Test
            for (ContentDetailBase localContent : allLocalContent) {
                if (localContent.getInternalContentType().contentType == 3) {
                    TestDetail test = TestDetail.getTesDetailObjectFromLocal(localContent.localFilePath);

                    if (test.status == TestDetail.TestStatus.TEST_SUBMITTED)
                        uploadTest(test);
                }
            }

            /*// The files which are in local list but not in remote needs to be deleted
            for (int localIterator = 0; localIterator < allLocalContent.length; localIterator++) {
                if (!isFilePresentInRemote[localIterator]&&allLocalContent[localIterator].contentTypeId!=Content_Type.LAUGHGURU.getValue())

                {  DeleteFile(allLocalContent[localIterator]);}
                else if(!isFilePresentInRemote[localIterator]&&allLocalContent[localIterator].contentTypeId==Content_Type.LAUGHGURU.getValue())

                {
                    DeleteFile(allLocalContent[localIterator]);

                }

            }*/

            //List<ContentDetailBase> afterDeleteLocalContentList = DatabaseOperations.getLocalContentList(context);

            // The files which are in remote list but not in local needs to be downloaded
            for (int remoteIterator = 0; remoteIterator < allRemoteContent.length; remoteIterator++) {
                try {
                    if (!isFilePresentInLocal[remoteIterator] && !isCancelled()) {
                        if (syncFailed)
                            NotificationBar.downloadFailedNotification(context, allRemoteContent.length - totalLocal);
                        else
                            NotificationBar.startNotification(context, allRemoteContent.length -totalLocal);
                        if (allRemoteContent[remoteIterator].contentTypeId != Content_Type.LAUGHGURU.getValue()) {
                            DownloadFile(allRemoteContent[remoteIterator]);
                        } else {
                            List<LaughguruContentDetailBase> arraylist = new ArrayList<>();
                            arraylist =  new RemoteHelper(this.context).getLaughguruContentList(allRemoteContent[remoteIterator].contentFileId);
                            int l = arraylist.size();
                            LaughguruContentDetailBase[] laughguruContent =
                                    arraylist.toArray(new LaughguruContentDetailBase[l]);
                            //for(int i=0;i<l;++i) {
                            int a = laughguruContent.length;
                            DownloadLaughguruFile(laughguruContent);
                            //  }
                            DatabaseOperations.addContentToDatabase(allRemoteContent[remoteIterator], context);
                        }
                    }

//                    if (!isFilePresentInLocal[remoteIterator]) {
//                        DownloadFile(allRemoteContent[remoteIterator]);
//                        this.syncFailed = false;
//                    }

                } catch (IOException | NetworkErrorException e) {
                    HandleDownloadException(e, R.string.error_message_internet);
                } catch (JSONException e) {
                    HandleDownloadException(e, R.string.error_message_json);
                } catch (SQLException e) {
                    HandleDownloadException(e, R.string.error_message_sql);
                }
            }

            // Upload notes
            uploadNotes();
        } catch (IOException | NetworkErrorException e) {
            HandleException(e, R.string.error_message_internet);
        } catch (JSONException e) {
            HandleException(e, R.string.error_message_json);
        } catch (SQLException | ClassNotFoundException e) {
            HandleException(e, R.string.error_message_sql);
        }
        return null;
    }

    public static boolean checkanddestroy(){
        if (isSyncing){
            try{
                t.suspend();
            }catch (Exception e){
                e.printStackTrace();
            }

            return true;
        }else{
            return false;
        }
    }

    private String HandleException(Exception e, Integer errorStringId) {
        e.printStackTrace();
        return context.getResources().getString(errorStringId);
    }

    private void HandleDownloadException(Exception e, Integer errorStringId) {
        this.syncFailed = true;
        HandleException(e, errorStringId);
    }

    private void DeleteFile(ContentDetailBase contentToDelete) throws SQLException, JSONException {
        if(contentToDelete.contentTypeId!=Content_Type.LAUGHGURU.getValue()) {
            DatabaseOperations.deleteContent(contentToDelete.contentFileId, context);
            File file = new File(contentToDelete.localFilePath);
            file.delete();
        }
        else if(contentToDelete.contentTypeId==Content_Type.LAUGHGURU.getValue())
        {


            //Laughguru contents are not present in local folder. So, they have no filepath
            if (contentToDelete.contentTypeId==Content_Type.LAUGHGURU.getValue())
            {
                lgList = DatabaseOperations.getPath(context, Integer.toString(contentToDelete.contentFileId));
                for (int j = 0; j < lgList.size(); ++j) {



                    File file = new File("/storage/sdcard0/E-SchoolContent/"+lgList.get(j).imagePath);
                    file.delete();
                    File file1 = new File("/storage/sdcard0/E-SchoolContent/"+lgList.get(j).audioPath);
                    file1.delete();

                }
            }
            DatabaseOperations.deleteLauguguruContent(contentToDelete.contentFileId, context);
            DatabaseOperations.deleteContent(contentToDelete.contentFileId, context);
        }
    }

    protected void onPostExecute(String message) {
        this.isSyncing = false;
        if (message == null) {
            NotificationBar.updateNotification(context, this.syncFailed);
            if (isCalledFromMainActivity) {
                Intent goToMain = new Intent(context, MainActivity.class);
                context.startActivity(goToMain);
            }

        } else {
            ExceptionHandler.thrownExceptions(context, context.getResources().getString(R.string.sync_failure), message);
        }
    }

    public static boolean checkandcancle(){
        if (isSyncing){
            syncManager.cancel(true);
            isSyncing = false;
            return true;
        }else{
            return false;
        }
    }

    public static boolean isSyncing(){
        return isSyncing;
    }


    private void DownloadFile(ContentDetailBase contentToDownload) throws JSONException, IOException, NetworkErrorException, SQLException {
        //NotificationBar.startNotification(context);
        if(contentToDownload.contentTypeId!=Content_Type.VIDEO.getValue()) {
            contentToDownload.SaveContentToLocal(context);
        }
        else
        {
            contentToDownload.SaveVideoContentToLocal(context);
        }
        // Add the content detail to database
        DatabaseOperations.addContentToDatabase(contentToDownload, context);
        this.syncFailed = false;
    }


    private void uploadTest(TestDetail test) throws JSONException, IOException, NetworkErrorException {

        String TEST_STORE_ANSWER_PAGE = context.getResources().getString(R.string.test_store_answer_page);
        String URL = ServerAddress.getServerAddress(context) + "/" + TEST_STORE_ANSWER_PAGE;

        int questionAnswered = 0;

        final List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("TestId", String.valueOf(test.contentFileId)));
        params.add(new BasicNameValuePair("TeacherId", String.valueOf(test.teachersId)));
        params.add(new BasicNameValuePair("TimeDuration", String.valueOf(test.timeLimit)));
        params.add(new BasicNameValuePair("Score", String.valueOf("0")));


        String AnswerIds = "";
        for (int k = 0; k < test.questions.size(); k++) {
            if (test.questions.get(k).selectedAnswer != null) {
                AnswerIds = AnswerIds + test.questions.get(k).selectedAnswer;
                questionAnswered++;
            } else
                AnswerIds = AnswerIds + "@@null";
        }

        params.add(new BasicNameValuePair("AnswerIds", String.valueOf(AnswerIds)));
        params.add(new BasicNameValuePair("SectionId", String.valueOf(test.sectionId)));
        params.add(new BasicNameValuePair("QuestionAnswerd", String.valueOf(questionAnswered)));
        params.add(new BasicNameValuePair("Status", "0"));

        HttpHelper.getInstance().MakeHttpRequestWithRetries(URL, params);
        test.status = TestDetail.TestStatus.TEST_UPLOADED;
        test.SaveTestToLocal();
    }

    /*private String syncNotes() {
        List<NotesDetail> allLocalNotes;
        List<NotesDetail> allRemoteNotes;

        //Get server notes
        try{
            JSONObject allNotes = new JSONObject();
            NotesDetail note = new NotesDetail(allNotes);
            uploadNotes(note);
        } catch (IOException | NetworkErrorException e) {
            HandleException(e, R.string.error_message_internet);
        } catch (JSONException e) {
            HandleException(e, R.string.error_message_json);
        }
        return null;
    }*/
    private void DownloadLaughguruFile(LaughguruContentDetailBase[] contentToDownload) throws JSONException, IOException, NetworkErrorException, SQLException
    {
        for(int i=0;i<contentToDownload.length;++i) {

            contentToDownload[i].SaveLaughguruContentToLocal(context,contentToDownload[i].laughguruContentTypeId);
            DatabaseOperations.addLaughguruContentToDatabase(contentToDownload[i], context);
        }
        this.syncFailed = false;
    }
    private void uploadNotes() throws JSONException, IOException, NetworkErrorException, SQLException {

        List<NotesDetail> notes = DatabaseOperations.getLocalNotesList(context);
        JSONArray notesJsonArray = new JSONArray();

        for (NotesDetail note : notes) {
            notesJsonArray.put(note.getJsonObject());
        }

        String NOTES_INSERT_PAGE = context.getResources().getString(R.string.notes_insert_page);
        String URL = ServerAddress.getServerAddress(context) + "/" + NOTES_INSERT_PAGE;

        final List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("Notes", String.valueOf(notesJsonArray.toString())));

        HttpHelper.getInstance().MakeHttpRequestWithRetries(URL, params);
    }
}