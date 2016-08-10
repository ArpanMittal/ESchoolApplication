package com.organization.sjhg.e_school.Content.Laughguru;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.organization.sjhg.e_school.Database.DatabaseHelper;
import com.organization.sjhg.e_school.Structure.LaughguruContentDetailBase;

import java.sql.SQLException;

/**
 * Created by IT SJHG on 4/29/2016.
 */
public class LaughguruDataTable {
    public static  String KEY_ContentFileId = "ContentFileId";
    public static  String KEY_imagePath = "imagePath";
    public static  String KEY_audioPath = "audioPath";
    public static  String KEY_Order = "Order1";
    public static  String KEY_ProtectionDataI = "protectionDataI";
    public static  String KEY_ProtectionDataA = "protectionDataA";
    public static String KEY_LaughguruContentTypeId="laughguruContentTypeId";

    public static final String TABLE_NAME = "laughgurudatafile";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + KEY_ContentFileId + " VARCHAR(255) ,"
            + KEY_imagePath + "  VARCHAR(255) ,"
            + KEY_audioPath + "  VARCHAR(255) ,"
            + KEY_Order + " VARCHAR(255),"
            + KEY_ProtectionDataI + " BLOB ,"
            + KEY_ProtectionDataA + " BLOB ,"
            + KEY_LaughguruContentTypeId + " VARCHAR(255) "
            + " );";

    private final Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    public LaughguruDataTable(Context ctx) {
        this.context = ctx;
        databaseHelper = new DatabaseHelper(context);
    }

    public LaughguruDataTable open() throws SQLException {
        db = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    public boolean deleteRow(int content_fileId) {
        return db.delete(TABLE_NAME, KEY_ContentFileId + " = " + content_fileId, null) > 0;
    }

    public long insertLaughguruContent(LaughguruContentDetailBase laughguruContentDetailBase) {
        ContentValues initialValues = new ContentValues();
        //int o=(Integer) initialValues.getAsInteger(laughguruContentDetailBase.Order1);
      //  int phone1 = (Integer) cv.getAsInteger(Contacts.PHONE);
        initialValues.put(KEY_ContentFileId, laughguruContentDetailBase.ContentFileId);
        initialValues.put(KEY_imagePath, laughguruContentDetailBase.imagePath);
        initialValues.put(KEY_Order,laughguruContentDetailBase.Order1);
   //     if(!laughguruContentDetailBase.audioPath.equals(null))
        initialValues.put(KEY_audioPath, laughguruContentDetailBase.audioPath);
        initialValues.put(KEY_ProtectionDataI,laughguruContentDetailBase.protectionDataI);
        initialValues.put(KEY_ProtectionDataA,laughguruContentDetailBase.protectionDataA);
        initialValues.put(KEY_LaughguruContentTypeId,laughguruContentDetailBase.laughguruContentTypeId);
        return db.insert(TABLE_NAME, null, initialValues);
        //int a=0;
    }

    public Cursor fetchPath(String contentFileId) {
        return db.query(TABLE_NAME,
                new String[]{KEY_ContentFileId,KEY_imagePath,KEY_audioPath,KEY_Order,KEY_ProtectionDataI,KEY_ProtectionDataA,KEY_LaughguruContentTypeId},KEY_ContentFileId + "=" + contentFileId,
                null, null, null, null, null);
    }

    public Cursor fetchCtid(String contentFileId)
    {
        return db.query(TABLE_NAME,
                new String[]{KEY_ContentFileId, KEY_imagePath, KEY_audioPath, KEY_Order, KEY_ProtectionDataI, KEY_ProtectionDataA, KEY_LaughguruContentTypeId}, KEY_ContentFileId + "=" + contentFileId,
                null, null, null, null,String.valueOf(1));
    }

  /*  public long insertContent(LaughguruContentDetailBase laughguruContentDetailBase) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ContentFileId,laughguruContentDetailBase.contentFileId);
        initialValues.put(KEY_FileName,laughguruContentDetailBase.contentName);
        initialValues.put(KEY_Order, laughguruContentDetailBase.order);
        return db.insert(TABLE_NAME, null, initialValues);
    }
    public boolean deleteRow(int content_fileId) {
        return db.delete(TABLE_NAME, KEY_ContentFileId + " = " + content_fileId, null) > 0;
    }
*/
}
