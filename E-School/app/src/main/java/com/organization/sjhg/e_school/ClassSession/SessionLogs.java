package com.organization.sjhg.e_school.ClassSession;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.organization.sjhg.e_school.Helpers.StudentApplicationUserData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * Created by siddharth on 04/01/16.
 */
public class SessionLogs {

    public static void foreground(String processName) {
        try {

            File root = Environment.getExternalStorageDirectory();
            File foregroundProcess = new File(root.getAbsolutePath() + "/E-SchoolContent/ForegroundProcess.txt");
            if (!foregroundProcess.exists())
                foregroundProcess.createNewFile();
            //FileReader in = new FileReader(root.getAbsolutePath() + "/E-SchoolContent/Foregroundprocess.txt");

            FileWriter fileWriter = new FileWriter(foregroundProcess.getAbsoluteFile(), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(String.valueOf(DateFormat.getDateTimeInstance().format(new Date())) + " Process opened is: " + processName);
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            Log.e("error","Exception occured");
            //e.printStackTrace();
        }
    }
    public static void LogEntry(Context context, String className, Level level, String statement) {

        if (!StudentApplicationUserData.getSessionLogsStatus(context))
            return;

        try {

            // Checking if sessionLogs text file exists.
            File root = Environment.getExternalStorageDirectory();

            File sessionLog = new File(root.getAbsolutePath() + "/E-SchoolContent/sessionLogs.txt");

            // If file doesn't exist. Create the file
            if (!sessionLog.exists())
                sessionLog.createNewFile();

            // Write log message in the file
            FileWriter fileWriter = new FileWriter(sessionLog.getAbsoluteFile(), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            // Adding timestamp and log class name
            bufferedWriter.write(String.valueOf(DateFormat.getDateTimeInstance().format(new Date()))
                    + " " + className);
            bufferedWriter.newLine();
            // Writing log in next line
            bufferedWriter.write(level + ": " + statement);
            // One line gap before next log
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            // Close buffer and file
            bufferedWriter.close();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
