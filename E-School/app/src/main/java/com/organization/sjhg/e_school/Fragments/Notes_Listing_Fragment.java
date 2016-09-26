package com.organization.sjhg.e_school.Fragments;

import android.accounts.NetworkErrorException;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Toast;

import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.LoginActivity;
import com.organization.sjhg.e_school.MainParentActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.TakeNotes.AddSmallNotesActivity;
import com.organization.sjhg.e_school.TakeNotes.NoteListingActivity;
import com.organization.sjhg.e_school.TakeNotes.NotesDetailTable;
import com.organization.sjhg.e_school.TakeNotes.whiteboard.WhiteBoardActivity;
import com.organization.sjhg.e_school.Utils.SharedPrefrence;
import com.organization.sjhg.e_school.Utils.ToastActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by arpan on 8/25/2016.
 */
public class Notes_Listing_Fragment extends AppCompatActivity implements RemoteCallHandler{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_listing_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getFragmentManager().beginTransaction().replace(R.id.notesfragment, new NoteListingActivity()).commit();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note_listing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_new) {
            Intent intent=new Intent(getApplicationContext(), AddSmallNotesActivity.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.action_whiteboard){
            Intent intent=new Intent(getApplicationContext(), WhiteBoardActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_restore){
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper( this, android.R.style.Theme_Holo_Light_Dialog));
                builder.setMessage(getResources().getString(R.string.restore_warning));
                builder.setTitle(getResources().getString(R.string.attention));
                builder.setCancelable(true);
                builder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                new RemoteHelper(Notes_Listing_Fragment.this).getServerNotes(Notes_Listing_Fragment.this, RemoteCalls.GET_NOTES);
                                Toast.makeText(Notes_Listing_Fragment.this,"Notes restore in progress",Toast.LENGTH_LONG).show();
                            }
                        });
                builder.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder.create();
                alert11.show();
            return true;
        }else if(id == R.id.action_backup){
            try {
                    new RemoteHelper(this).backupNotes(this, RemoteCalls.BACKUP_NOTES);
                    Toast.makeText(this,"Notes backup in progress",Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NetworkErrorException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        if (!isSuccessful) {
            new LogHelper(exception);
            exception.printStackTrace();
        } else {
            SharedPrefrence sharedPrefrence = new SharedPrefrence();
            ToastActivity toastActivity = new ToastActivity();
            try {
                if (response.get("code").toString().equals(GlobalConstants.EXPIRED_TOKEN)) {

                    if (sharedPrefrence.getRefreshToken(getApplicationContext()) == null) {

                        toastActivity.makeToastMessage(response, this);
                        return;
                    } else {
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                    }

                } else if (response.get("code").toString().equals(GlobalConstants.INAVLID_TOKEN)) {
                    toastActivity.makeToastMessage(response, this);
                }
            } catch (JSONException e) {
                LogHelper logHelper = new LogHelper(e);
                e.printStackTrace();
            }
            switch (callFor) {
                case GET_NOTES:
                    if (isSuccessful && response != null) {
                        NotesDetailTable table_obj;
                        table_obj = new NotesDetailTable(this);
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
                            Toast.makeText(this,
                                    "No backup found!",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    } else {
                        Toast.makeText(this,
                                "Restoration failed! Try again",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                    break;
            }
        }
    }
}
