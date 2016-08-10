package com.organization.sjhg.e_school.TakeNotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Prateek Tulsyan on 30-04-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class NotesDetailTable
{
    public static final String KEY_TITLE = "title";
    public static final String KEY_DATE = "date";
    public static final String KEY_BODY = "body";
    public static final String KEY_ROWID = "_id";

    private static final String TABLE_NAME = "notes";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                                                                                          + KEY_TITLE + " VARCHAR(255) NOT NULL ,"
                                                                                          + KEY_BODY + " VARCHAR(255) NOT NULL ,"
                                                                                          + KEY_DATE + " VARCHAR(255) NOT NULL "
                                                                                   + " );";

    private final Context context;
    private NotesDatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    public NotesDetailTable(Context ctx) {
        this.context = ctx;
        databaseHelper = new NotesDatabaseHelper(context);
    }

    public NotesDetailTable open() throws SQLException
    {
        db = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        databaseHelper.close();
    }

    public long createNote(String title, String body, String date)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_BODY, body);
        initialValues.put(KEY_DATE, date);

        return db.insert(TABLE_NAME, null, initialValues);
    }

    public long insertNote(JSONObject notesDetail) throws JSONException {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID, (String) notesDetail.get("NoteId"));
        initialValues.put(KEY_TITLE, String.valueOf(notesDetail.get("Title")));
        initialValues.put(KEY_BODY, String.valueOf(notesDetail.get("Body")));
        initialValues.put(KEY_DATE, String.valueOf(notesDetail.get("Date")));

        return db.insert(TABLE_NAME, null, initialValues);
    }

    public boolean deleteNote(long rowId)
    {
        return db.delete(TABLE_NAME, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public int deleteAllNotes()
    {
        return db.delete(TABLE_NAME, null, null);
    }

    public Cursor fetchAllNotes()
    {
        return db.query(TABLE_NAME, new String[] {KEY_ROWID, KEY_TITLE, KEY_BODY, KEY_DATE}, null, null, null, null, null);
    }

    public Cursor fetchSingleNote(long rowId) throws SQLException
    {
        Cursor mCursor = db.query(true, TABLE_NAME, new String[] {KEY_ROWID, KEY_TITLE, KEY_BODY,KEY_DATE}, KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateNote(long rowId, String title, String body,String date)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_BODY, body);
        args.put(KEY_DATE, date);

        return db.update(TABLE_NAME, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
