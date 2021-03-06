package com.organization.sjhg.e_school.TakeNotes;

import android.accounts.NetworkErrorException;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.HttpHelper;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.organization.sjhg.e_school.Structure.NotesDetail;
import com.organization.sjhg.e_school.TakeNotes.whiteboard.WhiteBoardActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prateek Tulsyan on 30-04-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class NoteListingActivity extends ListFragment implements RemoteCallHandler
{
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST;
    Context context;
    View view;
    private NotesDetailTable table_obj;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=super.onCreateView(inflater, container, savedInstanceState);
        context=getActivity();
        return view;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //DeviceAdminUtil.checkAndPrompt(context);
        // setContentView(R.layout.activity_note_listing);
        table_obj = new NotesDetailTable(context);
        table_obj.open();

        populateListView();
        registerForContextMenu(getListView());
/*
        ImageButton addnote = (ImageButton)getActivity().findViewById(R.id.addnotebutton);
        addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createNote();
            }
        });*/

        ImageButton RestoreBtn;
//        RestoreBtn = (ImageButton) getActivity().findViewById(R.id.restore);
//        RestoreBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper( context, android.R.style.Theme_Holo_Light_Dialog));
//                builder.setMessage(getResources().getString(R.string.restore_warning));
//                builder.setTitle(getResources().getString(R.string.attention));
//                builder.setCancelable(true);
//                builder.setPositiveButton("YES",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//
//                                new RemoteHelper(context).getServerNotes(NoteListingActivity.this, RemoteCalls.GET_NOTES);
//                                Toast.makeText(context,"Notes restore in progress",Toast.LENGTH_LONG).show();
//                            }
//                        });
//                builder.setNegativeButton("NO",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                AlertDialog alert11 = builder.create();
//                alert11.show();
//            }
//        });
//        ImageButton BackupBtn;
//        BackupBtn = (ImageButton) getActivity().findViewById(R.id.backup);
//        BackupBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                try {
////                    //new RemoteHelper(context).backupNotes(NoteListingActivity.this, RemoteCalls.BACKUP_NOTES);
////                    Toast.makeText(context,"Notes backup in progress",Toast.LENGTH_LONG).show();
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                } catch (NetworkErrorException e) {
////                    e.printStackTrace();
////                } catch (SQLException e) {
////                    e.printStackTrace();
////                }
//            }
//        });
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceAdminUtil.checkAndPrompt(this);
       // setContentView(R.layout.activity_note_listing);

        return view;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //DeviceAdminUtil.checkAndPrompt(context);
        // setContentView(R.layout.activity_note_listing);
        table_obj = new NotesDetailTable(context);
        table_obj.open();

        populateListView();
        registerForContextMenu(getListView());


    }*/

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor note = table_obj.fetchSingleNote(id);
        String type = note.getString(note.getColumnIndexOrThrow(NotesDetailTable.KEY_TYPE));
        Intent i = new Intent(context, AddSmallNotesActivity.class);
        if (type.equals("2")){
            i = new Intent(context, WhiteBoardActivity.class);
        }
        i.putExtra(NotesDetailTable.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);


     /*   View view=getActivity().findViewById(R.id.notesEdit);

        NoteEditActivity  notesEditFragment = new NoteEditActivity();
        Bundle bundle = new Bundle();
        bundle.putLong(NotesDetailTable.KEY_ROWID, id);
        //bundle.putInt();
        notesEditFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.notesfragment, notesEditFragment).commit();*/



       /* view.setVisibility(View.VISIBLE);*/
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceAdminUtil.checkAndPrompt(this);
       // setContentView(R.layout.activity_note_listing);

        table_obj = new NotesDetailTable(this);
        table_obj.open();

        populateListView();
        registerForContextMenu(getListView());


    }*/

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
                table_obj.deleteNote(info.id);
                populateListView();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        populateListView();
    }
   /*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        populateListView();
    }*/

    // DATABASE OPERATIONS

    private void createNote()
    {
        Intent i = new Intent(context, AddSmallNotesActivity.class);
        startActivityForResult(i, ACTIVITY_CREATE);
       /* View view=getActivity().findViewById(R.id.notesEdit);
        NoteEditActivity  notesEditFragment = new NoteEditActivity();
        getFragmentManager().beginTransaction()
                .replace(R.id.notesfragment, notesEditFragment)
                .commit();
        view.setVisibility(View.VISIBLE);*/
    }

    private void populateListView()
    {
        Cursor fetchNotes = table_obj.fetchAllNotes();

        getActivity().startManagingCursor(fetchNotes);

        getActivity().startManagingCursor(fetchNotes);



        String[] fromDatabase = new String[] { NotesDetailTable.KEY_TITLE , NotesDetailTable.KEY_DATE , NotesDetailTable.KEY_BODY };
        int[] toActivity = new int[] { R.id.text1 , R.id.date_row , R.id.text2 };

        SimpleCursorAdapter notes = new SimpleCursorAdapter(context, R.layout.notes_row, fetchNotes, fromDatabase, toActivity);
        setListAdapter(notes);
    }

    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        if (!isSuccessful) {
            Toast.makeText(context,
                    "Remote Call failed: " + callFor.toString() + " " + exception.getMessage(),
                    Toast.LENGTH_SHORT)
                    .show();
        }
        switch (callFor) {
            case GET_NOTES:
                if(isSuccessful && response != null) {
                    NotesDetailTable table_obj;
                    table_obj = new NotesDetailTable(context);
                    table_obj.open();

                    // Delete local notes
                    table_obj.deleteAllNotes();

                    // Enter remote notes into local db
                    if (response.has("FetchNotes")) {
                        JSONArray noteArray = null;
                        try {
                            noteArray = response.getJSONArray("FetchNotes");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        assert noteArray != null;
                        int lengthOfArray = noteArray.length();
                        for (int i = 0; i < lengthOfArray; i++) {

                            JSONObject notesDetailAsJson = null;
                            try {
                                notesDetailAsJson = noteArray.getJSONObject(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                table_obj.insertNote(notesDetailAsJson);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (response.has("NoNotes")) {
                    Toast.makeText(context,
                            "No backup found!",
                            Toast.LENGTH_LONG)
                            .show();
                }
                } else {
                    Toast.makeText(context,
                            "Restoration failed! Try again",
                            Toast.LENGTH_LONG)
                            .show();
                }
                break;
        }
        Intent goToNotes = getActivity().getIntent();
        getActivity().finish();
        startActivity(goToNotes);
    }
}
