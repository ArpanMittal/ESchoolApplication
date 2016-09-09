package com.organization.sjhg.e_school.Database.contracts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Punit Chhajer on 10-08-2016.
 */
public class UserContract {
    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.organization.sjhg.e_school";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_USER_DETAIL = "userdetail";
    public static final String PATH_CONTENT = "content";
    public static final String PATH_TEST = "test";

    /* Inner class that defines the table contents of the login table */
    public static final class UserDetailEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER_DETAIL).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER_DETAIL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER_DETAIL;

        // Table name
        public static final String TABLE_NAME = "userdetail";

        // user detail column name
        public static final String COLUMN_ID = "_id";
        public static final String CoLUMN_EMAIL = "email";
        public static final String CoLUMN_NAME = "name";
        public static final String CoLUMN_VERIFIED = "verified";
        public static final String CoLUMN_PHOTO_PATH = "photo_path";
        public static final String CoLUMN_DATE_OF_BIRTH = "date_of_birth";
        public static final String CoLUMN_COUNTRY = "country";
        public static final String CoLUMN_STATE = "state";
        public static final String CoLUMN_CITY = "city";
        public static final String CoLUMN_PHONE_NUMBER = "phone_number";

        //function to build User detail uri for content provider
        public static Uri buildUserDetailUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static int getIdFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }
    }

    public static final class ContentEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTENT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTENT;

        // Table name
        public static final String TABLE_NAME = "content";

        // user detail column name
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_HASH = "hash";
        public static final String CoLUMN_PATH = "path";
        public static final String CoLUMN_PROTECTION = "protection";

        //function to build User detail uri for content provider
        public static Uri buildUserDetailUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    /* Inner class that defines the table contents of the login table */
    public static final class TestDetail implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TEST).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TEST;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TEST;

        // Table name
        public static final String TABLE_NAME = "testdetail";

        // user detail column name
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_QUESTION_ID = "question_id";
        public static final String COLUMN_OPTION_ID = "option_id";


        //function to build User detail uri for content provider
        public static Uri buildTestDetailUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static int getIdFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }
    }
}
