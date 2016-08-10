package com.organization.sjhg.e_school.Helpers;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by arpan on 7/11/2016.
 */
public class LogHelper {
    public LogHelper(Exception e)
    {
        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;

        try {
            File root = Environment.getExternalStorageDirectory();

            // This block configure the logger with handler and formatter
            fh = new FileHandler(root.getAbsolutePath()+"/E-SchoolContent/E-schoolLogFile.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log any messages
            logger.info(e.getMessage());

        } catch (SecurityException e1) {
            e.printStackTrace();
        } catch (IOException e1) {
            e.printStackTrace();
        }

        //logger.info("Hi How r u?");
    }
}
