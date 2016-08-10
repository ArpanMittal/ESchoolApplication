package com.organization.sjhg.e_school.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.organization.sjhg.e_school.Content.Laughguru.LaughguruDataTable;
/**
 * Created by Prateek Tulsyan on 10-03-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION  = 1;
    private static final String DATABASE_NAME  = "E-School";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.d("table", ContentDetailTable.CREATE_TABLE);
        db.execSQL(ContentDetailTable.CREATE_TABLE);
        db.execSQL(LaughguruDataTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }
}
