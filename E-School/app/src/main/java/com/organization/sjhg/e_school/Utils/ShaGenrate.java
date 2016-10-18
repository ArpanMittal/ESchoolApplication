package com.organization.sjhg.e_school.Utils;

import android.util.Log;

import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.Structure.GlobalConstants;

import org.apache.commons.codec.binary.Hex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by arpan on 8/16/2016.
 */
public class ShaGenrate {

    public String generate (String password)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(password.getBytes());
            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            password=sb.toString();

        }catch (NoSuchAlgorithmException e)
        {
            // loghelper class to record all the logs
            Log.d(GlobalConstants.LOG_TAG,e.getMessage());
            new LogHelper(e);
        }
        return password;
    }

    public boolean verifyCheckSum(String localFilePath,String checksum)
    {
        MessageDigest md;
        String orignal_checksum=checksum;
        String new_checksum="";
        try{
            md = MessageDigest.getInstance("MD5");
            File file = new File(localFilePath);

            new_checksum = getDigest(new FileInputStream(file), md, 2048);
        }
        catch (Exception e)
        {
            Log.e(GlobalConstants.LOG_TAG,e.getMessage());
        }

        if(orignal_checksum.equals(new_checksum))
            return true;
        else
            return false;
    }

    private String getDigest(InputStream is, MessageDigest md, int byteArraySize)
            throws NoSuchAlgorithmException, IOException {

        md.reset();
        byte[] bytes = new byte[byteArraySize];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            md.update(bytes, 0, numBytes);
        }
        byte[] digest = md.digest();
        String result = new String(Hex.encodeHex(digest));
        return result;
    }
}
