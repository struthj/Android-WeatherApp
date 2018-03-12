package com.example.android.weatherwithsqlite;

import android.provider.BaseColumns;


public class WeatherSearchContract {
    private WeatherSearchContract() {}
    public static class SavedRepos implements BaseColumns {
        public static final String TABLE_NAME = "savedLocation";
        public static final String COLUMN_LOCATION_NAME = "locationDesc";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
