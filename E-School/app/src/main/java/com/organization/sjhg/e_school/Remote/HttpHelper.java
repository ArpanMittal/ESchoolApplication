package com.organization.sjhg.e_school.Remote;

import android.accounts.NetworkErrorException;
import android.util.Log;
import android.widget.Toast;

import com.organization.sjhg.e_school.Structure.GlobalConstants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Prateek Tulsyan on 24-02-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class HttpHelper {
    private static HttpHelper httpHelper;
    // Implementing Singleton pattern to use the same HTTPClient for all the queries
    // This will help in maintaining session info when we implement server session
    private static DefaultHttpClient httpClient;
    CookieStore cookieStore;


    private HttpHelper() {
        httpClient = new DefaultHttpClient();
    }

    public static HttpHelper getInstance() {
        if (httpHelper == null) {
            httpHelper = new HttpHelper();
        }

        return httpHelper;
    }

    public JSONObject MakeHttpRequestWithRetries(String url, List<NameValuePair> params) throws IOException, NetworkErrorException, JSONException {
        return MakeHttpRequestWithRetries(url, params, 1);
    }

    public JSONObject MakeHttpRequestWithRetries(String url, List<NameValuePair> params, int tryCount) throws IOException, NetworkErrorException, JSONException {

        // on third retry call the function directly and don't handle exception
        if (tryCount >= 2) {
            return MakeHttpRequest(url, params);
        }

        try {
            return MakeHttpRequest(url, params);
        } catch (NetworkErrorException | IOException | JSONException e) {
            // In  case of an error; Log it and recall the function after one second
            Log.e(
                    GlobalConstants.LOG_TAG,
                    "HTTPHelper :: MakeHttpRequestWithRetries :: Retrying Remote Call. Retry Count : " + tryCount,
                    e);
            tryWait(1000);
            return MakeHttpRequestWithRetries(url, params, tryCount + 1);
        }
    }

    private void tryWait(int milliseconds) {
        // Try to wait for specified amount of time
        // If an exception occurs; continue
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Log.e(GlobalConstants.LOG_TAG, "HTTPHelper :: tryWait :: WaitInterrupted", e);
        }
    }

    private JSONObject MakeHttpRequest(String url, List<NameValuePair> params) throws IOException, NetworkErrorException, JSONException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        return MakeHttpRequest(httpPost);
    }

    public JSONObject MakeHttpRequest(HttpPost post) throws IOException, NetworkErrorException, JSONException {
        InputStream is;
        String returnValue = null;

        //if(httpClient.getCookieStore() != null && httpClient.getCookieStore().getCookies()!= null
        //        && httpClient.getCookieStore().getCookies().size() >0 )
        //    post.addHeader("Cookie", this.httpClient.getCookieStore().getCookies().get(0).getName()+"="+httpClient.getCookieStore().getCookies().get(0).getValue());
        // Make the remote call and save response
        HttpResponse httpResponse = httpClient.execute(post);

        // If the remote HTTP call fails, throw an exception
        if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {

            String errorMessage = httpResponse.getStatusLine().getReasonPhrase();
            throw new NetworkErrorException(errorMessage);
        }


        // Extract the response and convert to string
        HttpEntity httpEntity = httpResponse.getEntity();
        is = httpEntity.getContent();

        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
        StringBuilder str = new StringBuilder();
        try {
            String strLine;
            while ((strLine = reader.readLine()) != null) {
                str.append(strLine).append("\n");
            }
            is.close();
            returnValue = str.toString().trim();
        } finally {
            reader.close();
        }

        // If the reply is a valid JSONObject, return it
        // else return null
        try {
            return new JSONObject(returnValue);
        } catch (Exception exception) {
            Log.e(GlobalConstants.LOG_TAG, "Remote call result :: " + returnValue);
            Log.e(GlobalConstants.LOG_TAG, "HTTPHelper :: MakeHTTPRequest :: Json Not Proper", exception);
            return null;
        }
    }
}