package com.organization.sjhg.e_school.Remote;

import org.json.JSONObject;

/**
 * Created by Sir Ravi on 9/26/2015.
 */
public interface RemoteCallHandler {

    void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception);
}
