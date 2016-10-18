package com.organization.sjhg.e_school.TakeEvents;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.organization.sjhg.e_school.TakeNotes.NotesDatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Arpan on 3/2/2016.
 */
public class EventDetailTable {
    //static EventListingActivity event_list;
    public static final String KEY_TITLE = "title";
    public static final String KEY_DATE = "date";
    public static final String KEY_BODY = "body";
    public static final String KEY_ROWID = "_id";


    private static final String TABLE_NAME = "events";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_TITLE + " VARCHAR(255) NOT NULL ,"
            + KEY_BODY + " VARCHAR(255) NOT NULL ,"
            + KEY_DATE + " VARCHAR(255) NOT NULL "
            + " );";
    private final Context context;
    private EventDatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    public EventDetailTable(Context context)
    {
        this.context = context;
        databaseHelper = new EventDatabaseHelper(context);
    }
    public EventDetailTable open() throws SQLException
    {
        db = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        databaseHelper.close();
    }

    public Cursor fetchAllEvents()
    {
        return db.query(TABLE_NAME, new String[] {KEY_ROWID, KEY_TITLE, KEY_BODY, KEY_DATE}, null, null, null,null,KEY_ROWID +" DESC");
    }

    public long insertNote(JSONObject diaryDetail) throws JSONException {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID, (String) diaryDetail.get("DiaryId"));
        initialValues.put(KEY_TITLE, String.valueOf(diaryDetail.get("Title")));
        initialValues.put(KEY_BODY, String.valueOf(diaryDetail.get("Body")));
        initialValues.put(KEY_DATE, String.valueOf(diaryDetail.get("TimeStamp")));
        //event_list.populateListView();
        return db.insert(TABLE_NAME, null, initialValues);


    }

    public Cursor fetchSingleEvent(long rowId) throws SQLException
    {
        Cursor mCursor = db.query(true, TABLE_NAME, new String[] {KEY_ROWID, KEY_TITLE, KEY_BODY,KEY_DATE}, KEY_ROWID + "=" + rowId, null,null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

}
