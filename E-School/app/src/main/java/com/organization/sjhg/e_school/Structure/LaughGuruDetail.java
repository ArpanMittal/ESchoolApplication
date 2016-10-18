package com.organization.sjhg.e_school.Structure;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bharat Lodha on 9/5/2015.
 * Organization : Eurovision Hitech Gurukul
 */
public class LaughGuruDetail extends ContentDetail {

    protected LaughGuruDetail() {
    }

    protected LaughGuruDetail(JSONObject storedata) throws JSONException {

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
        return InternalContentType.getContentTypeFromContentTypeId(super.contentTypeId);
    }

    @Override
    public void SaveContentToLocal(Context context) {
        return;
    }
}
