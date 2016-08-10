package com.organization.sjhg.e_school.Sync;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.organization.sjhg.e_school.Content.Content_Type;
import com.organization.sjhg.e_school.Database.DatabaseOperations;
import com.organization.sjhg.e_school.MainActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.ExceptionHandler;
import com.organization.sjhg.e_school.Remote.HttpHelper;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.organization.sjhg.e_school.Structure.ContentDetailBase;
import com.organization.sjhg.e_school.Structure.LaughguruContentDetailBase;
import com.organization.sjhg.e_school.Structure.NotesDetail;
import com.organization.sjhg.e_school.Structure.TestDetail;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shreyas on 31/05/16.
 */
public class SubmitTests extends AsyncTask<String, String, String> {
    public List<LaughguruContentDetailBase> lgList;
    private Context context;


    public SubmitTests(Context ctx) {
        Initialize(ctx);
    }

    public void Initialize(Context ctx) {
        this.context = ctx;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(context.getApplicationContext(),
                "Submitting tests",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params) {
        return syncContent();
    }

    private String syncContent() {
        List<ContentDetailBase> allLocalContentList;

        try {
            // Get local content list
            allLocalContentList = DatabaseOperations.getLocalContentList(context);

            // Convert lists to arrays
            ContentDetailBase[] allLocalContent =
                    allLocalContentList.toArray(new ContentDetailBase[allLocalContentList.size()]);

            // Upload submitted Test
            for (ContentDetailBase localContent : allLocalContent) {
                if (localContent.getInternalContentType().contentType == 3) {
                    TestDetail test = TestDetail.getTesDetailObjectFromLocal(localContent.localFilePath);

                    if (test.status == TestDetail.TestStatus.TEST_SUBMITTED)
                        uploadTest(test);
                }
            }

        } catch (IOException | NetworkErrorException e) {
            HandleException(e, R.string.error_message_internet);
        } catch (JSONException e) {
            HandleException(e, R.string.error_message_json);
        } catch (SQLException | ClassNotFoundException e) {
            HandleException(e, R.string.error_message_sql);
        }
        return null;
    }

    private String HandleException(Exception e, Integer errorStringId) {
        e.printStackTrace();
        return context.getResources().getString(errorStringId);
    }


    protected void onPostExecute(String message) {

                Intent goToMain = new Intent(context, MainActivity.class);
                context.startActivity(goToMain);

    }

    private void uploadTest(TestDetail test) throws JSONException, IOException, NetworkErrorException {

        String TEST_STORE_ANSWER_PAGE = context.getResources().getString(R.string.test_store_answer_page);
        String URL = ServerAddress.getServerAddress(context) + "/" + TEST_STORE_ANSWER_PAGE;

        int questionAnswered = 0;

        final List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("TestId", String.valueOf(test.contentFileId)));
        params.add(new BasicNameValuePair("TeacherId", String.valueOf(test.teachersId)));
        params.add(new BasicNameValuePair("TimeDuration", String.valueOf(test.timeLimit)));
        params.add(new BasicNameValuePair("Score", String.valueOf("0")));


        String AnswerIds = "";
        for (int k = 0; k < test.questions.size(); k++) {
            if (test.questions.get(k).selectedAnswer != null) {
                AnswerIds = AnswerIds + test.questions.get(k).selectedAnswer;
                questionAnswered++;
            } else
                AnswerIds = AnswerIds + "@@null";
        }

        params.add(new BasicNameValuePair("AnswerIds", String.valueOf(AnswerIds)));
        params.add(new BasicNameValuePair("SectionId", String.valueOf(test.sectionId)));
        params.add(new BasicNameValuePair("QuestionAnswerd", String.valueOf(questionAnswered)));
        params.add(new BasicNameValuePair("Status", "0"));

        HttpHelper.getInstance().MakeHttpRequestWithRetries(URL, params);
        test.status = TestDetail.TestStatus.TEST_UPLOADED;
        test.SaveTestToLocal();
    }
}