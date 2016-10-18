package com.organization.sjhg.e_school.Remote;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.organization.sjhg.e_school.Structure.GlobalConstants;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JSONParserAsync
{
    private final RemoteCalls callFor;
    String url;
    Map<String, String> params;
    Map<String, String> header;
    JSONObject jsonObject;
    boolean isRemoteCallSuccessful = false;
    Exception exception;
    private RemoteCallHandler listener;
    //TODO: Remove list parameter from this call
    public JSONParserAsync(String url, Map<String, String> params,Map<String, String> header, RemoteCallHandler caller, RemoteCalls callFor) {
        this.url = url;
        this.listener = caller;
        this.callFor = callFor;

        this.params = params;
        this.header = header;

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        CustomRequest req = new  CustomRequest(Request.Method.POST, url,
                this.params,
                this.header,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        JSONParserAsync.this.isRemoteCallSuccessful = true;
                        JSONParserAsync.this.listener.HandleRemoteCall(JSONParserAsync.this.isRemoteCallSuccessful, JSONParserAsync.this.callFor, response, JSONParserAsync.this.exception);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        JSONParserAsync.this.isRemoteCallSuccessful = false;
                        JSONParserAsync.this.exception = error;
                        JSONParserAsync.this.listener.HandleRemoteCall(JSONParserAsync.this.isRemoteCallSuccessful, JSONParserAsync.this.callFor, null, JSONParserAsync.this.exception);
                    }
                });
        //setting time out and retries
        req.setRetryPolicy(new DefaultRetryPolicy(40 * 1000, 0, 1.0f));
        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(req, tag_json_obj);
    }


/*
    @Override
    protected JSONObject doInBackground(String... strings) {
        JSONObject returnValue = null;
        try {
            returnValue = HttpHelper.getInstance().MakeHttpRequestWithRetries(url, params);
            this.isRemoteCallSuccessful = true;
        } catch (Exception e) {
            this.isRemoteCallSuccessful = false;
            exception = e;
            Log.e(
                    GlobalConstants.LOG_TAG,
                    "JSONParserAsync :: doInBackground :: Remote Call Failure",
                    e);
        }
        return returnValue;
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        this.listener.HandleRemoteCall(this.isRemoteCallSuccessful, this.callFor, response, this.exception);
    }
    */
}