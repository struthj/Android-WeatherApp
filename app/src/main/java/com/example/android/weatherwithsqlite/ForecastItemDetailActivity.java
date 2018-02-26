package com.example.android.weatherwithsqlite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.weatherwithsqlite.utils.OpenWeatherMapUtils;

import java.text.DateFormat;

public class ForecastItemDetailActivity extends AppCompatActivity {

    private static final String FORECAST_HASHTAG = "#CS492Weather";
    private static final DateFormat DATE_FORMATTER = DateFormat.getDateTimeInstance();

    private TextView mDateTV;
    private TextView mTempDescriptionTV;
    private TextView mLowHighTempTV;
    private TextView mWindTV;
    private TextView mHumidityTV;
    private OpenWeatherMapUtils.ForecastItem mForecastItem;
    private String mForecastLocation;
    private String mTemperatureUnitsAbbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_item_detail);

        mDateTV = findViewById(R.id.tv_date);
        mTempDescriptionTV = findViewById(R.id.tv_temp_description);
        mLowHighTempTV = findViewById(R.id.tv_low_high_temp);
        mWindTV = findViewById(R.id.tv_wind);
        mHumidityTV = findViewById(R.id.tv_humidity);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mForecastLocation = sharedPreferences.getString(
                getString(R.string.pref_location_key),
                getString(R.string.pref_location_default_value)
        );
        String temperatureUnitsValue = sharedPreferences.getString(
                getString(R.string.pref_units_key),
                getString(R.string.pref_units_default_value)
        );
        mTemperatureUnitsAbbr = OpenWeatherMapUtils.getTemperatureUnitsAbbr(this, temperatureUnitsValue);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(OpenWeatherMapUtils.ForecastItem.EXTRA_FORECAST_ITEM)) {
            mForecastItem = (OpenWeatherMapUtils.ForecastItem)intent.getSerializableExtra(
                    OpenWeatherMapUtils.ForecastItem.EXTRA_FORECAST_ITEM
            );
            fillInLayoutText(mForecastItem);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast_item_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareForecast();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void shareForecast() {
        if (mForecastItem != null) {
            String shareText = "Weather for " + mForecastLocation +
                    ", " + DATE_FORMATTER.format(mForecastItem.dateTime) +
                    ": " + mForecastItem.temperature + mTemperatureUnitsAbbr +
                    " - " + mForecastItem.description +
                    " " + FORECAST_HASHTAG;
            ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(shareText)
                    .setChooserTitle(R.string.share_chooser_title)
                    .startChooser();
        }
    }

    private void fillInLayoutText(OpenWeatherMapUtils.ForecastItem forecastItem) {
        String dateString = DATE_FORMATTER.format(forecastItem.dateTime);
        String detailString = forecastItem.temperature + mTemperatureUnitsAbbr + " - " + forecastItem.description;
        String lowHighTempString = "Low: " + forecastItem.temperatureLow + mTemperatureUnitsAbbr +
                "   High: " + forecastItem.temperatureHigh + mTemperatureUnitsAbbr ;
        String windString = "Wind: " + forecastItem.windSpeed + " MPH " + forecastItem.windDirection;
        String humidityString = "Humidity: " + forecastItem.humidity + "%";

        mDateTV.setText(dateString);
        mTempDescriptionTV.setText(detailString);
        mLowHighTempTV.setText(lowHighTempString);
        mWindTV.setText(windString);
        mHumidityTV.setText(humidityString);
    }
}
