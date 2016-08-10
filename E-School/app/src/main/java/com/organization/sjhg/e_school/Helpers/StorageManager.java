package com.organization.sjhg.e_school.Helpers;

import android.os.Environment;

import com.organization.sjhg.e_school.Structure.GlobalConstants;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Bharat Lodha on 3/18/2015.
 * Organization : Eurovision Hitech Gurukul
 */
public class StorageManager {
    public static File getLocalStorageDirectory() {
        File root = Environment.getExternalStorageDirectory();

        File dir = new File(root.getAbsolutePath() + "/E-SchoolContent");

        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static String geLocalFilePathfromRemote(String remotePath) throws IOException {
        // Get remote file stream
        URL url = new URL(remotePath);
        URLConnection connection = url.openConnection();
        connection.connect();

        // Get local file stream
        File file;
        String filePartialPath = url.getFile();
        String extension=null;
        filePartialPath = new File(filePartialPath).getName();
        int pos = filePartialPath.lastIndexOf(".");
        if (pos > 0) {
            extension = filePartialPath.substring(pos,filePartialPath.length());
        }

        if(extension!=null&&extension.equals("vct"))
        {
            extension="mp4";
            String filename=filePartialPath.substring(0,pos)+extension;
             file=new File(StorageManager.getLocalStorageDirectory(),filename);
        }
        else {
             file = new File(StorageManager.getLocalStorageDirectory(), filePartialPath);
        }
        return file.getAbsolutePath();
    }

    public static String getLocalTestFilePath(int testId) {
        String extension = "." + GlobalConstants.TEST_FILE_EXTENSION;
        String fileName = testId + String.valueOf(System.currentTimeMillis()) + extension;

        File file = new File(StorageManager.getLocalStorageDirectory(), fileName);
        return file.getAbsolutePath();
    }
}
