package com.organization.sjhg.e_school.Sync;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.organization.sjhg.e_school.Content.Content_Type;
import com.organization.sjhg.e_school.Database.old.DatabaseOperations;
import com.organization.sjhg.e_school.Helpers.StudentApplicationUserData;
import com.organization.sjhg.e_school.HideNavigationBar;
import com.organization.sjhg.e_school.LoginActivity;
import com.organization.sjhg.e_school.MainActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Structure.ContentDetailBase;
import com.organization.sjhg.e_school.Structure.LaughguruContentDetailBase;
import com.organization.sjhg.e_school.Structure.RemoteResponse;
import com.organization.sjhg.e_school.Structure.TestDetail;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileManager extends Activity implements RemoteCallHandler {

    TextView displayContent;
    TableLayout checkBoxTable;
    Button sync, delete, upload;
    CheckBox selectAll;

    List<DisplayObject> displayObjectList;
    List<ContentDetailBase> allRemoteContentList;
    List<ContentDetailBase> allLocalContentList;
    List<ContentDetailBase> contentToSync;
    public List<LaughguruContentDetailBase> lgList;
    Boolean isTestListDownloaded = false;
    Boolean isContentListDownloaded = false;
    private int preSuccessful=0;

    private Dialog pDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        HideNavigationBar hideNavigationBar=new HideNavigationBar();
        hideNavigationBar.hideSettingNavigationBar(getWindow());
        setContentView(R.layout.activity_file_manager);
        //not allow syncing when connected to the session
       if( StudentApplicationUserData.getInstance(getApplicationContext()).isConnectedToSession())
       {
           AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog));
           builder1.setMessage(this.getResources().getString(R.string.error_message_sync_while_session));
           builder1.setCancelable(false);
           builder1.setPositiveButton("OK",
                   new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           dialog.cancel();
                           finish();
                       }
                   });
           AlertDialog alert11 = builder1.create();
           alert11.show();
           return;
       }


        allRemoteContentList = new ArrayList<>();
        allLocalContentList = new ArrayList<>();
        contentToSync = new ArrayList<>();

        // Get the remote content list
        new RemoteHelper(getApplicationContext()).getServerTestList(this, RemoteCalls.GET_SERVER_TEST_LIST);

        new RemoteHelper(getApplicationContext()).getServerContent(this, RemoteCalls.GET_SERVER_CONTENT_LIST);

        // Get local content list
        try {
            allLocalContentList = DatabaseOperations.getLocalContentList(getApplicationContext());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        pDialog = new Dialog(this);
        pDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(getLayoutInflater().inflate(R.layout.pdialog
                , null));
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        ImageView mImageViewFilling = (ImageView) pDialog.findViewById(R.id.pdialogimageview);
        ((AnimationDrawable) mImageViewFilling.getBackground()).start();
        pDialog.show();

        sync = (Button) findViewById(R.id.syncButton);
        delete = (Button) findViewById(R.id.deleteButton);
        upload = (Button) findViewById(R.id.uploadTestButton);

        selectAll = (CheckBox) findViewById(R.id.selectAll);

        if (!isTestStatusClear()){
            upload.setVisibility(View.VISIBLE);
        }
        if (SyncManager.isSyncing()){
            sync.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cancel_black_48dp, 0, 0, 0);
            sync.setVisibility(View.VISIBLE);
            sync.setText("Cancel download");
            sync.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SyncManager.checkandcancle();
                    NotificationBar.stopNotification(getApplicationContext());
                    Intent mainToFileManager = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainToFileManager);
                    finish();
                }
            });
        }else  {
            sync.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentToSync(allRemoteContentList);
                }
            });
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentToDelete(allRemoteContentList);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadtest();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        HideNavigationBar hideNavigationBar=new HideNavigationBar();
        hideNavigationBar.hideSettingNavigationBar(getWindow());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return false;
    }

    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        //if one of two remote call for content is not sucessfull
        preSuccessful ++;
        if ((!isSuccessful&&preSuccessful!=1) || preSuccessful==3) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog));
            builder1.setMessage("Seems like there is some issue with connectivity. Make sure you're connected to internet or try again after some time.");
            builder1.setTitle(getApplicationContext().getResources().getString(R.string.alert_error_title_wrong));
            builder1.setCancelable(false);
            builder1.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
            pDialog.dismiss();
            //finish();
        }else  if (!isSuccessful){
            preSuccessful++;
            return;
        }
        switch (callFor) {
            case GET_SERVER_TEST_LIST:

                allRemoteContentList.addAll(RemoteResponse.getServerContentList(response, "TestAvailable"));

                isTestListDownloaded = true;
                //allRemoteContentList;
                break;
            case GET_SERVER_CONTENT_LIST:
                allRemoteContentList.addAll(RemoteResponse.getServerContentList(response, "FetchFile"));
                isContentListDownloaded = true;
                break;
            default:

                break;

        }
        try {
            if (! (response==null) && response.getJSONArray("Error").length()>0){
                pDialog.dismiss();
                Toast.makeText(this, R.string.session_expire_message, Toast.LENGTH_LONG)
                        .show();
                // Move to Login Activity
                Intent moveToLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(moveToLogin);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (isTestListDownloaded && isContentListDownloaded)
            PopulateUI(allRemoteContentList, allLocalContentList);
    }


    // Populate table with remote content list
    private void PopulateUI(List<ContentDetailBase> allRemoteContentList, List<ContentDetailBase> allLocalContentList) {

        if (allRemoteContentList.isEmpty()) {
            pDialog.dismiss();
            TextView emptyState = (TextView) findViewById(R.id.emptystate);
            emptyState.setVisibility(View.VISIBLE);
        }

        Collections.sort(allRemoteContentList, new ContentDetailBase.ContentComparator());

        // Convert lists to arrays
        ContentDetailBase[] allLocalContent =
                allLocalContentList.toArray(new ContentDetailBase[allLocalContentList.size()]);
        ContentDetailBase[] allRemoteContent =
                allRemoteContentList.toArray(new ContentDetailBase[allRemoteContentList.size()]);

        //displayContent = (TextView) findViewById(R.id.contentListDisplay);
        checkBoxTable = (TableLayout) findViewById(R.id.contentCheckboxHolderTable);

        // Make a list of objects from remote content list
        displayObjectList = new ArrayList<>();
        for (int i = 0; i < allRemoteContentList.size(); i++) {
            if (i == 0)
                MakeDoubleHeaders(displayObjectList, allRemoteContentList.get(i));

            else {
                if (allRemoteContentList.get(i).subjectId != allRemoteContentList.get(i - 1).subjectId)
                    MakeDoubleHeaders(displayObjectList, allRemoteContentList.get(i));

                else if ((allRemoteContentList.get(i).contentTypeId != Content_Type.TEST.getValue() && allRemoteContentList.get(i).contentTypeId!=Content_Type.ADAPTIVE_TEST.getValue()) &&
                        allRemoteContentList.get(i).chapterId != allRemoteContentList.get(i - 1).chapterId)
                    displayObjectList.add(MakeChapterObject(allRemoteContentList.get(i)));


                else if (allRemoteContentList.get(i).contentTypeId == Content_Type.TEST.getValue() &&
                        allRemoteContentList.get(i - 1).contentTypeId != Content_Type.TEST.getValue())
                    displayObjectList.add(MakeTestHeaderObject(allRemoteContentList.get(i)));
                else if (allRemoteContentList.get(i).contentTypeId == Content_Type.ADAPTIVE_TEST.getValue() &&
                        allRemoteContentList.get(i - 1).contentTypeId != Content_Type.ADAPTIVE_TEST.getValue())
                    displayObjectList.add(MakeAdaptiveTestHeaderObject(allRemoteContentList.get(i)));
            }

            displayObjectList.add(new DisplayObject(
                    DisplayObjectType.CONTENT,
                    allRemoteContentList.get(i).contentFileId,
                    allRemoteContentList.get(i).chapterId, allRemoteContentList.get(i).subjectId,
                    allRemoteContentList.get(i).contentTypeId, allRemoteContentList.get(i).contentName, this));
        }

        // Display all the checkboxes
        for (final DisplayObject displayObject : displayObjectList) {
            TableRow row = new TableRow(this);
            row.setGravity(Gravity.START);
            switch (displayObject.objectType) {
                case SUBJECT:
                    row.setBackgroundColor(Color.BLACK);
                    displayObject.objectCheckBox.setTextColor(Color.WHITE);
                    break;
                case CHAPTER:
                case TEST:
                    row.setPadding(20, 0, 0, 0);
                    row.setBackgroundColor(Color.GRAY);
                    displayObject.objectCheckBox.setTextColor(Color.WHITE);
                    break;
                case ADAPTIVETEST:
                    row.setPadding(20, 0, 0, 0);
                    row.setBackgroundColor(Color.GRAY);
                    displayObject.objectCheckBox.setTextColor(Color.WHITE);
                    break;
                case CONTENT:
                    row.setPadding(40, 0, 0, 0);
                    displayObject.objectCheckBox.setTextColor(Color.BLACK);
                    break;
            }

            row.addView(displayObject.objectCheckBox);
            checkBoxTable.addView(row);

            displayObject.objectCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        switch (displayObject.objectType) {
                            case SUBJECT:
                                displayObject.objectCheckBox.setTypeface(Typeface.DEFAULT_BOLD);
                                Check(displayObject.objectSubjectId, DisplayObjectType.SUBJECT);
                                break;
                            case CHAPTER:
                                displayObject.objectCheckBox.setTypeface(Typeface.DEFAULT_BOLD);
                                Check(displayObject.objectChapterId, DisplayObjectType.CHAPTER);
                                break;
                            case TEST:
                                displayObject.objectCheckBox.setTypeface(Typeface.DEFAULT_BOLD);
                                Check(displayObject.objectSubjectId, DisplayObjectType.TEST);
                                break;
                            case ADAPTIVETEST:
                                displayObject.objectCheckBox.setTypeface(Typeface.DEFAULT_BOLD);
                                Check(displayObject.objectSubjectId, DisplayObjectType.ADAPTIVETEST);
                                break;
                            case CONTENT:
                                //displayObject.objectCheckBox.setTextColor(Color.GREEN);
                                break;
                        }
                        sync.setVisibility(View.VISIBLE);
                        delete.setVisibility(View.VISIBLE);
                    } else {
                        switch (displayObject.objectType) {
                            case SUBJECT:
                                displayObject.objectCheckBox.setTypeface(Typeface.DEFAULT);
                                Uncheck(displayObject.objectSubjectId, DisplayObjectType.SUBJECT);
                                break;
                            case CHAPTER:
                                displayObject.objectCheckBox.setTypeface(Typeface.DEFAULT);
                                Uncheck(displayObject.objectChapterId, DisplayObjectType.CHAPTER);
                                break;
                            case TEST:
                                displayObject.objectCheckBox.setTypeface(Typeface.DEFAULT);
                                Uncheck(displayObject.objectSubjectId, DisplayObjectType.TEST);
                                break;
                            case ADAPTIVETEST:
                                displayObject.objectCheckBox.setTypeface(Typeface.DEFAULT);
                                Uncheck(displayObject.objectSubjectId, DisplayObjectType.ADAPTIVETEST);
                                break;
                            case CONTENT:
                                //displayObject.objectCheckBox.setTextColor(Color.BLACK);
                                break;
                        }
                        boolean flag = true;
                        for (DisplayObject displayObject : displayObjectList){
                            if (displayObject.objectCheckBox.isChecked()){
                                flag = false;
                            }
                        }
                        if (flag){
                            sync.setVisibility(View.INVISIBLE);
                            delete.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }
        //check if file already deleted form server
        boolean[] isFilePresentInRemote = new boolean[allLocalContent.length];
        int filenumber =-1;
        // Check all the checkboxes with content present in local list
        for (ContentDetailBase content : allLocalContent) {
            filenumber++;
            for (DisplayObject displayObject : displayObjectList) {
                if (displayObject.objectType == DisplayObjectType.CONTENT &&
                        displayObject.objectId == content.contentFileId &&
                        displayObject.objectContentTypeId == content.contentTypeId) {
                    //displayObject.objectCheckBox.setChecked(true);
                    displayObject.objectCheckBox.setTextColor(Color.GREEN);
                    isFilePresentInRemote[filenumber] = true;
                    //sync.setEnabled(true);
                }
            }

        }

        //delete from loacal file system
        for (int localIterator = 0; localIterator < allLocalContent.length; localIterator++) {
            if (!isFilePresentInRemote[localIterator])
                try {
                    if (allLocalContent[localIterator].contentTypeId != 1 && allLocalContent[localIterator].localFilePath != null) {
                        DatabaseOperations.deleteContent(allLocalContent[localIterator].contentFileId, this);

                        File file = new File(allLocalContent[localIterator].localFilePath);
                        file.delete();
                    } else if (allLocalContent[localIterator].contentTypeId != 1 && allLocalContent[localIterator].localFilePath == null) {

                        lgList = DatabaseOperations.getPath(getApplicationContext(), Integer.toString(allLocalContent[localIterator].contentFileId));
                        for (int j = 0; j < lgList.size(); ++j) {


                            File file = new File("/storage/sdcard0/E-SchoolContent/" + lgList.get(j).imagePath);
                            file.delete();
                            File file1 = new File("/storage/sdcard0/E-SchoolContent/" + lgList.get(j).audioPath);
                            file1.delete();
                            DatabaseOperations.deleteLauguguruContent(allLocalContent[localIterator].contentFileId, this);
                        }
                        DatabaseOperations.deleteContent(allLocalContent[localIterator].contentFileId, this);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
            pDialog.dismiss();

        if (!displayObjectList.isEmpty()) {
            //sync.setVisibility(View.VISIBLE);
            selectAll.setVisibility(View.VISIBLE);
            //delete.setVisibility(View.VISIBLE);
        }

        // Select All checkbox
        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // If select all is checked then check all other checkboxes
                if (isChecked) {
                    selectAll.setTypeface(Typeface.DEFAULT_BOLD);
                    for (DisplayObject displayObject : displayObjectList)
                        displayObject.objectCheckBox.setChecked(true);

                    sync.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                }
                // If select all is unchecked then uncheck all other checkboxes
                else {
                    selectAll.setTypeface(Typeface.DEFAULT);
                    for (DisplayObject displayObject : displayObjectList)
                        displayObject.objectCheckBox.setChecked(false);
                }
            }
        });

    }


    // Make a list of selected checkboxes and initiate sync process
    private void ContentToSync(List<ContentDetailBase> allRemoteContentList) {
        for (DisplayObject displayObject : displayObjectList) {
            if (displayObject.objectType == DisplayObjectType.CONTENT
                    && displayObject.objectCheckBox.isChecked()) {
                for (ContentDetailBase content : allRemoteContentList) {
                    if (displayObject.objectId == content.contentFileId &&
                            displayObject.objectContentTypeId == content.contentTypeId)
                        contentToSync.add(content);
                }
            }
        }

        finish();

        new SyncManager(this, true, contentToSync).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    // Make a list of selected checkboxes and initiate sync process
    private void ContentToDelete(List<ContentDetailBase> allRemoteContentList) {
        for (DisplayObject displayObject : displayObjectList) {
            if (displayObject.objectType == DisplayObjectType.CONTENT
                    && displayObject.objectCheckBox.isChecked()) {
                for (ContentDetailBase content : allRemoteContentList) {
                    if (displayObject.objectId == content.contentFileId &&
                            displayObject.objectContentTypeId == content.contentTypeId)
                        contentToSync.add(content);
                }
            }
        }

        finish();

        new DeleteFiles(this, true, contentToSync).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    //upload test
    private void uploadtest() {
        finish();

        new SubmitTests(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    // Reset selection list
    private void ResetSelection(List<ContentDetailBase> allLocalContentList) {
        // Convert list to array
        ContentDetailBase[] allLocalContent =
                allLocalContentList.toArray(new ContentDetailBase[allLocalContentList.size()]);

        for (DisplayObject displayObject : displayObjectList) {
            displayObject.objectCheckBox.setChecked(false);

            for (ContentDetailBase content : allLocalContent) {
                if (displayObject.objectType == DisplayObjectType.CONTENT &&
                        displayObject.objectId == content.contentFileId
                        && displayObject.objectContentTypeId == content.contentTypeId) {
                    displayObject.objectCheckBox.setChecked(true);
                }
            }
        }
    }

    // Set checkbox checked
    private void Check(Integer reference, DisplayObjectType type) {
        switch (type) {
            case SUBJECT:
                for (DisplayObject childObject : displayObjectList) {
                    if (childObject.objectSubjectId == reference)
                        childObject.objectCheckBox.setChecked(true);
                }
                break;
            case CHAPTER:
                for (DisplayObject childObject : displayObjectList) {
                    if (childObject.objectChapterId == reference)
                        childObject.objectCheckBox.setChecked(true);
                }
                break;
            case TEST:
                for (DisplayObject childObject : displayObjectList) {
                    if (childObject.objectSubjectId == reference &&
                            childObject.objectContentTypeId == Content_Type.TEST.getValue())
                        childObject.objectCheckBox.setChecked(true);

                }
                break;
            case ADAPTIVETEST:
                for (DisplayObject childObject : displayObjectList) {
                    if (childObject.objectSubjectId == reference &&
                            childObject.objectContentTypeId == Content_Type.ADAPTIVE_TEST.getValue())
                        childObject.objectCheckBox.setChecked(true);

                }
                break;
        }
    }

    // Set checkbox unchecked
    private void Uncheck(Integer reference, DisplayObjectType type) {
        switch (type) {
            case SUBJECT:
                for (DisplayObject childObject : displayObjectList) {
                    if (childObject.objectSubjectId == reference)
                        childObject.objectCheckBox.setChecked(false);
                }
                break;
            case CHAPTER:
                for (DisplayObject childObject : displayObjectList) {
                    if (childObject.objectChapterId == reference)
                        childObject.objectCheckBox.setChecked(false);
                }
                break;
            case TEST:
                for (DisplayObject childObject : displayObjectList) {
                    if (childObject.objectSubjectId == reference &&
                            (childObject.objectContentTypeId == Content_Type.TEST.getValue()||childObject.objectContentTypeId==Content_Type.ADAPTIVE_TEST.getValue()))
                        childObject.objectCheckBox.setChecked(false);
                }
                break;
        }
    }

    private DisplayObject MakeSubjectObject(ContentDetailBase contentObject) {

        return new DisplayObject(
                DisplayObjectType.SUBJECT, contentObject.subjectId, -1,
                contentObject.subjectId, -1, contentObject.subjectName, this);
    }

    private DisplayObject MakeChapterObject(ContentDetailBase contentObject) {

        return new DisplayObject(
                DisplayObjectType.CHAPTER, contentObject.chapterId, contentObject.chapterId,
                contentObject.subjectId, -1, contentObject.chapterName, this);
    }

    private DisplayObject MakeTestHeaderObject(ContentDetailBase contentObject) {

        return new DisplayObject(
                DisplayObjectType.TEST, contentObject.contentTypeId, -1, contentObject.subjectId,
                -1, contentObject.getInternalContentType().getContentTypeName(), this);
    }
    private DisplayObject MakeAdaptiveTestHeaderObject(ContentDetailBase contentObject) {

        return new DisplayObject(
                DisplayObjectType.ADAPTIVETEST, contentObject.contentTypeId, -1, contentObject.subjectId,
                -1, contentObject.getInternalContentType().getContentTypeName(), this);
    }
    private void MakeDoubleHeaders(List<DisplayObject> displayObjectList, ContentDetailBase contentObject) {
        displayObjectList.add(MakeSubjectObject(contentObject));

        if (contentObject.contentTypeId != Content_Type.TEST.getValue()&&contentObject.contentTypeId !=Content_Type.ADAPTIVE_TEST.getValue()) {
            //Log.d("my","i am in content");
            displayObjectList.add(MakeChapterObject(contentObject));
        }
        else if(contentObject.contentTypeId ==Content_Type.TEST.getValue()) {
            //Log.d("my","i am in test");
            displayObjectList.add(MakeTestHeaderObject(contentObject));
        }
        else if(contentObject.contentTypeId==Content_Type.ADAPTIVE_TEST.getValue()) {
            //Log.d("my","here i am");
            displayObjectList.add(MakeAdaptiveTestHeaderObject(contentObject));
        }

    }

    private enum DisplayObjectType {
        SUBJECT,
        CHAPTER,
        TOPIC,
        CONTENT,
        TEST,
        ADAPTIVETEST

    }

    private class DisplayObject {

        DisplayObjectType objectType;
        int objectId, objectChapterId, objectSubjectId, objectContentTypeId;
        String objectName;
        CheckBox objectCheckBox;

        private DisplayObject(DisplayObjectType objectType, Integer objectId, Integer chapterId,
                              Integer subjectId, Integer contentTypeId, String objectName, Context context) {
            this.objectType = objectType;
            this.objectId = objectId;
            this.objectSubjectId = subjectId;
            this.objectChapterId = chapterId;
            this.objectContentTypeId = contentTypeId;
            this.objectName = objectName;

            this.objectCheckBox = new CheckBox(context);
            objectCheckBox.setVisibility(View.VISIBLE);
            objectCheckBox.setChecked(false);
            objectCheckBox.setTextSize(15);
            objectCheckBox.setText(objectName);


        }
    }
    // Function to check if there are any tests that need to be uploaded
    private Boolean isTestStatusClear() {
        try {
            List<ContentDetailBase> allLocalContent = DatabaseOperations
                    .getLocalContentList(this);
            for (ContentDetailBase contentObject : allLocalContent) {
                // Check if content is of type test
                if (contentObject.contentTypeId == Content_Type.TEST.getValue()) {
                    TestDetail test = TestDetail.getTesDetailObjectFromLocal(contentObject.localFilePath);
                    // Check if test is started or submitted
                    if (test.status == TestDetail.TestStatus.TEST_STARTED
                            || test.status == TestDetail.TestStatus.TEST_SUBMITTED) {
                        return false;
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
