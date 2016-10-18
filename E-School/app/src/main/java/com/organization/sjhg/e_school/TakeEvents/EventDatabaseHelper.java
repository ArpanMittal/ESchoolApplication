package com.organization.sjhg.e_school.TakeEvents;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLClientInfoException;

/**
 * Created by Arpan on 3/2/2016.
 */
public class EventDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION  = 1;
    private static final String DATABASE_NAME  = "E-School-Events";

    public EventDatabaseHelper (Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EventDetailTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }
}
