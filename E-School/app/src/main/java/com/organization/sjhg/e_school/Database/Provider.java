package com.organization.sjhg.e_school.Database;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.organization.sjhg.e_school.Database.contracts.UserContract;

/**
 * Created by Punit Chhajer on 10-08-2016.
 */
public class Provider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private UserDbHelper mOpenUserHelper;

    static final int USER = 100;
    static final int USER_WITH_ID = 101;
    static final int CONTENT = 200;
    static final int CONTENT_WITH_ID = 201;

    /*
        Here is where you need to create the UriMatcher. This UriMatcher will
        match each URI to the USER integer constants defined above.
     */
    static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = UserContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, UserContract.PATH_USER_DETAIL, USER);

        matcher.addURI(authority, UserContract.PATH_USER_DETAIL + "/#", USER_WITH_ID);

        matcher.addURI(authority, UserContract.PATH_CONTENT, CONTENT);

        matcher.addURI(authority, UserContract.PATH_CONTENT + "/#", CONTENT_WITH_ID);

        return matcher;
    }

    /*
        We've coded this for you.  We just create a new UserDbHelper for later use
        here.
     */
    @Override
    public boolean onCreate() {
        mOpenUserHelper = new UserDbHelper(getContext());
        return true;
    }

    /*
        Here's where you'll code the getType function that uses the UriMatcher.

     */
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case USER:
                return UserContract.UserDetailEntry.CONTENT_TYPE;
            case USER_WITH_ID:
                return UserContract.UserDetailEntry.CONTENT_ITEM_TYPE;
            case CONTENT:
                return UserContract.UserDetailEntry.CONTENT_TYPE;
            case CONTENT_WITH_ID:
                return UserContract.UserDetailEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case USER: {
                retCursor = mOpenUserHelper.getReadableDatabase().query(
                        UserContract.UserDetailEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case USER_WITH_ID: {
                int id = UserContract.UserDetailEntry.getIdFromUri(uri);
                retCursor = mOpenUserHelper.getReadableDatabase().query(
                        UserContract.UserDetailEntry.TABLE_NAME,
                        projection,
                        UserContract.UserDetailEntry._ID + " = ?",
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case CONTENT: {
                retCursor = mOpenUserHelper.getReadableDatabase().query(
                        UserContract.ContentEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case CONTENT_WITH_ID: {
                String id = UserContract.ContentEntry.getIdFromUri(uri);
                retCursor = mOpenUserHelper.getReadableDatabase().query(
                        UserContract.ContentEntry.TABLE_NAME,
                        projection,
                        UserContract.ContentEntry.COLUMN_ID + " = ?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Add the ability to insert data to the implementation of this function.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenUserHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case USER: {
                long _id = db.insert(UserContract.UserDetailEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = UserContract.UserDetailEntry.buildUserDetailUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CONTENT: {
                long _id = db.insert(UserContract.ContentEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = UserContract.ContentEntry.buildUserDetailUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenUserHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case USER:
                rowsDeleted = db.delete(
                        UserContract.UserDetailEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case USER_WITH_ID:
                int id = UserContract.UserDetailEntry.getIdFromUri(uri);
                rowsDeleted = db.delete(
                        UserContract.UserDetailEntry.TABLE_NAME, UserContract.UserDetailEntry._ID + " = ?", new String[]{String.valueOf(id)});
                break;
            case CONTENT:
                rowsDeleted = db.delete(
                        UserContract.ContentEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CONTENT_WITH_ID:
                String cid = UserContract.ContentEntry.getIdFromUri(uri);
                rowsDeleted = db.delete(
                        UserContract.UserDetailEntry.TABLE_NAME, UserContract.ContentEntry._ID + " = ?", new String[]{cid});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenUserHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case USER:
                rowsUpdated = db.update(UserContract.UserDetailEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case USER_WITH_ID:
                int id = UserContract.UserDetailEntry.getIdFromUri(uri);
                rowsUpdated = db.update(UserContract.UserDetailEntry.TABLE_NAME, values, UserContract.UserDetailEntry._ID + " = ?", new String[]{String.valueOf(id)});
                break;
            case CONTENT:
                rowsUpdated = db.update(UserContract.ContentEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case CONTENT_WITH_ID:
                String cid = UserContract.ContentEntry.getIdFromUri(uri);
                rowsUpdated = db.update(UserContract.ContentEntry.TABLE_NAME, values, UserContract.ContentEntry._ID + " = ?", new String[]{cid});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenUserHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case USER:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(UserContract.UserDetailEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenUserHelper.close();
        super.shutdown();
    }
}