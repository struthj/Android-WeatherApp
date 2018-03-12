package com.example.android.weatherwithsqlite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.

import com.example.android.githubsearchwithsqlite.utils.GitHubUtils;
import com.example.android.weatherwithsqlite.utils.OpenWeatherMapUtils;

import java.util.ArrayList;

public class SavedLocationActivity extends AppCompatActivity {

    private RecyclerView mSavedSearchResultsRV;
    private ForecastAdapter mAdapter;

    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_location_results);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSavedSearchResultsRV = findViewById(R.id.rv_saved_search_results);
        mSavedSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSavedSearchResultsRV.setHasFixedSize(true);

        WeatherDBHelper dbHelper = new WeatherDBHelper(this);
        mDB = dbHelper.getReadableDatabase();

        onSharedPreferenceChanged()

        mAdapter = new ForecastAdapter(this);
        mAdapter.updateForecastItems(getAllSavedReposFromDB());
        mSavedSearchResultsRV.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        mDB.close();
        super.onDestroy();
    }

    private ArrayList<OpenWeatherMapUtils.LocationResult> getAllSavedReposFromDB() {
        Cursor cursor = mDB.query(
                WeatherSearchContract.SavedRepos.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WeatherSearchContract.SavedRepos.COLUMN_TIMESTAMP + " DESC"
        );

        ArrayList<OpenWeatherMapUtils.LocationResult> savedSearchResults = new ArrayList<>();
        while (cursor.moveToNext()) {
            OpenWeatherMapUtils.LocationResult searchResult = new OpenWeatherMapUtils.LocationResult();
            searchResult.locationDesc = cursor.getString(
                    cursor.getColumnIndex(WeatherSearchContract.SavedRepos.COLUMN_LOCATION_NAME)
            );
            savedSearchResults.add(searchResult);
        }
        cursor.close();
        return savedSearchResults;
    }

}
