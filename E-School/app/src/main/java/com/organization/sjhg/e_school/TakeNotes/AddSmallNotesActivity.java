package com.organization.sjhg.e_school.TakeNotes;

/**
 * Created by Arpan on 5/21/2016.
 */
import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.RowId;
import java.util.Date;

import com.organization.sjhg.e_school.HideNavigationBar;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.deviceadmin.DeviceAdminUtil;

import java.text.SimpleDateFormat;

public class AddSmallNotesActivity extends AppCompatActivity
{
    public static String curDate = "";
    public static String curText = "";

    private EditText mTitleText;
    private EditText mBodyText;
    private Button saveNote;
    private Button deleteNote;
    private Long mRowId;
    private CoordinatorLayout coordinatorLayout;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        long msTime = System.currentTimeMillis();
        Date curDateTime = new Date(msTime);
        SimpleDateFormat formatter = new SimpleDateFormat("d'/'M'/'y");
        curDate = formatter.format(curDateTime);

        mRowId = (savedInstanceState == null) ? null : (Long) savedInstanceState.getSerializable(NotesDetailTable.KEY_ROWID);
        if (mRowId == null)
        {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(NotesDetailTable.KEY_ROWID) : null;
        }
        populateNoteEditWindow();
        if (savedInstanceState !=null){
            String title = savedInstanceState.getString(NotesDetailTable.KEY_TITLE);
            String body  = savedInstanceState.getString(NotesDetailTable.KEY_BODY);
            mTitleText.setText(title);
            mBodyText.setText(body);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(NotesDetailTable.KEY_ROWID, mRowId);
        outState.putString(NotesDetailTable.KEY_TITLE, String.valueOf(mTitleText.getText()));
        outState.putString(NotesDetailTable.KEY_BODY, String.valueOf(mBodyText.getText()));
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note_edit, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        showMessage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                showMessage();
                break;
            case R.id.action_delete:
                if (mRowId == null){
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Note not yet saved", Snackbar.LENGTH_LONG);

                    snackbar.show();
                    break;
                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle(R.string.delete_menu_noteedit);
                    alert.setMessage(R.string.test_submit_message);
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
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

                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });

                    alert.show();
                    break;
                }

            case R.id.action_save:
                saveNoteState();
                finish();
                break;
        }
        return false;
    }
    private void showMessage(){
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();

        if((!title.equals("") && !body.equals("")) || (!title.equals("") || !body.equals(""))) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(R.string.exit);
            alert.setMessage(R.string.save_note);
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    saveNoteState();
                    finish();
                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    finish();
                }
            });

            alert.show();
        }else{
            finish();
        }


    }

    // DATABASE OPERATIONS

    private void saveNoteState()
    {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();

        if((!title.equals("") && !body.equals("")) || (!title.equals("") || !body.equals(""))) {
            if (mRowId == null) {
                long id = table_obj.createNote(title, body, curDate,1);
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
            Date curDateTime = new Date(note.getString(note.getColumnIndexOrThrow(NotesDetailTable.KEY_DATE)));
            SimpleDateFormat formatter = new SimpleDateFormat("d'/'M'/'y");
            curDate = formatter.format(curDateTime);
            getSupportActionBar().setTitle("last saved "+curDate);
        }
    }
}