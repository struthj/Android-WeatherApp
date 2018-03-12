package com.example.android.weatherwithsqlite.utils;

import android.content.Context;
import android.net.Uri;

import com.example.android.weatherwithsqlite.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by hessro on 5/10/17.
 */

public class OpenWeatherMapUtils {

    private final static String OWM_FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast";
    private final static String OWM_FORECAST_QUERY_PARAM = "q";
    private final static String OWM_FORECAST_UNITS_PARAM = "units";
    private final static String OWM_FORECAST_APPID_PARAM = "appid";
    private final static String OWM_FORECAST_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static String OWM_FORECAST_TIME_ZONE = "UTC";

    /*
     * Set your own APPID here.
     */
    private final static String OWM_FORECAST_APPID = "ba2386709c047d224d3432dbf3f73113";

    public static class ForecastItem implements Serializable {
        public static final String EXTRA_FORECAST_ITEM = "com.example.android.lifecycleweather.utils.ForecastItem.SearchResult";
        public Date dateTime;
        public String description;
        public long temperature;
        public long temperatureLow;
        public long temperatureHigh;
        public long humidity;
        public long windSpeed;
        public String windDirection;
    }

    public static String buildForecastURL(String forecastLocation, String temperatureUnits) {
        return Uri.parse(OWM_FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(OWM_FORECAST_QUERY_PARAM, forecastLocation)
                .appendQueryParameter(OWM_FORECAST_UNITS_PARAM, temperatureUnits)
                .appendQueryParameter(OWM_FORECAST_APPID_PARAM, OWM_FORECAST_APPID)
                .build()
                .toString();
    }

    public static ArrayList<ForecastItem> parseForecastJSON(String forecastJSON) {
        try {
            JSONObject forecastObj = new JSONObject(forecastJSON);
            JSONArray forecastList = forecastObj.getJSONArray("list");
            SimpleDateFormat dateParser = new SimpleDateFormat(OWM_FORECAST_DATE_FORMAT);
            dateParser.setTimeZone(TimeZone.getTimeZone(OWM_FORECAST_TIME_ZONE));

            ArrayList<ForecastItem> forecastItemsList = new ArrayList<ForecastItem>();
            for (int i = 0; i < forecastList.length(); i++) {
                ForecastItem forecastItem = new ForecastItem();
                JSONObject forecastListElem = forecastList.getJSONObject(i);

                String dateString = forecastListElem.getString("dt_txt");
                forecastItem.dateTime = dateParser.parse(dateString);

                forecastItem.description = forecastListElem.getJSONArray("weather").getJSONObject(0).getString("main");

                JSONObject mainObj = forecastListElem.getJSONObject("main");
                forecastItem.temperature = Math.round(mainObj.getDouble("temp"));
                forecastItem.temperatureLow = Math.round(mainObj.getDouble("temp_min"));
                forecastItem.temperatureHigh = Math.round(mainObj.getDouble("temp_max"));
                forecastItem.humidity = Math.round(mainObj.getDouble("humidity"));

                JSONObject windObj = forecastListElem.getJSONObject("wind");
                forecastItem.windSpeed = Math.round(windObj.getDouble("speed"));
                forecastItem.windDirection = windAngleToDirection(windObj.getDouble("deg"));

                forecastItemsList.add(forecastItem);
            }
            return forecastItemsList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String windAngleToDirection(double angleDegrees) {
        if (angleDegrees >= 0 && angleDegrees < 11.25) {
            return "N";
        } else if (angleDegrees >= 11.25 && angleDegrees < 33.75) {
            return "NNE";
        } else if (angleDegrees >= 33.75 && angleDegrees < 56.25) {
            return "NE";
        } else if (angleDegrees >= 56.25 && angleDegrees < 78.75) {
            return "ENE";
        } else if (angleDegrees >= 78.75 && angleDegrees < 101.25) {
            return "E";
        } else if (angleDegrees >= 101.25 && angleDegrees < 123.75) {
            return "ESE";
        } else if (angleDegrees >= 123.75 && angleDegrees < 146.25) {
            return "SE";
        } else if (angleDegrees >= 146.25 && angleDegrees < 168.75) {
            return "SSE";
        } else if (angleDegrees >= 168.75 && angleDegrees < 191.25) {
            return "S";
        } else if (angleDegrees >= 191.25 && angleDegrees < 213.75) {
            return "SSW";
        } else if (angleDegrees >= 213.75 && angleDegrees < 236.25) {
            return "SW";
        } else if (angleDegrees >= 236.25 && angleDegrees < 258.75) {
            return "WSW";
        } else if (angleDegrees >= 258.75 && angleDegrees < 281.25) {
            return "W";
        } else if (angleDegrees >= 281.25 && angleDegrees < 303.75) {
            return "WNW";
        } else if (angleDegrees >= 303.75 && angleDegrees < 326.25) {
            return "WNW";
        } else if (angleDegrees >= 326.25 && angleDegrees < 348.75) {
            return "NNW";
        } else {
            return "N";
        }
    }

    public static String getTemperatureUnitsAbbr(Context context, String temperatureUnitsValue) {
        if (temperatureUnitsValue.equals(context.getString(R.string.pref_units_kelvin_value))) {
            return context.getString(R.string.units_kelvin);
        } else if (temperatureUnitsValue.equals(context.getString(R.string.pref_units_metric_value))) {
            return context.getString(R.string.units_metric);
        } else {
            return context.getString(R.string.units_imperial);
        }
    }
}
