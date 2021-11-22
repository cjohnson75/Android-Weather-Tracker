package com.example.cole.termproject.data;

/**
 * Created by Cole on 5/4/2019.
 */

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.cole.termproject.data.DatabaseDescription.Weather;

public class WeatherTrackerDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "WeatherBook.db";
    private static final int DATABASE_VERSION = 1;

    public WeatherTrackerDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TRACKER_TABLE =
                "CREATE TABLE " + Weather.TABLE_NAME + "(" +
                        Weather._ID + " integer primary key, " +
                        Weather.COLUMN_DATE + " TEXT, " +
                        Weather.COLUMN_CATEGORY + " TEXT, " +
                        Weather.COLUMN_CITY + " TEXT, " +
                        Weather.COLUMN_STATE + " TEXT, " +
                        Weather.COLUMN_TEMP + " TEXT, " +
                        Weather.COLUMN_HUMID + " TEXT);";
        db.execSQL(CREATE_TRACKER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) { }

}