package com.organization.sjhg.e_school.TakeNotes;

/*import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Date;

import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.deviceadmin.DeviceAdminUtil;

import java.text.SimpleDateFormat;

/**
 * Created by Prateek Tulsyan on 30-04-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

/*public class NoteEditActivity extends Fragment
{
    public static String curDate = "";
    public static String curText = "";

    private EditText mTitleText;
    private EditText mBodyText;
    private TextView mDateText;
    private Button saveNote;
    private Button deleteNote;
    private Long mRowId;
    View view;
    private Cursor note;

    private NotesDetailTable table_obj;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =super.onCreateView(inflater, container, savedInstanceState);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        table_obj = new NotesDetailTable(getActivity());
        table_obj.open();

        //getActivity().setContentView(R.layout.activity_note_edit);

        mTitleText = (EditText) getActivity().findViewById(R.id.title);
        mBodyText = (EditText) getActivity().findViewById(R.id.body);

        mDateText = (TextView) getActivity().findViewById(R.id.notelist_date1);

        long msTime = System.currentTimeMillis();
        Date curDateTime = new Date(msTime);
        SimpleDateFormat formatter = new SimpleDateFormat("d'/'M'/'y");
        curDate = formatter.format(curDateTime);
        mDateText.setText(""+curDate);

        //mRowId = (savedInstanceState == null) ? null : (Long) savedInstanceState.getSerializable(NotesDetailTable.KEY_ROWID);
        if (mRowId == null)
        {
            Bundle extras = this.getArguments();
           // mRowId = extras != null ? extras.getLong(NotesDetailTable.KEY_ROWID) : null;
            mRowId = extras!=null?extras.getLong(NotesDetailTable.KEY_ROWID):null;
        }
        populateNoteEditWindow();

        saveNote = (Button)getActivity().findViewById(R.id.save);
        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNoteState();
                afterActionComplete();

                //populateNoteEditWindow();
            }
        });


        deleteNote = (Button)getActivity().findViewById(R.id.delete);
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
               // getActivity().finish();
                afterActionComplete();
                //View vi = (View)getActivity().findViewById(R.id.notesEdit);
                //vi.setVisibility(View.INVISIBLE);
                //populateNoteEditWindow();
            }
        });
    }
    public void afterActionComplete()
    {
        Fragment fragment=getFragmentManager().findFragmentById(R.id.notesfragment);
        getFragmentManager().beginTransaction().detach(fragment).commit();
        //vi.setVisibility(View.INVISIBLE);
       /* View vi = (View)getActivity().findViewById(R.id.notesEdit);
        vi.setVisibility(View.INVISIBLE);
        fragment=getFragmentManager().findFragmentById(R.id.notesfragment);
        getFragmentManager().beginTransaction().detach(fragment).commit();*/
    /*   View vi=(View)getActivity().findViewById(R.id.notesfragment);
        vi.setVisibility(View.INVISIBLE);*/
       /* vi=getActivity().findViewById(R.id.notesSidebar);
        vi.setVisibility(View.INVISIBLE);
        vi=getActivity().findViewById(R.id.addnotebutton);
        vi.setVisibility(View.INVISIBLE);*/
    /*}*/

/*    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceAdminUtil.checkAndPrompt(this);


    }*/

   /* @Override
    public void onResume() {
        super.onResume();
        populateNoteEditWindow();
    }*/

    /* @Override
    protected void onResume() {
        super.onResume();
        populateNoteEditWindow();
    }*/

   /* @Override
    public void onPause() {
        super.onPause();
        saveNoteState();
    }*/

   /* @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveNoteState();
        outState.putSerializable(NotesDetailTable.KEY_ROWID, mRowId);
    }*/

    // DATABASE OPERATIONS

   /* private void saveNoteState()
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
 //           getActivity().finish();

           // Fragment fragment=getFragmentManager().findFragmentById(R.id.notesfragment);
           // getFragmentManager().beginTransaction().remove(fragment).commit();
            View vi = (View)getActivity().findViewById(R.id.notesEdit);
            vi.setVisibility(View.INVISIBLE);
        }
    }

    private void populateNoteEditWindow()
    {
        if (mRowId != null)
        {
            note = table_obj.fetchSingleNote(mRowId);
            getActivity().startManagingCursor(note);

            mTitleText.setText(note.getString(note.getColumnIndexOrThrow(NotesDetailTable.KEY_TITLE)));
            mBodyText.setText(note.getString(note.getColumnIndexOrThrow(NotesDetailTable.KEY_BODY)));
            curText = note.getString(note.getColumnIndexOrThrow(NotesDetailTable.KEY_BODY));
        }
        else
        {
            mTitleText.setText(null);
            mBodyText.setText(null);
        }
    }
}*/
