package com.organization.sjhg.e_school.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.organization.sjhg.e_school.Database.contracts.UserContract.UserDetailEntry;

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

        sqLiteDatabase.execSQL(SQL_CREATE_USER_DETAIL_TABLE);
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
        onCreate(sqLiteDatabase);
    }
}
