package com.organization.sjhg.e_school.Utils;

import android.util.Log;

import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.Structure.GlobalConstants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by arpan on 8/16/2016.
 */
public class ShaGenrate {

    public String generate (String password)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
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
}
