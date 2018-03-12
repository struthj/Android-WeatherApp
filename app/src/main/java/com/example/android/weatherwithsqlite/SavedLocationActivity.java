package com.example.android.weatherwithsqlite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.githubsearchwithsqlite.utils.GitHubUtils;

import java.util.ArrayList;

public class SavedLocationActivity extends AppCompatActivity implements ForecastAdapter.OnForecastItemClickListener {

    private RecyclerView mSavedSearchResultsRV;
    private ForecastAdapter mAdapter;

    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_location_results);

        mSavedSearchResultsRV = findViewById(R.id.rv_saved_search_results);
        mSavedSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSavedSearchResultsRV.setHasFixedSize(true);

        GitHubSearchDBHelper dbHelper = new GitHubSearchDBHelper(this);
        mDB = dbHelper.getReadableDatabase();

        mAdapter = new GitHubSearchAdapter(this);
        mAdapter.updateSearchResults(getAllSavedReposFromDB());
        mSavedSearchResultsRV.setAdapter(mAdapter);
    }

    @Override
    public void onSearchItemClick(GitHubUtils.SearchResult searchResult) {
        Intent detailedSearchResultIntent = new Intent(this, SearchResultDetailActivity.class);
        detailedSearchResultIntent.putExtra(GitHubUtils.EXTRA_SEARCH_RESULT, searchResult);
        startActivity(detailedSearchResultIntent);
    }

    @Override
    protected void onDestroy() {
        mDB.close();
        super.onDestroy();
    }

    private ArrayList<GitHubUtils.SearchResult> getAllSavedReposFromDB() {
        Cursor cursor = mDB.query(
                GitHubSearchContract.SavedRepos.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                GitHubSearchContract.SavedRepos.COLUMN_TIMESTAMP + " DESC"
        );

        ArrayList<GitHubUtils.SearchResult> savedSearchResults = new ArrayList<>();
        while (cursor.moveToNext()) {
            GitHubUtils.SearchResult searchResult = new GitHubUtils.SearchResult();
            searchResult.fullName = cursor.getString(
                    cursor.getColumnIndex(GitHubSearchContract.SavedRepos.COLUMN_FULL_NAME)
            );
            searchResult.description = cursor.getString(
                    cursor.getColumnIndex(GitHubSearchContract.SavedRepos.COLUMN_DESCRIPTION)
            );
            searchResult.htmlURL = cursor.getString(
                    cursor.getColumnIndex(GitHubSearchContract.SavedRepos.COLUMN_URL)
            );
            searchResult.stars = cursor.getInt(
                    cursor.getColumnIndex(GitHubSearchContract.SavedRepos.COLUMN_STARS)
            );
            savedSearchResults.add(searchResult);
        }
        cursor.close();
        return savedSearchResults;
    }
}
