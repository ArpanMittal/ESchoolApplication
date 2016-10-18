package com.organization.sjhg.e_school.Helpers;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.organization.sjhg.e_school.Content.PdfDisplayActivity;
import com.organization.sjhg.e_school.Content.Quest.QuestListActivity;
import com.organization.sjhg.e_school.Database.contracts.UserContract;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.organization.sjhg.e_school.Structure.ContentDetail;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.Sync.FileDownloader;
import com.organization.sjhg.e_school.Utils.ShaGenrate;
import com.organization.sjhg.e_school.Utils.ToastActivity;
import com.sun.pdfview.action.PDFAction;

import org.apache.commons.codec.binary.Hex;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Punit Chhajer on 07-09-2016.
 */
public class FileHelper {
    private Context context;
    private String file_path,root,checksum,hash;
    public byte[] protectionData;

    public FileHelper(Context context,String file_path,String checksum, String hash){
        this.context = context;
        this.file_path = file_path;
        this.checksum = checksum;
        this.hash = hash;
        File root = Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/E-SchoolContent/");
        this.root = String.valueOf(file.getAbsoluteFile());

        if (isExist()){
            Cursor cursor = context.getContentResolver().query(
                    UserContract.ContentEntry.CONTENT_URI,
                    null,
                    UserContract.ContentEntry.COLUMN_HASH+" =? ",
                    new String[]{hash},
                    null,
                    null
            );
            int count = cursor.getCount();
            if(count >0){
                cursor.moveToFirst();
                protectionData = cursor.getBlob(cursor.getColumnIndex(UserContract.ContentEntry.CoLUMN_PROTECTION));
            }
        }
    }

    public Boolean isExist(){
        String path = root+"/"+file_path;
        File file = new File(path);
        return file.exists();
    }

    public String getInternalPath() {
        String path = root+"/"+file_path;
        File file = new File(path);
        return String.valueOf(file.getAbsoluteFile());
    }

    public Uri getRemotePath() {
        String path = ServerAddress.getServerAddress(context)+"/"+file_path;
        return Uri.parse(path);
    }

    public String getParent(){
        File file = new File(root+"/"+file_path);
        return file.getParent();
    }

    public String getFileName(){
        String path = root+"/"+file_path;
        return path.substring(path.lastIndexOf("/")+1);
    }

    public void download(){
        new DownloadFileAsync().execute(String.valueOf(getInternalPath()), String.valueOf(getRemotePath()));
    }

    public void openFile(){
        Intent intent = new Intent(context, PdfDisplayActivity.class);
        intent.putExtra("localFilePath",getInternalPath());
        intent.putExtra("protectionData",protectionData);
        context.startActivity(intent);
    }

    public boolean delete(){
        File file = new File(root+"/"+file_path);
        return file.delete();
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((QuestListActivity) context).showDialog();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {
                new FileDownloader(aurl[0], aurl[1]).Download();
                if(!new ShaGenrate().verifyCheckSum(aurl[0],checksum))
                {
                    NetworkErrorException networkErrorException=new NetworkErrorException();
                    throw networkErrorException;
                }
                protectionData=ProtectionHelper.InitialProtect(aurl[0]);
                ContentValues contentValues = new ContentValues();
                contentValues.put(UserContract.ContentEntry.COLUMN_HASH,hash);
                contentValues.put(UserContract.ContentEntry.CoLUMN_PATH,aurl[0]);
                contentValues.put(UserContract.ContentEntry.CoLUMN_PROTECTION,protectionData);
                Cursor cursor = context.getContentResolver().query(
                        UserContract.ContentEntry.CONTENT_URI,
                        null,
                        UserContract.ContentEntry.COLUMN_HASH+" =? ",
                        new String[]{hash},
                        null,
                        null
                );
                int count = cursor.getCount();
                if(count >0){
                    cursor.moveToFirst();
                    context.getContentResolver().update(
                            UserContract.ContentEntry.CONTENT_URI, contentValues,
                            UserContract.ContentEntry.COLUMN_HASH+" =? ",
                            new String[]{hash}
                    );
                }else{
                    context.getContentResolver().insert(
                            UserContract.ContentEntry.CONTENT_URI, contentValues
                    );
                }
                return aurl[0];
            } catch ( Exception e) {
                e.printStackTrace();
                new LogHelper(e);
                delete();
            }
            return null;

        }
        protected void onProgressUpdate(String... progress) {
        }

        @Override
        protected void onPostExecute(String unused) {
            if (unused != null){
                openFile();
            }else{
                new ToastActivity().showMessage("Error in Downloading file",(Activity) context);
                new LogHelper(new Exception("Error in Downloading file from "+getInternalPath()));
            }
            ((QuestListActivity) context).dismissDialog();
        }


    }

}
