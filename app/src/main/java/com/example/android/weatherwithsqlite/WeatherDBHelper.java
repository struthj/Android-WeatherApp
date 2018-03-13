package com.example.android.weatherwithsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.weatherwithsqlite.WeatherSearchContract;


public class WeatherDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weatherSearch.db";
    private static int DATABASE_VERSION = 1;

    public WeatherDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SAVED_REPOS_TABLE =
                "CREATE TABLE " + WeatherSearchContract.SavedRepos.TABLE_NAME + "(" +
                        WeatherSearchContract.SavedRepos._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        WeatherSearchContract.SavedRepos.COLUMN_LOCATION_NAME + " TEXT NOT NULL, " +
                        WeatherSearchContract.SavedRepos.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ");";
        db.execSQL(SQL_CREATE_SAVED_REPOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WeatherSearchContract.SavedRepos.TABLE_NAME + ";");
        onCreate(db);
    }

    public long checkIsPrefSaved(SQLiteDatabase db, String location) {
        boolean isSaved = false;
        if (location != null) {
            String sqlSelection = WeatherSearchContract.SavedRepos.COLUMN_LOCATION_NAME + " = ?";
            String[] sqlSelectionArgs = {location};
            Cursor cursor = db.query(
                    WeatherSearchContract.SavedRepos.TABLE_NAME,
                    null,
                    sqlSelection,
                    sqlSelectionArgs,
                    null,
                    null,
                    null
            );
            isSaved = cursor.getCount() > 0;
            cursor.close();
        }
        if (!isSaved) {
            if (location != null) {
                ContentValues row = new ContentValues();
                row.put(WeatherSearchContract.SavedRepos.COLUMN_LOCATION_NAME, location);
                return db.insert(WeatherSearchContract.SavedRepos.TABLE_NAME, null, row);
            } else {
                return -1;
            }
        }
        return 0;
    }
}
