package com.organization.sjhg.e_school.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.organization.sjhg.e_school.Database.contracts.UserContract;
import com.organization.sjhg.e_school.Database.contracts.UserContract.UserDetailEntry;
import com.organization.sjhg.e_school.Database.contracts.UserContract.ContentEntry;

/**
 * Created by Punit Chhajer on 10-08-2016.
 */
public class UserDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "user.db";

    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold user details.
        final String SQL_CREATE_USER_DETAIL_TABLE = "CREATE TABLE " + UserDetailEntry.TABLE_NAME + " (" +
                UserDetailEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                UserDetailEntry.CoLUMN_EMAIL + " TEXT NOT NULL, " +
                UserDetailEntry.CoLUMN_NAME + " TEXT, " +
                UserDetailEntry.CoLUMN_VERIFIED + " INTEGER, " +
                UserDetailEntry.CoLUMN_PHOTO_PATH + " TEXT, " +
                UserDetailEntry.CoLUMN_DATE_OF_BIRTH + " TEXT, " +
                UserDetailEntry.CoLUMN_COUNTRY + " TEXT, " +
                UserDetailEntry.CoLUMN_STATE + " TEXT, " +
                UserDetailEntry.CoLUMN_CITY + " TEXT, " +
                UserDetailEntry.CoLUMN_PHONE_NUMBER + " TEXT " +
                " );";

        final String SQL_CREATE_CONTENT_TABLE = "CREATE TABLE " + ContentEntry.TABLE_NAME + " ("+
                ContentEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                ContentEntry.COLUMN_HASH + " TEXT NOT NULL," +
                ContentEntry.CoLUMN_PATH + " TEXT NOT NULL, " +
                ContentEntry.CoLUMN_PROTECTION + " BLOB " +
                " );";


        final String SQL_CREATE_TEST_DETAIL_TABLE = "CREATE TABLE " + UserContract.TestDetail.TABLE_NAME + " ("+
                UserContract.TestDetail.COLUMN_ID+" INTEGER PRIMARY KEY," +
                UserContract.TestDetail.COLUMN_QUESTION_ID+" TEXT," +
                UserContract.TestDetail.COLUMN_OPTION_ID+" TEXT," +
                UserContract.TestDetail.COLUMN_IS_CORRECT +" TEXT, " +
                UserContract.TestDetail.COLUMN_TIME_SPEND +" REAL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_USER_DETAIL_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CONTENT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TEST_DETAIL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserDetailEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContentEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS"+ UserContract.TestDetail.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
