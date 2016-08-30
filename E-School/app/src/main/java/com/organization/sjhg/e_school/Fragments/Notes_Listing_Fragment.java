package com.organization.sjhg.e_school.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.organization.sjhg.e_school.MainParentActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.TakeNotes.AddSmallNotesActivity;
import com.organization.sjhg.e_school.TakeNotes.NoteListingActivity;
import com.organization.sjhg.e_school.TakeNotes.whiteboard.WhiteBoardActivity;

/**
 * Created by arpan on 8/25/2016.
 */
public class Notes_Listing_Fragment extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_listing_activity);
        Fragment fragment=new NoteListingActivity();
        getFragmentManager().beginTransaction().replace(R.id.notesfragment, new NoteListingActivity()).commit();
        View addnote= findViewById(R.id.addnotebutton);
        addnote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), AddSmallNotesActivity.class);
                startActivity(intent);
            }
        });
        View addBoard= findViewById(R.id.newWhiteBoard);
        addBoard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), WhiteBoardActivity.class);
                startActivity(intent);
            }
        });

        View restore= findViewById(R.id.restore);

        View backup= findViewById(R.id.backup);
    }
}
