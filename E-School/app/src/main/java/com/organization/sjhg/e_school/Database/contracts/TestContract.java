package com.organization.sjhg.e_school.Database.contracts;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by arpan on 9/9/2016.
 */
public class TestContract {

    public static final String CONTENT_AUTHORITY = "com.organization.sjhg.e_school";
    private TestContract()
    {

    }

    public static class Test implements BaseColumns {

        public static final String COLUMN_ID = "_id";
        public static final String TABLE_NAME = "testdetail";
        public static final String COLUMN_QUESTION_ID = "question_id";
        public static final String CoLUMN_OPTION_ID = "option_id";



        public static int getIdFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }
    }
}
