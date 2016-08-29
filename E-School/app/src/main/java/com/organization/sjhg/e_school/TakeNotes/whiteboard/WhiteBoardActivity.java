package com.organization.sjhg.e_school.TakeNotes.whiteboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.TakeNotes.NotesDetailTable;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Punit Chhajer on 09-06-2016.
 */
public class WhiteBoardActivity extends Activity implements View.OnClickListener {
    private CustomView drawView;
    private float smallBrush, mediumBrush, largeBrush;
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn;
    private TextView title;

    private NotesDetailTable table_obj;
    public static String curDate = "";

    private Long mRowId;
    private Cursor note;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whiteboard);

        drawView = (CustomView) findViewById(R.id.drawing);

        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawBtn = (ImageButton)findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        drawView.setBrushSize(mediumBrush);

        eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        saveBtn = (ImageButton)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        title = (TextView)findViewById(R.id.title);
        table_obj = new NotesDetailTable(getApplicationContext());
        table_obj.open();
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
    }

    public void paintClicked(View view){
        //use chosen color
        if(view!=currPaint){
            //update color
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);

            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());
    }

    @Override
    public void onClick(View view) {
        //respond to clicks
        if(view.getId()==R.id.draw_btn){
            //draw button clicked
            final Dialog brushDialog = new Dialog(new ContextThemeWrapper( this, android.R.style.Theme_Holo_Light_Dialog));
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }else if(view.getId()==R.id.erase_btn){
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(new ContextThemeWrapper( this, android.R.style.Theme_Holo_Light_Dialog));
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }else if(view.getId()==R.id.new_btn){
            //new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(new ContextThemeWrapper( this, android.R.style.Theme_Holo_Light_Dialog));
            newDialog.setTitle("New Note");
            newDialog.setMessage("Start new note (you will lose the current note)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();
        }else if(view.getId()==R.id.save_btn){
            //save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(new ContextThemeWrapper( this, android.R.style.Theme_Holo_Light_Dialog));
            saveDialog.setTitle("Save note");
            saveDialog.setMessage("Save note?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //save drawing
                    saveNoteState();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        //save drawing
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(new ContextThemeWrapper( this, android.R.style.Theme_Holo_Light_Dialog));
        saveDialog.setTitle("Save note");
        saveDialog.setMessage("Save note?");
        saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                //save drawing
                saveNoteState();
                finish();
            }
        });
        saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
                finish();
            }
        });
        saveDialog.show();
    }

    private void saveNoteState() {
        String ttl = title.getText().toString();
        String body = "[image]";
        drawView.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap( drawView.getDrawingCache());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        if((!ttl.equals("") && byteArray.length!=0) || (!ttl.equals("") || byteArray.length!=0)) {
            if (mRowId == null) {
                long id = table_obj.createNote(ttl, body, curDate,2);
                table_obj.updateImage(id,byteArray);
                if (id > 0) {
                    mRowId = id;
                } else {
                    Log.e("saveNoteState", "failed to create note");
                }
            } else {
                if (!table_obj.updateNote(mRowId, ttl, body, curDate)) {
                    Log.e("saveNoteState", "failed to update note");
                }else {
                    table_obj.updateImage(mRowId,byteArray);
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
        }
        drawView.destroyDrawingCache();
        finish();
    }

    private void populateNoteEditWindow() {
        if (mRowId != null)
        {
            note = table_obj.getImage(mRowId);
            startManagingCursor(note);

            title.setText(note.getString(note.getColumnIndexOrThrow(NotesDetailTable.KEY_TITLE)));

            byte[] decodedString = note.getBlob(note.getColumnIndexOrThrow(NotesDetailTable.KEY_IMAGE));
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            drawView.loadBitmap(bitmap);
        }
    }

}