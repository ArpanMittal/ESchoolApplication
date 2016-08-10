package com.organization.sjhg.e_school.Helpers;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Bharat Lodha on 3/29/2015.
 * Organization : Eurovision Hitech Gurukul
 */
/**
 * Edited by Prateek Tulsyan on 30-03-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class ProtectionHelper {

    private static  Integer protection_data_size_bytes = 1024;
    private static  Integer protection_data_offset = 0;

    public static byte[]  InitialProtect(String localFilePath) throws IOException {

        byte[] protectionData = new byte[protection_data_size_bytes];

        File file = new File(localFilePath);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file,"rw");
        randomAccessFile.read(protectionData, protection_data_offset, protection_data_size_bytes);

        ProtectFile(localFilePath);
        return protectionData;
    }

    public static void ProtectFile(String localFilePath) throws IOException {

        if (localFilePath == null)
            return;

        // Open the file
        File file = new File(localFilePath);
        if (!file.exists()) {
            return;
        }

        // Zero buffer to write to file
        byte[] zeroes = new byte[protection_data_size_bytes];
        for (int i = 0; i < zeroes.length; i++) {
            zeroes[i] = 0;
        }

        // Write zeroes at the desired location
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        randomAccessFile.write(zeroes, protection_data_offset, protection_data_size_bytes);
        randomAccessFile.close();
    }


    public static void AccessFile(Context context, String localFilePath, byte[] protection_data) throws IOException {

        // Lock the last accessed file
        String lastAccessedFilePath = StudentApplicationUserData.getLastAccessedFile(context);
        if (lastAccessedFilePath != null) {
            ProtectionHelper.ProtectFile(lastAccessedFilePath);
        }

        // Unlock the requested file
        File file = new File(localFilePath);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file,"rw");
        randomAccessFile.write(protection_data, protection_data_offset, protection_data_size_bytes);

        // Add this file to the last accessed file
        StudentApplicationUserData.SaveLastAccessedFile(context, localFilePath);
    }
}
