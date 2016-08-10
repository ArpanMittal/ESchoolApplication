package com.organization.sjhg.e_school.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.organization.sjhg.e_school.Structure.ContentDetailBase;

import java.sql.SQLException;

/**
 * Created by Prateek Tulsyan on 26-02-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class ContentDetailTable {
    public static final String KEY_ContentFileId = "ContentFileId";
    public static final String KEY_ContentTypeId = "ContentTypeId";
    public static final String KEY_TeacherId = "TeacherId";
    public static final String KEY_SectionId = "SectionId";
    public static final String KEY_SubjectId = "SubjectId";
    public static final String KEY_ChapterId = "ChapterId";
    public static final String KEY_TopicId = "TopicId";
    public static final String KEY_PublishDate = "PublishDate";
    public static final String KEY_FilePath = "FilePath";
    public static final String KEY_SubjectName = "SubjectName";
    public static final String KEY_ChapterName = "ChapterName";
    public static final String KEY_TopicName = "TopicName";
    public static final String KEY_ProtectionData = "ProtectionData";
    public static final String KEY_LocalFilePath = "LocalFilePath";
    public static final String KEY_FileName = "FileName";

    public static final String TABLE_NAME = "contentfile";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + KEY_ContentFileId + " INT(10) ,"
            + KEY_ContentTypeId + " INT(10) ,"
            + KEY_TeacherId + " INT(10) ,"
            + KEY_SubjectId + " INT(10) ,"
            + KEY_ChapterId + " INT(10) ,"
            + KEY_TopicId + " INT(10) ,"
            + KEY_SectionId + " INT(10) ,"
            + KEY_PublishDate + " datetime ,"
            + KEY_FilePath + " VARCHAR(255) ,"
            + KEY_SubjectName + " VARCHAR(255) ,"
            + KEY_ChapterName + " VARCHAR(255) ,"
            + KEY_TopicName + " VARCHAR(255) ,"
            + KEY_ProtectionData + " BLOB ,"
            + KEY_FileName + " VARCHAR(255) ,"
            + KEY_LocalFilePath + " VARCHAR(255) "
            + " );";

    private final Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    public ContentDetailTable(Context ctx) {
        this.context = ctx;
        databaseHelper = new DatabaseHelper(context);
    }

    public ContentDetailTable open() throws SQLException {
        db = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    public long insertContent(ContentDetailBase contentDetailBase) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ContentFileId, contentDetailBase.contentFileId);
        initialValues.put(KEY_ContentTypeId, contentDetailBase.contentTypeId);
        initialValues.put(KEY_TeacherId, contentDetailBase.teachersId);
        initialValues.put(KEY_SubjectId, contentDetailBase.subjectId);
        initialValues.put(KEY_ChapterId, contentDetailBase.chapterId);
        initialValues.put(KEY_TopicId, contentDetailBase.topicId);
        initialValues.put(KEY_SectionId, contentDetailBase.sectionId);
        initialValues.put(KEY_PublishDate, contentDetailBase.assignedPublishedDate);
        initialValues.put(KEY_FilePath, contentDetailBase.contentIdentifier);
        initialValues.put(KEY_SubjectName, contentDetailBase.subjectName);
        initialValues.put(KEY_ChapterName, contentDetailBase.chapterName);
        initialValues.put(KEY_TopicName, contentDetailBase.topicName);
        initialValues.put(KEY_ProtectionData, contentDetailBase.protectionData);
        initialValues.put(KEY_LocalFilePath, contentDetailBase.localFilePath);
        initialValues.put(KEY_FileName, contentDetailBase.contentName);

        return db.insert(TABLE_NAME, null, initialValues);
    }

    public boolean deleteRow(int content_fileId) {
        return db.delete(TABLE_NAME, KEY_ContentFileId + " = " + content_fileId, null) > 0;
    }

    public Cursor fetchContent() {
        return db.query(TABLE_NAME,
                new String[]{
                        KEY_ContentFileId, KEY_ContentTypeId, KEY_TeacherId, KEY_SubjectId, KEY_ChapterId,
                        KEY_TopicId, KEY_SectionId, KEY_PublishDate, KEY_FilePath, KEY_SubjectName,
                        KEY_ChapterName, KEY_TopicName, KEY_ProtectionData, KEY_LocalFilePath, KEY_FileName},
                null, null, null, null, null);
    }

    public Cursor fetchSubjectContentDistinct() {
        String SUBJECT_QUERY = "SELECT DISTINCT " + KEY_SubjectId + "," + KEY_SubjectName + " FROM " + TABLE_NAME + " ;";

        return db.rawQuery(SUBJECT_QUERY, null);
    }

/*    public Cursor fetchSubjectContentBookDetails(int subjectId)
    {
        String SUBJECT_CONTENT_BOOK_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + " ( " + KEY_FileExtension + "=" + " 'jpg' " + " OR "
                + KEY_FileExtension + "=" + " 'jpeg' " + " OR "
                + KEY_FileExtension + "=" + " 'pdf' " + " OR "
                + KEY_FileExtension + "=" + " 'PDF' " + " OR "
                + KEY_FileExtension + "=" + " 'JPG' " + " OR "
                + KEY_FileExtension + "=" + " 'JPEG' " + " ) " + " AND "
                + KEY_SubjectId + " = " + subjectId + " ;";

        return db.rawQuery(SUBJECT_CONTENT_BOOK_QUERY,null);
    }

    public Cursor fetchSubjectContentVideoDetails(int subjectId)
    {
        String SUBJECT_CONTENT_VIDEO_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + " ( " + KEY_FileExtension + "=" + " 'mp4' " + " OR "
                + KEY_FileExtension + "=" + " 'MP4' " + " OR "
                + KEY_FileExtension + "=" + " 'mp3' " + " OR "
                + KEY_FileExtension + "=" + " 'MP3' " + " ) " + " AND "
                + KEY_SubjectId + " = " + subjectId + " ;";

        return db.rawQuery(SUBJECT_CONTENT_VIDEO_QUERY,null);
    }*/
}
