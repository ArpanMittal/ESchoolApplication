package com.organization.sjhg.e_school.Sync;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Prateek Tulsyan on 04-03-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class FileDownloader {
    private String localFilePath;
    private String remoteFilePath;

    public FileDownloader(String localFilePath, String remoteFilePath) {
        this.localFilePath = localFilePath;
        this.remoteFilePath = remoteFilePath;
    }

    public void Download() throws IOException {
        InputStream input = null;
        FileOutputStream output = null;
        String errorMessage = null;
        int count;
        try {
            // Get remote file stream
            URL url = new URL(remoteFilePath);
            URLConnection connection = url.openConnection();
            connection.connect();

            int lengthOfFile = connection.getContentLength();
            Log.d("ANDROID_ASYNC", "Length of file: " + lengthOfFile);

            input = new BufferedInputStream(url.openStream());

            File file = new File(localFilePath);
            file.getParentFile().mkdirs();
            output = new FileOutputStream(file);
            // Read in chunks of 1024 bytes from remote stream and write to local  stream
            byte data[] = new byte[1024];
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
        } finally {
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
                if (input != null)
                    input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}