package com.example.android.weatherwithsqlite;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.weatherwithsqlite.utils.NetworkUtils;

import java.io.IOException;

/**
 * Created by hessro on 2/24/18.
 */

public class ForecastLoader extends AsyncTaskLoader<String> {

    private final static String TAG = ForecastLoader.class.getSimpleName();

    private String mCachedForecastJSON;
    private String mForecastURL;

    public ForecastLoader(Context context, String forecastURL) {
        super(context);
        mForecastURL = forecastURL;
    }

    @Override
    protected void onStartLoading() {
        if (mForecastURL != null) {
            if (mCachedForecastJSON != null) {
                Log.d(TAG, "using cached forecast");
                deliverResult(mCachedForecastJSON);
            } else {
                forceLoad();
            }
        }
    }

    @Nullable
    @Override
    public String loadInBackground() {
        String forecastJSON = null;
        if (mForecastURL != null) {
            Log.d(TAG, "loading forecast from OpenWeatherMap using this URL: " + mForecastURL);
            try {
                forecastJSON = NetworkUtils.doHTTPGet(mForecastURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return forecastJSON;
    }

    @Override
    public void deliverResult(@Nullable String data) {
        mCachedForecastJSON = data;
        super.deliverResult(data);
    }
}
