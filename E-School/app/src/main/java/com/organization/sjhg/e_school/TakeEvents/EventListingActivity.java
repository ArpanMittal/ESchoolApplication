package com.organization.sjhg.e_school.TakeEvents;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.organization.sjhg.e_school.HideNavigationBar;
import com.organization.sjhg.e_school.MainActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
/*import com.organization.sjhg.e_school.TakeNotes.NoteEditActivity;*/
import com.organization.sjhg.e_school.TakeNotes.NotesDetailTable;
import com.organization.sjhg.e_school.deviceadmin.DeviceAdminUtil;

import org.json.JSONObject;

/**
 * Created by Arpan on 3/2/2016.
 */
public class EventListingActivity extends ListActivity implements RemoteCallHandler {
    private static EventListingActivity inst;
    private EventDetailTable event_table;
    private static final int ACTIVITY_EDIT = 1;

    @Override
    protected void onStart() {
        super.onStart();
        inst=this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceAdminUtil.checkAndPrompt(this);
        HideNavigationBar hideNavigationBar=new HideNavigationBar();
        hideNavigationBar.hideNavigationBar(getWindow());
        setContentView(R.layout.activity_event_listing);
        event_table=new EventDetailTable(this);
        event_table.open();

        populateListView();
      //  registerForContextMenu(getListView());

    }

    @Override
    protected void onResume() {
        super.onResume();
        HideNavigationBar hideNavigationBar=new HideNavigationBar();
        hideNavigationBar.hideNavigationBar(getWindow());
    }


    public static EventListingActivity inst()
    {
        return inst;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, EventDisplayActivity.class);
        i.putExtra(EventDetailTable.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
        //finish();
    }

    public void populateListView()
    {
        Cursor fetchNotes = event_table.fetchAllEvents();
        startManagingCursor(fetchNotes);



        String[] fromDatabase = new String[] { EventDetailTable.KEY_TITLE , EventDetailTable.KEY_DATE , EventDetailTable.KEY_BODY };
        int[] toActivity = new int[] { R.id.text1 , R.id.date_row , R.id.text2 };

        SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.notes_row, fetchNotes, fromDatabase, toActivity);
        setListAdapter(notes);
    }

    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {

    }
}