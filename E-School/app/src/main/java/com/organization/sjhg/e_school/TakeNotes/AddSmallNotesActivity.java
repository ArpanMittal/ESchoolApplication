package com.organization.sjhg.e_school.TakeNotes;

/**
 * Created by Arpan on 5/21/2016.
 */
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Date;

import com.organization.sjhg.e_school.HideNavigationBar;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.deviceadmin.DeviceAdminUtil;

import java.text.SimpleDateFormat;

public class AddSmallNotesActivity extends Activity
{
    public static String curDate = "";
    public static String curText = "";

    private EditText mTitleText;
    private EditText mBodyText;
    private TextView mDateText;
    private Button saveNote;
    private Button deleteNote;
    private Long mRowId;

    private Cursor note;

    private NotesDetailTable table_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //DeviceAdminUtil.checkAndPrompt(this);
       // HideNavigationBar hideNavigationBar=new HideNavigationBar();
       // hideNavigationBar.hideNavigationBar(getWindow());
        table_obj = new NotesDetailTable(this);
        table_obj.open();

        setContentView(R.layout.activity_note_edit);

        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);

        mDateText = (TextView) findViewById(R.id.notelist_date);
        long msTime = System.currentTimeMillis();
        Date curDateTime = new Date(msTime);
        SimpleDateFormat formatter = new SimpleDateFormat("d'/'M'/'y");
        curDate = formatter.format(curDateTime);
        mDateText.setText(""+curDate);

        mRowId = (savedInstanceState == null) ? null : (Long) savedInstanceState.getSerializable(NotesDetailTable.KEY_ROWID);
        if (mRowId == null)
        {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(NotesDetailTable.KEY_ROWID) : null;
        }
        populateNoteEditWindow();

        saveNote = (Button)findViewById(R.id.save);
        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNoteState();
                finish();
            }
        });

        deleteNote = (Button)findViewById(R.id.delete);
        deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(note != null){
                    note.close();
                    note = null;
                }
                if(mRowId != null){
                    table_obj.deleteNote(mRowId);
                }
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateNoteEditWindow();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveNoteState();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveNoteState();
        outState.putSerializable(NotesDetailTable.KEY_ROWID, mRowId);
    }

    // DATABASE OPERATIONS

    private void saveNoteState()
    {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();

        if((!title.equals("") && !body.equals("")) || (!title.equals("") || !body.equals(""))) {
            if (mRowId == null) {
                long id = table_obj.createNote(title, body, curDate);
                if (id > 0) {
                    mRowId = id;
                } else {
                    Log.e("saveNoteState", "failed to create note");
                }
            } else {
                if (!table_obj.updateNote(mRowId, title, body, curDate)) {
                    Log.e("saveNoteState", "failed to update note");
                }
            }
        }else{
            if(note != null){
                note.close();
                note = null;
            }
            if(mRowId != null){
                table_obj.deleteNote(mRowId);
            }
            finish();
        }
    }

    private void populateNoteEditWindow()
    {
        if (mRowId != null)
        {
            note = table_obj.fetchSingleNote(mRowId);
            startManagingCursor(note);

            mTitleText.setText(note.getString(note.getColumnIndexOrThrow(NotesDetailTable.KEY_TITLE)));
            mBodyText.setText(note.getString(note.getColumnIndexOrThrow(NotesDetailTable.KEY_BODY)));
            curText = note.getString(note.getColumnIndexOrThrow(NotesDetailTable.KEY_BODY));
        }
    }
}