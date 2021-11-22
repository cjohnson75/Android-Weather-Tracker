package com.example.cole.termproject.data;

/**
 * Created by Cole on 5/4/2019.
 */

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseDescription {

    public static final String AUTHORITY = "com.example.cole.termproject.data";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Weather implements BaseColumns {
        public static final String TABLE_NAME = "tracker";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_TEMP = "temp";
        public static final String COLUMN_HUMID = "humid";

        public static Uri buildWeatherUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
