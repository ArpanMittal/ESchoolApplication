package com.organization.sjhg.e_school.Utils;

import android.app.Activity;
import android.widget.Toast;

import com.organization.sjhg.e_school.Helpers.LogHelper;

import org.json.JSONObject;

/**
 * Created by arpan on 8/12/2016.
 */
public  class ToastActivity {

   public void makeToastMessage(JSONObject response, Activity activity)
   {
       try {
           Toast.makeText(activity, "error code:" + response.get("code").toString() + " " + response.get("message").toString(), Toast.LENGTH_LONG).show();
       }catch (Exception e)
       {

           new LogHelper(e);
           e.printStackTrace();
       }
   }
    public void makeUknownErrorMessage(Activity activity)
    {
        Toast.makeText(activity,"error code:520 unknown error",Toast.LENGTH_LONG).show();
    }
    public void makeJsonException(Activity activity)
    {
        Toast.makeText(activity,"error code:420 retry please",Toast.LENGTH_LONG).show();
    }
}
