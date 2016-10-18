package com.organization.sjhg.e_school.Structure;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.database.Cursor;

import com.organization.sjhg.e_school.Content.Content_Type;
import com.organization.sjhg.e_school.Database.old.ContentDetailTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;


/**
 * Created by Prateek Tulsyan on 10-03-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public abstract class ContentDetailBase implements Serializable {
    public int contentFileId;
    public int contentTypeId;
    public String contentName;
    public int teachersId;
    public int subjectId;
    public int chapterId;
    public int topicId;
    public int sectionId;
    public String assignedPublishedDate;
    public String contentIdentifier;
    public String localFilePath;
    public String subjectName;
    public String chapterName;
    public String topicName;
    public byte[] protectionData;

    public static ContentDetailBase getInstance(JSONObject obj)
            throws JSONException {
        ContentDetailBase returnObject;

        switch (obj.getInt("ContentTypeId")) {
            case 1:
                returnObject = new LaughGuruDetail(obj);
                break;
            case 2:
            case 3:
            case 4:
                returnObject = new ContentDetail(obj);
                break;
            case 5:
            case 6:
                returnObject = new TestDetail(obj);
                break;
            default:
                returnObject = null;
                break;
        }

//        if (obj.has("TestId"))
//            returnObject = new TestDetail(obj);
//        else if (obj.has("ConentFileId"))
//            returnObject = new ContentDetail(obj);

        return returnObject;
    }

    public static ContentDetailBase getInstance(Cursor cursor) {
        ContentDetailBase returnObject;
        String localFilePath = (cursor.getString(cursor.getColumnIndex(ContentDetailTable.KEY_LocalFilePath)));

        switch (cursor.getInt(cursor.getColumnIndex(ContentDetailTable.KEY_ContentTypeId))) {
            case 1:
                returnObject = new LaughGuruDetail();
                break;
            case 2:
            case 3:
            case 4:
                returnObject = new ContentDetail();
                break;
            case 5:
            case 6:
                returnObject = new TestDetail();
                break;
            default:
                returnObject = null;
                break;
        }

        if (returnObject != null) {
            returnObject.contentFileId = (cursor.getInt(cursor.getColumnIndex(ContentDetailTable.KEY_ContentFileId)));
            returnObject.contentTypeId = (cursor.getInt(cursor.getColumnIndex(ContentDetailTable.KEY_ContentTypeId)));
            returnObject.contentIdentifier = (cursor.getString(cursor.getColumnIndex(ContentDetailTable.KEY_FilePath)));
            returnObject.sectionId = (cursor.getInt(cursor.getColumnIndex(ContentDetailTable.KEY_SectionId)));
            returnObject.localFilePath = localFilePath;
            returnObject.assignedPublishedDate = (cursor.getString(cursor.getColumnIndex(ContentDetailTable.KEY_PublishDate)));
            returnObject.subjectId = (cursor.getInt(cursor.getColumnIndex(ContentDetailTable.KEY_SubjectId)));
            returnObject.chapterId = (cursor.getInt(cursor.getColumnIndex(ContentDetailTable.KEY_ChapterId)));
            returnObject.topicId = (cursor.getInt(cursor.getColumnIndex(ContentDetailTable.KEY_TopicId)));
            returnObject.subjectName = (cursor.getString(cursor.getColumnIndex(ContentDetailTable.KEY_SubjectName)));
            returnObject.chapterName = (cursor.getString(cursor.getColumnIndex(ContentDetailTable.KEY_ChapterName)));
            returnObject.topicName = (cursor.getString(cursor.getColumnIndex(ContentDetailTable.KEY_TopicName)));
            returnObject.teachersId = (cursor.getInt(cursor.getColumnIndex(ContentDetailTable.KEY_TeacherId)));
            returnObject.protectionData = (cursor.getBlob(cursor.getColumnIndex(ContentDetailTable.KEY_ProtectionData)));
            returnObject.contentName = (cursor.getString(cursor.getColumnIndex(ContentDetailTable.KEY_FileName)));
        }
        return returnObject;
    }

    public abstract InternalContentType getInternalContentType();

    public abstract void SaveContentToLocal(Context context) throws IOException, JSONException, NetworkErrorException;

    public abstract void SaveVideoContentToLocal(Context context) throws IOException, JSONException, NetworkErrorException;

    public static class ContentComparator implements Comparator<ContentDetailBase> {

        @Override
        public int compare(ContentDetailBase lhs, ContentDetailBase rhs) {

            if (lhs.subjectId != rhs.subjectId)
                return ((Integer) lhs.subjectId).compareTo(rhs.subjectId);

            if (lhs.contentTypeId == Content_Type.ADAPTIVE_TEST.getValue() && rhs.contentTypeId == Content_Type.ADAPTIVE_TEST.getValue())
                return ((Integer) lhs.contentFileId).compareTo(rhs.contentFileId);
            if (lhs.contentTypeId == Content_Type.TEST.getValue() && rhs.contentTypeId == Content_Type.TEST.getValue())
                return ((Integer) lhs.contentFileId).compareTo(rhs.contentFileId);


         /*   if (lhs.contentTypeId == 5 || rhs.contentTypeId == 5)
                return ((Integer) lhs.contentTypeId).compareTo(rhs.contentTypeId);
            if (lhs.contentTypeId == 6 || rhs.contentTypeId == 6)
                return ((Integer) lhs.contentTypeId).compareTo(rhs.contentTypeId);*/


            if (lhs.chapterId != rhs.chapterId)
                return ((Integer) lhs.chapterId).compareTo(rhs.chapterId);


            if (lhs.topicId != rhs.topicId)
                return ((Integer) lhs.topicId).compareTo(rhs.topicId);


            if (lhs.contentFileId != rhs.contentFileId)
                return ((Integer) lhs.contentFileId).compareTo(rhs.contentFileId);

            return 0;
        }
    }
}

