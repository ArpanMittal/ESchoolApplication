package com.organization.sjhg.e_school.Database;

import android.content.Context;
import android.database.Cursor;
import android.util.Pair;

import com.organization.sjhg.e_school.Content.Laughguru.LaughguruDataTable;
import com.organization.sjhg.e_school.Structure.ContentDetailBase;
import com.organization.sjhg.e_school.Structure.LaughguruContentDetailBase;
import com.organization.sjhg.e_school.Structure.NotesDetail;
import com.organization.sjhg.e_school.TakeNotes.NotesDetailTable;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prateek Tulsyan on 10-03-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class DatabaseOperations {
    public static void addContentToDatabase(ContentDetailBase contentDetailBase, Context context) throws SQLException {
        ContentDetailTable table_obj = new ContentDetailTable(context);
        table_obj.open();
        table_obj.insertContent(contentDetailBase);
        table_obj.close();
    }
    public static String getLgctid(Context context,String contentFileId) throws SQLException, JSONException {
        List<LaughguruContentDetailBase> returnValue = new ArrayList<>();
        String cfid=contentFileId;
        LaughguruDataTable table_obj = new LaughguruDataTable(context);
        try {
            table_obj.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Cursor c = table_obj.fetchCtid(contentFileId);
        try {
            returnValue = convertCursorToList(c);
            table_obj.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnValue.get(0).laughguruContentTypeId;

    }
    public static List<LaughguruContentDetailBase> getPath(Context context, String contentFileId) throws SQLException, JSONException {
        List<LaughguruContentDetailBase> returnValue = new ArrayList<>();
        String cfid=contentFileId;
        LaughguruDataTable table_obj = new LaughguruDataTable(context);
        table_obj.open();
        Cursor c = table_obj.fetchPath(contentFileId);
        returnValue = convertCursorToList(c);
        table_obj.close();
        return returnValue;
    }
    public static void addLaughguruContentToDatabase(LaughguruContentDetailBase laughguruContentDetailBase, Context context) throws SQLException {
        LaughguruDataTable table_obj = new LaughguruDataTable(context);
        table_obj.open();
        table_obj.insertLaughguruContent(laughguruContentDetailBase);
        table_obj.close();
    }
    public static List<ContentDetailBase> getLocalContentList(Context context) throws SQLException {
        List<ContentDetailBase> returnValue;
        ContentDetailTable table_obj = new ContentDetailTable(context);
        table_obj.open();
        Cursor c = table_obj.fetchContent();
        returnValue = convertCursorToContentList(c);
        table_obj.close();
        return returnValue;
    }

    public static List<Pair<Integer, String>> getLocalSubjectDetails(Context context) throws SQLException {
        List<Pair<Integer, String>> returnValue = new ArrayList<>();
        ContentDetailTable table_obj = new ContentDetailTable(context);
        table_obj.open();
        Cursor c = table_obj.fetchSubjectContentDistinct();
        if (c.moveToFirst()) {
            do {
                Pair<Integer, String> subjectData = new Pair<>(
                        c.getInt(c.getColumnIndex("SubjectId")),
                        c.getString(c.getColumnIndex("SubjectName"))
                );
                returnValue.add(subjectData);
            }
            while (c.moveToNext());
        }
        table_obj.close();
        return returnValue;
    }

    public static List<NotesDetail> getLocalNotesList(Context context) throws SQLException {
        List<NotesDetail> returnValue;
        NotesDetailTable table_obj = new NotesDetailTable(context);
        table_obj.open();
        Cursor c = table_obj.fetchAllNotes();
        returnValue = convertCursorToNotesList(c);
        table_obj.close();
        return returnValue;
    }
    private static List<LaughguruContentDetailBase> convertCursorToList(Cursor c) throws JSONException {
        List<LaughguruContentDetailBase> returnValue = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                LaughguruContentDetailBase lgfiles = new LaughguruContentDetailBase(c);
                returnValue.add(lgfiles);
            }
            while (c.moveToNext());
        }
        return returnValue;
    }
    private static List<NotesDetail> convertCursorToNotesList(Cursor c) {
        List<NotesDetail> returnValue = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                NotesDetail localNotes = new NotesDetail(c);
                returnValue.add(localNotes);
            }
            while (c.moveToNext());
        }
        return returnValue;
    }

    private static List<ContentDetailBase> convertCursorToContentList(Cursor c) {
        List<ContentDetailBase> returnValue = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                ContentDetailBase localContent = ContentDetailBase.getInstance(c);
                returnValue.add(localContent);
            }
            while (c.moveToNext());
        }
        return returnValue;
    }

    public static void deleteContent(int content_fileId, Context context) throws SQLException {
        ContentDetailTable table_obj = new ContentDetailTable(context);
        table_obj.open();
        table_obj.deleteRow(content_fileId);
        table_obj.close();
    }
    public static void deleteLauguguruContent(int content_fileId, Context context) throws SQLException
    {
        LaughguruDataTable table_obj = new LaughguruDataTable(context);
        table_obj.open();
        table_obj.deleteRow(content_fileId);
        table_obj.close();
    }


}
