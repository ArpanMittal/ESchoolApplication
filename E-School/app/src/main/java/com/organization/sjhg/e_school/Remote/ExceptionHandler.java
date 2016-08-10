package com.organization.sjhg.e_school.Remote;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

import com.organization.sjhg.e_school.R;

/**
 * Created by Prateek Tulsyan on 03-04-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class ExceptionHandler {
    public static void thrownExceptions(Context context, String title, String message) {
        showAlertDialog(context, title, message);
    }

    private static void showAlertDialog(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog)).create();

        alertDialog.setMessage(message);
        alertDialog.setTitle(title);
        alertDialog.setIcon(R.drawable.connection_error);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    public static void showAlertDialogContent(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog)).create();

        alertDialog.setMessage(message);
        alertDialog.setTitle(title);
        alertDialog.setIcon(R.drawable.nocontent);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();

    }

}
