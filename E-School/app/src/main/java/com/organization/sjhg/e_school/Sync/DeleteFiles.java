package com.organization.sjhg.e_school.Sync;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.organization.sjhg.e_school.Content.Content_Type;
import com.organization.sjhg.e_school.Database.DatabaseOperations;
import com.organization.sjhg.e_school.MainActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Structure.ContentDetailBase;
import com.organization.sjhg.e_school.Structure.LaughguruContentDetailBase;

import org.json.JSONException;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Punit Chhajer on 28-05-2016.
 */
public class DeleteFiles extends AsyncTask<String, String, Integer> {
    boolean isCalledFromMainActivity;
    Boolean syncFailed = false;
    public List<LaughguruContentDetailBase> lgList;
    List<ContentDetailBase> allRemoteContentList;
    private Context context;
    private static boolean isDeleting = false;


    public DeleteFiles(Context ctx, boolean isCalledFromMainActivity, List<ContentDetailBase> contentToSync) {
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
        if (this.isDeleting){
            Toast.makeText(context.getApplicationContext(),
                    context.getResources().getString(R.string.deleting_already),
                    Toast.LENGTH_LONG).show();
            this.cancel(true);
            return;
        }
    }

    @Override
    protected Integer doInBackground(String... params) {
        return syncContent();
    }

    private Integer syncContent() {
        this.isDeleting = true;
        Integer downloaded = 0;
        Integer total = 0;
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

            // Match the list to figure out which local files are in remote and which remote files are in local
            for (int localIterator = 0; localIterator < allLocalContent.length; localIterator++) {
                for (int remoteIterator = 0; remoteIterator < allRemoteContent.length; remoteIterator++) {
                    if (allLocalContent[localIterator].contentFileId
                            == allRemoteContent[remoteIterator].contentFileId
                            && allLocalContent[localIterator].getInternalContentType().contentType
                            == allRemoteContent[remoteIterator].getInternalContentType().contentType) {
                        isFilePresentInRemote[localIterator] = true;
                        isFilePresentInLocal[remoteIterator] = true;
                        total++;
                    }
                }
            }

            // The files which are in local list but not in remote needs to be deleted
            for (int localIterator = 0; localIterator < allLocalContent.length; localIterator++) {

                if (isFilePresentInRemote[localIterator]&&allLocalContent[localIterator].contentTypeId!=Content_Type.LAUGHGURU.getValue()) {
                    NotificationBar.deletingNotification(context,downloaded,total);
                    DeleteFile(allLocalContent[localIterator]);
                    downloaded++;
                }
                else if(isFilePresentInRemote[localIterator]&&allLocalContent[localIterator].contentTypeId==Content_Type.LAUGHGURU.getValue()) {
                    NotificationBar.deletingNotification(context,downloaded,total);
                    DeleteFile(allLocalContent[localIterator]);
                    downloaded++;
                }
            }

        } catch (JSONException e) {
            HandleException(e, R.string.error_message_json);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return downloaded;
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
        else if(contentToDelete.contentTypeId== Content_Type.LAUGHGURU.getValue())
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

    protected void onPostExecute(Integer message) {
        this.isDeleting = false;
        NotificationBar.deleteCompleteNotification(context, message);
        Intent goToMain = new Intent(context, MainActivity.class);
        context.startActivity(goToMain);
    }
}