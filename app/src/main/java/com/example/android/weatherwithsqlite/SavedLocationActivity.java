package com.example.android.weatherwithsqlite;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.weatherwithsqlite.utils.OpenWeatherMapUtils;

import java.util.ArrayList;

public class SavedLocationActivity extends AppCompatActivity implements LocationAdapter.OnLocationItemClickListener {

    private RecyclerView mSavedSearchResultsRV;
    private LocationAdapter mAdapter;
    private DrawerLayout mDrawerLayout;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_saved_location_results);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mSavedSearchResultsRV = findViewById(R.id.rv_saved_location_results);
        mSavedSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSavedSearchResultsRV.setHasFixedSize(true);

        WeatherDBHelper dbHelper = new WeatherDBHelper(this);
        mDB = dbHelper.getReadableDatabase();

        //onSharedPreferenceChanged()

        mAdapter = new LocationAdapter(this,this);
        mAdapter.updateLocationItems(getAllSavedReposFromDB());
        mSavedSearchResultsRV.setAdapter(mAdapter);
    }

    @Override
    public void onLocationItemClick(OpenWeatherMapUtils.LocationResult item){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putString(getString(R.string.pref_location_key), item.locationDesc);
        prefEditor.apply();
        mDrawerLayout.closeDrawers();

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
