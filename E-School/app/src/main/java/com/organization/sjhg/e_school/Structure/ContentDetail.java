package com.organization.sjhg.e_school.Structure;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.organization.sjhg.e_school.Helpers.ProtectionHelper;
import com.organization.sjhg.e_school.Helpers.StorageManager;
import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.organization.sjhg.e_school.Sync.FileDownloader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Bharat Lodha on 9/5/2015.
 * Organization : Eurovision Hitech Gurukul
 */
public class ContentDetail extends ContentDetailBase {
    static Cipher cipher;

    protected ContentDetail() {
    }

    protected ContentDetail(JSONObject storedata) throws JSONException {
        super.contentFileId = (storedata.getInt("ContentFileId"));
        super.contentTypeId = (storedata.getInt("ContentTypeId"));
        super.teachersId = (storedata.getInt("TeacherId"));
        super.subjectId = (storedata.getInt("SubjectId"));
        super.chapterId = (storedata.getInt("ChapterId"));
        super.topicId = (storedata.getInt("TopicId"));
        super.sectionId = (storedata.getInt("SectionId"));
        super.assignedPublishedDate = (storedata.getString("PublishDate"));
        super.contentIdentifier = (storedata.getString("FilePath"));
        super.subjectName = (storedata.getString("Subject"));
        super.chapterName = (storedata.getString("Chapter"));
        super.topicName = (storedata.getString("Topic"));
        super.contentName = (storedata.getString("FileName"));
        super.localFilePath = null;
    }

    @Override
    public InternalContentType getInternalContentType() {
        {
            return InternalContentType.getContentTypeFromContentTypeId(super.contentTypeId);
        }
    }

    @Override
    public void SaveVideoContentToLocal(Context context) throws IOException, JSONException, NetworkErrorException {
        String remotePathUrl = ServerAddress.getRemoteContentPath(context, this.contentIdentifier);

        // Get the local path to store the file
        this.localFilePath = StorageManager.geLocalFilePathfromRemote(remotePathUrl);

        // Download the file
        new FileDownloader(localFilePath, remotePathUrl).Download();
        this.protectionData=ProtectionHelper.InitialProtect(localFilePath);
    }

    @Override
    public void SaveContentToLocal(Context context) throws IOException {
        String remotePathUrl = ServerAddress.getRemoteContentPath(context, this.contentIdentifier);

        // Get the local path to store the file
        this.localFilePath = StorageManager.geLocalFilePathfromRemote(remotePathUrl);

        // Download the file
        new FileDownloader(localFilePath, remotePathUrl).Download();

        String keyString = "D4:6E:AC:3F:F0:BE";
        byte[] encodedKey = keyString.getBytes();
        File file = new File(localFilePath);
        SecretKey key = new SecretKeySpec(encodedKey, "Blowfish");
        try {
            cipher = Cipher.getInstance("Blowfish/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            FileInputStream fin = new FileInputStream(file);
            byte[] encode = new byte[(int) file.length()];
            fin.read(encode);
            String str = new String(encode);
            byte[] encode1 = Base64.decode(str, Base64.DEFAULT);
            byte[] result = cipher.doFinal(encode1);
            str=new String(result);
            encode1=Base64.decode(str,Base64.DEFAULT);
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(encode1);


        }catch (Exception e)
        {
            Log.e("error", "Exception" + e.toString());
        }
      this.protectionData=ProtectionHelper.InitialProtect(localFilePath);
    }
}
