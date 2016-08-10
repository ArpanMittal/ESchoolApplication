package com.organization.sjhg.e_school.Structure;

import android.database.Cursor;

import com.organization.sjhg.e_school.TakeNotes.NotesDetailTable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shreyas Tripathy on 12 Dec 15.
 */
public class NotesDetail {
    public String noteTitle;
    public String noteBody;
    public String noteDate;
    public String noteId;

    public NotesDetail(Cursor cursor) {

        noteTitle = (cursor.getString(cursor.getColumnIndex(NotesDetailTable.KEY_TITLE)));
        noteBody = (cursor.getString(cursor.getColumnIndex(NotesDetailTable.KEY_BODY)));
        noteDate = (cursor.getString(cursor.getColumnIndex(NotesDetailTable.KEY_DATE)));
        noteId = (cursor.getString(cursor.getColumnIndex(NotesDetailTable.KEY_ROWID)));
    }

    public NotesDetail(JSONObject storedata) throws JSONException {

        noteTitle = (storedata.getString("Title"));
        noteBody = (storedata.getString("Body"));
        noteDate = (storedata.getString("Date"));
        noteId = (storedata.getString("NoteId"));
    }

    public JSONObject getJsonObject() throws JSONException {
        JSONObject returnObject = new JSONObject();

        returnObject.put(NotesDetailTable.KEY_BODY, noteBody);
        returnObject.put(NotesDetailTable.KEY_TITLE, noteTitle);
        returnObject.put(NotesDetailTable.KEY_DATE, noteDate);
        returnObject.put(NotesDetailTable.KEY_ROWID, noteId);

        return returnObject;
    }
}
